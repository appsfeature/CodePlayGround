package com.posadvertise

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.helper.util.BaseConstants
import com.posadvertise.POSAdvertiseCallback.OnAdvertiseUpdate
import com.posadvertise.banner.POSBanner
import com.posadvertise.banner.logBanner
import com.posadvertise.screensaver.POSScreenSaver
import com.posadvertise.tutorials.POSTutorials
import com.posadvertise.tutorials.view.TutorialsActivity
import com.posadvertise.util.POSAdvertiseDataManager
import com.posadvertise.util.POSAdvertisePreference
import com.posadvertise.util.POSAdvertiseUtility
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun logAdv(message: String){
    if(POSAdvertise.isDebugMode) {
        Log.d(POSAdvertise::class.java.simpleName, message)
    }
}

fun logAdv(tag: String, message: String){
    if(POSAdvertise.isDebugMode) {
        Log.d(POSAdvertise::class.java.simpleName, tag + message)
    }
}

object POSAdvertise {

    var isScreenSaverFreeze: Boolean = false
    var httpsTutorialUrl: String? = null
    var isDebugMode: Boolean = false
    var posScreenSaver: POSScreenSaver? = null
    var posBanner : POSBanner? = null
    var posTutorials : POSTutorials? = null
    var mListener : POSAdvertiseCallback.OnAdvertiseListener? = null

    fun showBanner(activity: Activity, container: Int) {
        posBanner?.show(activity, container)
    }

    fun showBannerOnHomeScreen(fragment: Fragment, container: Int) {
        addLiveChangeListener(fragment, object : OnAdvertiseUpdate{
            override fun onUIUpdate() {
                posBanner?.let {
                    if(it.property?.isEnableHomeScreen == true){
                        it.show(fragment, container, true)
                    }
                }
            }
        })
    }

    fun showBannerOnVoidScreen(fragment: Fragment, container: Int) {
        addLiveChangeListener(fragment, object : OnAdvertiseUpdate{
            override fun onUIUpdate() {
                posBanner?.let {
                    if(it.property?.isEnableVoidScreen == true){
                        it.show(fragment, container, false)
                    }
                }
            }
        })
    }

    fun showBannerOnPinEntry(fragment: Fragment, container: Int) {
        addLiveChangeListener(fragment, object : OnAdvertiseUpdate{
            override fun onUIUpdate() {
                posBanner?.let {
                    if(it.property?.isEnablePinEntryScreen == true){
                        it.show(fragment, container, false)
                    }
                }
            }
        })
    }

    fun showBannerOnAmtEntryScreen(fragment: Fragment, container: Int) {
        addLiveChangeListener(fragment, object : OnAdvertiseUpdate{
            override fun onUIUpdate() {
                posBanner?.let {
                    if(it.property?.isEnableAmtEntryScreen == true){
                        it.show(fragment, container, false)
                    }
                }
            }
        })
    }

    private fun addLiveChangeListener(fragment: Fragment, listener : OnAdvertiseUpdate) {
        listener.onUIUpdate()
        registerUIUpdateCallbacks(fragment.hashCode(), object : OnAdvertiseUpdate{
            override fun onUIUpdate() {
                listener.onUIUpdate()
            }
        })
        fragment.lifecycle.addObserver(object : DefaultLifecycleObserver{
            override fun onDestroy(owner: LifecycleOwner) {
                super.onDestroy(owner)
                unregisterUIUpdateCallbacks(fragment.hashCode())
                owner.lifecycle.removeObserver(this)
            }
        })
    }

    fun registerScreenSaver(context: Context) {
        this.isScreenSaverFreeze = false
        resetPOSAdvertiseProperty(context)
        POSScreenSaver.registerScreenSaver(context)
    }

    fun unregisterScreenSaver() {
        this.isScreenSaverFreeze = true
        POSScreenSaver.unregisterScreenSaver()
    }

    fun openTutorial(activity: Activity, title: String) {
        activity.startActivity(Intent(activity, TutorialsActivity::class.java)
            .putExtra(BaseConstants.EXTRA_PROPERTY, title))
    }

    fun init(application: Application, callback: POSAdvertiseCallback.Callback<Boolean>) {
        if(POSAdvertisePreference.getConfigBannerJson(application).isNullOrEmpty()){
            POSAdvertiseDataManager.downloadLocally(application, object : POSAdvertiseCallback.Callback<Boolean>{
                override fun onSuccess(response: Boolean) {
                    POSScreenSaver.setUserInteractionCallbackIfNotInitialize()
                    callback.onSuccess(response)
                }

                override fun onFailure(e: Exception?) {
                    super.onFailure(e)
                    callback.onFailure(e)
                }
            })
        }
    }

    private val mUIUpdateCallbacks = HashMap<Int, OnAdvertiseUpdate?>()

    fun registerUIUpdateCallbacks(hashCode: Int, callback: OnAdvertiseUpdate?) {
        synchronized(mUIUpdateCallbacks) { mUIUpdateCallbacks.put(hashCode, callback) }
    }

    fun unregisterUIUpdateCallbacks(hashCode: Int) {
        if (mUIUpdateCallbacks[hashCode] != null) {
            synchronized(mUIUpdateCallbacks) { mUIUpdateCallbacks.remove(hashCode) }
        }
    }

    fun updateUICallback() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                if (mUIUpdateCallbacks.size > 0) {
                    mUIUpdateCallbacks.forEach { (_, v) ->
                        v?.onUIUpdate()
                    }
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }

    fun downloadFileFromServer(context: Context, downloadFileUrl : String, callback: POSAdvertiseCallback.Download<Boolean>) {
        POSAdvertiseDataManager.downloadUpdate(context, downloadFileUrl, object : POSAdvertiseCallback.Download<Boolean> {
            override fun onSuccess(response: Boolean) {
                logAdv("syncPOSAdvertise: onSuccess")
                mListener?.onDownloadCompletedUpdateUi()
                callback.onSuccess(true)
            }

            override fun onProgressUpdate(progress: Int) {
                callback.onProgressUpdate(progress)
            }

            override fun onFailure(e: Exception?) {
                super.onFailure(e)
                mListener?.onDownloadCompletedUpdateUi()
                callback.onFailure(e)
                logAdv("syncPOSAdvertise: onFailure")
            }
        })
    }

    fun resetPOSAdvertiseProperty(context: Context?) {
        POSBanner.property = POSAdvertiseUtility.getBannerProperty(context)
        POSScreenSaver.apply {
            property = POSAdvertiseUtility.getSSProperty(context)
            resetAttributes(context)
        }
        POSTutorials.property = POSAdvertiseUtility.getTutorialProperty(context)
    }

    fun startScreenSaverFreeze(activity: Activity) {
        if(activity is AppCompatActivity){
            activity.lifecycle.addObserver(object : DefaultLifecycleObserver{
                override fun onCreate(owner: LifecycleOwner) {
                    super.onCreate(owner)
                    isScreenSaverFreeze = true
                }

                override fun onDestroy(owner: LifecycleOwner) {
                    super.onDestroy(owner)
                    activity.lifecycle.removeObserver(this)
                    isScreenSaverFreeze = false
                }
            })
        }
    }

    fun startScreenSaverFreeze(fragment: Fragment) {
        fragment.lifecycle.addObserver(object : DefaultLifecycleObserver{
            override fun onCreate(owner: LifecycleOwner) {
                super.onCreate(owner)
                isScreenSaverFreeze = true
            }

            override fun onDestroy(owner: LifecycleOwner) {
                fragment.lifecycle.removeObserver(this)
                isScreenSaverFreeze = false
                POSScreenSaver.resetScreenTimeOutTask(fragment.requireContext())
                super.onDestroy(owner)
            }
        })
    }
}