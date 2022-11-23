package com.example.pbrg_android.wall

import com.example.pbrg_android.R
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.pbrg_android.Application
import com.example.pbrg_android.login.LoginViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class WallActivity : AppCompatActivity() {
    // @Inject annotated fields will be provided by Dagger
    @Inject
    lateinit var wallViewModel: WallViewModel

    //ListView component
    private var mList: ListView? = null

    //ListView data source
    private var data: MutableList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        // Creates an instance of Login component by grabbing the factory from the app graph
        // and injects this activity to that Component
        (application as Application).appComponent.wallComponent().create().inject(this)

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_wall_routes)
        // Set toolbar with back button
        var toolbar: Toolbar = findViewById(R.id.wall_toolbar)
        toolbar.title = ""
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        findViewById<TextView>(R.id.selectedGymName).apply {
            text = intent.getStringExtra("selectedGym")
        }
        mList = findViewById<View>(R.id.routeList) as ListView

        // Get data through data source
        GlobalScope.launch {
            wallViewModel.getWall()
        }
        data = mutableListOf<String>()
        for (i in 0..19) {
            data!!.add("#$i")
        }
        val adapter = WallAdapter(data)
        mList!!.adapter = adapter
        //ListView item click event
        mList!!.setOnItemClickListener { _, _, i, l ->
            Toast.makeText(
                this@WallActivity,
                "item clicked i = " + i + "l = " + l,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}