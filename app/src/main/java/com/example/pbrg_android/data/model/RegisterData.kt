package com.example.pbrg_android.data.model

import com.google.gson.annotations.SerializedName

/**
 * Data class that captures user information entred
 */
data class RegisterData (
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String,
    @SerializedName("email") val email: String
)