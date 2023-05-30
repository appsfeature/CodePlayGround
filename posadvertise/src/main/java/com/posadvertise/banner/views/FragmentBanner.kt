package com.posadvertise.banner.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.posadvertise.POSAdvertise
import com.posadvertise.POSAdvertiseCallback
import com.posadvertise.banner.POSBanner
import com.posadvertise.databinding.AdvFragmentBannerBinding
import com.posadvertise.util.POSAdvertiseConstants
import com.posadvertise.util.POSAutoSlider
import com.posadvertise.util.common.AdvertiseModel
import com.posadvertise.util.common.AdvertiseType
import com.posadvertise.util.common.AdvertiseViewModel


const val IS_ACTION_PERFORMED = "isActionPerformed"

class FragmentBanner : Fragment() {

    private var isActionPerformed: Boolean = false
    private var viewBinding: AdvFragmentBannerBinding? = null
    private lateinit var viewModel: AdvertiseViewModel
    private var autoSlider : POSAutoSlider? = null

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = AdvFragmentBannerBinding.inflate(layoutInflater, container, false)
        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isActionPerformed = arguments?.getBoolean(IS_ACTION_PERFORMED, false) ?: false
        viewModel = ViewModelProvider(requireActivity())[AdvertiseViewModel::class.java]
        autoSlider = POSAutoSlider(viewBinding?.viewPager, viewBinding?.indicatorView, getSliderDelayTime())
        autoSlider?.apply {
            designType = AdvertiseType.Banner
            initUi(lifecycle, object : POSAdvertiseCallback.OnClickListener {
                override fun onItemClicked(view: View?, item: AdvertiseModel?) {
                    if(isActionPerformed) {
                        POSAdvertise.mListener?.onBannerItemClicked(view?.context, item)
                    }
                }
            })
        }

        viewModel.apply {
            loadBannerData()
            liveBannerList.observe(viewLifecycleOwner, Observer {
                autoSlider?.loadViewPager(it)
            })
        }
    }

    private fun getSliderDelayTime(): Long {
        return POSBanner.property?.getBannerTransitionTime() ?: POSAdvertiseConstants.DefaultTransitionTime
    }


}