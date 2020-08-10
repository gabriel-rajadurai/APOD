package com.gabriel.astronomypod.features.apodOfTheDay

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.gabriel.astronomypod.ApodApplication
import com.gabriel.astronomypod.R
import com.gabriel.astronomypod.common.*
import com.gabriel.data.models.APOD
import kotlinx.android.synthetic.main.activity_view_apod.*
import kotlinx.android.synthetic.main.apod_list_fragment.*
import kotlinx.android.synthetic.main.apod_today_fragment.*
import kotlinx.android.synthetic.main.apod_today_fragment.ivApod
import kotlinx.android.synthetic.main.apod_today_fragment.loadingView
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
                if (apod.mediaType == APOD.MEDIA_TYPE_IMAGE)
                    ivApod.loadUrl(apod.hdUrl ?: apod.url, ScaleType.CENTER_CROP) {
                        stopLoadAnimation()
                        btDiscoverMore.visible()
                    }
                else {
                    ivApod.setImageResource(R.drawable.ic_play)
                    loadingView.stopLoadAnimation()
                    loadingView.gone()
                    btDiscoverMore.visible()
                }
            }
        })
    }

    private fun stopLoadAnimation() {
        loadingView.stopLoadAnimation()
        loadingView.gone()
    }

}