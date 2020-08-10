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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.gabriel.astronomypod.ApodApplication
import com.gabriel.astronomypod.R
import com.gabriel.astronomypod.common.PermissionManager
import com.gabriel.astronomypod.common.VerticalSpacesItemDecoration
import com.gabriel.astronomypod.common.ViewModelFactory
import com.gabriel.astronomypod.common.gone
import com.gabriel.astronomypod.features.viewApod.ViewApodActivity
import com.gabriel.data.models.APOD
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.android.synthetic.main.apod_list_fragment.*
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

class ApodListFragment : Fragment(), ApodListAdapter.ApodItemListener {

    @Inject
    lateinit var viewModel: ApodListViewModel
    private val adapter by lazy { ApodListAdapter(this) }
    private val permissionManager by lazy { PermissionManager(this) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (requireActivity().application as ApodApplication).appGraph.inject(this)
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        return inflater.inflate(R.layout.apod_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvApod.addItemDecoration(VerticalSpacesItemDecoration(20))
        rvApod.adapter = adapter
        loadingView.startLoadAnimation()
        setupObservers()
        fabDate.setOnClickListener {
            showDatePicker()
        }
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
        findNavController().navigate(R.id.viewApodActivity, Bundle().apply {
            putString(ViewApodActivity.EXTRA_APOD_DATE, apod.date)
        })
    }

    private fun setupObservers() {
        viewModel.apodList.observe(viewLifecycleOwner, Observer {
            loadingView.stopLoadAnimation()
            loadingView.gone()
            progressBar.visibility = View.GONE
            adapter.submitList(it)
        })
    }

    private fun showDatePicker() {
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

            )
            .build().apply {
                addOnPositiveButtonClickListener {
                    findNavController().navigate(R.id.viewApodActivity, Bundle().apply {
                        val selectedDate =
                            LocalDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneOffset.UTC)
                                .toLocalDate().format(
                                    DateTimeFormatter.ofPattern(APOD.DATE_FORMAT)
                                )
                        putString(ViewApodActivity.EXTRA_APOD_DATE, selectedDate)
                    })
                }
            }
            .show(childFragmentManager, "ApodListFragment")
    }

}