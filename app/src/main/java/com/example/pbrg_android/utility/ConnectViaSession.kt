package com.example.pbrg_android.utility

import android.content.Context
import com.android.volley.Header
import com.android.volley.NetworkResponse
import com.example.pbrg_android.data.model.LoggedInUser
import com.google.gson.Gson
import com.tencent.mmkv.MMKV
import org.json.JSONObject

/**
 * Handles http request with session and cookies
 */
class ConnectViaSession(private val context: Context) {
    private var mHeader: MutableList<Header> = mutableListOf()
    private var cookies: MutableMap<String, String> = mutableMapOf()
    private var sesstionID: String? = null

    /**
     * Extract session ID from the cookies
     * */
    fun getSession(response: NetworkResponse) {
        // Get header
        println(response.allHeaders.toString())
        try {
            mHeader = response.allHeaders as MutableList<Header>
            var rawCookies: LoggedInUser
            // Iterate headers to find cookies
            mHeader.forEach {
                val header: JSONObject  =JSONObject(Gson().toJson(it))
                if (header["mName"] == "Set-Cookie") {
                    val cookieContent: String = header["mValue"].toString()
                    println(cookieContent.substring(0, cookieContent.indexOf(";")))
                    val key: String = cookieContent.substring(0, cookieContent.indexOf("="))
                    val value: String = cookieContent.substring(cookieContent.indexOf("=")+1, cookieContent.indexOf(";"))
                    cookies.set(key, value)
                }
            }
//            cookies.forEach{
//                println("${it.key}, ${it.value}")
//            }
//            println("sessionid is $sesstionID")
            sesstionID = cookies.get("JSESSIONID")

            // Save session id to local storage
            saveSession(sesstionID)
        } catch (e: Throwable) {
            println(e.stackTraceToString())
        }
    }

    /**
     * Save session id to local storage with mmkv
     * */
    fun saveSession(sessionID: String?) {
        var kv: MMKV = MMKV.defaultMMKV()
        kv.encode("sessionID", sessionID)
    }

    /**
     * Get session id from local storage
     * */
    fun getSession(): String? {
        val kv: MMKV = MMKV.defaultMMKV()
        return kv.decodeString("sessionID")
    }


}