package com.gabriel.astronomypod.features.viewApod

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.webkit.MimeTypeMap
import androidx.activity.viewModels
import androidx.core.app.ShareCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.gabriel.astronomypod.ApodApplication
import com.gabriel.astronomypod.R
import com.gabriel.astronomypod.common.*
import com.gabriel.astronomypod.databinding.ActivityViewApodBinding
import com.gabriel.data.models.APOD
import kotlinx.android.synthetic.main.activity_view_apod.*
import kotlinx.android.synthetic.main.activity_view_apod.ivApod
import kotlinx.android.synthetic.main.activity_view_apod.loadingView
import kotlinx.android.synthetic.main.apod_list_fragment.*
import kotlinx.android.synthetic.main.apod_today_fragment.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class ViewApodActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: ViewApodViewModel
    private val apodDate by lazy {
        intent?.extras?.getString(EXTRA_APOD_DATE)
            ?: throw IllegalArgumentException("APOD date cannot be null")
    }
    private val permissionManager by lazy { PermissionManager(this) }
    private var binding: ActivityViewApodBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as ApodApplication).appGraph.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityViewApodBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding?.lifecycleOwner = this
        binding?.model = viewModel

        startLoadingAnimation()
        viewModel.fetchApod(apodDate)
        setupObservers()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionManager.handleRequestPermissionResult(requestCode)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_view_apod, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.downloadApod -> {
                downloadApod()
            }
            R.id.shareApod -> {
                shareApod()
            }
            else -> return false
        }
        return true
    }

    private fun setupObservers() {
        viewModel.currentApod.observe(this, Observer {
            it?.let { apod ->
                ivApod.visible()
                loadingView.stopLoadAnimation()
                loadingView.gone()
                if (apod.mediaType == APOD.MEDIA_TYPE_IMAGE)
                    ivApod.loadUrl(apod.hdUrl ?: apod.url)
                else {
                    ivApod.setImageResource(R.drawable.ic_play)
                }
            }
        })
    }

    private fun startLoadingAnimation() {
        loadingView.startLoadAnimation()
        loadingView.setLoadingText(getString(R.string.loading_media))
    }

    private fun shareApod() {
        viewModel.currentApod.value?.let { apod ->
            ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setChooserTitle("Share APOD")
                .setSubject(apod.title)
                .setText("${apod.explanation} \n\n View it here -> ${apod.hdUrl ?: apod.url}")
                .startChooser()
        }
    }

    private fun downloadApod() {
        viewModel.currentApod.value?.let { apod ->
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

                        val dm = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                        dm.enqueue(downloadRequest)

                    }

                    override fun onPermissionDenied() {

                    }
                })
        }
    }

    companion object {
        const val EXTRA_APOD_DATE = "date"
    }
}