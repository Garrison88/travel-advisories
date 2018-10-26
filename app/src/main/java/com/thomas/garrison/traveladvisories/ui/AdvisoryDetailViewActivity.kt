package com.thomas.garrison.traveladvisories.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import com.thomas.garrison.traveladvisories.R
import com.thomas.garrison.traveladvisories.api.Advisory
import com.thomas.garrison.traveladvisories.api.ScruffService
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class AdvisoryDetailViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_advisory_detail_view)
        setSupportActionBar(toolbar)
        toolbar.title = ""
        val countryCode = intent.getStringExtra("country_code")


//        Utils.hasInternetConnection().subscribe { hasInternet ->
//        }
        getAdvisory(countryCode)

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24px)

        toolbar.setNavigationOnClickListener { finish() }
    }

    private fun getAdvisory(countryCode: String) {

        val flagImage = findViewById<ImageView>(R.id.flag_image)
        val comments = findViewById<TextView>(R.id.tv_comments)
        val pBar = findViewById<ProgressBar>(R.id.flag_pbar)
        pBar.visibility = View.VISIBLE

        val gson = GsonBuilder()
                .setLenient()
                .create()

        val retrofit = Retrofit.Builder()
                .baseUrl(ScruffService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

        val service = retrofit.create(ScruffService::class.java)

        val advisory = service.getAdvisoryByCountryCode(countryCode)

        advisory.enqueue(object : Callback<Advisory> {

            override fun onResponse(call: Call<Advisory>?, response: Response<Advisory>?) {

                if (response != null && response.isSuccessful && response.body() != null) {

                    toolbar.title = response.body()!!.country

                    comments.movementMethod = ScrollingMovementMethod()
                    comments.text = response.body()?.comments
                    val code = response.body()?.countryCode?.toLowerCase()
                    Picasso.with(applicationContext)
                            .load("https://www.countryflags.io/$code/flat/64.png")
                            .resize(0, flagImage.height)
                            .into(flagImage, object : com.squareup.picasso.Callback {
                                override fun onSuccess() {
                                    pBar.visibility = View.GONE
                                }

                                override fun onError() {

                                }
                            })
                }
            }

            override fun onFailure(call: Call<Advisory>?, t: Throwable?) {

                pBar.visibility = View.GONE
                Log.d("Error ", t?.message)

            }
        })
    }
}
