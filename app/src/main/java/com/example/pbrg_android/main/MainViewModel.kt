package com.example.pbrg_android.main

import androidx.lifecycle.ViewModel
import com.example.pbrg_android.data.MainDataSource
import javax.inject.Inject

class MainViewModel @Inject constructor(private val mainDataSource: MainDataSource) : ViewModel() {

}