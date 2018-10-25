package com.thomas.garrison.traveladvisories.ui

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.animation.CycleInterpolator
import android.view.animation.TranslateAnimation
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import com.google.gson.GsonBuilder
import com.thomas.garrison.traveladvisories.R
import com.thomas.garrison.traveladvisories.api.CountriesWithAdvisories
import com.thomas.garrison.traveladvisories.api.ScruffService
import com.thomas.garrison.traveladvisories.database.AppDatabase
import com.thomas.garrison.traveladvisories.database.Trip
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
        var countriesWithAdvisories = arrayListOf<String>()
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

        fab.setOnClickListener {
            openAddTripDialog()
        }

        getAdvisories()

    }

    private fun getAdvisories() {

        val gson = GsonBuilder()
                .setLenient()
                .create()

        val retrofit = Retrofit.Builder()
                .baseUrl(ScruffService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

        val service = retrofit.create(ScruffService::class.java)

        val advisories = service.advisories()

        advisories.enqueue(object : Callback<CountriesWithAdvisories> {

            override fun onResponse(call: Call<CountriesWithAdvisories>?, response: Response<CountriesWithAdvisories>?) {

                if (response != null && response.isSuccessful && response.body() != null) {

                    countriesWithAdvisories.addAll(response.body()!!.africa)
                    countriesWithAdvisories.addAll(response.body()!!.asia)
                    countriesWithAdvisories.addAll(response.body()!!.latinAmericaAndCaribbean)
                    countriesWithAdvisories.addAll(response.body()!!.oceania)
                    countriesWithAdvisories.addAll(response.body()!!.europe)

                }
            }

            override fun onFailure(call: Call<CountriesWithAdvisories>?, t: Throwable?) {

                Log.d("Error ", t?.message)

            }
        })
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
            return when (position) {
                0 -> {
                    TripsFragment.newInstance(position + 1)
                }
                1 -> {
                    AdvisoriesFragment.newInstance(position + 1
//                           , countriesWithAdvisories
                    )
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


    private fun openAddTripDialog() {

        val dialogBuilder = AlertDialog.Builder(this)

        val dialogView = layoutInflater.inflate(R.layout.fragment_add_trip, null)

        val countryNamesArray = resources.getStringArray(R.array.country_names)
        val countryCodesArray = resources.getStringArray(R.array.country_codes)
        val adapter = ArrayAdapter<String>(this, android.R.layout.select_dialog_item, countryNamesArray)
        val autoCompleteTextView = dialogView.findViewById<AutoCompleteTextView>(R.id.auto_tv_countries)
        autoCompleteTextView.threshold = 1
        autoCompleteTextView.setAdapter(adapter)

        val btnChooseStartDate = dialogView.findViewById<Button>(R.id.btn_start_date)
        val btnChooseEndDate = dialogView.findViewById<Button>(R.id.btn_end_date)

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

                .setCancelable(false)

        val alert = dialogBuilder.create()
        alert.setTitle("Add Trip")
        alert.show()

        alert.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {

            val chosenCountry = autoCompleteTextView.text.toString()

            if (!countryNamesArray.contains(chosenCountry)) {
                autoCompleteTextView.startAnimation(shakeError())
                autoCompleteTextView.error = "Invalid country"
            } else if (btnChooseStartDate.text == getString(R.string.depart_btn_txt)) {
                btnChooseStartDate.startAnimation(shakeError())
                Toast.makeText(applicationContext, "Please choose a departure date", Toast.LENGTH_SHORT).show()
            } else if (btnChooseEndDate.text == (getString(R.string.return_btn_txt))) {
                btnChooseEndDate.startAnimation(shakeError())
                Toast.makeText(applicationContext, "Please choose a return date", Toast.LENGTH_SHORT).show()
            } else {
                val chosenCountryCode = countryCodesArray[countryNamesArray.indexOf(chosenCountry)]
                val hasAdvisory = countriesWithAdvisories.contains(chosenCountryCode)
                addTrip(Trip(0, chosenCountry,
                        chosenCountryCode,
                        btnChooseStartDate.text.toString(),
                        btnChooseEndDate.text.toString(),
                        hasAdvisory))
                alert.dismiss()
            }
        }
    }

    private fun addTrip(trip: Trip) {
        MainActivity.database?.tripDao()?.insert(trip)
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
        val dpd = DatePickerDialog(this,
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
}
