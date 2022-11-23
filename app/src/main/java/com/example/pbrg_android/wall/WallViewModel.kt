package com.example.pbrg_android.wall

import androidx.lifecycle.ViewModel
import com.example.pbrg_android.data.WallDataSource
import com.example.pbrg_android.data.model.RouteData
import com.example.pbrg_android.user.UserManager
import com.example.pbrg_android.utility.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class WallViewModel @Inject constructor(private val wallDataSource: WallDataSource) : ViewModel() {
    suspend fun getWall(): Result<Array<RouteData>> {
        return withContext(Dispatchers.IO) {
            val result: Result<Array<RouteData>> = wallDataSource.routeSearch()
            result
        }
    }

}