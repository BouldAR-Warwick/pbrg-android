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
        // initialize mmkv
        MMKV.initialize(this)
    }

}