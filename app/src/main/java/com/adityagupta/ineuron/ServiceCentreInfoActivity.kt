package com.adityagupta.ineuron

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.adityagupta.ineuron.databinding.ActivityNearbyServiceCenterGeoBinding
import com.adityagupta.ineuron.databinding.ActivityServiceCentreInfoBinding

class ServiceCentreInfoActivity : AppCompatActivity() {
    lateinit var binding: ActivityServiceCentreInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_centre_info)


        binding = ActivityServiceCentreInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.wdTitleText.text = intent.getStringExtra("title")
        binding.wdNumber.text = intent.getStringExtra("number")
        Log.i("something", intent.getStringExtra("title")!!)
        binding.bookASlotButton.setOnClickListener {
            startActivity(Intent(this, BookASlotActivity::class.java))
        }
    }
}