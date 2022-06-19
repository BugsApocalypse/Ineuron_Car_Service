package com.adityagupta.ineuron

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.adityagupta.ineuron.databinding.ActivitySignupAdminBinding
import com.adityagupta.ineuron.databinding.ActivitySignupBinding
import com.adityagupta.ineuron.helpers.RetrofitHelper
import com.adityagupta.ineuron.interfaces.DBApi
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONObject

class SignupActivityAdmin : AppCompatActivity() {
    private lateinit var binding: ActivitySignupAdminBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.switchLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        binding.btnSignupAdmin.setOnClickListener {
            val email = binding.etEmailAdmin.text.toString()
            val pass = binding.etPasswordAdmin.text.toString()
            val confirmPass = binding.etRepasswordAdmin.text.toString()
            val lat = binding.locLatitude.text.toString()
            val long = binding.locLongitude.text.toString()
            val phone = binding.etPhoneNumber.text.toString()
            val name = binding.etNameAdmin.text.toString()
            val desc = binding.etDescriptionAdmin.text.toString()



            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
                if (pass == confirmPass) {

                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {

                            val oxfordApi = RetrofitHelper.getInstance().create(DBApi::class.java)

                            GlobalScope.launch {
                                val jsonObject = JSONObject()
                                jsonObject.put("name",  name)
                                jsonObject.put("email", email)
                                jsonObject.put("password", pass)
                                jsonObject.put("latitude", lat )
                                jsonObject.put("longitude", long )
                                jsonObject.put("desc", desc )
                                jsonObject.put("phone_number", phone )
                                jsonObject.put("title", "hello" )




                                val requestBody = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), jsonObject.toString())
                                val response = oxfordApi.createAdmin(requestBody)
                                Log.i("qwerty", response.toString())

                            }
                            val intent = Intent(this, AdminBookingListActivity::class.java)
                            startActivity(intent)

                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()

                        }
                    }
                } else {
                    Toast.makeText(this, "Password is not matching", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()

            }
        }
    }
}