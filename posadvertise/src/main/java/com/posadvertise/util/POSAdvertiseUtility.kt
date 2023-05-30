package com.posadvertise.util

import android.content.Context
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.posadvertise.banner.logBanner
import com.posadvertise.banner.model.BannerProperty
import com.posadvertise.banner.views.FragmentBanner
import com.posadvertise.banner.views.IS_ACTION_PERFORMED
import com.posadvertise.screensaver.logSS
import com.posadvertise.screensaver.model.ScreenSaverProperty
import com.posadvertise.tutorials.logTut
import com.posadvertise.tutorials.model.TutorialProperty
import java.text.SimpleDateFormat
import java.util.*


class POSAdvertiseUtility {

    companion object{
        fun getTimeInMillis(inputDate: String?): Long {
            try {
                inputDate?.let {
                    if(it.isNotEmpty()){
                        val date: Date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).parse(inputDate)
                        return date.time
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return 0
        }

        fun toast(context: Context?, message: String) {
            context?.let {
                Toast.makeText(it, message, Toast.LENGTH_LONG).show()
            }
        }

        fun mapFragment(fragment: Fragment, container: Int, isActionPerformed: Boolean) {
            fragment.childFragmentManager.beginTransaction().apply {
                replace(container, FragmentBanner().apply {
                    arguments = Bundle().apply { putBoolean(IS_ACTION_PERFORMED, isActionPerformed) }
                })
            }.commitAllowingStateLoss()
        }

        fun getSystemScreenOffTimeOut(context: Context): Long {
            return Settings.System.getInt(context.contentResolver, Settings.System.SCREEN_OFF_TIMEOUT).toLong()
        }

        fun getBannerProperty(context: Context?): BannerProperty? {
            val savedJson = POSAdvertisePreference.getConfigBannerJson(context)
            logBanner("configBannerJson : $savedJson")
            return Gson().fromJson(savedJson, object : TypeToken<BannerProperty>() {}.type)
        }

        fun getSSProperty(context: Context?): ScreenSaverProperty? {
            val savedJson = POSAdvertisePreference.getConfigScreenSaverJson(context)
            logSS("configSSJson : $savedJson")
            return Gson().fromJson(savedJson, object : TypeToken<ScreenSaverProperty>() {}.type)
        }

        fun getTutorialProperty(context: Context?): TutorialProperty? {
            val savedJson = POSAdvertisePreference.getConfigTutorialJson(context)
            logTut("configTutorialJson : $savedJson")
            return Gson().fromJson(savedJson, object : TypeToken<TutorialProperty>() {}.type)
        }
    }
}