package com.example.pbrg_android.data

import android.content.Context
import com.android.volley.NetworkResponse
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.Volley
import com.example.pbrg_android.data.model.LoggedInUser
import com.example.pbrg_android.data.model.LoginData
import com.example.pbrg_android.utility.ConnectViaSession
import com.example.pbrg_android.utility.Result
import com.google.gson.Gson
import com.google.gson.internal.`$Gson$Types`
import kotlinx.coroutines.*
import org.json.JSONObject
import java.io.IOException
import java.lang.reflect.Type
import java.math.BigInteger
import java.security.MessageDigest
import javax.inject.Inject

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource @Inject constructor(private val context: Context) {

    /**
     * Login via HTTP request
     * */
    suspend fun login(baseUrl: String, loginData: LoginData) : Result<LoggedInUser> {
        return withContext(Dispatchers.IO) {
            var result: Result<LoggedInUser>
            try {
                val data = JSONObject(Gson().toJson(loginData))
                println("=====================$data")
                val url = "$baseUrl/Login"
                // POST request
                val requestQueue = Volley.newRequestQueue(context)
                var future: RequestFuture<JSONObject> = RequestFuture.newFuture()
                val jsonObjRequest: JsonObjectRequest = object : JsonObjectRequest(
                    Method.POST, url, data, future, future){
                    override fun parseNetworkResponse(response: NetworkResponse?): Response<JSONObject>
                    {
                        ConnectViaSession(context).getSession(response!!)
                        return super.parseNetworkResponse(response)
                    }
                }

                requestQueue.add(jsonObjRequest)
                // Process fetched data
                result = try {
                    val response : JSONObject = future.get()
                    val sessionId = ConnectViaSession(context).getSession()
                    val existingUser = LoggedInUser(
                        sessionId!!,
                        response.getInt("uid"),
                        response.getString("username"))
                    Result.Success(existingUser)
                } catch (e: Throwable) {
                    println(e.message)
                    Result.Error(IOException("Error logging in", e))
                }

            } catch (e: Throwable) {
                result = Result.Error(IOException("Error logging in", e))
            }

            result
        }
    }
}