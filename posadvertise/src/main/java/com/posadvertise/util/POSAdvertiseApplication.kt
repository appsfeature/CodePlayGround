package com.posadvertise.util


import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.posadvertise.POSAdvertise
import com.posadvertise.POSAdvertiseCallback
import com.posadvertise.banner.POSBanner
import com.posadvertise.banner.model.BannerProperty
import com.posadvertise.logAdv
import com.posadvertise.screensaver.POSScreenSaver
import com.posadvertise.screensaver.model.ScreenSaverProperty
import com.posadvertise.tutorials.POSTutorials
import com.posadvertise.tutorials.model.TutorialProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class POSAdvertiseApplication : Application(), POSAdvertiseCallback.OnAdvertiseListener {

    private var posAdvertise : POSAdvertise? = null

    abstract fun isDebugMode() : Boolean;

    override fun onCreate() {
        super.onCreate()
        initLibs()

        if (POSAdvertise.isDebugMode) {
            getTempLocalJson()
        }
    }

    private fun getTempLocalJson() {
        CoroutineScope(Dispatchers.IO).launch {
            val bannerProperty : BannerProperty? = POSAdvertise.posBanner?.property
            val ssProperty : ScreenSaverProperty? = POSAdvertise.posScreenSaver?.property
            val tProperty : TutorialProperty? = POSAdvertise.posTutorials?.property
            val banner = Gson().toJson(bannerProperty)
            val screenSaver = Gson().toJson(ssProperty)
            val tutorial = Gson().toJson(tProperty)
            logAdv("Banners : $banner")
            logAdv("ScreenSaver : $screenSaver")
            logAdv("tutorial : $tutorial")
        }
    }

    fun downloadFileFromServer(downloadFileUrl : String, callback: POSAdvertiseCallback.Download<Boolean>) {
        val isUpdateAvailable = POSAdvertisePreference.isUpdateAvailable(this)
        if(isUpdateAvailable) {
            POSAdvertise.downloadFileFromServer(this, downloadFileUrl, callback)
        }
    }

    override fun onDownloadCompletedUpdateUi() {
        updatePOSAdvertiseUiAndProperty()
    }

    private fun updatePOSAdvertiseUiAndProperty() {
        POSAdvertise.updateUICallback()
    }

    private fun initLibs(){
        getPosAdvertise()
            .init(this, object : POSAdvertiseCallback.Callback<Boolean>{
                override fun onSuccess(response: Boolean) {
                    logAdv("init: onSuccess")
                    POSAdvertise.updateUICallback()
//                    updatePOSAdvertiseUiAndProperty()
//                    registerScreenSaver(this)
                }

                override fun onFailure(e: Exception?) {
                    super.onFailure(e)
                    logAdv("init: onFailure")
                }
            })
    }

    fun getPosAdvertise(): POSAdvertise {
        if(posAdvertise == null) {
            posAdvertise = POSAdvertise.apply {
                isDebugMode = isDebugMode()
                posBanner = getPOSBanner()
                posScreenSaver = getPOSScreenSaver()
                posTutorials = getPOSTutorial()
                mListener = this@POSAdvertiseApplication
            }
        }
        return posAdvertise!!
    }

    private fun getPOSBanner(): POSBanner {
        return POSBanner.apply {
            property = POSAdvertiseUtility.getBannerProperty(this@POSAdvertiseApplication)
        }
    }

    private fun getPOSScreenSaver(): POSScreenSaver {
        return POSScreenSaver.apply {
            property = POSAdvertiseUtility.getSSProperty(this@POSAdvertiseApplication)
            init(this@POSAdvertiseApplication)
        }
    }

    private fun getPOSTutorial(): POSTutorials {
        return POSTutorials.apply {
            property = POSAdvertiseUtility.getTutorialProperty(this@POSAdvertiseApplication)
        }
    }

    fun registerScreenSaver(context: Context) {
        getPosAdvertise().registerScreenSaver(context)
    }

    fun unregisterPosAdvertise() {
        getPosAdvertise().unregisterScreenSaver()
    }

}