package com.thomas.garrison.traveladvisories.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import com.google.gson.GsonBuilder
import com.thomas.garrison.traveladvisories.AdvisoryAdapter
import com.thomas.garrison.traveladvisories.R
import com.thomas.garrison.traveladvisories.api.Advisory
import com.thomas.garrison.traveladvisories.api.CountriesWithAdvisories
import com.thomas.garrison.traveladvisories.api.ScruffService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [AdvisoriesFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 *
 */
class AdvisoriesFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    lateinit var refreshBtn: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_advisories, container, false)
    }

    // TODO: Rename method, updateTrip argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getAdvisories()
        refreshBtn = view.findViewById(R.id.refresh_btn)
        refreshBtn.setOnClickListener {
            getAdvisories()
        }
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

    private fun advisoryClicked(advisory: Advisory) {

        val advisoryDetailView = Intent(context, AdvisoryDetailViewActivity::class.java)
        advisoryDetailView.putExtra("country_code", advisory.countryCode)
        startActivity(advisoryDetailView)
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

        val advisories = service.getAllAdvisories()

        advisories.enqueue(object : Callback<CountriesWithAdvisories> {

            override fun onResponse(call: Call<CountriesWithAdvisories>?, response: Response<CountriesWithAdvisories>?) {

                if (response != null && response.isSuccessful && response.body() != null) {

                    val recyclerView = view?.findViewById(R.id.rv_advisories) as RecyclerView
                    recyclerView.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)

                    val countriesWithAdvisories = ArrayList<String>()
                    val foundAdvisories = ArrayList<Advisory>()
                    val countryNamesArray = resources.getStringArray(R.array.country_names)
                    val countryCodesArray = resources.getStringArray(R.array.country_codes)

                    countriesWithAdvisories.addAll(response.body()!!.africa)
                    countriesWithAdvisories.addAll(response.body()!!.asia)
                    countriesWithAdvisories.addAll(response.body()!!.latinAmericaAndCaribbean)
                    countriesWithAdvisories.addAll(response.body()!!.oceania)
                    countriesWithAdvisories.addAll(response.body()!!.europe)

                    for (code in countriesWithAdvisories) {
                        if (countryCodesArray.contains(code)) {
                            foundAdvisories.add(Advisory(countryNamesArray[countryCodesArray.indexOf(code)], code, ""))
                            Log.d("!@#$", countryNamesArray[countryCodesArray.indexOf(code)])
                        }
                    }

                    val adapter = AdvisoryAdapter(foundAdvisories) { advisory: Advisory -> advisoryClicked(advisory) }

                    recyclerView.adapter = adapter

                    adapter.notifyDataSetChanged()

                    refreshBtn.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<CountriesWithAdvisories>?, t: Throwable?) {

                Log.d("Error ", t?.message)

            }
        })
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"
        const val ADVISORY_CODES = "test"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int
//                        , advisoryCodes: ArrayList<String>
        ) =
                AdvisoriesFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_COLUMN_COUNT, columnCount)
//                        putStringArrayList(ADVISORY_CODES, advisoryCodes)
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
