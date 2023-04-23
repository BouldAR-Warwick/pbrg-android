package com.example.pbrg_android.data

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.Volley
import com.example.pbrg_android.data.model.HoldData
import com.example.pbrg_android.utility.ConnectViaSession
import com.example.pbrg_android.utility.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import javax.inject.Inject

class RouteGenDataSource @Inject constructor(private val context: Context) {

    /**
     * Generate route via HTTP POST request
     * */
    suspend fun generateRoute(baseUrl : String, difficulty : Int) : Result<Int> {
        return withContext(Dispatchers.IO) {
            var result: Result<Int>

            try {
                val data = JSONObject("""{"difficulty":$difficulty}""")
                val url = "$baseUrl/GenerateRouteServlet"

                val requestQueue = Volley.newRequestQueue(context)
                var future: RequestFuture<JSONObject> = RequestFuture.newFuture()
                val jsonObjRequest: JsonObjectRequest = object: JsonObjectRequest(
                    Method.POST, url, data, future, future)
                {
                    override fun getHeaders(): MutableMap<String, String> {
                        val sessionId: String = ConnectViaSession(context).getSession()!!
                        return if (sessionId != "") {
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
                    val response: JSONObject = future.get()
                    Result.Success(response.getInt("routeID"))
                } catch (e: Throwable) {
                    println(e.message)
                    Result.Error(IOException("Error generating route", e))
                }

            } catch (e: Throwable) {
                println(e.message)
                result = Result.Error(IOException("Error generating route!", e))
            }

            result
        }
    }

    suspend fun getRouteImage(baseUrl : String, routeID : Int) : Result<Bitmap> {

        return withContext(Dispatchers.IO) {
            var result: Result<Bitmap>

            try {
                val data = JSONObject("""{"routeID":$routeID}""")
                val url = "$baseUrl/GetRouteImage"

                val requestQueue = Volley.newRequestQueue(context)
                var future: RequestFuture<Bitmap> = RequestFuture.newFuture()
                val imageRequest: ImageRequest = object: ImageRequest(url, future, 0, 0, ImageView.ScaleType.CENTER,Bitmap.Config.RGB_565, future){
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

                requestQueue.add(imageRequest)

                result = try {
                    val response: Bitmap = future.get()
                    Result.Success(response)
                } catch (e: Throwable) {
                    Result.Error(IOException("Error fetching image", e))
                }

            } catch (e: Throwable) {
                result = Result.Error(IOException("Error fetching routes", e))
            }

            result
        }
    }


    /**
     * Fetch route info via HTTP POST request
     * */
    suspend fun getRouteInfo(baseUrl : String, routeID: Int): Result<Array<HoldData>> {

        return withContext(Dispatchers.IO) {
            var result: Result<Array<HoldData>>

            try {
                val data = JSONObject("""{"routeID":$routeID}""")
                val url = "$baseUrl/GetRouteInfo"

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
                    val response: JSONObject = future.get()
                    val jsonArray = response.get("info") as JSONArray

                    var holdList: Array<HoldData> = Array(jsonArray.length()) {
                        val routeInfo = jsonArray.getJSONObject(it)
                        HoldData(routeInfo.getDouble("x"), routeInfo.getDouble("y"))
                    }

                    Result.Success(holdList)

                } catch (e: Throwable) {
                    Result.Error(IOException("Error fetching route info", e))
                }

            } catch (e: Throwable) {
                result = Result.Error(IOException("Error fetching route info", e))
            }

            result
        }
    }

    }