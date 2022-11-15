package com.example.pbrg_android.activities

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.pbrg_android.R
import org.json.JSONObject

interface MyCallback{
    fun onCallback(response:Boolean)
}

class MainPageActivity : AppCompatActivity(), MyCallback {
    val myCallback = this
    override fun onCallback(response:Boolean) {

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)
        setSupportActionBar(findViewById(R.id.my_toolbar))
//        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        displayUsername()
//        httpGet()
        postJSON()
    }




    private fun displayUsername() {
        // Extract the string from the Intent that started this activity
        val username = intent.getStringExtra(com.example.pbrg_android.ui.login.EXTRA_MESSAGE)

        // Capture the layout's TextView and set the string as its text
        val textView = findViewById<TextView>(R.id.displayName).apply {
            text = username
        }
    }

    private fun httpGet() {
        Thread {
            val textView = findViewById<TextView>(R.id.textView)
            // Instantiate the RequestQueue.
            val queue = Volley.newRequestQueue(this)
            val url = "https://www.google.com"

            // Request a string response from the provided URL.
            val stringRequest = StringRequest(
                Request.Method.GET, url,
                Response.Listener<String> { response ->
                    // Display the first 500 characters of the response string.
                    textView.text = "Response is: ${response.substring(0, 50)}"
                },
                Response.ErrorListener { textView.text = "That didn't work!" })

            // Add the request to the RequestQueue.
            queue.add(stringRequest)

        }.start()
    }

    private fun postJSON() {
        Thread {
            val textView = findViewById<TextView>(R.id.textView)
            val data = JSONObject("""{"name":"test name", "age":25}""")
            val url = "https://webhook.site/924f4f23-e388-4aa1-882f-d0846425d208"
            val requstQueue = Volley.newRequestQueue(this)
            val jsonobj: JsonObjectRequest = object : JsonObjectRequest(
                Method.POST, url, data,
                Response.Listener { response ->
                    val JSONObj = response.getString("Status")
                    if(JSONObj=="200"){
                        //return true
                        myCallback.onCallback(true)
                        textView.text = "Response is OK"
                    }
                    else{
                        myCallback.onCallback(false)
                    }
                }, Response.ErrorListener {
                    // return  false
                    textView.text = "That didn't work!"
                }
            ) { //here I want to post data to sever
            }
            requstQueue.add(jsonobj)
        }.start()

    }



    // Select wall
    private fun selectWall() {

    }
}