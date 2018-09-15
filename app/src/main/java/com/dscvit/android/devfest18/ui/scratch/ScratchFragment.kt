package com.dscvit.android.devfest18.ui.scratch

import androidx.fragment.app.Fragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.cooltechworks.views.ScratchTextView
import android.widget.TextView
import androidx.cardview.widget.CardView
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dscvit.android.devfest18.R
import kotlinx.android.synthetic.main.fragment_scratch.*
import android.R.id.edit
import com.dscvit.android.devfest18.R.id.scratchView
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import android.content.Context.MODE_PRIVATE
import android.util.Log
import com.dscvit.android.devfest18.R.id.scratchCardParentView
import com.dscvit.android.devfest18.R.id.keyTextView
import com.dscvit.android.devfest18.R.id.afterRevealCardView




class ScratchFragment : Fragment() {

    val MY_PREFS_NAME = "MyPrefsFile"
    var CouponKey = "couponKey"
    var sharedpreferences: SharedPreferences? = null
    private val database = FirebaseDatabase.getInstance()
    private val keyRef = database.getReference("keys")
    private val enabledRef = database.getReference("scratchEnabled")

    companion object {
        fun newInstance() = ScratchFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_scratch, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        afterRevealCardView.visibility = View.GONE

        sharedpreferences = activity?.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE)

        sharedpreferences?.let { sharedPreferences ->
            if (sharedPreferences.getBoolean("revealed", false)) {
                afterRevealCardView.visibility = View.VISIBLE
                keyTextView.text = sharedPreferences.getString(CouponKey, "")
                scratchCardParentView.visibility = View.GONE
            }

            if (sharedPreferences.contains(CouponKey)) {
                scratchView?.text = sharedPreferences.getString(CouponKey, "")
            } else {
                var key: String = keyRef.push().key!!//key to be taken from Firebase
                key = key.substring(key.length - 8, key.length)
                keyRef.child(key).setValue(key)
                keyRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        scratchView?.setText(sharedPreferences.getString(CouponKey, ""))
                    }

                    override fun onCancelled(databaseError: DatabaseError) {

                    }
                })

                with(sharedPreferences.edit()) {
                    putString(CouponKey, key)
                    apply()
                }
            }
        }


        scratchView?.setRevealListener(object : ScratchTextView.IRevealListener {
            override fun onRevealed(scratchTextView: ScratchTextView) {

            }

            override fun onRevealPercentChangedListener(scratchTextView: ScratchTextView, v: Float) {
                if (v > 0.8) {
                    sharedpreferences?.let { sharedPreferences ->
                        with(sharedPreferences.edit()) {
                            putBoolean("revealed", true)
                            apply()
                        }
                    }
                }
            }
        })
    }
}