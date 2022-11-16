package com.example.pbrg_android.data

import android.content.Context
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.pbrg_android.data.model.LoggedInUser
import com.example.pbrg_android.data.model.LoginData
import com.example.pbrg_android.utility.Result
import com.google.gson.Gson
import com.google.gson.internal.`$Gson$Types`
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

    fun login(username: String, password: String): Result<LoggedInUser> {
        try {
            // TODO: handle loggedInUser authentication
            val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), 1223, "Jane Doe")
            return Result.Success(fakeUser)
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun login(loginData: LoginData) : Result<LoggedInUser> {
        try {
            // TODO: handle loggedInUser authentication
            val data = JSONObject(Gson().toJson(loginData))
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

    fun hash(input:String): String {
        val md = MessageDigest.getInstance("SHA-256")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }

    private fun Any.toMyJson(): String? {
        return Gson().toJson(this)
    }

    inline fun <reified T> String.toMyObject(): List<T> {
        val listType: Type = `$Gson$Types`.newParameterizedTypeWithOwner(null, ArrayList::class.java, T::class.java)
        return if(!contains("[")){
            Gson().fromJson("[${this}]", listType)
        }else{
            Gson().fromJson(this, listType)
        }
    }
}