package com.thomas.garrison.traveladvisories.ui

import android.app.DatePickerDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.CycleInterpolator
import android.view.animation.TranslateAnimation
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import com.thomas.garrison.traveladvisories.R
import com.thomas.garrison.traveladvisories.TripAdapter
import com.thomas.garrison.traveladvisories.TripViewModel
import com.thomas.garrison.traveladvisories.database.Trip
import kotlinx.android.synthetic.main.fragment_trips.*
import java.text.SimpleDateFormat
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [TripsFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 *
 */
class TripsFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_trips, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_trips.layoutManager = LinearLayoutManager(context)
        rv_trips.hasFixedSize()

        val tripViewModel = ViewModelProviders.of(this).get(TripViewModel::class.java)
        subscribeUi(tripViewModel)

    }

    private fun subscribeUi(viewModel: TripViewModel) {
        // Update the list when the data changes
        viewModel.getTrips().observe(this, Observer<List<Trip>> { trips ->
            if (trips != null) {
                rv_trips.adapter = TripAdapter(trips) { trip: Trip -> openEditTripDialog(trip) }
            } else {

            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    private fun tripClicked(trip: Trip) {

        Toast.makeText(context, trip.country, Toast.LENGTH_SHORT).show()


//        MainActivity.open
//        val advisoryDetailView = Intent(context, AdvisoryDetailViewActivity::class.java)
//        advisoryDetailView.putExtra("country", trip.country)
//        startActivity(advisoryDetailView)
    }

    private fun openEditTripDialog(trip: Trip) {

        val dialogBuilder = AlertDialog.Builder(context!!)

        val dialogView = layoutInflater.inflate(R.layout.fragment_add_trip, null)

        val countryNamesArray = resources.getStringArray(R.array.country_names)
        val countryCodesArray = resources.getStringArray(R.array.country_codes)
        val adapter = ArrayAdapter<String>(context, android.R.layout.select_dialog_item, countryNamesArray)
        val autoCompleteTextView = dialogView.findViewById<AutoCompleteTextView>(R.id.auto_tv_countries)
        autoCompleteTextView.threshold = 1
        autoCompleteTextView.setAdapter(adapter)
        autoCompleteTextView.setText(trip.country)

        val btnChooseStartDate = dialogView.findViewById<Button>(R.id.btn_start_date)
        val btnChooseEndDate = dialogView.findViewById<Button>(R.id.btn_end_date)

        btnChooseStartDate.text = trip.startDate
        btnChooseEndDate.text = trip.endDate

        btnChooseStartDate.setOnClickListener {
            pickDate(btnChooseStartDate)
        }
        btnChooseEndDate.setOnClickListener {
            pickDate(btnChooseEndDate)
        }

        dialogBuilder.setView(dialogView)

                .setPositiveButton("Save") { dialog, id ->

                }

                .setNegativeButton("Cancel") { dialog, id ->
                    dialog.cancel()
                }

                .setNeutralButton(" ") { dialog, id -> }
                .setCancelable(false)

        val alert = dialogBuilder.create()
        alert.setTitle("Edit Trip")
        alert.show()

        alert.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {

            val chosenCountry = autoCompleteTextView.text.toString()

            if (!countryNamesArray.contains(chosenCountry)) {
                autoCompleteTextView.startAnimation(shakeError())
                autoCompleteTextView.error = "Invalid country"
            } else {
                val chosenCountryCode = countryCodesArray[countryNamesArray.indexOf(chosenCountry)]
                val hasAdvisory = MainActivity.countriesWithAdvisories.contains(chosenCountryCode)

                updateTrip(Trip(trip.tid,
                        chosenCountry,
                        chosenCountryCode,
                        btnChooseStartDate.text.toString(),
                        btnChooseEndDate.text.toString(),
                        hasAdvisory))

                alert.dismiss()
            }
        }

        val negBtn = alert.getButton(DialogInterface.BUTTON_NEUTRAL)
        negBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_delete_24px, 0, 0, 0)
        negBtn.setOnClickListener {
            val dialogClickListener = DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show()
                        deleteTrip(trip)
                        alert.cancel()
                    }
                    DialogInterface.BUTTON_NEGATIVE -> {
                        dialog.cancel()
                    }
                }
            }
            val builder = AlertDialog.Builder(context!!)
            builder
                    .setTitle("Delete")
                    .setMessage("Really delete?")
                    .setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener)
                    .show()
        }
    }

    private fun deleteTrip(trip: Trip) {
        MainActivity.database?.tripDao()?.delete(trip)
    }

    private fun updateTrip(trip: Trip) {
        MainActivity.database?.tripDao()?.update(trip)
    }

    private fun pickDate(btn: Button) {
        val cal = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val sdf = SimpleDateFormat(" MMM dd, yyyy", Locale.ENGLISH)
            btn.text = sdf.format(cal.time)
        }
        val dpd = DatePickerDialog(context,
                dateSetListener,
                // default to today's date
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH))
        dpd.datePicker.minDate = (System.currentTimeMillis() - 1000)
        dpd.show()
    }

    private fun shakeError(): TranslateAnimation {
        val shake = TranslateAnimation(0f, 10f, 0f, 0f)
        shake.duration = 400
        shake.interpolator = CycleInterpolator(4f)
        return shake
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
                TripsFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_COLUMN_COUNT, columnCount)
                    }
                }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

}
