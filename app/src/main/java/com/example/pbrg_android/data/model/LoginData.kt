package com.example.pbrg_android.data.model

import com.google.gson.annotations.SerializedName

/**
 * Data class that captures user information entered
 */
data class LoginData (
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String,
    val stayLoggedIn: Boolean = true
    )

