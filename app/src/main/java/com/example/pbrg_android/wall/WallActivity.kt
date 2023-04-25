package com.example.pbrg_android.wall

import android.content.Intent
import com.example.pbrg_android.R
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.pbrg_android.Application
import com.example.pbrg_android.data.model.RouteListItem
import com.example.pbrg_android.route.RouteActivity
import com.example.pbrg_android.routeGen.RouteGenActivity
import com.example.pbrg_android.utility.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

class WallActivity : AppCompatActivity() {
    // @Inject annotated fields will be provided by Dagger
    @Inject
    lateinit var wallViewModel: WallViewModel

    //ListView component
    private var routeList: ListView? = null

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

        val selectedGymName = intent.getStringExtra("selectedGym")
        findViewById<TextView>(R.id.selectedGymName).apply {
            text = selectedGymName
        }
        routeList = findViewById<View>(R.id.routeList) as ListView

        var adapter = WallAdapter(null)
        var result: Result<Array<RouteListItem>> = Result.Error(IOException("Error getting routes"))
        GlobalScope.launch(Dispatchers.IO) {
            // Get route data through data source
            result = wallViewModel.getWall()
            if (result is Result.Success) {
                runOnUiThread {
                    adapter = WallAdapter((result as Result.Success<Array<RouteListItem>>).data.toList())
                    routeList!!.adapter = adapter
                }
            } else {
                runOnUiThread {
                    Toast.makeText(applicationContext, "Error fetching wall", Toast.LENGTH_SHORT).show()
                }
            }
        }


        // ListView item click event
        routeList!!.setOnItemClickListener { _, _, i, l ->

            val intent = Intent(this, RouteActivity::class.java).apply{
                val route: RouteListItem = adapter.getItem(i)
                putExtra("routeID", route.routeID)
                putExtra("routeName", route.routeName)
                putExtra("difficulty", route.difficulty)
                putExtra("selectedGym", selectedGymName)
            }
            startActivity(intent)
        }

        // Generate new route button listener
        val generate = findViewById<Button>(R.id.generateNewRoute)
        generate.setOnClickListener {
            val intent: Intent = Intent(this, RouteGenActivity::class.java).apply {
                putExtra("selectedGym", selectedGymName)
            }
            startActivity(intent)
        }
    }

    override fun onResume() {
        // Creates an instance of Login component by grabbing the factory from the app graph
        // and injects this activity to that Component
        (application as Application).appComponent.wallComponent().create().inject(this)

        super.onResume()

        setContentView(R.layout.activity_wall_routes)
        // Set toolbar with back button
        var toolbar: Toolbar = findViewById(R.id.wall_toolbar)
        toolbar.title = ""
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        val selectedGymName = intent.getStringExtra("selectedGym")
        findViewById<TextView>(R.id.selectedGymName).apply {
            text = selectedGymName
        }
        routeList = findViewById<View>(R.id.routeList) as ListView

        var adapter = WallAdapter(null)
        var result: Result<Array<RouteListItem>> = Result.Error(IOException("Error getting routes"))
        GlobalScope.launch(Dispatchers.IO) {
            // Get route data through data source
            result = wallViewModel.getWall()
            if (result is Result.Success) {
                runOnUiThread {
                    adapter = WallAdapter((result as Result.Success<Array<RouteListItem>>).data.toList())
                    routeList!!.adapter = adapter
                }
            } else {
                runOnUiThread {
                    Toast.makeText(applicationContext, "Error fetching wall", Toast.LENGTH_SHORT).show()
                }
            }
        }


        // ListView item click event
        routeList!!.setOnItemClickListener { _, _, i, l ->

            val intent = Intent(this, RouteActivity::class.java).apply{
                val route: RouteListItem = adapter.getItem(i)
                putExtra("routeID", route.routeID)
                putExtra("routeName", route.routeName)
                putExtra("difficulty", route.difficulty)
                putExtra("selectedGym", selectedGymName)
            }
            startActivity(intent)
        }

        // Generate new route button listener
        val generate = findViewById<Button>(R.id.generateNewRoute)
        generate.setOnClickListener {
            val intent: Intent = Intent(this, RouteGenActivity::class.java).apply {
                putExtra("selectedGym", selectedGymName)
            }
            startActivity(intent)
        }
    }


}