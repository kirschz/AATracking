package com.doubleapaper.photostamp.aatracking.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.doubleapaper.photostamp.aatracking.R
import com.doubleapaper.photostamp.aatracking.manager.PrefManage
import com.google.android.gms.plus.PlusOneButton
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_login.*




/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * [LoginFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    // The URL to +1.  Must be a valid URL.
    private val PLUS_ONE_URL = "http://developer.android.com"

    private lateinit var mPlusOneButton: Button

    private var mListener: OnFragmentInteractionListener? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        var editTextUser = view.findViewById(R.id.editTextUser) as EditText
        val userAction = arguments!!.getString("userAction", "")
        if ( PrefManage.getInstance().getUserName() != "")
            editTextUser.setText(PrefManage.getInstance().getUserName())
        Log.i("joke","userAction $userAction")
        mPlusOneButton = view.findViewById(R.id.buttonOk)
        mPlusOneButton.setOnClickListener(View.OnClickListener {
            if (editTextUser.text.toString() != ""){
                PrefManage.getInstance().setUserName(editTextUser.text.toString())
                editTextUser.isEnabled = false
                Toast.makeText(context!!.applicationContext, "OK", Toast.LENGTH_SHORT).show()
            }else
                Toast.makeText(context!!.applicationContext, "Input User", Toast.LENGTH_SHORT).show()
        })
        return view
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"
        // The request code must be 0 or greater.
        private val PLUS_ONE_REQUEST_CODE = 0

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): LoginFragment {
            val fragment = LoginFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

}// Required empty public constructor
