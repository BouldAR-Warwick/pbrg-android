package com.example.pbrg_android.route

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.pbrg_android.Application
import com.example.pbrg_android.R
import com.example.pbrg_android.databinding.ActivityLoginBinding
import com.example.pbrg_android.databinding.ActivityRouteBinding
import javax.inject.Inject

class RouteActivity : AppCompatActivity(){

    private lateinit var binding: ActivityRouteBinding

    // @Inject annotated fields will be provided by Dagger
    @Inject
    lateinit var routeViewModel: RouteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        // Creates an instance of Login component by grabbing the factory from the app graph
        // and injects this activity to that Component
        (application as Application).appComponent.routeComponent().create().inject(this)

        super.onCreate(savedInstanceState)

        binding = ActivityRouteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set toolbar with back button
        val toolbar= binding.routeToolbar
        toolbar.title = ""
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        val selectedGymName = binding.selectedGymName
        val selectedRouteName = binding.selectedRouteName
        val difficulty = binding.difficulty
        val viewAR = binding.viewAR
        val deleteRoute = binding.deleteRoute
        val commentRoute = binding.commentRoute
        val routeImage = binding.routeImage

        selectedGymName.text = intent.getStringExtra("selectedGym")
        selectedRouteName.text = intent.getStringExtra("selectedRoute")
        difficulty.text = intent.getStringExtra("selectedRoute")

        viewAR.setOnClickListener {
            //TODO: Add AR functionality
        }

        deleteRoute.setOnClickListener {
//            routeViewModel.deleteRoute(routeID)
        }

        commentRoute.setOnClickListener {
            //TODO: Add comment functionality
        }

    }
}