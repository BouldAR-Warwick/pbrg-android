package com.example.pbrg_android.routeGen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import com.example.pbrg_android.data.RouteGenDataSource
import com.example.pbrg_android.data.model.HoldData
import com.tencent.mmkv.MMKV
import com.example.pbrg_android.utility.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RouteGenViewModel@Inject constructor(private val routeGenDataSource: RouteGenDataSource) : ViewModel() {
    private val baseUrl = "https://grabourg.dcs.warwick.ac.uk/webservices-1.0"

    /**
     * Read stored wall image from MMKV local storage
     * */
    fun readWallImage(): Bitmap? {
        var kv: MMKV = MMKV.defaultMMKV()
        val byteArray = kv.decodeBytes("wallImage")
        return  BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size)
    }

    /**
     * Generate route of the selected difficulty
     * */
    suspend fun generateRoute(difficulty : Int) : Result<Int> {
        var result: Result<Int>
        return withContext(Dispatchers.IO) {
            result = routeGenDataSource.generateRoute(baseUrl, difficulty)
            result
        }
    }

    /**
     * Fetch generated route
     * */
    suspend fun getRoute(routeID: Int): Result<Int> {
        return withContext(Dispatchers.IO) {
            var result: Result<Int> = routeGenDataSource.getRoute(baseUrl, routeID)
            result
        }
    }

    /**
     * Fetch generated route image
     * */
    suspend fun getRouteImage(routeID : Int) : Result<Bitmap> {
        var result: Result<Bitmap>
        return withContext(Dispatchers.IO) {
            result = routeGenDataSource.getRouteImage(baseUrl, routeID)
            result
        }
    }

    /**
     * Fetch generated route information
     * */
    suspend fun getRouteInfo(routeID: Int): Result<Array<HoldData>> {
        var result: Result<Array<HoldData>>
        return withContext(Dispatchers.IO) {
            result = routeGenDataSource.getRouteInfo(baseUrl, routeID)
            result
        }
    }

}