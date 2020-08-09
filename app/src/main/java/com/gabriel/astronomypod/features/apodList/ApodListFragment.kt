package com.gabriel.astronomypod.features.apodList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.gabriel.astronomypod.R
import com.gabriel.astronomypod.common.VerticalSpacesItemDecoration
import kotlinx.android.synthetic.main.apod_list_fragment.*
import kotlinx.coroutines.launch

class ApodListFragment : Fragment() {

    //This creates the viewModel using lazy initialization
    private val viewModel: ApodListViewModel by viewModels()
    private val adapter by lazy { ApodListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.apod_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvApod.addItemDecoration(VerticalSpacesItemDecoration(16))
        rvApod.adapter = adapter
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.apodlist.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }

}