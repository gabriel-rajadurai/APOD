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
import com.gabriel.astronomypod.common.ScaleType
import com.gabriel.astronomypod.common.loadUrl
import com.gabriel.astronomypod.common.onComplete
import com.gabriel.astronomypod.common.visible
import kotlinx.android.synthetic.main.apod_today_fragment.*
import javax.inject.Inject

class APODTodayFragment : Fragment() {

    @Inject
    lateinit var viewModel: APODTodayViewModel
    private val anim1 by lazy {
        ivShape1.animate().rotationBy(360f).setDuration(1000)
    }
    private val anim2 by lazy {
        ivShape2.animate().rotationBy(360f).setDuration(1000)
    }
    private val anim3 by lazy {
        ivShape3.animate().rotationBy(360f).setDuration(1000)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (requireActivity().application as ApodApplication).appGraph.inject(this)
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
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
        anim1.onComplete {
            anim2.start()
            anim2.onComplete {
                anim3.start()
                anim3.onComplete {
                    anim1.start()
                }
            }
        }
        anim1.start()
    }

    private fun setupObservers() {
        viewModel.apodOfTheDay.observe(viewLifecycleOwner, Observer {
            it?.let { apod ->
                stopLoadAnimation()
                btDiscoverMore.visible()
                ivApod.loadUrl(apod.hdUrl ?: apod.url, ScaleType.CENTER_CROP)
            }
        })
    }

    private fun stopLoadAnimation() {
        anim1.cancel()
        anim2.cancel()
        anim3.cancel()
    }

}