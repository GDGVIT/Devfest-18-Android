package com.dscvit.android.devfest18.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.core.app.TaskStackBuilder
import com.dscvit.android.devfest18.ui.MainActivity

class SplashActivity : AppCompatActivity() {

    private val SPLASH_TIME_OUT: Long = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Handler().postDelayed({
            TaskStackBuilder.create(this)
                    .addNextIntentWithParentStack(Intent(this, MainActivity::class.java))
                    .startActivities()
            finish()
        }, SPLASH_TIME_OUT)
    }
}
