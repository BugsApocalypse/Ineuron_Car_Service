package com.adityagupta.ineuron

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.adityagupta.ineuron.data.users.user
import com.adityagupta.ineuron.data.users.userItem

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.adityagupta.ineuron.databinding.ActivityNearbyServiceCenterGeoBinding
import com.adityagupta.ineuron.helpers.RetrofitHelper
import com.adityagupta.ineuron.interfaces.DBApi
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.Marker
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import org.json.JSONObject


class ModalBottomSheet() : BottomSheetDialogFragment() {
    var userList = mutableListOf<userItem>()
    var id = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val something = inflater.inflate(R.layout.workshop_mini_details_bottom_sheet, container, false)
        val button = something.findViewById<Button>(R.id.bsBookServiceButton)
        val titleTextView = something.findViewById<TextView>(R.id.bsServiceCenterTitle)
        for(i in userList.indices){
            Log.i("result",userList[i].admin_id.toString() )
            if(userList[i].admin_id.toString() == id){
                titleTextView.text = userList[i].title
                titleTextView.text = userList[i].name
                Log.i("resulttt", userList[i].admin_id.toString())
                break
            }
        }
        button.setOnClickListener {
            val intent = Intent(context, ServiceCentreInfoActivity::class.java)
            for(i in userList.indices){
                if(userList[i].admin_id.toString() == id){
                    intent.putExtra("title", userList[i].title )
                    intent.putExtra("number", userList[i].phone_number)
                    intent.putExtra("email", userList[i].email)
                    intent.putExtra("lat", userList[i].latitude)
                    intent.putExtra("lon", userList[i].longitude)
                    break
                }
            }
            startActivity(intent)

        }

        return something
    }



    companion object {
        const val TAG = "ModalBottomSheet"
    }
}

class NearbyServiceCenterGeoActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    var usersList = mutableListOf<userItem>()
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityNearbyServiceCenterGeoBinding
    private var cameraPosition: CameraPosition? = null
    private lateinit var placesClient: PlacesClient
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val modalBottomSheet = ModalBottomSheet()

    private val defaultLocation = LatLng(-33.8523341, 151.2106085)
    private var locationPermissionGranted = false
    private var lastKnownLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION)
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION)
        }

        binding = ActivityNearbyServiceCenterGeoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        GlobalScope.launch {
            val oxfordApi = RetrofitHelper.getInstance().create(DBApi::class.java)
            val users = oxfordApi.getUsers().body()

            val jsonObject = JSONObject()
            jsonObject.put("name", "Jack")
            jsonObject.put("salary", "3540")
            jsonObject.put("age", "23")

            val jsonObjectString = jsonObject.toString()
            val mediaType = "application/json; charset=utf-8".toMediaType()

            val requestBody = RequestBody.create(mediaType, jsonObjectString)
            val response = oxfordApi.createEmployee(requestBody)

            runOnUiThread(Runnable {
                if (users != null) {
                    for (i in users.indices) {
                        mMap.addMarker(
                            MarkerOptions()
                                .position(LatLng(users[i].latitude, users[i].longitude))
                                .title(users[i].admin_id.toString())

                        )
                        usersList.add(users[i])

                    }

                }
                Log.i("result", users.toString())

            })

        }


        Places.initialize(applicationContext, "AIzaSyAY0O8EOmuGiO6SmQAm7s8UfTzaTI77sUk")
        placesClient = Places.createClient(this)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)



        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        mMap.let { map ->
            outState.putParcelable(KEY_CAMERA_POSITION, map.cameraPosition)
            outState.putParcelable(KEY_LOCATION, lastKnownLocation)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onMapReady(map: GoogleMap) {
        this.mMap = map

        this.mMap.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
            override fun getInfoWindow(arg0: Marker): View? {
                return null
            }

            override fun getInfoContents(p0: Marker): View? {
                return null
            }


        })

        val sydney = LatLng(-33.852, 151.211)
        mMap.addMarker(
            MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney")
        )

        getLocationPermission()
        updateLocationUI()
        getDeviceLocation()

        mMap.setOnMarkerClickListener (this)

    }


    private fun getLocationPermission() {

        if (ContextCompat.checkSelfPermission(this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        }
    }

    @SuppressLint("MissingPermission")
    private fun updateLocationUI() {
        try {
            if (locationPermissionGranted) {
                mMap.isMyLocationEnabled = true
                mMap.uiSettings.isMyLocationButtonEnabled = true
            } else {
                mMap.isMyLocationEnabled = false
                mMap.uiSettings.isMyLocationButtonEnabled = false
                lastKnownLocation = null
                getLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    @SuppressLint("MissingPermission")
    private fun getDeviceLocation() {
        try {
            if (locationPermissionGranted) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        lastKnownLocation = task.result
                        if (lastKnownLocation != null) {
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                LatLng(lastKnownLocation!!.latitude,
                                    lastKnownLocation!!.longitude), DEFAULT_ZOOM.toFloat()))
                            mMap.addMarker(MarkerOptions().position(LatLng(lastKnownLocation!!.latitude, lastKnownLocation!!.longitude)))
                        }
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.")
                        Log.e(TAG, "Exception: %s", task.exception)
                        mMap.moveCamera(CameraUpdateFactory
                            .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat()))
                        mMap.uiSettings.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    companion object {
        private val TAG = NearbyServiceCenterGeoActivity::class.java.simpleName
        private const val DEFAULT_ZOOM = 15
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
        private const val KEY_CAMERA_POSITION = "camera_position"
        private const val KEY_LOCATION = "location"
        private const val M_MAX_ENTRIES = 5
    }

    override fun onMarkerClick(p0: Marker): Boolean {
        modalBottomSheet.userList = usersList
        modalBottomSheet.id = p0.title.toString()
        Log.i("resulttt", p0.title.toString())

        modalBottomSheet.show(supportFragmentManager, ModalBottomSheet.TAG)

        return false
    }


}