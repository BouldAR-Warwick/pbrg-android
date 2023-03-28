package com.example.pbrg_android.utility

data class LoginInfo(
    var uid: Int = 0,
    var username: String = "",
    var expireTime: Long = 254800 // 7 days
)
