package com.doubleapaper.photostamp.aatracking.fragment

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.doubleapaper.photostamp.aatracking.R
import com.doubleapaper.photostamp.aatracking.dao.SaveMobile
import com.doubleapaper.photostamp.aatracking.dao.SaveTracking
import com.doubleapaper.photostamp.aatracking.dao.ServiceResponse
import com.doubleapaper.photostamp.aatracking.database.GPSStamp
import com.doubleapaper.photostamp.aatracking.manager.RealmManager
import com.doubleapaper.photostamp.aatracking.service.CallService


import com.google.android.gms.maps.SupportMapFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.fragment_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [MainFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class MainFragment : Fragment() {


        override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var v =  inflater.inflate(R.layout.fragment_main, container, false)
           // (activity as AppCompatActivity).setSupportActionBar(toolbar)
        initInstances(v)
        return  v
    }
    private fun initInstances(rootView: View) {
        var gps =  RealmManager.getInstance().getGPSStamp()
        var location:String = ""
        var i = 0
        for (l in gps){
            i++
            if (i > gps.size - 20)
                location +=  l.datestamp +" | " + l.location!!.lat +", " +l.location!!.lng + "|"+ l.sendResult +"\n"


        }
        val tt = rootView.findViewById<TextView>(R.id.text1)
        tt.text =location
    }
    fun sendDataToServer(gps:GPSStamp) {
        var server:CallService.SaveTrackingDataService =  CallService().retrofit.create(CallService.SaveTrackingDataService::class.java)
        /*var saveTracking = SaveTracking()
        val call = server.SaveTrackingDataS(saveTracking)
        call.enqueue(object : Callback<ServiceResponse> {
            override fun onResponse(
                call: Call<ServiceResponse>,
                response: Response<ServiceResponse>
            ) {
                if (response.isSuccessful()) {
                    Log.i("joke","Res" + response.body()!!.status)
                    var result = response.body()
                    RealmManager.getInstance().updateGPSStamp(gps,result?.status!!)
                }
            }

            override fun onFailure(call: Call<ServiceResponse>, t: Throwable) {

            }
        })*/

    }
    override fun onDetach() {
        super.onDetach()

    }


    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
