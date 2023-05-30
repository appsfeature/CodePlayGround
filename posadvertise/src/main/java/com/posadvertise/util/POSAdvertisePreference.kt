package com.posadvertise.util

import android.content.Context
import com.helper.util.BasePrefUtil

object POSAdvertisePreference {

    private const val IS_UPDATE_AVAILABLE = "is_update_available"
    private const val UPDATE_DOWNLOADED = "update_downloaded"
    private const val CONFIG_BANNER = "config_banner"
    private const val CONFIG_SCREEN_SAVER = "config_screen_saver"
    private const val CONFIG_TUTORIAL = "config_tutorial"


    fun setUpdateAvailable(context: Context?, value: Boolean) {
        BasePrefUtil.setBoolean(context, IS_UPDATE_AVAILABLE, value)
    }

    fun isUpdateAvailable(context: Context?): Boolean {
        return BasePrefUtil.getBoolean(context, IS_UPDATE_AVAILABLE, true)
    }

    fun setUpdateDownloaded(context: Context?, value: Boolean) {
        BasePrefUtil.setBoolean(context, UPDATE_DOWNLOADED, value)
    }

    fun isUpdateDownloaded(context: Context?): Boolean {
        return BasePrefUtil.getBoolean(context, UPDATE_DOWNLOADED, false)
    }

    fun getConfigBannerJson(context: Context?): String? {
        return BasePrefUtil.getString(context, CONFIG_BANNER)
    }

    fun setConfigBannerJson(context: Context?, value: String?) {
        BasePrefUtil.setString(context, CONFIG_BANNER, value)
    }

    fun getConfigScreenSaverJson(context: Context?): String? {
        return BasePrefUtil.getString(context, CONFIG_SCREEN_SAVER)
    }

    fun setConfigScreenSaverJson(context: Context?, value: String?) {
        BasePrefUtil.setString(context, CONFIG_SCREEN_SAVER, value)
    }

    fun getConfigTutorialJson(context: Context?): String? {
        return BasePrefUtil.getString(context, CONFIG_TUTORIAL)
    }

    fun setConfigTutorialJson(context: Context?, value: String?) {
        BasePrefUtil.setString(context, CONFIG_TUTORIAL, value)
    }
}