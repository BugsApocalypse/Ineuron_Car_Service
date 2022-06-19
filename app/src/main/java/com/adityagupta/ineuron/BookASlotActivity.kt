package com.adityagupta.ineuron

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.content.ContentProviderCompat.requireContext
import com.adityagupta.ineuron.data.services.servicesItem
import com.adityagupta.ineuron.databinding.ActivityBookAslotBinding
import com.adityagupta.ineuron.databinding.ActivityNearbyServiceCenterGeoBinding
import com.adityagupta.ineuron.helpers.RetrofitHelper
import com.adityagupta.ineuron.interfaces.DBApi
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class BookASlotActivity : AppCompatActivity() {


    val items = mutableListOf<String>()
    var time = ""
    var date = ""
    var adminId = ""
    var serviceId = ""
    var Idd = ""
    var serv = ""
    var amount = ""
    val serviceList = mutableListOf<servicesItem>()
    lateinit var binding: ActivityBookAslotBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_aslot)

        binding = ActivityBookAslotBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val oxfordApi = RetrofitHelper.getInstance().create(DBApi::class.java)
        adminId = intent.getStringExtra("admin_id").toString()

        GlobalScope.launch {
            var services = oxfordApi.getServices(adminId!!).body()
            var ii = oxfordApi.getUserId().body()
            Idd = ii?.get(0)?.user_id.toString()

            Log.i("werty", Idd)
            runOnUiThread(Runnable {
                if (services != null) {
                    for (i in services.indices) {
                        items.add(services[i].service_type)
                        serviceList.add(services[i])
                    }
                    val adapter = ArrayAdapter(applicationContext, R.layout.list_item, items)
                    (binding.serviceTypeDropDown.editText as? AutoCompleteTextView)?.setAdapter(
                        adapter
                    )
                }
            })
        }




        binding.selectDate.setOnClickListener {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select date")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()

            datePicker.addOnPositiveButtonClickListener {
                // Respon
                val dateString: String = SimpleDateFormat("MM/dd/yyyy").format( Date(it));
                Log.i("result", dateString)
                date = dateString
                binding.selectDate.text = dateString

            }
            datePicker.addOnNegativeButtonClickListener {
                // Respond to negative button click.
            }
            datePicker.addOnCancelListener {
                // Respond to cancel button click.
            }
            datePicker.addOnDismissListener {
                // Respond to dismiss events.
            }
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

            picker.addOnPositiveButtonClickListener {
                // Respond to positive button click.
                picker.minute
                time =  picker.hour.toString() + ":" + picker.minute.toString()
                binding.selectTime.text = picker.hour.toString() + ":" + picker.minute.toString()
            }
            picker.addOnNegativeButtonClickListener {
                // Respond to negative button click.
            }
            picker.addOnCancelListener {
                // Respond to cancel button click.
            }
            picker.addOnDismissListener {
                // Respond to dismiss events.
            }

            picker.show(supportFragmentManager, "tag");
        }

        binding.confirmBooking.setOnClickListener {

            GlobalScope.launch {
                val oxfordApi = RetrofitHelper.getInstance().create(DBApi::class.java)
                var selectedServ: String = binding.serviceTypeDropDown.editText!!.text.toString()
                var servId = ""
                for(i in serviceList.indices){
                    if(serviceList[i].service_type == selectedServ){
                        servId = serviceList[i].service_id.toString()
                        serv = serviceList[i].service_type.toString()
                        amount = serviceList[i].service_cost.toString()
                    }
                }
                val jsonObject = JSONObject()
                jsonObject.put("service_id", servId )
                jsonObject.put("time", time)
                jsonObject.put("date", date)
                jsonObject.put("admin_id", adminId )
                jsonObject.put("user_id", Idd )

                val requestBody = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), jsonObject.toString())
                val response = oxfordApi.createBooking(requestBody)
                runOnUiThread(Runnable {
                    val intent = Intent(applicationContext, PaymentActivity::class.java)
                    intent.putExtra("time", time.toString())
                    intent.putExtra("date", date.toString())
                    intent.putExtra("type", serv.toString())
                    intent.putExtra("cost", amount.toString())
                    startActivity(intent)
                })
            }

        }
    }


}