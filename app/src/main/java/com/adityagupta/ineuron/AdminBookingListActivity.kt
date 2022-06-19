package com.adityagupta.ineuron

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.adityagupta.ineuron.data.Admin.bookingsAdminItem
import com.adityagupta.ineuron.data.bookings.bookingsItem
import com.adityagupta.ineuron.databinding.ActivityAdminBookingListBinding
import com.adityagupta.ineuron.databinding.ActivityUserBookingsBinding
import com.adityagupta.ineuron.helpers.RetrofitHelper
import com.adityagupta.ineuron.interfaces.DBApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AdminBookingListActivity : AppCompatActivity() {
    lateinit var binding : ActivityAdminBookingListBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.adityagupta.ineuron.R.layout.activity_user_bookings)

        binding = ActivityAdminBookingListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        GlobalScope.launch {
            val oxfordApi = RetrofitHelper.getInstance().create(DBApi::class.java)

            /*val userId = oxfordApi.getUserId()
            Log.i("qwerty", userId.body()!![0].user_id.toString())*/

            var result = oxfordApi.getAllAdminBookings("4")
            Log.i("qwerty", result.body().toString())

            var values = mutableListOf<bookingsAdminItem>()
            for(i in result.body()!!.indices){
                values.add(result.body()!![i])
            }

            runOnUiThread(Runnable {
                val customAdapter = CustomAdapterAdmin(result)
                var recyclerView = binding.recyclerViewAdmin
/*
        Log.i("qwerty", iu!!)
*/

                recyclerView.adapter = customAdapter
            })

        }
    }
}