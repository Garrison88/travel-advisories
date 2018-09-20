package com.thomas.garrison.traveladvisories

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.thomas.garrison.traveladvisories.database.Trip
import kotlinx.android.synthetic.main.fragment_trips.*

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

//        val tripList = ArrayList<Trip>()
//        tripList.add(Trip(100411, "Canada", "Sept 5, 2018", "Oct 10, 2018"))
//        tripList.add(Trip(1004, "Bermuda", "Sept 13, 2018", "Oct 16, 2018"))
//        tripList.add(Trip(100, "Canada", "Sept 28, 2018", "Nov 26, 2018"))
//
//
//        model.getTrips().observe(this, {  ->
//
//        })

//        val model = ViewModelProviders.of(this).get(TripViewModel::class.java)
//        val tripsArray = model.getTrips()
//        tripsArray.observe(this, object: Observer<List<Trip>> {
//            override fun onChanged(t: List<Trip>?) {
//                tripsArray.removeObserver(this)
//            }
//        })

        val tripViewModel = ViewModelProviders.of(this).get(TripViewModel::class.java)
        subscribeUi(tripViewModel)
    }

    private fun subscribeUi(viewModel: TripViewModel) {
        // Update the list when the data changes
        viewModel.getTrips().observe(this, Observer<List<Trip>> { trips ->
            if (trips != null) {
//                    mBinding.setIsLoading(false)
                rv_trips.adapter = TripAdapter(trips)
            } else {
//                mBinding.setIsLoading(true)
            }
            // espresso does not know how to wait for data binding's loop so we execute changes
            // sync.
//            mBinding.executePendingBindings()
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
