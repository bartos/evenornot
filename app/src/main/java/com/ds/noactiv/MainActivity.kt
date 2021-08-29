package com.ds.noactiv

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.webkit.WebSettings
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.w3c.dom.Text
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

        val valueLen = 10.0.pow(Random.nextInt(1, 6)).toInt()
        val nextValue = Random.nextInt(0, 999999/valueLen)

        theNumber.setText("$nextValue")
    }

    private fun setResults(isEven: Boolean, ad: String? = null){
        if(ad!=null && ad.isNotEmpty() ) {
            Toast.makeText(this, ad, Toast.LENGTH_SHORT).show()
        }


//            <item name="android:textSize">25dp</item>
//            <item name="android:textColor">@color/purple_500</item>
//
//
//            <item name="android:textSize">15dp</item>
//            <item name="android:textColor">@color/teal_700</item>
        val activeColor = ContextCompat.getColor(this, R.color.purple_500);
        val nonActiveColor = ContextCompat.getColor(this, R.color.teal_700);
        if(isEven){
            labelEven.setTextSize(TypedValue.COMPLEX_UNIT_DIP,25f)
            labelEven.setTextColor(activeColor)
            labelOdd.setTextSize(TypedValue.COMPLEX_UNIT_DIP,15f)
            labelOdd.setTextColor(nonActiveColor)


        }else
        {
            labelEven.setTextColor(nonActiveColor)
            labelEven.setTextSize(TypedValue.COMPLEX_UNIT_DIP,15f)
            labelOdd.setTextColor(activeColor)
            labelOdd.setTextSize(TypedValue.COMPLEX_UNIT_DIP,25f)
        }


    }

    /**
     * alternative implementation, if offline
     */
    private fun isEven(x: Int): Boolean = x % 2 == 0


    fun checkTheNumber(view: android.view.View) {

        val value = "${theNumber.text}".toInt()
        val apiUrl = "$url$value"
        Log.d("check","ApiUrl: $apiUrl")
        val jsonObjectRequest = JsonObjectRequest( Request.Method.GET, apiUrl, null,
            { response ->
                val resp = "Response: %s".format(response.toString())
                Log.d("check:resp", resp)
                val even = response.getBoolean("iseven")
                val ad = response.getString("ad")
                setResults(even, ad)
            },
            { error ->
                Log.d("JSON_error", "$error")
                setResults(isEven(value))
            }
        )
        requestQueue.add(jsonObjectRequest)

    }


}