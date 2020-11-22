package com.example.adsexamples

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.adsexamples.nativeAdapter.MenuItem
import com.example.adsexamples.nativeAdapter.RecyclerViewAdapter
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.formats.UnifiedNativeAd
import kotlinx.android.synthetic.main.activity_native_ad.*
import org.json.JSONArray
import org.json.JSONException
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import kotlin.collections.ArrayList

class NativeAdActivity : AppCompatActivity() {
    // List of MenuItems and native ads that populate the RecyclerView.
    private val mRecyclerViewItems: ArrayList<Any> = ArrayList()

    // List of native ads that have been successfully loaded.
    private val mNativeAds: ArrayList<UnifiedNativeAd> = ArrayList()

    // The AdLoader used to load ads.
    lateinit var adLoader: AdLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_native_ad)
        loadNativeAds()
        addMenuItemsFromJson()
        setupAdapter()
    }

    private fun setupAdapter() {

        // Use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView.

        // Use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView.
        recyclerView.setHasFixedSize(true)

        // Specify a linear layout manager.

        // Specify a linear layout manager.
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView.setLayoutManager(layoutManager)

        // Specify an adapter.

        // Specify an adapter.
        val adapter: RecyclerView.Adapter<*> =
            RecyclerViewAdapter(this, mRecyclerViewItems)
        recyclerView.adapter = adapter
    }


    private fun insertAdsInMenuItems() {
        if (mNativeAds.isEmpty()) {
            return
        }
        val offset: Int = mRecyclerViewItems.size / mNativeAds.size + 1
        var index = 0
        for (ad in mNativeAds) {
            mRecyclerViewItems.add(index, ad)
            index += offset
        }
    }

    private fun loadNativeAds() {
        val builder = AdLoader.Builder(this, "ca-app-pub-3940256099942544/8135179316")
        adLoader =
            builder.forUnifiedNativeAd { unifiedNativeAd -> // A native ad loaded successfully, check if the ad loader has finished loading
                // and if so, insert the ads into the list.
                mNativeAds.add(unifiedNativeAd)
                if (!adLoader.isLoading) {
                    insertAdsInMenuItems()
                }
            }.withAdListener(
                object : AdListener() {
                    override fun onAdFailedToLoad(errorCode: Int) {
                        // A native ad failed to load, check if the ad loader has finished loading
                        // and if so, insert the ads into the list.
                        Log.e("MainActivity", "The previous native ad failed to load. Attempting to"
                                    + " load another.")
                        if (!adLoader.isLoading) {
                            insertAdsInMenuItems()
                        }
                    }
                }).build()

        // Load the Native ads.
        adLoader.loadAds(AdRequest.Builder().build(), 3)
    }

    /**
     * Adds [MenuItem]'s from a JSON file.
     */
    private fun addMenuItemsFromJson() {
        try {
            val jsonDataString = readJsonDataFromFile()
            val menuItemsJsonArray = JSONArray(jsonDataString)
            for (i in 0 until menuItemsJsonArray.length()) {
                val menuItemObject = menuItemsJsonArray.getJSONObject(i)
                val menuItemName = menuItemObject.getString("name")
                val menuItemDescription = menuItemObject.getString("description")
                val menuItemPrice = menuItemObject.getString("price")
                val menuItemCategory = menuItemObject.getString("category")
                val menuItemImageName = menuItemObject.getString("photo")
                val menuItem = MenuItem(
                    menuItemName, menuItemDescription, menuItemPrice,
                    menuItemCategory, menuItemImageName
                )
                mRecyclerViewItems.add(menuItem)
            }
        } catch (exception: IOException) {
            Log.e(NativeAdActivity::class.java.name, "Unable to parse JSON file.", exception)
        } catch (exception: JSONException) {
            Log.e(NativeAdActivity::class.java.name, "Unable to parse JSON file.", exception)
        }
    }

    /**
     * Reads the JSON file and converts the JSON data to a [String].
     *
     * @return A [String] representation of the JSON data.
     * @throws IOException if unable to read the JSON file.
     */
    @Throws(IOException::class)
    private fun readJsonDataFromFile(): String {
        var inputStream: InputStream? = null
        val builder = StringBuilder()
        try {
            var jsonDataString: String? = null
            inputStream = resources.openRawResource(R.raw.menu_items_json)
            val bufferedReader = BufferedReader(InputStreamReader(inputStream, "UTF-8")
            )
            while (bufferedReader.readLine().also({ jsonDataString = it }) != null) {
                builder.append(jsonDataString)
            }
        } finally {
            inputStream?.close()
        }
        return String(builder)
    }
}