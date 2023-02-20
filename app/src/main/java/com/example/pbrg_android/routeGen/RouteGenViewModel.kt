package com.example.pbrg_android.routeGen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import com.example.pbrg_android.data.RouteGenDataSource
import com.tencent.mmkv.MMKV
import com.example.pbrg_android.utility.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RouteGenViewModel@Inject constructor(private val routeGenDataSource: RouteGenDataSource) : ViewModel() {

    fun readWallImage(): Bitmap? {
        var kv: MMKV = MMKV.defaultMMKV()
        val byteArray = kv.decodeBytes("wallImage")
        return  BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size)
    }

    suspend fun generateRoute(difficulty : Int) : Result<Bitmap> {
        var result: Result<Bitmap>
        return withContext(Dispatchers.IO) {
            result = routeGenDataSource.generateRoute(difficulty)
            result
        }
    }

}