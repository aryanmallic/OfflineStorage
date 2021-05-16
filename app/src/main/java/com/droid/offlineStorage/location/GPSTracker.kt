package com.droid.offlineStorage.location

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.*
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import java.io.IOException
import java.util.*
import android.location.LocationManager

class GPSTracker(private val mContext: Activity) : Service(), LocationListener {

    // flag for GPS Status
    private var isGPSEnabled = false

    // flag for network status
    private var isNetworkEnabled = false

    // flag for GPS Tracking is enabled
    /**
     * GPSTracker isGPSTrackingEnabled getter.
     * Check GPS/wifi is enabled
     */
    var isGPSTrackingEnabled = false
        internal set

    private var location: Location? = null
    private var latitude: Double = 0.toDouble()
    private var longitude: Double = 0.toDouble()

    // How many Geocoder should return our GPSTracker
    private var geocoderMaxResults = 1

    // Declaring a Location Manager
    private var locationManager: LocationManager? = null

    // Store LocationManager.GPS_PROVIDER or LocationManager.NETWORK_PROVIDER information
    private var providerInfo: String? = null


    companion object {

        // Get Class Name
        private val TAG = GPSTracker::class.java.name

        // The minimum distance to change updates in meters
        private const val MIN_DISTANCE_CHANGE_FOR_UPDATES: Long = 10 // 10 meters

        // The minimum time between updates in milliseconds
        private const val MIN_TIME_BW_UPDATES = (1000 * 20 * 1).toLong() // 1 minute
    }


    init {
        getLocation()
    }


    /**
     * Try to get my current location by GPS or Network Provider
     */
    @SuppressLint("MissingPermission")
    private fun getLocation() {

        try {
            locationManager = mContext.getSystemService(LOCATION_SERVICE) as LocationManager?

            //getting GPS status
            isGPSEnabled = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)

            //getting network status
            isNetworkEnabled = locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            // Try to get location if you GPS Service is enabled
            if (isGPSEnabled) {
                this.isGPSTrackingEnabled = true

                Log.d(TAG, "Application use GPS Service")

                /*
                 * This provider determines location using
                 * satellites. Depending on conditions, this provider may take a while to return
                 * a location fix.
                 */

                providerInfo = LocationManager.GPS_PROVIDER

            } else if (isNetworkEnabled) { // Try to get location if you Network Service is enabled
                this.isGPSTrackingEnabled = true

                Log.d(TAG, "Application use Network State to get GPS coordinates")

                /*
                 * This provider determines location based on
                 * availability of cell tower and WiFi access points. Results are retrieved
                 * by means of a network lookup.
                 */
                providerInfo = LocationManager.NETWORK_PROVIDER

            }

            // Application can use GPS or Network Provider


            providerInfo?.let {
                locationManager?.let { locManager ->
                    if (it.isNotEmpty()) {
                        locManager.requestLocationUpdates(
                            it,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(),
                            this
                        )

                        location = locManager.getLastKnownLocation(it)
                        // location = getLastKnownLocation()
                        updateGPSCoordinates()
                    }
                }
            }

        } catch (e: Exception) {
            //e.printStackTrace();
            Log.e(TAG, "Impossible to connect to LocationManager", e)
        }

    }

    @SuppressLint("MissingPermission")
    private fun getLastKnownLocation(): Location? {
        locationManager =
            applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        var bestLocation: Location? = null
        locationManager?.let {
            val providers = it.getProviders(true)

            for (provider in providers) {
                val l = it.getLastKnownLocation(provider) ?: continue
                if (bestLocation == null /*|| l.accuracy < bestLocation!!.accuracy*/) {
                    // Found best last known location: %s", l);
                    bestLocation = l
                }
            }
        }


        return bestLocation
    }

    /**
     * Update GPSTracker latitude and longitude
     */
    private fun updateGPSCoordinates() {
        location?.let {
            latitude = it.latitude
            longitude = it.longitude
        }
    }

    /**
     * GPSTracker latitude getter and setter
     * @return latitude
     */
    fun getLatitude(): Double {
        location?.let {
            latitude = it.latitude
        }
        return latitude
    }

    /**
     * GPSTracker longitude getter and setter
     * @return
     */
    fun getLongitude(): Double {
        location?.let {
            longitude = it.longitude
        }
        return longitude
    }

    /**
     * GPSTracker isGPSTrackingEnabled getter.
     * Check GPS/wifi is enabled
     */
    fun getIsGPSTrackingEnabled(): Boolean {

        return this.isGPSTrackingEnabled
    }

    /**
     * Stop using GPS listener
     * Calling this method will stop using GPS in your app
     */
    fun stopUsingGPS() {
        locationManager?.let {
            it.removeUpdates(this@GPSTracker)
        }
    }

    /**
     * Function to show settings alert dialog
     */
    /*fun showSettingsAlert() {
        val alertDialog = AlertDialog.Builder(mContext)

        //Setting Dialog Title
        alertDialog.setTitle(R.string.GPSAlertDialogTitle)

        //Setting Dialog Message
        alertDialog.setMessage(R.string.GPSAlertDialogMessage)

        //On Pressing Setting button
        alertDialog.setPositiveButton(R.string.action_settings,
            DialogInterface.OnClickListener { dialog, which ->
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                mContext.startActivity(intent)
            })

        //On pressing cancel button
        alertDialog.setNegativeButton(R.string.cancel,
            DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

        alertDialog.show()
    }*/

    /**
     * Get list of address by latitude and longitude
     * @return null or List<Address>
    </Address> */
    fun getGeocoderAddress(context: Context): List<Address>? {

        location?.let {

            val geoCoder = Geocoder(context, Locale.ENGLISH)

            try {
                return geoCoder.getFromLocation(latitude, longitude, geocoderMaxResults)
            } catch (e: IOException) {
                //e.printStackTrace();
                Log.e(TAG, "Impossible to connect to Geocoder", e)
            }

        }

        return null
    }

    /**
     * Try to get AddressLine
     * @return null or addressLine
     */
    fun getAddressLine(context: Context): String? {
        val addresses = getGeocoderAddress(context)

        if (addresses != null && addresses.size > 0) {
            val address = addresses[0]

            return address.getAddressLine(0)
        } else {
            return null
        }
    }

    /**
     * Try to get Locality
     * @return null or locality
     */
    fun getLocality(context: Context): String? {
        val addresses = getGeocoderAddress(context)

        if (addresses != null && addresses.size > 0) {
            val address = addresses[0]

            return address.locality
        } else {
            return null
        }
    }

    /**
     * Try to get Postal Code
     * @return null or postalCode
     */
    fun getPostalCode(context: Context): String? {
        val addresses = getGeocoderAddress(context)

        return if (addresses != null && addresses.isNotEmpty()) {
            val address = addresses[0]

            address.postalCode
        } else {
            null
        }
    }

    /**
     * Try to get CountryName
     * @return null or postalCode
     */
    fun getCountryName(context: Context): String? {
        val addresses = getGeocoderAddress(context)
        if (addresses != null && addresses.isNotEmpty()) {
            val address = addresses[0]

            return address.countryName
        } else {
            return null
        }
    }

    override fun onLocationChanged(location: Location) {}

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}

    override fun onProviderEnabled(provider: String) {}

    override fun onProviderDisabled(provider: String) {}

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

}