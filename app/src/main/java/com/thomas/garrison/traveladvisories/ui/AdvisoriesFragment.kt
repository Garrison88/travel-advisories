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
import android.widget.LinearLayout
import com.google.gson.GsonBuilder
import com.thomas.garrison.traveladvisories.Advisory
import com.thomas.garrison.traveladvisories.AdvisoryAdapter
import com.thomas.garrison.traveladvisories.R
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
    private var CWA = ArrayList<String>()
    private lateinit var adapter : AdvisoryAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_advisories, container, false)
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getAdvisories()

        val advisories = ArrayList<Advisory>()

        val advisoryCodes = ArrayList<String>()
        advisoryCodes.addAll(CWA)
//        val advisories = ArrayList<Advisory>()
        //                    for (advisory in response.body()!!.Africa) {
//                        Log.d("Africa: ", advisory)
//                        countriesWithAdvisories.add(advisory)
//                    }
        for (code in advisoryCodes) {
            advisories.add(Advisory(code, code, code))
        }



        val recyclerView = view.findViewById(R.id.rv_advisories) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)




        Log.d("ADVISORIES", arguments?.get(ADVISORY_CODES).toString())

//        Log.d("!@#$", MainActivity.countriesWithAdvisories.toString())

//        advisories.add(MainActivity.countriesWithAdvisories[1])

        adapter = AdvisoryAdapter(advisories) { advisory : Advisory -> advisoryClicked(advisory) }

//        MainActivity.countriesWithAdvisories

        recyclerView.adapter = adapter

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

    private fun advisoryClicked(advisory : Advisory) {

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

        val advisories = service.advisories

        advisories.enqueue(object : Callback<CountriesWithAdvisories> {

            override fun onResponse(call: Call<CountriesWithAdvisories>?, response: Response<CountriesWithAdvisories>?) {

                if (response != null && response.isSuccessful && response.body() != null) {
//
//                    for (advisory in response.body()!!.Africa) {
//                        Log.d("Africa: ", advisory)
//                        countriesWithAdvisories.add(advisory)
//                    }
                    val countriesWithAdvisories = ArrayList<String>()

                    countriesWithAdvisories.addAll(response.body()!!.africa)
                    countriesWithAdvisories.addAll(response.body()!!.asia)
                    countriesWithAdvisories.addAll(response.body()!!.latinAmericaAndCaribbean)
                    countriesWithAdvisories.addAll(response.body()!!.oceania)
                    countriesWithAdvisories.addAll(response.body()!!.europe)

                    CWA.addAll(countriesWithAdvisories)
                    adapter.notifyDataSetChanged()

//                    Log.d("STUFF", MainActivity.countriesWithAdvisories.toString())
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
