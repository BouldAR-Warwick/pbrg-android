package com.example.pbrg_android.search

import androidx.appcompat.widget.Toolbar
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.Volley
import com.example.pbrg_android.Application
import com.example.pbrg_android.R
import com.example.pbrg_android.utility.ConnectViaSession
import com.example.pbrg_android.utility.Result
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import javax.inject.Inject


class SearchActivity: AppCompatActivity() {

    @Inject
    lateinit var searchViewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        // Ask Dagger to inject our dependencies
        (application as Application).appComponent.injectSearch(this)

        super.onCreate(savedInstanceState)
        // Load search page
        setContentView(R.layout.activity_search)
        var toolbar: Toolbar = findViewById(R.id.search_toolbar)
        toolbar.title = ""
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        // Load SearchView
        val mSearchView: SearchView = findViewById<SearchView>(R.id.searchView)
        mSearchView.isIconified = false
        mSearchView.isSubmitButtonEnabled = true

        var searchResult: Result<Array<String>>

        mSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                GlobalScope.launch {
                    searchResult = searchViewModel.gymSearch(query)
                    if (searchResult is Result.Success) {
                        updateSearchResult((searchResult as Result.Success<Array<String>>).data)
                    } else {
                        println("error >?>>>>>>>>>>>>")
                    }
                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }

        })


    }

    // Update search result, populate list view
    private fun updateSearchResult(gymList: Array<String>) {
        runOnUiThread(Runnable {
            val listView = findViewById<View>(R.id.search_result_listview) as ListView

            for (s in gymList) {
                println("gym is : $s")
            }
            val arrayAdapter = ArrayAdapter(applicationContext, android.R.layout.simple_list_item_1, gymList)
            listView.adapter = arrayAdapter
            // Deal with gym selection
            listView.setOnItemClickListener{ _, _, position, _ ->
                val selectedGym = gymList[position]

                Toast.makeText(applicationContext, "Selected $selectedGym", Toast.LENGTH_SHORT).show()
                returnWithSelectedGym(selectedGym)
            }
        })
    }

    // return to main activity with the selected gym
    private fun returnWithSelectedGym(selectedGym: String) {
        intent.putExtra("selectedGym", selectedGym)
        setResult(RESULT_OK, intent)
        finish()
    }

    private suspend fun doMySearch(query: String?): Result<MutableList<String>> {
        return withContext(Dispatchers.IO) {
            val listView = findViewById<View>(R.id.search_result_listview) as ListView
//            val recyclerView = findViewById<View>(R.id.search_result_recyclerview) as RecyclerView
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
                    val gList = mutableListOf<String>()
                    val gymList = Array(jsonArray.length()) {
                        jsonArray.getString(it)
                    }
                    runOnUiThread(Runnable {
                        // Populate list view
                        val arrayAdapter = ArrayAdapter(applicationContext, android.R.layout.simple_list_item_1, gymList)
                        listView.adapter = arrayAdapter
                    })
                    // Deal with gym selection
                    listView.setOnItemClickListener{ _, _, position, _ ->
                        val selectedGym = gymList[position]
                        Toast.makeText(applicationContext, "Selected $selectedGym", Toast.LENGTH_SHORT).show()
                    }
                    //TODO: Display search result in a recycler view
                    result = Result.Success(gList)
                } catch (e: Throwable) {
                    println("error $e")
                    result = Result.Error(IOException("Error searching gym", e))
                }

            } catch (e: Throwable) {
                result = Result.Error(IOException("Error searching gym", e))
            }

            result
        }
    }

}