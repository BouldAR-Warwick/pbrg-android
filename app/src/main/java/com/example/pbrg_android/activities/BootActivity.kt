package com.example.pbrg_android.activities

import android.os.Bundle
import com.example.pbrg_android.R
import com.example.pbrg_android.Application
import com.example.pbrg_android.login.LoginViewModel
import javax.inject.Inject

class BootActivity : BaseActivity() {
    @Inject
    lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?){

        (application as Application).appComponent.loginComponent().create().inject(this)

        setContentView(R.layout.activity_boot)

        super.onCreate(savedInstanceState)


        loginViewModel.checkLoginStatus()

    }
}
