package com.doubleapaper.photostamp.aatracking

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.doubleapaper.photostamp.aatracking.manager.RealmManager

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolygonOptions
import com.google.android.gms.maps.model.Marker
import io.realm.Sort
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View


class MapsActivity : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view =  inflater.inflate(com.doubleapaper.photostamp.aatracking.R.layout.fragment_map, container, false)
        initInstances(view)
        return view
    }
   /* fun newInstance(): MapsActivity {
        val fragment = MapsActivity()
        val args = Bundle()
        fragment.setArguments(args)
        return fragment
    }*/

    private fun initInstances(rootView: View) {
        val mapFragment = childFragmentManager.findFragmentById(com.doubleapaper.photostamp.aatracking.R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }
  /*  override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }*/

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val opts = PolygonOptions()
        val markers = ArrayList<Marker>()
        var gps = RealmManager.getInstance().getGPSStampByField("datestamp",
            SimpleDateFormat("yyyy-MM-dd").format(Date())+ "*"
            ,Sort.DESCENDING)
        for (location in gps) {
            var latlng = LatLng(location.location!!.lat, location.location!!.lng)
            opts.add(latlng).strokeWidth(5f)
            val marker = mMap.addMarker(MarkerOptions().position(latlng).title(location.datestamp))
            markers.add(marker)
        }

        val polygon = mMap.addPolygon(opts.strokeColor(Color.RED))

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(gps[0].location!!.lat, gps[0].location!!.lng)

        //mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.setMinZoomPreference(10f)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}
