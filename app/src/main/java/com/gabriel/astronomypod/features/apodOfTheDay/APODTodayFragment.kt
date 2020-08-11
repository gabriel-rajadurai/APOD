package com.gabriel.astronomypod.features.apodOfTheDay

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.gabriel.astronomypod.ApodApplication
import com.gabriel.astronomypod.R
import com.gabriel.astronomypod.common.ScaleType
import com.gabriel.astronomypod.common.gone
import com.gabriel.astronomypod.common.loadUrl
import com.gabriel.astronomypod.common.visible
import com.gabriel.data.models.APOD
import kotlinx.android.synthetic.main.apod_today_fragment.*
import kotlinx.android.synthetic.main.apod_today_fragment.loadingView
import kotlinx.android.synthetic.main.apod_today_fragment.tvError
import javax.inject.Inject

class APODTodayFragment : Fragment() {

    @Inject
    lateinit var viewModel: APODTodayViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (requireActivity().application as ApodApplication).appGraph.inject(this)
        return inflater.inflate(R.layout.apod_today_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()

        btDiscoverMore.setOnClickListener {
            findNavController().navigate(R.id.apodListFragment)
        }
        startLoadAnimation()
    }

    private fun startLoadAnimation() {
        loadingView.startLoadAnimation()
        loadingView.setLoadingText(getString(R.string.load_today_picture))
    }

    private fun setupObservers() {
        viewModel.apodOfTheDay.observe(viewLifecycleOwner, Observer {
            it?.let { apod ->
                tvError.gone()
                tvTitle.text = apod.title
                tvDescription.text = apod.explanation
                if (apod.mediaType == APOD.MEDIA_TYPE_IMAGE)
                    ivApod.loadUrl(apod.hdUrl ?: apod.url, ScaleType.CENTER_CROP) {
                        stopLoadAnimation()
                        btDiscoverMore.visible()
                        tvTodayPicture.visible()
                    }
                else {
                    ivApod.setImageResource(R.drawable.ic_play)
                    loadingView.stopLoadAnimation()
                    loadingView.gone()
                    btDiscoverMore.visible()
                    tvTodayPicture.visible()
                }
            } ?: run {
                if (viewModel.error.value.isNullOrBlank()) {
                    tvError.visible()
                    tvError.text = getString(R.string.error_unable_to_fetch)
                }
                stopLoadAnimation()
                btDiscoverMore.visible()
            }
        })
        viewModel.error.observe(viewLifecycleOwner, Observer {
            loadingView.gone()
            tvError.visible()
            tvError.text = it
        })
    }

    private fun stopLoadAnimation() {
        loadingView.stopLoadAnimation()
        loadingView.gone()
    }

}