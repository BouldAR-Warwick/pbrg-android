package com.example.pbrg_android.search

import androidx.appcompat.widget.Toolbar
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.pbrg_android.Application
import com.example.pbrg_android.R
import com.example.pbrg_android.utility.Result
import kotlinx.coroutines.*
import javax.inject.Inject


class SearchActivity: AppCompatActivity() {

    @Inject
    lateinit var searchViewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        // Ask Dagger to inject our dependencies
        (application as Application).appComponent.injectSearch(this)

        super.onCreate(savedInstanceState)
        // Load search page
        setContentView(R.layout.activity_search)
        var toolbar: Toolbar = findViewById(R.id.search_toolbar)
        toolbar.title = ""
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        // Load SearchView
        val mSearchView: SearchView = findViewById(R.id.searchView)
        mSearchView.isIconified = false
        mSearchView.isSubmitButtonEnabled = true

        var searchResult: Result<Array<String>>

        mSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                GlobalScope.launch {
                    searchResult = searchViewModel.gymSearch(query)
                    if (searchResult is Result.Success) {
                        updateSearchResult((searchResult as Result.Success<Array<String>>).data)
                    } else {
                        runOnUiThread {
                            Toast.makeText(applicationContext, "Error fetching search results", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }

        })


    }

    /**
     * Update search result, populate list view
     * */
    private fun updateSearchResult(gymList: Array<String>) {
        runOnUiThread(Runnable {
            val listView = findViewById<View>(R.id.search_result_listview) as ListView

            for (s in gymList) {
                println("gym is : $s")
            }
            val arrayAdapter = ArrayAdapter(applicationContext, android.R.layout.simple_list_item_1, gymList)
            listView.adapter = arrayAdapter
            // Deal with gym selection
            listView.setOnItemClickListener{ _, _, position, _ ->
                val selectedGym = gymList[position]

                Toast.makeText(applicationContext, "Selected $selectedGym", Toast.LENGTH_SHORT).show()
                returnWithSelectedGym(selectedGym)
            }
        })
    }

    /**
     * Return to main activity with the selected gym
     * */
    private fun returnWithSelectedGym(selectedGym: String) {
        intent.putExtra("selectedGym", selectedGym)
        setResult(RESULT_OK, intent)
        finish()
    }


}