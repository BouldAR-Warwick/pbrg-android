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

    suspend fun login(baseUrl: String, loginData: LoginData) : Result<LoggedInUser> {
        return withContext(Dispatchers.IO) {
            var result: Result<LoggedInUser>
            val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), 1223, "Jane")
            result = Result.Success(fakeUser)
            try {
                val data = JSONObject(Gson().toJson(loginData))
//                val url = "https://webhook.site/924f4f23-e388-4aa1-882f-d0846425d208"
                val url = "$baseUrl/Login"
                println(url)

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
                try {
                    val response : JSONObject = future.get()
                    val sessionId = ConnectViaSession(context).getSession()
                    val existingUser = LoggedInUser(
                        sessionId!!,
                        response.getInt("uid"),
                        response.getString("username"))
                    result = Result.Success(existingUser)
                } catch (e: Throwable) {
                    println(e.message)
                    // TODO: handle exception
                }

            } catch (e: Throwable) {
                result = Result.Error(IOException("Error logging in", e))
            }

            result
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