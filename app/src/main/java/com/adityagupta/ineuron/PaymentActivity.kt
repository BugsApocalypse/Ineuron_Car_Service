package com.adityagupta.ineuron

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.adityagupta.ineuron.databinding.ActivityLoginBinding
import com.adityagupta.ineuron.databinding.ActivityPaymentBinding
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject

class PaymentActivity : AppCompatActivity(), PaymentResultListener {

    private lateinit var binding: ActivityPaymentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Checkout.preload(getApplicationContext())

        binding.pay.setOnClickListener {
            makePayment()
        }
    }

    private fun makePayment() {
        val co = Checkout()

        try {
            val options = JSONObject()
            options.put("name","INeuron Car-Services")
            options.put("description","Car Services Payment")
            //You can omit the image option to fetch the image from dashboard
            options.put("image","https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
            options.put("theme.color", "#3399cc");
            options.put("currency","INR");
            options.put("amount","10000")//pass amount in currency subunits

            /* val retryObj =  JSONObject()
             retryObj.put("enabled", true);
             retryObj.put("max_count", 4);
             options.put("retry", retryObj);*/

            val prefill = JSONObject()
            prefill.put("email","sohailss2412@gmail.com")
            prefill.put("contact","7061552655")

            options.put("prefill",prefill)
            co.open(this,options)
        }catch (e: Exception){
            Toast.makeText(this,"Error in payment: "+ e.message,Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(p0: String?) {
        Toast.makeText(this, "Payment Sucsess: $p0",Toast.LENGTH_LONG).show()
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this, "Payment Failed: $p1",Toast.LENGTH_LONG).show()
    }
}
