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

class MainDataSource @Inject constructor(private val context: Context) {
    /**
     * Fetch wall image via HTTP request
     * */
    suspend fun getImage(baseUrl: String): Result<Bitmap> {
        return withContext(Dispatchers.IO) {
            var result: Result<Bitmap>
            try {
                val data = JSONObject("""{}""") // empty data
                val url = "$baseUrl/GetWallImage"

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
                result = Result.Error(IOException("Error fetching image", e))
            }

            result
        }
    }
    /**
     * Fetch wall image of selected gym via HTTP request
     * */
    suspend fun getGym(baseUrl: String, selectedGym: String): Result<Int> {
        return withContext(Dispatchers.IO) {
            var result: Result<Int>

            try {
                val data = JSONObject("""{"gymname":"$selectedGym"}""")
                val url = "$baseUrl/GetGym"

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
                    Result.Success(response.getInt("gid"))
                } catch (e: Throwable) {
                    Result.Error(IOException("Error getting selected gym", e))
                }

            } catch (e: Throwable) {
                result = Result.Error(IOException("Error getting selected gym", e))
            }

            result
        }
    }
    /**
     * Fetch wall image of primary gym via HTTP request
     * */
    suspend fun getPrimaryGym(baseUrl: String): Result<String> {
        return withContext(Dispatchers.IO) {
            var result: Result<String>

            try {
                val data = JSONObject("""{}""")
                val url = "$baseUrl/GetPrimaryGym"

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
                    println(response.toString())
                    Result.Success(response.getString("gymName"))
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