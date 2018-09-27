package com.thomas.garrison.traveladvisories.ui

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import com.thomas.garrison.traveladvisories.R
import com.thomas.garrison.traveladvisories.database.AppDatabase
import com.thomas.garrison.traveladvisories.database.Trip
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity(), AdvisoriesFragment.OnFragmentInteractionListener, TripsFragment.OnFragmentInteractionListener {
    override fun onFragmentInteraction(uri: Uri) {

    }

    /**
     * The [android.support.v4.view.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [android.support.v4.app.FragmentStatePagerAdapter].
     */
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    companion object {
        var database: AppDatabase? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = AppDatabase.getAppDatabase(this)

        setSupportActionBar(toolbar)
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        container.adapter = mSectionsPagerAdapter

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))

        fab.setOnClickListener { openAddTripDialog() }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment? {
            return when(position) {
                0 -> {
                    TripsFragment.newInstance(position + 1)
                }
                1 -> {
                    AdvisoriesFragment.newInstance(position + 1)
                }
                else -> {
                    null
                }
            }
        }

        override fun getCount(): Int {
            // Show 2 total pages
            return 2
        }
    }

    private fun openAddTripDialog () {

        val dialogBuilder = AlertDialog.Builder(this)

        val dialogView = layoutInflater.inflate(R.layout.fragment_add_trip, null)

        val countriesArray = resources.getStringArray(R.array.countries_array)
        val adapter = ArrayAdapter<String>(this, android.R.layout.select_dialog_item, countriesArray)
        val autoCompleteTextView = dialogView.findViewById<AutoCompleteTextView>(R.id.auto_tv_countries)
        autoCompleteTextView.threshold = 1
        autoCompleteTextView.setAdapter(adapter)

        val btnChooseStartDate = dialogView.findViewById<Button>(R.id.btn_start_date)
        val btnChooseEndDate = dialogView.findViewById<Button>(R.id.btn_end_date)

        var startTime = 0
        var endTime = 0

        btnChooseStartDate.setOnClickListener {
            startTime = pickDate(btnChooseStartDate)
        }
        btnChooseEndDate.setOnClickListener {
            endTime = pickDate(btnChooseEndDate)
        }

        dialogBuilder.setView(dialogView)

                .setPositiveButton("Save") { dialog, id ->
//                    Log.d("$#@!", startTime.toString())
//                    Log.d("$#@!", endTime.toString())
//                    if (startTime < endTime) {
                        addTrip(autoCompleteTextView.text.toString(),
                                "Starts on" + btnChooseStartDate.text.toString(),
                                "Ends on" + btnChooseEndDate.text.toString())
                        dialog.dismiss()
//                    } else {
//                        Toast.makeText(applicationContext, "Try again", Toast.LENGTH_SHORT).show()
//                    }
                }

                .setNegativeButton("Cancel") { dialog, id ->
                    dialog.cancel()
                }
                .setCancelable(false)

        val alert = dialogBuilder.create()
        alert.setTitle("Add Trip")
        alert.show()
    }

    private fun addTrip(country: String, startDate: String, endDate: String) {
        val trip = Trip(0, country, startDate, endDate)

        MainActivity.database?.tripDao()?.insert(trip)
    }

    private fun pickDate(btn : Button): Int {
        val cal = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val sdf = SimpleDateFormat(" MMM dd, yyyy", Locale.ENGLISH)
//            Log.d("!@#$ TIME", ().toString())
            btn.text = sdf.format(cal.time)
        }
        DatePickerDialog(this,
            dateSetListener,
            // default to today's date
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)).show()

        return cal.get(Calendar.YEAR) + cal.get(Calendar.DATE) + cal.get(Calendar.DAY_OF_MONTH)

    }

}
