package com.example.pbrg_android.data

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.Volley
import com.example.pbrg_android.utility.ConnectViaSession
import com.example.pbrg_android.utility.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.IOException
import javax.inject.Inject

class RouteDataSource @Inject constructor(private val context: Context) {

    suspend fun getRoute(routeID: Int): Result<Int> {
        return withContext(Dispatchers.IO) {
            var result: Result<Int>
            result = Result.Success(0)

            // POST get route request
            try {
                val data = JSONObject("""{"routeID":$routeID}""")
                val url = "https://grabourg.dcs.warwick.ac.uk/webservices-1.0-SNAPSHOT/GetRoute"

                val requestQueue = Volley.newRequestQueue(context)
                var future: RequestFuture<JSONObject> = RequestFuture.newFuture()
                val jsonObjRequest: JsonObjectRequest = object: JsonObjectRequest(
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
//                    val response: JSONObject = future.get()
                    Result.Success(1)
                } catch (e: Throwable) {
                    println("eeeeeeeeeeeerror $e")
                    Result.Error(IOException("Error getting selected gym", e))
                }

            } catch (e: Throwable) {
                result = Result.Error(IOException("Error getting selected gym", e))
            }

            result
        }
    }

    suspend fun getRouteImage(routeID: Int): Result<Bitmap> {

        return withContext(Dispatchers.IO) {
            var result: Result<Bitmap>
            result = Result.Error(IOException("Error loading image"))

            // POST route image request
            try {
                val data = JSONObject("""{"routeID":$routeID}""")
                val url = "https://grabourg.dcs.warwick.ac.uk/webservices-1.0-SNAPSHOT/GetRouteImage"

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
                    Result.Error(IOException("Error getting image", e))
                }

            } catch (e: Throwable) {
                result = Result.Error(IOException("Error getting routes", e))
            }

            result
        }
    }

    suspend fun deleteRoute(): Result<Int> {
        return withContext(Dispatchers.IO) {
            var result: Result<Int>
            result = Result.Error(IOException("Error deleting route"))

            // POST delete route request
            try {
                val data = JSONObject("""{}""")
                val url = "https://grabourg.dcs.warwick.ac.uk/webservices-1.0-SNAPSHOT/DeleteRoute"

                val requestQueue = Volley.newRequestQueue(context)
                var future: RequestFuture<JSONObject> = RequestFuture.newFuture()
                val jsonObjRequest: JsonObjectRequest = object: JsonObjectRequest(
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
                    Result.Success(1)
                } catch (e: Throwable) {
                    Result.Error(IOException("Error getting selected gym", e))
                }

            } catch (e: Throwable) {
                result = Result.Error(IOException("Error getting selected gym", e))
            }

            result
        }
    }

}