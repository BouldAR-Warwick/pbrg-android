package com.example.pbrg_android.data.model

import com.google.gson.annotations.SerializedName

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 * User details post authentication that is exposed to the UI
 */
data class LoggedInUser(
    @SerializedName("sessionID") val sessionId: String,
    @SerializedName("uid") val uid: Int,
    @SerializedName("displayName") val displayName: String,
)