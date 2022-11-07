package com.example.pbrg_android.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pbrg_android.R
import com.example.pbrg_android.main.Application
import com.example.pbrg_android.ui.login.LoginActivity
import com.example.pbrg_android.utility.LoginInfo
import com.google.gson.Gson
import com.google.gson.internal.`$Gson$Types`
import com.tencent.mmkv.MMKV
import java.lang.reflect.Type


abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (MMKV.defaultMMKV().containsKey("login_info")){
            // have login info cached
             Application.login_info = MMKV.defaultMMKV().decodeString("login_info")!!.toMyObject<LoginInfo>()[0]
            // continue to destination
        } else {
            // otherwise initialise login_info
            Application.login_info = LoginInfo()
            // Load log in page
            setContentView(R.layout.activity_login)
            val intent = Intent(Intent.ACTION_VIEW)
            intent.addCategory("login")
            startActivity(intent)
            finish()
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