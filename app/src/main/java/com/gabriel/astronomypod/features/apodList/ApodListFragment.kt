package com.gabriel.astronomypod.features.apodList

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import androidx.annotation.StringRes
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.app.ShareCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.gabriel.astronomypod.R
import com.gabriel.astronomypod.common.NetworkStateReceiver
import com.gabriel.astronomypod.common.PermissionManager
import com.gabriel.astronomypod.common.VerticalSpacesItemDecoration
import com.gabriel.astronomypod.common.snackBar
import com.gabriel.astronomypod.databinding.ApodListFragmentBinding
import com.gabriel.astronomypod.features.viewApod.ViewApodActivity
import com.gabriel.data.models.APOD
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Calendar

@AndroidEntryPoint
class ApodListFragment : Fragment(), ApodListAdapter.ApodItemListener,
    NetworkStateReceiver.NetworkStateListener {

    lateinit var binding: ApodListFragmentBinding

    private val viewModel: ApodListViewModel by viewModels()
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
                    .setValidator(DateValidatorPointBackward.now())
                    .build()

            ).build()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ApodListFragmentBinding.inflate(
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
        binding.rvApod.addItemDecoration(VerticalSpacesItemDecoration(20))
        binding.rvApod.adapter = adapter
        startLoadingAnimation()
        setupObservers()
        binding.fabDate.setOnClickListener {
            selectDate()
        }

        //Get astronomy pictures
        viewModel.getAstronomyPictures()
    }

    override fun onStart() {
        super.onStart()
        networkStateReceiver.register()
    }

    override fun onStop() {
        super.onStop()
        networkStateReceiver.unregister()
    }

    @Deprecated("Deprecated in Java")
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
        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                binding.loadingView.startLoadAnimation()
            } else {
                binding.loadingView.stopLoadAnimation()
            }
        }
        viewModel.apodList.observe(viewLifecycleOwner) {
            with(binding) {
                refreshLayout.isRefreshing = false
                viewModel.isLoading.value = false
                fabDate.isVisible = true
                adapter.submitList(it)
                if (it.isEmpty()) {
                    viewModel.isError.value = true
                    fabDate.isVisible = false
                    tvError.text = getString(R.string.error_unable_to_fetch)
                }
            }
        }
    }

    private fun startLoadingAnimation() {
        with(binding) {
            loadingView.startLoadAnimation()
            loadingView.setLoadingText(getString(R.string.loading))
        }
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
        binding.root.snackBar(
            getString(messageResId),
            Snackbar.LENGTH_SHORT
        )
    }

    override fun onNetworkLost() {
        with(binding) {
            snackMessage(R.string.connection_lost)
            refreshLayout.setOnRefreshListener(null)
            refreshLayout.isEnabled = false
            refreshLayout.isRefreshing = false
        }
    }

    override fun onNetworkConnected() {
        with(binding) {
            refreshLayout.isEnabled = true
            refreshLayout.setOnRefreshListener {
                viewModel.getAstronomyPictures()
            }
        }
    }

}