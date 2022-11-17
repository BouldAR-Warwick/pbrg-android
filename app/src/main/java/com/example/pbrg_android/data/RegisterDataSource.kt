package com.example.pbrg_android.data


import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.Volley
import com.example.pbrg_android.data.model.LoggedInUser
import com.example.pbrg_android.data.model.RegisterData
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

    suspend fun register(registerData: RegisterData) : Result<LoggedInUser> {
        return withContext(Dispatchers.IO) {
            var result: Result<LoggedInUser>
            val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), 1223, "Jane Doe")
            result = Result.Success(fakeUser)

            try {
                val data = JSONObject(Gson().toJson(registerData))
                val url = "https://grabourg.dcs.warwick.ac.uk/webservices-1.0-SNAPSHOT/Register"

                val requstQueue = Volley.newRequestQueue(context)
                var future: RequestFuture<JSONObject> = RequestFuture.newFuture()
                val jsonObjRequest: JsonObjectRequest = object : JsonObjectRequest(
                    Method.POST, url, data, future, future){}

                requstQueue.add(jsonObjRequest)

                try {
                    val response: JSONObject = future.get()
                    val newUser = LoggedInUser(
                        response.getString("sessionID"),
                        response.getInt("uid"),
                        response.getString("username"))
                    result = Result.Success(newUser)
                } catch (e: Throwable) {
                    // TODO: handle exception
                }

            } catch (e: Throwable) {
                result = Result.Error(IOException("Error registering", e))
            }

            result
        }
    }

    fun unregister() {
        // TODO: revoke authentication and unregister
    }
}