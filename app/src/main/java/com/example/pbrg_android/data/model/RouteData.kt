package com.example.pbrg_android.data.model

import android.graphics.Bitmap

/**
 * Data class that captures route information
 */
data class RouteData (
    val routeID: Int,
    val routeName: String,
    val difficulty: String,
    val routeImage: Bitmap
)