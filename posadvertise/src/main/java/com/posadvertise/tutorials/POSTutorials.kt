package com.posadvertise.tutorials

import android.app.Activity
import android.util.Log
import com.posadvertise.POSAdvertise
import com.posadvertise.tutorials.model.TutorialProperty

fun logTut(message: String){
    if(POSAdvertise.isDebugMode) {
        Log.d(POSTutorials::class.java.simpleName, message)
    }
}

object POSTutorials{

    var property: TutorialProperty? = null
        get() {
            if(field == null) field = TutorialProperty()
            return field
        }

    fun open(activity: Activity) {
        property?.let {
            if(it.isEnable) {
                //start tutorial activity
            }
        }

    }
    fun open(activity: Activity, tutorialId : Int) {
        property?.let {
            if(it.isEnable) {
                //start tutorial activity
            }
        }

    }
}