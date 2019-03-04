package com.thgcode.randomcat.ui

import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.perf.FirebasePerformance
import com.thgcode.randomcat.R
import com.thgcode.randomcat.databinding.ActivityMainBinding
import com.thgcode.randomcat.model.ConnectionModel
import com.thgcode.randomcat.network.ConnectionLiveData
import com.thgcode.randomcat.utils.PERMISSION_SHARED
import com.thgcode.randomcat.utils.contentView
import com.thgcode.randomcat.utils.sharedImage
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by contentView(R.layout.activity_main)
    private val viewModel: CatViewModel by lazy {
        ViewModelProviders.of(this).get(CatViewModel::class.java)
    }
    private var errorSnackbar: Snackbar? = null
    private val connectionLiveData: ConnectionLiveData by lazy {
        ConnectionLiveData(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupViewModel()
        FirebasePerformance.getInstance().newTrace("my_trace").start()

        val mp: MediaPlayer = MediaPlayer.create(this, R.raw.gato_mia)
        btnNewCat.setOnClickListener {
            viewModel.loadRandomCat()
            mp.start()
            ivCat.isDrawingCacheEnabled = false
        }

        btnSharedCat.setOnClickListener {
            checkPermission()
        }

        checkingConnection()
    }

    private fun setupViewModel() {
        binding.viewModel = viewModel
        viewModel.errorMessage.observe(this, Observer { errorMessage ->
            errorMessage?.let {
                showError(errorMessage)
            } ?: hideError()
        })

        viewModel.loadingCat.observe(this, Observer { cat ->
            binding.cat = cat
        })
    }

    private fun checkPermission() {
        val permissionCheck =
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PERMISSION_SHARED
            )
        } else {
            ivCat.sharedImage(contentResolver)
        }
    }

    private fun showError(@StringRes errorMessage: Int) {
        errorSnackbar = Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_INDEFINITE)
        errorSnackbar?.setAction(com.thgcode.randomcat.R.string.retry, viewModel.clickListener)
        errorSnackbar?.show()
    }

    private fun hideError() {
        errorSnackbar?.dismiss()
    }

    private fun checkingConnection() {
        connectionLiveData.observe(this, Observer<ConnectionModel> {
            if (it.isConnected) {
                when (it.type) {
                    ConnectivityManager.TYPE_WIFI -> managerVisibility(true)
                    ConnectivityManager.TYPE_MOBILE -> managerVisibility(true)
                }
            } else managerVisibility(false)
        })
    }

    private fun managerVisibility(isVisible: Boolean) {

        if (isVisible) {
            successGroup.visibility = View.VISIBLE
            errorGroup.visibility = View.GONE
        } else {
            successGroup.visibility = View.GONE
            errorGroup.visibility = View.VISIBLE
        }
    }
}
