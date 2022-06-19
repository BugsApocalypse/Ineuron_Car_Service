package com.adityagupta.ineuron.data.Admin

data class bookingsAdminItem(
    val admin_id: String,
    val booking_id: Int,
    val date_available: String,
    val service_cost: Int,
    val service_type: String,
    val time_slot: String,
    val user_id: Int
)