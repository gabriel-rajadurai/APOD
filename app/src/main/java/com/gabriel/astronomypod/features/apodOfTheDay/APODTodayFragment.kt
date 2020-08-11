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
import com.gabriel.astronomypod.common.*
import com.gabriel.data.models.APOD
import kotlinx.android.synthetic.main.apod_today_fragment.*
import kotlinx.android.synthetic.main.apod_today_fragment.loadingView
import kotlinx.android.synthetic.main.apod_today_fragment.tvError
import javax.inject.Inject

class APODTodayFragment : Fragment(), NetworkStateReceiver.NetworkStateListener {

    @Inject
    lateinit var viewModel: APODTodayViewModel
    private val networkStateReceiver by lazy {
        NetworkStateReceiver(requireContext(), this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (requireActivity().application as ApodApplication).appGraph.inject(this)
        return inflater.inflate(R.layout.apod_today_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fetchApodOfTheDay()
        setupObservers()

        btDiscoverMore.setOnClickListener {
            findNavController().navigate(R.id.apodListFragment)
        }
        startLoadAnimation()
    }

    override fun onStart() {
        super.onStart()
        networkStateReceiver.register()
    }

    override fun onStop() {
        super.onStop()
        networkStateReceiver.unregister()
    }

    private fun startLoadAnimation() {
        stopLoadAnimation()
        tvError.gone()
        tvTodayPicture.gone()
        tvTitle.gone()
        tvDescription.gone()
        btDiscoverMore.gone()
        loadingView.visible()
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
                        tvTitle.visible()
                        tvDescription.visible()
                        btDiscoverMore.visible()
                        tvTodayPicture.visible()
                    }
                else {
                    ivApod.setImageResource(R.drawable.ic_play)
                    stopLoadAnimation()
                    tvTitle.visible()
                    tvDescription.visible()
                    btDiscoverMore.visible()
                    tvTodayPicture.visible()
                }
            } ?: run {
                if (viewModel.error.value.isNullOrBlank()) {
                    tvError.visible()
                    tvTitle.gone()
                    tvDescription.gone()
                    tvError.text = getString(R.string.error_unable_to_fetch)
                }
                stopLoadAnimation()
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

    override fun onNetworkLost() {
        if(viewModel.apodOfTheDay.value == null) {
            tvError.visible()
            tvTitle.gone()
            tvDescription.gone()
            tvError.text = getString(R.string.error_unable_to_fetch)
            stopLoadAnimation()
        }
    }

    override fun onNetworkConnected() {
        if(viewModel.apodOfTheDay.value == null) {
            startLoadAnimation()
            viewModel.fetchApodOfTheDay()
        }
    }

}