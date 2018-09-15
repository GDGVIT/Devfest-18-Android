package com.dscvit.android.devfest18.utils.fcm.service

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

class Devfest18FirebaseInstanceIdService : FirebaseInstanceIdService() {

    override fun onTokenRefresh() {
        val refreshedToken = FirebaseInstanceId.getInstance().token
        Log.d("FIREBASE TOKEN: ", refreshedToken)
    }
}