package com.example.openremote.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.openremote.R
import com.example.openremote.callapi.viewmodel.AssetViewModel
import com.example.openremote.databinding.ActivityHomeBinding
import com.example.openremote.details.DetailsActivity
import com.example.openremote.details.InsightsActivity
import com.example.openremote.request.RequestActivity
import com.example.openremote.util.Const.REQUEST_PERMISSIONS_REQUEST_CODE
import com.google.android.gms.maps.model.Marker
import org.osmdroid.views.MapView
import org.osmdroid.config.Configuration.*
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.IconOverlay.ANCHOR_CENTER
import org.osmdroid.views.overlay.Marker.ANCHOR_CENTER

class HomeActivity : AppCompatActivity() {

    private var _binding: ActivityHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var map: MapView
    private val viewModel: AssetViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        map = binding.map
        map.setTileSource(TileSourceFactory.MAPNIK)
        mapConfig()
        initObserve()
        viewModel.callApi()
        initDetails()
        initInsight()
    }

    private fun initInsight() {
        binding.insight.setOnClickListener {
            startActivity(Intent(this, InsightsActivity::class.java))
        }
    }

    private fun initDetails() {
        binding.details.setOnClickListener {
            startActivity(Intent(this, DetailsActivity::class.java))
        }
    }

    private fun initObserve() {
        viewModel.callApiResult.observe(this) {result ->
            when (result) {
                is AssetViewModel.CallApiResult.ResultOk -> {
                    Log.e("Success", result.result.body().toString())
                }
                is AssetViewModel.CallApiResult.ResultError -> {
                    Log.e("Error", "Error")
                }
            }
        }
    }

    private fun mapConfig() {
        val currentLat = intent.getDoubleExtra("lat", 0.1)
        val currentLong = intent.getDoubleExtra("long", 0.1)
        Log.e("Lat", currentLat.toString())
        val mapController = map.controller
        mapController.setZoom(20)
        val startPoint = GeoPoint(currentLat, currentLong)
        mapController.setCenter(startPoint)

        //marker - location của thiết bị hiện tại
        val myLocation = org.osmdroid.views.overlay.Marker(map)
        myLocation.position = startPoint
        myLocation.icon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_location_on_24)
        myLocation.title = "Vị trí thiết bị của tôi"
        myLocation.subDescription = getDeviceInfo()
        map.overlays.add(myLocation)
        map.invalidate()
    }

    @SuppressLint("HardwareIds")
    private fun getDeviceInfo(): String {
        return "Brand: ${Build.BRAND} \n" +
                "DeviceID: ${
                    Settings.Secure.getString(
                        contentResolver,
                        Settings.Secure.ANDROID_ID
                    )
                } \n" +
                "Model: ${Build.MODEL} \n" +
                "ID: ${Build.ID} \n" +
                "SDK: ${Build.VERSION.SDK_INT} \n" +
                "Manufacture: ${Build.MANUFACTURER} \n" +
                "Brand: ${Build.BRAND} \n" +
                "User: ${Build.USER} \n" +
                "Type: ${Build.TYPE} \n" +
                "Base: ${Build.VERSION_CODES.BASE} \n" +
                "Incremental: ${Build.VERSION.INCREMENTAL} \n" +
                "Board: ${Build.BOARD} \n" +
                "Host: ${Build.HOST} \n" +
                "FingerPrint: ${Build.FINGERPRINT} \n" +
                "Version Code: ${Build.VERSION.RELEASE}"
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val permissionsToRequest = ArrayList<String>()
        var i = 0
        while (i < grantResults.size) {
            permissionsToRequest.add(permissions[i])
            i++
        }
        if (permissionsToRequest.size > 0) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                REQUEST_PERMISSIONS_REQUEST_CODE)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}