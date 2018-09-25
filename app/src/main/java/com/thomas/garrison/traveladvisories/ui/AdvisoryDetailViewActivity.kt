package com.thomas.garrison.traveladvisories.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.thomas.garrison.traveladvisories.R
import kotlinx.android.synthetic.main.activity_main.*



//import kotlinx.android.synthetic.main.activity_advisory_detail_view_fragment.*

class AdvisoryDetailViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_advisory_detail_view)
        setSupportActionBar(toolbar)

        val country = intent.getStringExtra("country") ?: ""
        toolbar.title = country

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24px)

        toolbar.setNavigationOnClickListener {
            finish()
        }

//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
//        }
    }

}
