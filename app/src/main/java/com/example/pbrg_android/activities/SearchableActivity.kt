package com.example.pbrg_android.activities

import android.app.Activity
import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.Volley
import com.example.pbrg_android.R
import com.example.pbrg_android.utility.ConnectViaSession
import com.example.pbrg_android.utility.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException


class SearchableActivity: Activity() {


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searchable)

        // Get the intent, verify the action and get the query
        val intent = intent
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            GlobalScope.launch {
                doMySearch(query)
            }
        }

        // Get value
        val appData = intent.getBundleExtra(SearchManager.APP_DATA)
        if (appData != null) {
            val testValue = appData.getString("KEY")
            println("extra data = $testValue")
        }
    }

    private suspend fun doMySearch(query: String?): Result<MutableList<String>> {
        return withContext(Dispatchers.IO) {
            val recyclerView = findViewById<View>(R.id.search_result_recyclerview) as RecyclerView
            var result: Result<MutableList<String>>
            var fakeGymList: MutableList<String> = mutableListOf<String>()
            result = Result.Success(fakeGymList)
            // POST search request
            try {
                val data = JSONObject("""{"queryword":$query}""")
//                val url = "https://webhook.site/924f4f23-e388-4aa1-882f-d0846425d208"
                val url = "https://grabourg.dcs.warwick.ac.uk/webservices-1.0-SNAPSHOT/SearchGym"

                val requestQueue = Volley.newRequestQueue(applicationContext)
                var future: RequestFuture<JSONObject> = RequestFuture.newFuture()
                val jsonObjRequest: JsonObjectRequest = object : JsonObjectRequest(
                    Method.POST, url, data, future, future){
                    override fun getHeaders(): MutableMap<String, String> {
                        val sessionId: String = ConnectViaSession(applicationContext).getSession()!!
                        if(sessionId != "") {
                            var headers: MutableMap<String, String> = mutableMapOf<String, String>()
                            headers["Cookie"] = "JSESSIONID=$sessionId"
                            println("header is ${headers.toString()}")
                            return headers
                        } else {
                            return super.getHeaders()
                        }
                    }

//                    override fun parseNetworkResponse(response: NetworkResponse?): Response<JSONObject>
//                    {
//                        ConnectViaSession(applicationContext).getSession(response!!)
//                        return super.parseNetworkResponse(response)
//                    }
                }

                requestQueue.add(jsonObjRequest)

                try {
                    val response: JSONObject = future.get()
                    println(response.toString())
                    val jsonArray: JSONArray = response.getJSONArray("gyms")
                    val gymList: MutableList<String> = mutableListOf<String>()
                    for (i in 0 until jsonArray.length()) {
                        gymList.add(jsonArray.getString(i))
                    }
                    gymList.forEach{
                        println("gymname: $it")
                    }
                    // Display search result as text
                    val textView: TextView = findViewById(R.id.search_result_textview) as TextView
                    textView.text = jsonArray.toString()
                    //TODO: Display search result in a recycler view
                    result = Result.Success(gymList)
                } catch (e: Throwable) {
                    println("werror")
                    result = Result.Error(IOException("Error searching gym", e))
                }

            } catch (e: Throwable) {
                result = Result.Error(IOException("Error searching gym", e))
            }

            result
        }
//        Toast.makeText(this, "do search", Toast.LENGTH_SHORT).show()
    }

}