package com.thomas.garrison.traveladvisories

import android.app.DatePickerDialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Spinner
import com.thomas.garrison.traveladvisories.database.Trip
import java.text.SimpleDateFormat
import java.util.*

//// TODO: Rename parameter arguments, choose names that match
//// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */

class AddTripDialogFragment : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.fragment_add_trip, container)

        val btnChooseStartDate = rootView.findViewById<Button>(R.id.btn_start_date)
        val btnChooseEndDate = rootView.findViewById<Button>(R.id.btn_end_date)
        val countriesSpinner = rootView.findViewById<Spinner>(R.id.spinner_countries)
        val btnSave = rootView.findViewById<Button>(R.id.btn_save)
        val btnCancel = rootView.findViewById<Button>(R.id.btn_cancel)

        btnSave.setOnClickListener {
            addTrip(countriesSpinner.selectedItem.toString(), btnChooseStartDate.text.toString(), btnChooseEndDate.text.toString())
            dismiss()
        }

        btnCancel.setOnClickListener {
            dismiss()
        }

        btnChooseStartDate.setOnClickListener {
            pickDate(btnChooseStartDate)
        }
        btnChooseEndDate.setOnClickListener {
            pickDate(btnChooseEndDate)

        }

        return rootView

    }

    private fun addTrip(country: String, startDate: String, endDate: String) {
        val trip = Trip(0, country, startDate, endDate)

        MainActivity.database?.tripDao()?.insert(trip)
        Log.d("%&%&%", "${trip.country}, ${trip.startDate}, ${trip.endDate}")
    }

    private fun pickDate(btn : Button) {
        val cal = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val myFormat = "MMMM dd, yyyy"
            val sdf = SimpleDateFormat(myFormat, Locale.ENGLISH)
            btn.text = sdf.format(cal.time)
        }
        DatePickerDialog(context,
                dateSetListener,
                // default to today's date
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
    }


}
