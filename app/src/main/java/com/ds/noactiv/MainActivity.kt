package com.ds.noactiv

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlin.math.pow
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var theNumber: EditText
    private  lateinit var requestQueue: RequestQueue
    private lateinit var labelOdd: TextView
    private  lateinit var labelEven: TextView

    private val url by lazy { getString(R.string.isevenapi_url) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        theNumber = findViewById(R.id.theNumber)
        labelEven = findViewById(R.id.labelEven)
        labelOdd = findViewById(R.id.labelOdd)
        requestQueue = Volley.newRequestQueue(this)

    }

    fun randomizer(view: android.view.View) {
        Log.d("randomizer","called by :${view.id}")
        val valueLen = 10.0.pow(Random.nextInt(1, 6)).toInt()
        val nextValue = Random.nextInt(0, 999999/valueLen)
        theNumber.setText("$nextValue")
    }

    private fun setResults(even: Boolean, ad: String? = null){

        if(ad!=null && ad.isNotEmpty() ) {
            Toast.makeText(this, ad, Toast.LENGTH_LONG).show()
        }
        labelEven.visibility =  if (even) VISIBLE else GONE
        labelOdd.visibility =  if (!even) VISIBLE else GONE
    }

    /**
     * alternative implementation, if offline
     */
    private fun isEven(x: Int): Boolean = x % 2 == 0

    /**
     * Verify if the number is even or odd.
     * Using external RESTapi.
     */
    fun checkTheNumber(view: android.view.View) {
        Log.d("check", "method called by :${view.id}")

        if(theNumber.text.isEmpty()){
            Log.d("check","Error: empty number value.")
            Toast.makeText(this, getString(R.string.empty_number_value), Toast.LENGTH_LONG).show()
            return
        }
        //make integer value from textView
        val numberValue = "${theNumber.text}".toInt()
        if (numberValue <0){
            Log.d("check","Error: negative value input.")
            Toast.makeText(this, "Only positive values are supported.", Toast.LENGTH_LONG).show()
            //TODO Check if api can support negatives in enterprise, add token in settings activity
            return
        }
        val apiUrl = "$url${numberValue}"

        val jsonObjectRequest = JsonObjectRequest( Request.Method.GET, apiUrl, null,
            { response ->
                Log.d("check:resp", "$response")
                val even = response.getBoolean("iseven")
                val ad = response.getString("ad")
                setResults(even, ad)
            },
            { error ->
                Log.d("JSON_error", "$error")
                setResults(isEven(numberValue))
            }
        )
        requestQueue.add(jsonObjectRequest)
    }
}