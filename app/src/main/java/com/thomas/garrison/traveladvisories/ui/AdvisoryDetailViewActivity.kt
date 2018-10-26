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
import com.thomas.garrison.traveladvisories.Advisory
import com.thomas.garrison.traveladvisories.R
import com.thomas.garrison.traveladvisories.api.ScruffService
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket


class AdvisoryDetailViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_advisory_detail_view)
        setSupportActionBar(toolbar)
        toolbar.title = ""
        val countryCode = intent.getStringExtra("country_code")


        hasInternetConnection().subscribe { hasInternet ->
            getAdvisory(countryCode)
        }


        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24px)

        toolbar.setNavigationOnClickListener { finish() }

//        flagImage.setImageDrawable(resources.getDrawable(R.drawable.flag_af))

//        Picasso.with(applicationContext)
//                .load("https://www.countryflags.io/$country/flat/64.png")
//                .placeholder(R.drawable.ic_baseline_flag_24px)
//                .resize(500, 500)
//                .centerCrop()
//                .into(flagImage)
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

        val advisory = service.advisoryByCode(countryCode)

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

                Log.d("Error ", t?.message)

            }
        })
    }

    fun hasInternetConnection(): Single<Boolean> {
        return Single.fromCallable {
            try {
                // Connect to Google DNS to check for connection
                val timeoutMs = 1500
                val socket = Socket()
                val socketAddress = InetSocketAddress("8.8.8.8", 53)

                socket.connect(socketAddress, timeoutMs)
                socket.close()

                true
            } catch (e: IOException) {
                false
            }
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}
