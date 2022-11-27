package com.example.pbrg_android.data

import android.content.Context
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.Volley
import com.example.pbrg_android.data.model.RouteListItem
import com.example.pbrg_android.utility.ConnectViaSession
import com.example.pbrg_android.utility.Result
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import javax.inject.Inject

class WallDataSource @Inject constructor(private val context: Context) {

    suspend fun routeSearch(): Result<Array<RouteListItem>> {
        return withContext(Dispatchers.IO) {
            var result: Result<Array<RouteListItem>>
            var fakeRoutes: Array<RouteListItem> = arrayOf()
            result = Result.Success(fakeRoutes)

            // POST search request
            try {
                val data = JSONObject() // empty data
                val url = "https://grabourg.dcs.warwick.ac.uk/webservices-1.0-SNAPSHOT/GetRoutes"

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
                    println(response.toString())
                    println(response.get("routes").toString())

                    val jsonArray = response.get("routes") as JSONArray

                    var routeList: Array<RouteListItem> = Array(jsonArray.length()) {
                        val route = jsonArray.getJSONObject(it)
                        RouteListItem(route.getInt("routeId"), route.getString("routeName"), route.getInt("difficulty"))
                    }

                    routeList.forEach {
                        println(it.toString())
                    }

                    Result.Success(routeList)
                } catch (e: Throwable) {
                    println("error $e")
                    Result.Error(IOException("Error getting routes", e))
                }

            } catch (e: Throwable) {
                result = Result.Error(IOException("Error getting routes", e))
            }

            result
        }
    }
}