package com.example.adsexamples

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.ads.AdRequest
import kotlinx.android.synthetic.main.activity_banner_ad.*

class BannerAdActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_banner_ad)

        loadAdd()
    }

    private fun loadAdd() {
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }
}