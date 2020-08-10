package com.gabriel.astronomypod.features.apodList

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.gabriel.astronomypod.R
import com.gabriel.astronomypod.common.PermissionManager
import com.gabriel.astronomypod.common.VerticalSpacesItemDecoration
import com.gabriel.data.models.APOD
import kotlinx.android.synthetic.main.apod_list_fragment.*
import kotlinx.coroutines.launch

class ApodListFragment : Fragment(), ApodListAdapter.ApodItemListener {

    //This creates the viewModel using lazy initialization
    private val viewModel: ApodListViewModel by viewModels()
    private val adapter by lazy { ApodListAdapter(this) }
    private val permissionManager by lazy { PermissionManager(this) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.apod_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvApod.addItemDecoration(VerticalSpacesItemDecoration(20))
        rvApod.adapter = adapter
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.apodlist.observe(viewLifecycleOwner, Observer {
            progressBar.visibility = View.GONE
            adapter.submitList(it)
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionManager.handleRequestPermissionResult(requestCode)
    }

    override fun shareApod(apod: APOD) {

    }

    override fun downloadApod(apod: APOD) {
        permissionManager.requestPermissions(
            PermissionManager.PERMISSION.STORAGE,
            listener = object :
                PermissionManager.PermissionsRequestListener {
                override fun onPermissionGranted() {
                    val ext = MimeTypeMap.getFileExtensionFromUrl(apod.hdUrl)
                    val downloadRequest =
                        DownloadManager.Request(Uri.parse(apod.hdUrl))
                            .setDestinationInExternalPublicDir(
                                Environment.DIRECTORY_DOWNLOADS,
                                "APOD/${apod.title}_${apod.date}.$ext"
                            )
                            .setDescription("Downloading Image")
                            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                            .setAllowedOverMetered(true)
                            .setAllowedOverRoaming(true)
                            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

                    val dm = requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                    dm.enqueue(downloadRequest)

                }

                override fun onPermissionDenied() {

                }

            })
    }

    override fun viewApod(apod: APOD) {
        TODO("Not yet implemented")
    }

}