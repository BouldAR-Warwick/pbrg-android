package com.example.pbrg_android.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pbrg_android.R
import com.example.pbrg_android.Application
import com.example.pbrg_android.utility.LoginInfo
import com.google.gson.Gson
import com.google.gson.internal.`$Gson$Types`
import com.tencent.mmkv.MMKV
import java.lang.reflect.Type
import android.os.Handler
import android.os.Looper
import com.example.pbrg_android.login.EXTRA_MESSAGE
import com.example.pbrg_android.main.MainActivity

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boot)
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