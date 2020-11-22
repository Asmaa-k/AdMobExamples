package com.example.adsexamples

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdCallback
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import kotlinx.android.synthetic.main.activity_rewarded_video_ad_activty.*


class RewardedVideoAdActivty : AppCompatActivity() {
    private lateinit var rewardedAd: RewardedAd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rewarded_video_ad_activty)
        rewardedAd = RewardedAd(this, "ca-app-pub-3940256099942544/5224354917")

        val adLoadCallback = object : RewardedAdLoadCallback() {
            override fun onRewardedAdLoaded() {
                //This method is executed when an ad has finished loading.
                print("Opened reward based video ad.")
                super.onRewardedAdLoaded()
            }

            override fun onRewardedAdFailedToLoad(p0: LoadAdError?) {
                //This method is invoked when an ad fails to load.
                // It includes an error parameter of type LoadAdError that indicates what type of failure occurred.
                print("Reward based video ad failed to load.")
                super.onRewardedAdFailedToLoad(p0)
            }
        }
        rewardedAd.loadAd(AdRequest.Builder().build(), adLoadCallback)

        setUpOnclick()
    }

    private fun setUpOnclick() {
        myButton.setOnClickListener {
            if (rewardedAd.isLoaded) {
                val activityContext: Activity = this@RewardedVideoAdActivty
                val adCallback = object : RewardedAdCallback() {
                    override fun onRewardedAdOpened() {
                        // Ad opened.
                    }

                    override fun onRewardedAdClosed() {
                        print("Reward based video ad is closed.")
                        //show another ad
                        onRewardedAdClosed1()
                    }

                    override fun onUserEarnedReward(reward: RewardItem) {
                        // User earned reward.

                        val amount = String.format(getString(R.string.default_coin_text_format), reward.amount)
                        coinCountText.text = amount
                    }

                    override fun onRewardedAdFailedToShow(adError: AdError) {
                        // Ad failed to display.
                    }
                }
                rewardedAd.show(activityContext, adCallback)
            } else {
                Log.d("TAG", "The rewarded ad wasn't loaded yet.")
            }
        }
    }

    fun createAndLoadRewardedAd(): RewardedAd {
        val rewardedAd = RewardedAd(this, "ca-app-pub-3940256099942544/5224354917")
        val adLoadCallback = object : RewardedAdLoadCallback() {
            override fun onRewardedAdLoaded() {
                // Ad successfully loaded.
            }

            override fun onRewardedAdFailedToLoad(adError: LoadAdError) {
                // Ad failed to load.
            }
        }
        rewardedAd.loadAd(AdRequest.Builder().build(), adLoadCallback)
        return rewardedAd
    }

    /*
    Using RewardedAdCallback to preload the next rewarded ad
    RewardedAd is a one-time-use object. This means that once a rewarded ad is shown, the object can't be used to load another ad.
 To request another rewarded ad, you'll need to create a new RewardedAd object.

A best practice is to load another rewarded ad in the onRewardedAdClosed() method on RewardedAdCallback so that the next rewarded ad starts loading as soon as the previous one is dismissed
     */
    fun onRewardedAdClosed1() {
        this.rewardedAd = createAndLoadRewardedAd()
    }
}
