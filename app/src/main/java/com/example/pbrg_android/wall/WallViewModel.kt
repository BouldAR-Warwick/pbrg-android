package com.example.pbrg_android.wall

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pbrg_android.data.WallDataSource
import com.example.pbrg_android.data.model.RouteListItem
import com.example.pbrg_android.utility.Result
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject


class WallViewModel @Inject constructor(private val wallDataSource: WallDataSource) : ViewModel() {
    fun getWall(): MutableList<RouteListItem> {
        var data: MutableList<RouteListItem> = mutableListOf()
        var result: Result<Array<RouteListItem>> = Result.Error(IOException("no route found"))
        viewModelScope.launch {
            result = wallDataSource.routeSearch()
        }
        if (result is Result.Success) {
            (result as Result.Success<Array<RouteListItem>>).data.forEach {
                data!!.add(it)
            }
        }
        return data
    }
}