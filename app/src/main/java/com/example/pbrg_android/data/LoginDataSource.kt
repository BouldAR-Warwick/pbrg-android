package com.example.pbrg_android.data

import android.content.Context
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.Volley
import com.example.pbrg_android.data.model.LoggedInUser
import com.example.pbrg_android.data.model.LoginData
import com.example.pbrg_android.utility.Result
import com.google.gson.Gson
import com.google.gson.internal.`$Gson$Types`
import kotlinx.coroutines.*
import kotlinx.coroutines.NonCancellable.join
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

    suspend fun login(loginData: LoginData) : Result<LoggedInUser> {
        return withContext(Dispatchers.IO) {

        var result : Result<LoggedInUser>
        val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), 1223, "Jane Doe")
        result = Result.Success(fakeUser)

//        try {
            val data = JSONObject(Gson().toJson(loginData))
//            val url = "https://webhook.site/924f4f23-e388-4aa1-882f-d0846425d208"
            val url = "https://grabourg.dcs.warwick.ac.uk/webservices-1.0-SNAPSHOT/Login"
            val requstQueue = Volley.newRequestQueue(context)
            var future : RequestFuture<JSONObject> = RequestFuture.newFuture()
            val jsonobj: JsonObjectRequest = object : JsonObjectRequest(
                Method.POST, url, data, future, future){}

            requstQueue.add(jsonobj)
            try {
                val response : JSONObject = future.get()
                val fUser = LoggedInUser(response.getString("sessionID"), response.getInt("uid"), response.getString("username"))
                result = Result.Success(fUser)
            } catch (e: Throwable) {
//                val displayName = (result as Result.Success<LoggedInUser>).data.displayName
//                println(displayName)
            }



//        } catch (e: Throwable) {
//            result = Result.Error(IOException("Error logging in", e))
//        }

        result
        }


    }


//    try {
//        // TODO: handle loggedInUser authentication
//        val data = JSONObject(Gson().toJson(loginData))
//        var result : Result<LoggedInUser>
//        val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), 1223, "Jane Doe")
//        result = Result.Success(fakeUser)
//
////            val url = "https://webhook.site/924f4f23-e388-4aa1-882f-d0846425d208"
//        val url = "https://grabourg.dcs.warwick.ac.uk/webservices-1.0-SNAPSHOT/Login"
//        val requstQueue = Volley.newRequestQueue(context)
//        val jsonobj: JsonObjectRequest = object : JsonObjectRequest(
//            Method.POST, url, data,
//            Response.Listener { response ->
////                    val status = response.getInt("Status")
//                val status = response.getString("sessionID")
//                println(status)
//                if(status=="123456"){
//                    println("==========1")
//
//                    //return true
//                    val fUser = LoggedInUser(response.getString("sessionID"), response.getInt("uid"), response.getString("username"))
//                    println("==========2")
//
//                    result = Result.Success(fUser)
//                }
//                else{
//                    result = Result.Error(IOException("Error logging in"))
//                }
//            }, Response.ErrorListener {
//
//                // return false
//                result = Result.Error(IOException("Error logging in"))
//            }
//        ) { //here I want to post data to sever
//        }
//
//        requstQueue.add(jsonobj)
//        val displayName = (result as Result.Success<LoggedInUser>).data.displayName
//        println(displayName)
//        return result
//
//    } catch (e: Throwable) {
//        return Result.Error(IOException("Error logging in", e))
//    }

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