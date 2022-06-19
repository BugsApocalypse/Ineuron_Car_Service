package com.adityagupta.ineuron

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.adityagupta.ineuron.data.bookings.bookingsItem
import com.adityagupta.ineuron.databinding.ActivityUserBookingsBinding
import com.adityagupta.ineuron.helpers.RetrofitHelper
import com.adityagupta.ineuron.interfaces.DBApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class UserBookingsActivity : AppCompatActivity() {
    lateinit var binding : ActivityUserBookingsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.adityagupta.ineuron.R.layout.activity_user_bookings)

        binding = ActivityUserBookingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        GlobalScope.launch {
            val oxfordApi = RetrofitHelper.getInstance().create(DBApi::class.java)

            var userId = oxfordApi.getUserId()
            Log.i("qwerty", userId.body()!![0].user_id.toString())

            var result = oxfordApi.getAllBookings(userId.body()!![0].user_id.toString())
            Log.i("qwerty", result.body().toString())

            var values = mutableListOf<bookingsItem>()
            for(i in result.body()!!.indices){
                values.add(result.body()!![i])
            }

            runOnUiThread(Runnable {
                val customAdapter = CustomAdapter(result)
                var recyclerView = binding.recyclerView
/*
        Log.i("qwerty", iu!!)
*/

                recyclerView.adapter = customAdapter
            })

        }
    }
}