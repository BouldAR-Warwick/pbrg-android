package com.example.pbrg_android.search

import androidx.lifecycle.ViewModel
import com.example.pbrg_android.data.SearchDataSource
import com.example.pbrg_android.utility.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchViewModel @Inject constructor(private val searchDataSource: SearchDataSource) : ViewModel() {
    private val baseUrl = "https://grabourg.dcs.warwick.ac.uk/webservices-1.0"

    /**
     * Perform gym search
     * */
    suspend fun gymSearch(query: String?): Result<Array<String>> {
        return withContext(Dispatchers.IO) {
            val result: Result<Array<String>> = searchDataSource.gymSearch(baseUrl, query)
            result
        }
    }
}