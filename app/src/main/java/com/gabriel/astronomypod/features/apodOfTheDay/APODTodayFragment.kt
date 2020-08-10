package com.gabriel.astronomypod.features.apodOfTheDay

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.gabriel.astronomypod.R
import com.gabriel.astronomypod.common.ScaleType
import com.gabriel.astronomypod.common.loadUrl
import kotlinx.android.synthetic.main.apod_today_fragment.*

class APODTodayFragment : Fragment() {

    private val viewModel: APODTodayViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        return inflater.inflate(R.layout.apod_today_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        btDiscoverMore.setOnClickListener {
            findNavController().navigate(R.id.apodListFragment)
        }
    }

    private fun setupObservers() {
        viewModel.apodOfTheDay.observe(viewLifecycleOwner, Observer {
            it?.let { apod ->
                ivApod.loadUrl(apod.hdUrl ?: apod.url, ScaleType.CENTER_CROP)
            }
        })
    }

}