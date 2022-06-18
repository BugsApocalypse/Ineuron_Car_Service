package com.adityagupta.ineuron.data.users

data class userItem(
    val title: String,
    val email: String,
    val admin_id: Int,
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val phone_number: String
)