package com.example.pbrg_android.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.example.pbrg_android.R
import com.example.pbrg_android.Application
import com.example.pbrg_android.data.model.LoggedInUser
import com.example.pbrg_android.login.EXTRA_MESSAGE
import com.example.pbrg_android.login.LoginViewModel
import com.example.pbrg_android.main.MainActivity
import com.example.pbrg_android.utility.LoginInfo
import com.tencent.mmkv.MMKV
import javax.inject.Inject

class BootActivity : BaseActivity() {
    @Inject
    lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        (application as Application).appComponent.loginComponent().create().inject(this)

        val handler : Handler = Handler(Looper.getMainLooper())
        var kv: MMKV = MMKV.defaultMMKV()
        if (kv.containsKey("login_info") && kv.containsKey("sessionID")){
            // Login info cached
            Application.login_info = kv.decodeString("login_info")!!.toMyObject<LoginInfo>()[0]
            // Fetch sessionID
            val sessionID = kv.decodeString("sessionID")
            // Create user component with cached user info and session ID
            loginViewModel.login(LoggedInUser(Application.login_info.username, Application.login_info.uid,
                sessionID!!
            ))
            // Navigate to main page
            handler.postDelayed(Runnable {
                run {
                    val displayName = Application.login_info.username
                    // display welcome popup
                    val welcome = getString(R.string.welcome)
                    Toast.makeText(
                        applicationContext,
                        "$welcome $displayName",
                        Toast.LENGTH_LONG
                    ).show()
                    val intent = Intent(this, MainActivity::class.java).apply{
                        putExtra(EXTRA_MESSAGE, displayName)
                    }
                    startActivity(intent)
                    this.finish()
                }
            }, 1000L)
        } else {
            // otherwise initialise login_info
            Application.login_info = LoginInfo()
            // Navigate to log in page
            handler.postDelayed(Runnable {
                run {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.addCategory("login")
                    startActivity(intent)
                    finish()
                }
            }, 1000L)
        }

        loginViewModel.checkLoginStatus()

    }
}
