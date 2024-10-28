package com.gabriel.astronomypod.features.apodOfTheDay

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.gabriel.astronomypod.R
import com.gabriel.astronomypod.common.NetworkStateReceiver
import com.gabriel.astronomypod.common.ScaleType
import com.gabriel.astronomypod.common.loadUrl
import com.gabriel.astronomypod.databinding.ApodTodayFragmentBinding
import com.gabriel.data.models.APOD
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class APODTodayFragment : Fragment(), NetworkStateReceiver.NetworkStateListener {

  lateinit var binding: ApodTodayFragmentBinding
  private val viewModel: APODTodayViewModel by viewModels()
  private val networkStateReceiver by lazy {
    NetworkStateReceiver(requireContext(), this)
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = ApodTodayFragmentBinding.inflate(
      inflater,
      container,
      false
    )
    binding.lifecycleOwner = this
    binding.model = viewModel
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
    with(binding) {
      tvTodayPicture.isVisible = false
      tvTitle.isVisible = false
      tvDescription.isVisible = false
      btDiscoverMore.isVisible = false
      loadingView.startLoadAnimation()
      loadingView.setLoadingText(getString(R.string.load_today_picture))
    }
  }

  private fun setupObservers() {
    viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
      if (isLoading) {
        startLoadAnimation()
      } else {
        stopLoadAnimation()
      }
    }
    viewModel.apodOfTheDay.observe(viewLifecycleOwner) {
      with(binding) {
        it?.let { apod ->
          tvTitle.text = apod.title
          tvDescription.text = apod.explanation

          when (apod.mediaType) {
            APOD.MEDIA_TYPE_IMAGE -> ivApod.loadUrl(
              (apod.hdUrl ?: apod.url)!!,
              ScaleType.CENTER_CROP
            ) {
              viewModel.isLoading.value = false
              tvTitle.isVisible = true
              tvDescription.isVisible = true
              btDiscoverMore.isVisible = true
              tvTodayPicture.isVisible = true
            }

            APOD.MEDIA_TYPE_VIDEO -> {
              apod.thumbnail?.let {
                binding.ivApod.loadUrl(it, ScaleType.CENTER_CROP) {}
              } ?: binding.ivApod.setImageResource(R.drawable.ic_play)
            }

            else -> {
              viewModel.isLoading.value = false
              tvTitle.isVisible = true
              tvDescription.isVisible = true
              btDiscoverMore.isVisible = true
              tvTodayPicture.isVisible = true
            }
          }
        } ?: run {
          if (viewModel.error.value.isNullOrBlank()) {
            tvTitle.isVisible = false
            tvDescription.isVisible = false
            viewModel.error.value = getString(R.string.error_unable_to_fetch)
          }
        }
      }
    }
  }

  private fun stopLoadAnimation() {
    with(binding) {
      loadingView.stopLoadAnimation()
    }
  }

  override fun onNetworkLost() {
    if (viewModel.apodOfTheDay.value == null) {
      with(binding) {
        tvTitle.isVisible = false
        tvDescription.isVisible = false
        viewModel.error.value = getString(R.string.error_unable_to_fetch)
      }
      stopLoadAnimation()
    }
  }

  override fun onNetworkConnected() {
    if (viewModel.apodOfTheDay.value == null) {
      startLoadAnimation()
      viewModel.fetchApodOfTheDay()
    }
  }

}