package com.example.pbrg_android.data


import android.content.Context
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.pbrg_android.data.model.LoggedInUser
import com.example.pbrg_android.data.model.RegisterData
import com.example.pbrg_android.utility.Result
import com.google.gson.Gson
import org.json.JSONObject
import java.io.IOException
import javax.inject.Inject

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class RegisterDataSource @Inject constructor(private val context: Context) {

    fun register(registerData: RegisterData) : Result<LoggedInUser> {
        try {
            // TODO: handle loggedInUser authentication
            val data = JSONObject(Gson().toJson(registerData))
            var result : Result<LoggedInUser>
            val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), 1223, "Jane Doe")
            result = Result.Success(fakeUser)

            val url = "https://webhook.site/924f4f23-e388-4aa1-882f-d0846425d208"
            val requstQueue = Volley.newRequestQueue(context)
            val jsonobj: JsonObjectRequest = object : JsonObjectRequest(
                Method.POST, url, data,
                Response.Listener { response ->
                    val JSONObj = response.getString("Status")
                    if(JSONObj=="200"){
                        //return true
                        val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), 1223, "Jane Doe")
                        result = Result.Success(fakeUser)
                    }
                    else{
                        result = Result.Error(IOException("Error logging in"))
                    }
                }, Response.ErrorListener {
                    // return  false
                    result = Result.Error(IOException("Error logging in"))
                }
            ) { //here I want to post data to sever
            }
            requstQueue.add(jsonobj)

            return result

        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }


    }

    fun logout() {
        // TODO: revoke authentication
    }
}