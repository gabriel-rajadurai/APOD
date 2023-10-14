package com.gabriel.astronomypod.features.apodOfTheDay

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.gabriel.astronomypod.R
import com.gabriel.astronomypod.common.*
import com.gabriel.data.models.APOD
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.gabriel.astronomypod.databinding.ApodTodayFragmentBinding

@AndroidEntryPoint
class APODTodayFragment : Fragment(), NetworkStateReceiver.NetworkStateListener {

    lateinit var binding : ApodTodayFragmentBinding
    private val viewModel: APODTodayViewModel by viewModels()
    private val networkStateReceiver by lazy {
        NetworkStateReceiver(requireContext(), this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ApodTodayFragmentBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fetchApodOfTheDay()
        setupObservers()

        binding.btDiscoverMore.setOnClickListener {
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
        with(binding){
            tvError.gone()
            tvTodayPicture.gone()
            tvTitle.gone()
            tvDescription.gone()
            btDiscoverMore.gone()
            loadingView.visible()
            loadingView.startLoadAnimation()
            loadingView.setLoadingText(getString(R.string.load_today_picture))
        }
    }

    private fun setupObservers() {
        viewModel.apodOfTheDay.observe(viewLifecycleOwner, Observer {
            with(binding){
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
            }
        })
        viewModel.error.observe(viewLifecycleOwner, Observer {
            with(binding){
                loadingView.gone()
                tvError.visible()
                tvError.text = it
            }
        })
    }

    private fun stopLoadAnimation() {
        with(binding){
            loadingView.stopLoadAnimation()
            loadingView.gone()
        }
    }

    override fun onNetworkLost() {
        if(viewModel.apodOfTheDay.value == null) {
            with(binding){
                tvError.visible()
                tvTitle.gone()
                tvDescription.gone()
                tvError.text = getString(R.string.error_unable_to_fetch)
            }
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