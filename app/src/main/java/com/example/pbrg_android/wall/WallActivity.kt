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
import javax.inject.Inject

class WallActivity : AppCompatActivity() {
    // @Inject annotated fields will be provided by Dagger
    @Inject
    lateinit var wallViewModel: WallViewModel

    //ListView component
    private var routeList: ListView? = null

    //ListView data source
    private var data: MutableList<RouteListItem>? = null

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


        // Get route data through data source
        data = wallViewModel.getWall()

        val adapter = WallAdapter(data)
        routeList!!.adapter = adapter

        // ListView item click event
        routeList!!.setOnItemClickListener { _, _, i, l ->
            Toast.makeText(
                this@WallActivity,
                "item clicked i = " + i + "l = " + l,
                Toast.LENGTH_SHORT
            ).show()

            val intent = Intent(this, RouteActivity::class.java).apply{
                val route: RouteListItem = adapter.getItem(i)
                putExtra("routeID", route.routeID)
                putExtra("routeName", route.routeName)
                putExtra("difficulty", route.difficulty)
                putExtra("selectedGym", intent.getStringExtra("selectedGym"))
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