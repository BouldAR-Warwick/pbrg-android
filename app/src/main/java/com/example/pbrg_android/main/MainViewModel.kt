package com.example.pbrg_android.main

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pbrg_android.R
import com.example.pbrg_android.data.MainDataSource
import com.example.pbrg_android.utility.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class MainViewModel @Inject constructor(private val mainDataSource: MainDataSource) : ViewModel() {

    suspend fun getGym(selectedGym: String): Result<Int> {
        return withContext(Dispatchers.IO) {
            var result: Result<Int> = mainDataSource.getGym(selectedGym)
            result
        }
    }

    suspend fun getWallImage(): Result<Bitmap> {
        return withContext(Dispatchers.IO) {
            var result: Result<Bitmap> = mainDataSource.getImage()
            result
        }
    }

}