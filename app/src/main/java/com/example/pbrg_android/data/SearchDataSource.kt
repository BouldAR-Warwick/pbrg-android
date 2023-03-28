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
    /**
     * Perform gym search via HTTP POST request
     * */
    suspend fun gymSearch(baseUrl : String, query: String?): Result<Array<String>> {
        return withContext(Dispatchers.IO) {
            var result: Result<Array<String>>

            try {
                val data = JSONObject("""{"queryword":"$query"}""")
                val url = "$baseUrl/SearchGym"

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

                result = try {
                    // Extract search result as a list of gyms
                    val response: JSONObject = future.get()
                    val jsonArray: JSONArray = response.getJSONArray("gyms")
                    val gymList = Array(jsonArray.length()) {
                        jsonArray.getString(it)
                    }
                    Result.Success(gymList)
                } catch (e: Throwable) {
                    Result.Error(IOException("Error searching gym", e))
                }

            } catch (e: Throwable) {
                result = Result.Error(IOException("Error searching gym", e))
            }

            result
        }
    }
}