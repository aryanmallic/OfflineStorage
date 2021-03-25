package com.droid.offlineStorage.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.droid.offlineStorage.R
import com.droid.offlineStorage.databinding.ActivityMainBinding
import com.droid.offlineStorage.local.ExampleDatabase
import com.droid.offlineStorage.location.GPSTracker
import com.droid.offlineStorage.model.ExResponse
import com.droid.offlineStorage.ui.edit.EditActivity
import com.droid.offlineStorage.ui.map.MapActivity
import com.droid.offlineStorage.utils.CommonUtils.appToast
import com.droid.offlineStorage.utils.CommonUtils.isNetworkConnected
import com.droid.offlineStorage.utils.Constants
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    private val locationPermissionCode = 101

    private lateinit var myListAdapter: MyListAdapter

    //private var mList: List<ExResponse> = ArrayList()
    private var listData: MutableList<ExResponse> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val dao = ExampleDatabase.getInstance(application).exampleDao
        val mainRepository = MainRepository(dao)
        val factory = MainViewModelFactory(mainRepository)
        mainViewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)

        binding.mainViewModel = mainViewModel
        binding.lifecycleOwner = this

        initList()

        callApi()

        getLocation()
    }

    override fun onResume() {
        super.onResume()

        mainViewModel.getListDataAsLD().observe(this, Observer {
            Log.d("MYTAG", "List Size: ${it.size}")
            //initList(it)
            if (listData.size > 0) listData.clear()
            listData = it as MutableList<ExResponse>
            myListAdapter.updateList(listData)
        })

    }

    private fun callApi() {
        if (this.isNetworkConnected()) {
            val latitude = GPSTracker(this).getLatitude()
            val longitude = GPSTracker(this).getLongitude()
            Log.d("TAG", "GPSTracker ACT: $latitude, $longitude")
            mainViewModel.getPostsFromApiAndStore(latitude, longitude)

        } else {
            this appToast "No internet found. Showing cached list in the view"
        }
    }

    private fun initList(/*listData: List<ExResponse>*/) {
        binding.activityMainRvList.layoutManager = LinearLayoutManager(this)
        myListAdapter = MyListAdapter(
            listData
        ) { selectedItem: ExResponse, clickType: String ->
            listItemClicked(
                selectedItem,
                clickType
            )
        }
        binding.activityMainRvList.adapter = myListAdapter
    }

    private fun getLocation() {
        if ((ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                locationPermissionCode
            )
        } else {
            // Update Location
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                this appToast "Permission Granted. Please Refresh to Continue"

                val latitude = GPSTracker(this@MainActivity).getLatitude()
                val longitude = GPSTracker(this@MainActivity).getLongitude()
                if (latitude != 0.0 && longitude != 0.0) {
                    Log.d("TAG", "GPSTracker1: Called")
                    mainViewModel.saveGeo(latitude, longitude)
                }
                Log.d("TAG", "GPSTracker1 GRANT: $latitude, $longitude")

            } else {
                this appToast "Permission Denied"
            }
        }
    }

    private fun listItemClicked(exResponse: ExResponse, clickType: String) {
        when (clickType) {
            Constants.CLICK_EDIT -> {
                EditActivity.start(this, exResponse.id, true)
            }
            Constants.CLICK_MAP -> {
                MapActivity.start(this, exResponse.lat, exResponse.lng)
            }
            Constants.CLICK_DELETE -> {
                mainViewModel.deleteById(exResponse.id)
            }
        }
    }
}