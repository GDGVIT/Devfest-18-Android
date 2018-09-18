package com.dscvit.android.devfest18.ui.scratch

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cooltechworks.views.ScratchTextView
import com.dscvit.android.devfest18.R
import com.dscvit.android.devfest18.utils.hide
import com.dscvit.android.devfest18.utils.show
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_scratch.*


class ScratchFragment : Fragment() {

    private val MY_PREFS_NAME = "MyPrefsFile"
    private var CouponKey = "couponKey"
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

        scratchCardParentView.hide()
        afterRevealCardView.hide()

        sharedpreferences = activity?.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE)

        enabledRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val isEnabled = dataSnapshot.getValue(Boolean::class.java) ?: true
                if (isEnabled) {
                    placeHolderView.hide()
                    scratchCardParentView.show()
                    sharedpreferences?.let { sharedPreferences ->
                        if (sharedPreferences.getBoolean("revealed", false)) {
                            placeHolderView.hide()
                            afterRevealCardView.show()
                            keyTextView.text = sharedPreferences.getString(CouponKey, "")
                            scratchCardParentView.hide()
                        }
                    }
                } else {
                    placeHolderView.show()
                    scratchCardParentView.hide()
                    afterRevealCardView.hide()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

        sharedpreferences?.let { sharedPreferences ->

            if (sharedPreferences.contains(CouponKey)) {
                scratchView?.text = sharedPreferences.getString(CouponKey, "")
            } else {
                var key: String = keyRef.push().key!!//key to be taken from Firebase
                key = key.substring(key.length - 8, key.length)
                keyRef.child(key).setValue(key)
                keyRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        scratchView?.text = sharedPreferences.getString(CouponKey, "")
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

//        data class QuizQuestion(
//                var question: String = "Sample question",
//                var answer: String = "Sample answer",
//                var optionList: List<String> = listOf("Sample option 1", "Sample option 2")
//        )
//
//        var classList = listOf(QuizQuestion(), QuizQuestion())
//
//        var sampleRef = database.getReference("quiz")
//        sampleRef.setValue(classList)
    }
}