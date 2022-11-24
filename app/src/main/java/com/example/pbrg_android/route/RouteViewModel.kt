package com.example.pbrg_android.route

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.example.pbrg_android.data.RouteDataSource
import com.example.pbrg_android.utility.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class RouteViewModel @Inject constructor(private val routeDataSource: RouteDataSource) : ViewModel(){

    suspend fun getRoute(routeID: Int): Result<Int> {
        return withContext(Dispatchers.IO) {
            var result: Result<Int> = routeDataSource.getRoute(routeID)
            result
        }
    }
    
    suspend fun getRouteImage(routeID: Int): Result<Bitmap> {
        var result: Result<Bitmap>
        return withContext(Dispatchers.IO) {
            result = routeDataSource.getRouteImage(routeID)
            result
        }
    }

    fun deleteRoute(): Result<Int> {
        var result: Result<Int> = Result.Error(IOException("Error deleting route"))
        GlobalScope.launch(Dispatchers.IO) {
            result = routeDataSource.deleteRoute()
        }
        return result
    }
}