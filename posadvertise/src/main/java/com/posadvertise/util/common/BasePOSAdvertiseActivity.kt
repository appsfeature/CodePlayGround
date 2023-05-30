package com.posadvertise.util.common

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.posadvertise.POSAdvertise

open class BasePOSAdvertiseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        //use this code before super.onCreate method.
        POSAdvertise.registerScreenSaver(this)
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        POSAdvertise.unregisterScreenSaver()
    }
}