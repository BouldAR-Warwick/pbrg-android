package com.example.pbrg_android.main

import android.annotation.SuppressLint
import android.app.Application
import com.example.pbrg_android.utility.LoginInfo
import com.google.gson.Gson
import com.google.gson.internal.`$Gson$Types`
import com.tencent.mmkv.MMKV
import java.lang.reflect.Type


class Application : Application() {

    companion object{
        @SuppressLint("StaticFieldLeak")

        lateinit var login_info: LoginInfo

    }

    //override onCreate function to read login info on every activity
    override fun onCreate() {
        super.onCreate()
        login_info = if (MMKV.defaultMMKV().containsKey("login_info")){
            //have login info cached
            MMKV.defaultMMKV().decodeString("login_info")!!.toMyObject<LoginInfo>()[0]
        } else {
            // otherwise initialise login_info
            LoginInfo()
        }
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