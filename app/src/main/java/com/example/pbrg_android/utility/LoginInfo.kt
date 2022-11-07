package com.example.pbrg_android.utility

data class LoginInfo(
    var uid: Int = 0,
    var expireTime:Long = 36400,
    var keepLogin:Boolean = false
)
