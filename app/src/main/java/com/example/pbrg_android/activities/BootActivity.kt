package com.example.pbrg_android.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pbrg_android.R
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.pbrg_android.databinding.ActivityLoginBinding
import com.example.pbrg_android.ui.login.LoginViewModel
import com.example.pbrg_android.ui.login.LoginViewModelFactory
import com.tencent.mmkv.MMKV
import java.util.concurrent.TimeUnit

class BootActivity : BaseActivity() {
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?){
        setContentView(R.layout.activity_boot)
//        TimeUnit.SECONDS.sleep(5L)
        super.onCreate(savedInstanceState)

        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        loginViewModel.checkLoginStatus()

    }
}
