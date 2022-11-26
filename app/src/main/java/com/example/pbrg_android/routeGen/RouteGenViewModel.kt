package com.example.pbrg_android.routeGen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import com.example.pbrg_android.data.RouteDataSource
import com.tencent.mmkv.MMKV
import javax.inject.Inject

class RouteGenViewModel@Inject constructor(private val routeDataSource: RouteDataSource) : ViewModel() {

    fun readWallImage(): Bitmap? {
        var kv: MMKV = MMKV.defaultMMKV()
        val byteArray = kv.decodeBytes("wallImage")
        return  BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size)
    }
}