package com.dscvit.android.devfest18.utils.fcm.service

import android.util.Log
import com.dscvit.android.devfest18.utils.fcm.FCMNotification
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class Devfest18FirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        remoteMessage?.let { remoteMessage ->
            val asdf = remoteMessage.from
            if (remoteMessage.data.isNotEmpty()) {
                val asfasdf = remoteMessage.data
            }
            val akkaa = remoteMessage.notification?.body ?: "Bleh - bleh"
            Log.d("asfd", akkaa)
            FCMNotification.notify(this, akkaa)
        }
    }
}
