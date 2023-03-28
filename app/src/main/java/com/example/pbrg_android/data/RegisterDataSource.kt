package com.example.pbrg_android.data


import android.content.Context
import com.android.volley.NetworkResponse
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.Volley
import com.example.pbrg_android.data.model.LoggedInUser
import com.example.pbrg_android.data.model.RegisterData
import com.example.pbrg_android.utility.ConnectViaSession
import com.example.pbrg_android.utility.Result
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.IOException
import javax.inject.Inject


/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class RegisterDataSource @Inject constructor(private val context: Context) {
    /**
     * Register via HTTP POST request
     * */
    suspend fun register(baseUrl : String, registerData: RegisterData) : Result<LoggedInUser> {
        return withContext(Dispatchers.IO) {
            var result: Result<LoggedInUser>

            try {
                val data = JSONObject(Gson().toJson(registerData))
                val url = "$baseUrl/Register"

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

                result = try {
                    val response: JSONObject = future.get()
                    val sessionId = ConnectViaSession(context).getSession()
                    val newUser = LoggedInUser(
                        sessionId!!,
                        response.getInt("uid"),
                        response.getString("username"))
                    Result.Success(newUser)
                } catch (e: Throwable) {
                    Result.Error(IOException("Error registering", e))
                }

            } catch (e: Throwable) {
                result = Result.Error(IOException("Error registering", e))
            }

            result
        }
    }

}