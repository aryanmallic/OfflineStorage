package com.droid.offlineStorage.ui.map

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.droid.offlineStorage.R
import com.droid.offlineStorage.utils.Constants
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap

    private var lat: Double = 0.0
    private var lng: Double = 0.0

    companion object {
        fun start(mActivity: Activity, lat: Double, lng: Double) {
            val intent = Intent(mActivity, MapActivity::class.java)
            intent.putExtra(Constants.INTENT_LAT, lat)
            intent.putExtra(Constants.INTENT_LNG, lng)
            mActivity.startActivity(intent)
            mActivity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        supportActionBar?.let {
            it.apply {
                setDisplayHomeAsUpEnabled(true)
                title = ""
            }
        }

        intent.extras?.let {
            lat = it.getDouble(Constants.INTENT_LAT, 0.0)
            lng = it.getDouble(Constants.INTENT_LNG, 0.0)
        }


        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        setMarker(lat, lng)
    }

    private fun setMarker(latitude: Double, longitude: Double) {
        val position = LatLng(latitude, longitude)
        mMap.addMarker(MarkerOptions().position(position).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15f))
    }
}