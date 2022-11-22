package com.example.pbrg_android.data

import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.Volley
import com.example.pbrg_android.utility.ConnectViaSession
import com.example.pbrg_android.utility.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import android.content.Context
import javax.inject.Inject

class SearchDataSource @Inject constructor(private val context: Context) {
    // Perform gym search
    suspend fun gymSearch(query: String?): Result<Array<String>> {
        return withContext(Dispatchers.IO) {
            var result: Result<Array<String>>
            var fakeGymList: Array<String> = arrayOf()
            result = Result.Success(fakeGymList)
            // POST search request
            try {
                val data = JSONObject("""{"queryword":$query}""")
//                val url = "https://webhook.site/924f4f23-e388-4aa1-882f-d0846425d208"
                val url = "https://grabourg.dcs.warwick.ac.uk/webservices-1.0-SNAPSHOT/SearchGym"

                val requestQueue = Volley.newRequestQueue(context)
                var future: RequestFuture<JSONObject> = RequestFuture.newFuture()
                val jsonObjRequest: JsonObjectRequest = object : JsonObjectRequest(
                    Method.POST, url, data, future, future){
                    override fun getHeaders(): MutableMap<String, String> {
                        val sessionId: String = ConnectViaSession(context).getSession()!!
                        return if(sessionId != "") {
                            var headers: MutableMap<String, String> = mutableMapOf<String, String>()
                            headers["Cookie"] = "JSESSIONID=$sessionId"
                            headers
                        } else {
                            super.getHeaders()
                        }
                    }
                }
                requestQueue.add(jsonObjRequest)

                try {
                    // Extract search result as a list of gyms
                    val response: JSONObject = future.get()
                    val jsonArray: JSONArray = response.getJSONArray("gyms")
                    val gymList = Array(jsonArray.length()) {
                        jsonArray.getString(it)
                    }
                    result = Result.Success(gymList)
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