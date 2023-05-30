package com.posadvertise.tutorials.view

import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.helper.util.BaseConstants
import com.posadvertise.R
import com.posadvertise.databinding.AdvActivityTutorialBinding

class TutorialsActivity : AppCompatActivity() {

    private var mTitle: String? = null
    private lateinit var binding: AdvActivityTutorialBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AdvActivityTutorialBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mTitle = intent.getStringExtra(BaseConstants.EXTRA_PROPERTY)
        setUpToolBar()

        setFragment()
    }

    private fun setFragment() {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.content, TutorialListFragment())
        }.commitAllowingStateLoss()
    }

    private fun setUpToolBar() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            if (!TextUtils.isEmpty(mTitle)) {
                title = mTitle
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        updateTitle(mTitle)
        super.onBackPressed()
    }

    fun updateTitle(mTitle : String?) {
        supportActionBar?.apply {
            if (!TextUtils.isEmpty(mTitle)) {
                title = mTitle
            }
        }
    }
}