package com.example.pbrg_android.search

import androidx.lifecycle.ViewModel
import com.example.pbrg_android.data.SearchDataSource
import com.example.pbrg_android.utility.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchViewModel @Inject constructor(private val searchDataSource: SearchDataSource) : ViewModel() {

    suspend fun gymSearch(query: String?): Result<Array<String>> {
        return withContext(Dispatchers.IO) {
            val result: Result<Array<String>> = searchDataSource.gymSearch(query)
            result
        }
    }
}