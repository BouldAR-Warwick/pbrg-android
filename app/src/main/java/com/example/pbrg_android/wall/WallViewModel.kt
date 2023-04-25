package com.example.pbrg_android.wall

import androidx.lifecycle.ViewModel
import com.example.pbrg_android.data.WallDataSource
import com.example.pbrg_android.data.model.RouteListItem
import com.example.pbrg_android.utility.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class WallViewModel @Inject constructor(private val wallDataSource: WallDataSource) : ViewModel() {
    private val baseUrl = "https://grabourg.dcs.warwick.ac.uk/webservices-1.0"

    suspend fun getWall(): Result<Array<RouteListItem>> {

        return withContext(Dispatchers.IO) {
            val result = wallDataSource.routeSearch(baseUrl)
            result
        }
    }
}