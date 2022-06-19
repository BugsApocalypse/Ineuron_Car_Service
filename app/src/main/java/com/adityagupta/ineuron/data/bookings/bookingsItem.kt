package com.adityagupta.ineuron.data.bookings

data class bookingsItem(
    val admin_id: String,
    val booking_id: Int,
    val date_available: String,
    val service_type: String,
    val time_slot: String,
    val user_id: Int
)