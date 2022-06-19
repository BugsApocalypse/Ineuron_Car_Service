package com.adityagupta.ineuron

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.adityagupta.ineuron.data.Admin.bookingsAdmin
import com.adityagupta.ineuron.data.bookings.bookings
import retrofit2.Response

class CustomAdapterAdmin(private val dataSet: Response<bookingsAdmin>) :
    RecyclerView.Adapter<CustomAdapterAdmin.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bookingid: TextView
        val date: TextView
        val time: TextView
        val type: TextView

        init {
            // Define click listener for the ViewHolder's View.
            bookingid = view.findViewById(R.id.bookingId)
            date = view.findViewById(R.id.bookingDate)
            time = view.findViewById(R.id.bookingTime)
            type = view.findViewById(R.id.serviceType)


        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recycler_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.bookingid.text = dataSet.body()?.get(position)?.booking_id.toString()
        viewHolder.date.text = dataSet.body()?.get(position)?.date_available
        viewHolder.time.text = dataSet.body()?.get(position)?.time_slot
        viewHolder.type.text = dataSet.body()?.get(position)?.service_type
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.body()!!.size

}
