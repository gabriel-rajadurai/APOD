package com.gabriel.astronomypod.features.apodList

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Parcel
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import androidx.annotation.StringRes
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.gabriel.astronomypod.ApodApplication
import com.gabriel.astronomypod.R
import com.gabriel.astronomypod.common.*
import com.gabriel.astronomypod.features.viewApod.ViewApodActivity
import com.gabriel.data.models.APOD
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.apod_list_fragment.*
import kotlinx.android.synthetic.main.apod_list_fragment.loadingView
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

class ApodListFragment : Fragment(), ApodListAdapter.ApodItemListener,
    NetworkStateReceiver.NetworkStateListener {

    @Inject
    lateinit var viewModel: ApodListViewModel
    private val adapter by lazy { ApodListAdapter(this) }
    private val permissionManager by lazy { PermissionManager(this) }
    private val networkStateReceiver by lazy {
        NetworkStateReceiver(requireContext(), this)
    }
    private val datePicker by lazy {
        MaterialDatePicker.Builder.datePicker()
            .setCalendarConstraints(
                CalendarConstraints.Builder()
                    .setEnd(Calendar.getInstance().timeInMillis)
                    .setValidator(object : CalendarConstraints.DateValidator {
                        override fun writeToParcel(dest: Parcel?, flags: Int) {

                        }

                        override fun isValid(date: Long): Boolean {
                            return date <= Calendar.getInstance().timeInMillis
                        }

                        override fun describeContents(): Int {
                            return 0
                        }

                    })
                    .build()

            ).build()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (requireActivity().application as ApodApplication).appGraph.inject(this)
        return inflater.inflate(R.layout.apod_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvApod.addItemDecoration(VerticalSpacesItemDecoration(20))
        rvApod.adapter = adapter
        startLoadingAnimation()
        setupObservers()
        fabDate.setOnClickListener {
            selectDate()
        }
    }

    override fun onStart() {
        super.onStart()
        networkStateReceiver.register()
    }

    override fun onStop() {
        super.onStop()
        networkStateReceiver.unregister()
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
        ShareCompat.IntentBuilder.from(requireActivity())
            .setType("text/plain")
            .setChooserTitle("Share APOD")
            .setSubject(apod.title)
            .setText("${apod.explanation} \n\n View it here -> ${apod.hdUrl ?: apod.url}")
            .startChooser()
    }

    override fun downloadApod(apod: APOD) {
        permissionManager.requestPermissions(
            PermissionManager.PERMISSION.STORAGE,
            listener = object :
                PermissionManager.PermissionsRequestListener {
                override fun onPermissionGranted() {
                    val ext = MimeTypeMap.getFileExtensionFromUrl(apod.hdUrl ?: apod.url)
                    val downloadRequest =
                        DownloadManager.Request(Uri.parse(apod.hdUrl ?: apod.url))
                            .setDestinationInExternalPublicDir(
                                Environment.DIRECTORY_DOWNLOADS,
                                "APOD/${apod.title}_${apod.date}.$ext"
                            )
                            .setDescription("Downloading Image")
                            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                            .setAllowedOverMetered(true)
                            .setAllowedOverRoaming(true)
                            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

                    val dm =
                        requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                    dm.enqueue(downloadRequest)

                }

                override fun onPermissionDenied() {

                }

            })
    }

    override fun viewApod(apod: APOD) {
        if (apod.mediaType == APOD.MEDIA_TYPE_IMAGE)
            findNavController().navigate(R.id.viewApodActivity, Bundle().apply {
                putString(ViewApodActivity.EXTRA_APOD_DATE, apod.date)
            })
        else if (apod.mediaType == APOD.MEDIA_TYPE_VIDEO) {
            CustomTabsIntent.Builder()
                .build()
                .launchUrl(requireContext(), Uri.parse(apod.url))
        }
    }

    private fun setupObservers() {
        viewModel.fetchApodList().observe(viewLifecycleOwner, Observer {
            refreshLayout.isRefreshing = false
            loadingView.stopLoadAnimation()
            loadingView.gone()
            tvError.gone()
            rvApod.visible()
            fabDate.visible()
            adapter.submitList(it)
            if (it.isEmpty()) {
                loadingView.gone()
                tvError.visible()
                rvApod.gone()
                fabDate.gone()
                tvError.text = getString(R.string.error_unable_to_fetch)
            }
        })
    }

    private fun startLoadingAnimation() {
        loadingView.startLoadAnimation()
        loadingView.setLoadingText(getString(R.string.loading))
    }

    private fun selectDate() {
        datePicker.apply {
            addOnPositiveButtonClickListener {
                val selectedDate =
                    LocalDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneOffset.UTC)
                        .toLocalDate().format(
                            DateTimeFormatter.ofPattern(APOD.DATE_FORMAT)
                        )
                this@ApodListFragment.lifecycleScope.launch {
                    viewModel.fetchApod(selectedDate)?.let { apod ->
                        viewApod(apod)
                    } ?: snackMessage(R.string.error_unable_to_fetch_apod)
                }
            }
        }.show(childFragmentManager, "ApodListFragment")
    }

    private fun snackMessage(@StringRes messageResId: Int) {
        root.snackBar(
            getString(messageResId),
            Snackbar.LENGTH_SHORT
        )
    }

    override fun onNetworkLost() {
        snackMessage(R.string.connection_lost)
        refreshLayout.setOnRefreshListener(null)
        refreshLayout.isEnabled = false
        refreshLayout.isRefreshing = false
    }

    override fun onNetworkConnected() {
        refreshLayout.isEnabled = true
        refreshLayout.setOnRefreshListener {
            viewModel.fetchApodsFromServer()
        }
    }

}