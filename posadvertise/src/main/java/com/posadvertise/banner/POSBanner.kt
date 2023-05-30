package com.posadvertise.banner

import android.app.Activity
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.posadvertise.POSAdvertise
import com.posadvertise.banner.model.BannerProperty
import com.posadvertise.banner.views.FragmentBanner
import com.posadvertise.util.POSAdvertiseUtility
import com.posadvertise.util.common.AdvertiseModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun logBanner(message: String?){
    if(POSAdvertise.isDebugMode) {
        Log.d(POSBanner::class.java.simpleName, message ?: "")
    }
}

object POSBanner {

    const val BannerAttachDelayTime : Long = 3 * 1000

    var property: BannerProperty? = null
    get() {
        if(field == null) field = BannerProperty()
        return field
    }

    fun show(activity: Activity, container: Int) {
        if(isValidDate()) {
            logBanner("isValidDateTimeToShow = true")
            if(isValidBanners()) {
                (activity as AppCompatActivity).lifecycleScope.launch {
                    delay(getBannerAttachDelayTime())
                    //Start Task
                    activity.supportFragmentManager.beginTransaction().apply {
                        replace(container, FragmentBanner())
                    }.commitAllowingStateLoss()
                }
            }
        }else{
            logBanner("Start or End Date Expired.")
        }
    }

    fun show(fragment: Fragment, container: Int, isActionPerformed: Boolean) {
        if(isValidDate()) {
            fragment.lifecycleScope.launch {
                delay(getBannerAttachDelayTime())
                //Start Task
                POSAdvertiseUtility.mapFragment(fragment, container, isActionPerformed)
            }
        }
    }

    private fun getBannerAttachDelayTime(): Long {
        property?.let {
            return it.getBannerAttachDelayTime()
        }
        return BannerAttachDelayTime
    }

    private fun isValidDate(): Boolean {
        return isValidDate(getStartDate(), getEndDate())
    }

    private fun isValidDate(startDate : String?, endDate : String?): Boolean {
        return (System.currentTimeMillis() > POSAdvertiseUtility.getTimeInMillis(startDate)
                && System.currentTimeMillis() < POSAdvertiseUtility.getTimeInMillis(endDate))
    }


    private fun getStartDate(): String? {
        return property?.startDate
    }

    private fun getEndDate(): String? {
        return property?.endDate
    }

    private fun isValidBanners(): Boolean {
        val mList = property?.list
        mList?.let {
            for (item in mList){
                if(isValidDate(item.startDate, item.endDate)){
                    return true
                }
            }
        }
        return false
    }

    fun getValidBanners(): MutableList<AdvertiseModel> {
        val finalList: MutableList<AdvertiseModel> = mutableListOf()
        val mList = property?.list
        mList?.let {
            for (item in mList){
                if(isValidDate(item.startDate, item.endDate)){
                    finalList.add(item)
                }
            }
        }
        return finalList
    }
}
