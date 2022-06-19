package com.adityagupta.ineuron

import android.R
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import com.adityagupta.ineuron.databinding.ActivityPaymentBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.razorpay.Checkout
import org.json.JSONObject

class PaymentActivity: Activity() {

    val TAG: String = PaymentActivity::class.toString()
    private lateinit var binding: ActivityPaymentBinding
    private lateinit var logout: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.payServiceAmount.text = intent.getStringExtra("cost")
        binding.payServiceType.text = intent.getStringExtra("type")
        binding.payServiceTime.text = intent.getStringExtra("time")
        binding.payServiceDate.text = intent.getStringExtra("date")
        binding.logout.setOnClickListener(){
            Firebase.auth.signOut()
            val intent = Intent(this, PaymentActivity::class.java)
            startActivity(intent)
            startActivity(Intent(this,LoginActivity::class.java))
            Toast.makeText(this, "Logout successful", Toast.LENGTH_LONG).show()
        }

        Checkout.preload(applicationContext)
        binding.pay.setOnClickListener {
            if(!binding.checkBox.isChecked) {
                startPayment()
            }else{
                binding.yourBookingConfirmed.visibility = View.VISIBLE
                binding.checkImage.visibility  = View.VISIBLE

                val handler = Handler()
                handler.postDelayed(
                    Runnable {  startActivity(Intent(this, UserBookingsActivity::class.java))
                    },
                    2000
                )
            }

        }
    }

    private fun startPayment() {

        val activity: Activity = this


            val co = Checkout()

            try {
                val options = JSONObject()
                options.put("name", "Razorpay Corp")
                options.put("description", "Demoing Charges")
                options.put("name", "INeuron Car Service")
                options.put("description", "Car Service Payment")
                //You can omit the image option to fetch the image from dashboard
                options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
                options.put("theme.color", "#3399cc");
                options.put("currency", "INR");
                options.put("order_id", "order_DBJOWzybf0sJbb");
                options.put("amount", "50000")//pass amount in currency subunits
                options.put("currency", "INR");
                options.put("amount", "1000")//pass amount in currency subunits

//            val retryObj = JSONObject();
//            retryObj.put("enabled", true);
//            retryObj.put("max_count", 4);
//            options.put("retry", retryObj);

                val prefill = JSONObject()
                prefill.put("email", "gaurav.kumar@example.com")
                prefill.put("contact", "9876543210")

                options.put("prefill", prefill)
                co.open(activity, options)
                startActivity(Intent(this, UserBookingsActivity::class.java))

            } catch (e: Exception) {
                Toast.makeText(activity, "Error in payment: " + e.message, Toast.LENGTH_LONG).show()
                e.printStackTrace()
            }

    }
}