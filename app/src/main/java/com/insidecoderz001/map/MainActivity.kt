package com.insidecoderz001.map

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object{
        val  REQUESTCODE_LOACTION=1
        val geu_lat:Float = 30.2686F
        val geu_lng:Float = 77.9948F
    }

    private var mLocationPermissiionGranted:Boolean =false
    private lateinit var mMap: GoogleMap
    lateinit var cardView :CardView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cardView=findViewById(R.id.cardView)

        initMap()

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        cardView.setOnClickListener {

            if (mMap!=null){

                // Code to automate the location from anywhere to target

//                mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f))
//                val targetLocation=LatLng(30.2690, 77.995)
//                val cameraUpdate : CameraUpdate =CameraUpdateFactory.newLatLngZoom(targetLocation,17.0f)
//                mMap.animateCamera(cameraUpdate,5000,object  :GoogleMap.CancelableCallback{
//                    override fun onCancel() {
//                        Toast.makeText(this@MainActivity,"Cancel",Toast.LENGTH_SHORT).show()
//                    }
//
//                    override fun onFinish() {
//                        Toast.makeText(this@MainActivity,"Finish",Toast.LENGTH_SHORT).show()
//                    }
//
//                })


//                val bottomBound:Double =33.6620
//                val LeftBound:Double =72.9972
//                val topBound:Double =33.7472
//                val rightBound:Double =73.0879

                val bottomBound:Double =geu_lat-3.0
                val LeftBound:Double =geu_lng-3.0
                val topBound:Double =geu_lat+3.0
                val rightBound:Double = geu_lng+3.0

                val bounda : LatLngBounds =LatLngBounds(LatLng(bottomBound,LeftBound),LatLng(topBound,rightBound))

                //Help to direct to the particular region by clicking
//                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounda,1))
//                (bounda.center)


                // This code help to show the map to limited area surface only which is defined by the developer

//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(bounda.center,15.0f))


                //To restrict the user from viewing outside the range

//                mMap.setLatLngBoundsForCameraTarget(bounda)

                // This is used to define the padding of the region
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounda,400,400,1))

                showMarker(bounda.center)

            }
            mMap.mapType=GoogleMap.MAP_TYPE_SATELLITE


        }

    }

    private fun showMarker(loc:LatLng){

        val marker =MarkerOptions().position(loc)
        mMap.addMarker(marker)

    }



    private fun initMap() {
        if(okServiceOk()){
            if(checkLocationPermission()){
                Toast.makeText(this,"Ready to map !", Toast.LENGTH_SHORT).show()
            }else{
                requestLoactionPermission()
            }
        }
    }

    private fun gotoLoaction(lat: Double, lng: Double){
        val targetLocation = LatLng(lat,lng)
        val cameraUpdate : CameraUpdate =CameraUpdateFactory.newLatLngZoom(targetLocation,17.0f)
        mMap.moveCamera(cameraUpdate)
        mMap.mapType=GoogleMap.MAP_TYPE_SATELLITE

        mMap.uiSettings.isMapToolbarEnabled=true
        mMap.uiSettings.isZoomControlsEnabled=true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.mapType_none ->{
                mMap.mapType=GoogleMap.MAP_TYPE_NONE
            }
            R.id.mapType_normal ->{
                mMap.mapType=GoogleMap.MAP_TYPE_NORMAL
            }
            R.id.mapType_hybrid ->{
                mMap.mapType=GoogleMap.MAP_TYPE_HYBRID
            }
            R.id.mapType_satellite->{
                mMap.mapType=GoogleMap.MAP_TYPE_SATELLITE
            }
            R.id.mapType_terrain ->{
                mMap.mapType=GoogleMap.MAP_TYPE_TERRAIN
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap =googleMap

        gotoLoaction(geu_lat.toDouble(), geu_lng.toDouble())

//        val geu = LatLng(23.6,45.6 )
//        mMap.addMarker(MarkerOptions().position(geu).title("Graphic Era University"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(geu))

//        googleMap.addMarker(MarkerOptions()
//            .position(LatLng(0.0, 0.0))
//            .title("Marker"))
    }

    private fun okServiceOk():Boolean {

        val gaa : GoogleApiAvailability = GoogleApiAvailability.getInstance()

        val result = gaa.isGooglePlayServicesAvailable(this)

        if(result== ConnectionResult.SUCCESS){
            return true
        }else if(gaa.isUserResolvableError(result)){
            Toast.makeText(this,"Resolvable error", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this,"Error", Toast.LENGTH_SHORT).show()
        }
        return false
    }

    private fun checkLocationPermission(): Boolean {
        return  ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED
    }

    private fun requestLoactionPermission() {
        if (ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),REQUESTCODE_LOACTION)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode==1 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
            mLocationPermissiionGranted=true
            Toast.makeText(this,"Permission granted successfully", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this,"Permission granted failed", Toast.LENGTH_SHORT).show()
        }
    }


}