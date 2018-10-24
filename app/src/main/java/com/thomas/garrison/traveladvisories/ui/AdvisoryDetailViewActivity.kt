package com.thomas.garrison.traveladvisories.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import com.thomas.garrison.traveladvisories.R
import kotlinx.android.synthetic.main.activity_main.*

class AdvisoryDetailViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_advisory_detail_view)
        setSupportActionBar(toolbar)
        val country = intent.getStringExtra("country_code")
        toolbar.title = country

        val flagImage = findViewById<ImageView>(R.id.flag_image)

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24px)

        toolbar.setNavigationOnClickListener { finish() }

//        flagImage.setImageDrawable(R.drawable.)

//        Picasso.with(applicationContext)
//                .load("https://www.countryflags.io/$country/flat/64.png")
//                .placeholder(R.drawable.ic_baseline_flag_24px)
//                .resize(500, 500)
//                .centerCrop()
//                .into(flagImage)
    }
}
