package com.example.pbrg_android.data

import android.content.Context
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.pbrg_android.activities.MyCallback
import com.example.pbrg_android.data.model.LoggedInUser
import com.example.pbrg_android.data.model.UserData
import com.google.gson.Gson
import org.json.JSONObject
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(username: String, password: String): Result<LoggedInUser> {
        try {
            // TODO: handle loggedInUser authentication
            val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), 1223, "Jane Doe")
            return Result.Success(fakeUser)
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    private fun login(myCallback: MyCallback, context: Context, userData: UserData) {
        Thread {
            val data = JSONObject(Gson().toJson(userData))

            val url = "https://webhook.site/924f4f23-e388-4aa1-882f-d0846425d208"
            val requstQueue = Volley.newRequestQueue(context)
            val jsonobj: JsonObjectRequest = object : JsonObjectRequest(
                Method.POST, url, data,
                Response.Listener { response ->
                    val JSONObj = response.getString("Status")
                    if(JSONObj=="200"){
                        //return true
                        myCallback.onCallback(true)
                    }
                    else{
                        myCallback.onCallback(false)
                    }
                }, Response.ErrorListener {
                    // return  false
                }
            ) { //here I want to post data to sever
            }
            requstQueue.add(jsonobj)
        }.start()

    }

    fun logout() {
        // TODO: revoke authentication
    }
}