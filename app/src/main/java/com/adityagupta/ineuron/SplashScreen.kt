package com.adityagupta.ineuron

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth

class SplashScreen : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private val SPLASH_TIME: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        firebaseAuth = FirebaseAuth.getInstance()

        Handler().postDelayed ({
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        },SPLASH_TIME)
    }

    override fun onStart() {
        super.onStart()

        if(firebaseAuth.currentUser != null){
            val intent = Intent(this, NearbyServiceCenterGeoActivity::class.java)
            startActivity(intent)
        }
    }

}