package com.adityagupta.ineuron

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.content.ContentProviderCompat.requireContext
import com.adityagupta.ineuron.databinding.ActivityBookAslotBinding
import com.adityagupta.ineuron.databinding.ActivityNearbyServiceCenterGeoBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat

class BookASlotActivity : AppCompatActivity() {

    lateinit var binding: ActivityBookAslotBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_aslot)

        binding = ActivityBookAslotBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val items = listOf("Material", "Design", "Components", "Android")
        val adapter = ArrayAdapter(applicationContext, R.layout.list_item, items)
        (binding.serviceTypeDropDown.editText as? AutoCompleteTextView)?.setAdapter(adapter)

        binding.selectDate.setOnClickListener {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select date")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()

            datePicker.show(supportFragmentManager, "tag")
        }

        binding.selectTime.setOnClickListener {
            val picker =
                MaterialTimePicker.Builder()
                    .setTitleText("Select Appointment time")
                    .setTimeFormat(TimeFormat.CLOCK_12H)
                    .setHour(12)
                    .setMinute(10)
                    .build()

            picker.show(supportFragmentManager, "tag");
        }

        binding.confirmBooking.setOnClickListener {
            startActivity(Intent(this, Payment::class.java))
        }
    }
}