package com.example.pbrg_android

import android.annotation.SuppressLint
import android.app.Application
import com.example.pbrg_android.di.AppComponent
import com.example.pbrg_android.di.DaggerAppComponent
import com.example.pbrg_android.utility.LoginInfo
import com.google.gson.Gson
import com.google.gson.internal.`$Gson$Types`
import com.tencent.mmkv.MMKV
import java.lang.reflect.Type


class Application : Application() {

    // Instance of the AppComponent that will be used by all the Activities in the project
    val appComponent: AppComponent by lazy {
        initializeComponent()
    }

    open fun initializeComponent(): AppComponent {
        // Creates an instance of AppComponent using its Factory constructor
        // We pass the applicationContext that will be used as Context in the graph
        return DaggerAppComponent.factory().create(applicationContext)
    }

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