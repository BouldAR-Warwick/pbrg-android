package com.example.pbrg_android.route

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.pbrg_android.Application
import com.example.pbrg_android.databinding.ActivityRouteBinding
import com.example.pbrg_android.utility.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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
        val routeID = intent.getIntExtra("routeID", 0)
        selectedRouteName.text = intent.getStringExtra("routeName")
        difficulty.text = intent.getStringExtra("difficulty")

        // Get route image
        getRouteSequence(routeImage, routeID)

        viewAR.setOnClickListener {
            //TODO: Add AR functionality
        }

        deleteRoute.setOnClickListener {
            routeViewModel.deleteRoute()
        }

        commentRoute.setOnClickListener {
            //TODO: Add comment functionality
        }

    }

    private fun getRouteSequence(routeImage: ImageView, routeID: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            var result: Result<Int> = routeViewModel.getRoute(routeID)
            if (result is Result.Success) {
                println("got selected route")
                var imageResult: Result<Bitmap> = routeViewModel.getRouteImage(routeID)
                if (imageResult is Result.Success) {
                    runOnUiThread {
                        routeImage.setImageBitmap(imageResult.data)
                    }
                } else {
                    println("Error getting route image")
                }
            } else {
                println("Error getting selected route")
            }

        }
    }
}