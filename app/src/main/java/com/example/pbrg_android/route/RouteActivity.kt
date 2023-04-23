package com.example.pbrg_android.route

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.pbrg_android.Application
import com.example.pbrg_android.data.model.HoldData
import com.example.pbrg_android.databinding.ActivityRouteBinding
import com.example.pbrg_android.routeVis.RouteVisARActivity
import com.example.pbrg_android.utility.Result
import kotlinx.coroutines.*
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
        println(intent.getStringExtra("selectedGym"))
        val routeID = intent.getIntExtra("routeID", 0)
        selectedRouteName.text = intent.getStringExtra("routeName")
        difficulty.text = "V${intent.getIntExtra("difficulty", -1).toString()}"



        // Get route image with route info highlighted
//        getRouteSequence(routeImage, routeID)

        var routeInfo : Array<HoldData> = arrayOf()
        getRoute(routeID)

        println("Route Info ===========================")
        for (hold in routeInfo) {
            println("$hold.x, $hold.y")
        }

        viewAR.setOnClickListener {
            var routeInfo : Result<Array<HoldData>> = Result.Error(Exception("Error getting route info"))
            GlobalScope.launch(Dispatchers.IO) {
               getRouteInfo(routeID)
            }


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

    private fun getRoute(routeID: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            var result: Result<Int> = routeViewModel.getRoute(routeID)
            if (result is Result.Success) {
                println("got selected route")
            } else {
                println("Error getting selected route")
            }
        }
    }

    private suspend fun getRouteInfo(routeID: Int) {
        var routeInfo : Result<Array<HoldData>> = Result.Error(Exception("Error getting route info"))
        val value = GlobalScope.async {
            routeInfo = routeViewModel.getRouteInfo(routeID)
        }

        value.await()
        if (routeInfo is Result.Success) {
            val holdDataArray = (routeInfo as Result.Success<Array<HoldData>>).data
            val floatHoldArray : FloatArray = FloatArray(holdDataArray.size*2)
            for (i in holdDataArray.indices) {
                floatHoldArray[2*i] = holdDataArray[i].x.toFloat()
                floatHoldArray[2*i+1] = holdDataArray[i].y.toFloat()
            }
            val intent = Intent(this, RouteVisARActivity::class.java).apply{
                putExtra("holdDataArray", floatHoldArray)
            }
            startActivity(intent)
        } else {
            println("Error getting route info")
        }
    }


}