package com.example.openremote.request

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import com.example.openremote.R
import com.example.openremote.databinding.ActivityRequestBinding
import com.example.openremote.databinding.LocationAccessDialogBinding
import com.example.openremote.details.DetailsActivity
import com.example.openremote.details.InsightsActivity
import com.example.openremote.home.HomeActivity
import com.example.openremote.util.Const.permissionId
import com.example.openremote.util.Variables.currentAddress
import com.example.openremote.util.Variables.currentLat
import com.example.openremote.util.Variables.currentLong
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.*

class RequestActivity : AppCompatActivity() {

    private var _binding: ActivityRequestBinding? = null
    private val binding get() = _binding!!
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var locationAccessDialogBinding: LocationAccessDialogBinding
    var currentLat : Double? = null
    var currentLong : Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest()
        triggerAccess()
        nextPage()
    }

    private fun triggerAccess() {
        binding.accessLocation.setOnClickListener {
            locationRequest()
        }
    }

    private fun nextPage() {
        binding.continuous.setOnClickListener {
            val intent = Intent(this@RequestActivity, HomeActivity::class.java)
            intent.putExtra("lat", currentLat)
            intent.putExtra("lat", currentLong)
            startActivity(Intent(intent))
        }
    }

    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun locationRequest() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    val location: Location? = task.result
                    if (location != null) {
                        val geocoder = Geocoder(this, Locale.getDefault())
                        val list: List<Address> =
                            geocoder.getFromLocation(location.latitude, location.longitude, 1) as List<Address>
                        currentLat = list[0].latitude
                        currentLong = list[0].longitude
                        currentAddress = list[0].getAddressLine(0)
                        Log.e("Address", currentAddress.toString())
                    }
                }
            } else {
                Toast.makeText(this, "Please turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled (
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun requestPermissions() {
        val dialog = Dialog(this, R.style.MyAlertDialogTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        locationAccessDialogBinding = LocationAccessDialogBinding.inflate(layoutInflater)
        dialog.setContentView(locationAccessDialogBinding.root)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        locationAccessDialogBinding.yes.setOnClickListener {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                permissionId
            )
            dialog.dismiss()
        }
        locationAccessDialogBinding.no.setOnClickListener {
            dialog.dismiss()
            binding.welcomeBackground.visibility = View.GONE
            binding.notificationBackground.isVisible = true
        }
        dialog.show()
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == permissionId) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                locationRequest()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}