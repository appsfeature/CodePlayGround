package com.posadvertise.util.common

import androidx.annotation.IntDef




//@IntDef(AdvertiseType.Banner, AdvertiseType.ScreenSaver, AdvertiseType.Tutorials)
//@Retention(AnnotationRetention.SOURCE)
//annotation class AdvertiseType{
//    companion object{
//        const val Banner = 0
//        const val ScreenSaver = 1
//        const val Tutorials = 2
//    }
//}

enum class AdvertiseType(val value : Int){
    Banner(0),
    ScreenSaver(1),
    Tutorials(2)
}