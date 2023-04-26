package com.example.pbrg_android.routeGen

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.pbrg_android.utility.Result
import com.example.pbrg_android.Application
import com.example.pbrg_android.R
import com.example.pbrg_android.data.model.HoldData
import com.example.pbrg_android.databinding.ActivityRouteGenBinding
import com.example.pbrg_android.routeVis.RouteVisARActivity
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class RouteGenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRouteGenBinding

    // @Inject annotated fields will be provided by Dagger
    @Inject
    lateinit var routeGenViewModel: RouteGenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        // Creates an instance of Login component by grabbing the factory from the app graph
        // and injects this activity to that Component
        (application as Application).appComponent.routeGenComponent().create().inject(this)

        super.onCreate(savedInstanceState)

        binding = ActivityRouteGenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set toolbar with back button
        val toolbar= binding.routeToolbar
        toolbar.title = "Generate a new route"
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        var difficulty = -1
        var routeId = -1
        val spinner: Spinner = binding.selectDifficulty
        val generate = binding.generate
        val viewInAR = binding.viewInAr
        val selectedGymName = binding.selectedGymName
        var routeImage = binding.routeImage
        selectedGymName.text = intent.getStringExtra("selectedGym")

        // Create an ArrayAdapter using the string array and a default spinner layout
        // Populate difficulties choices (V0-13)
        ArrayAdapter.createFromResource(
            this,
            R.array.route_difficulties,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        // Set up original wall image
        routeImage.setImageBitmap(routeGenViewModel.readWallImage())

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                generate.isEnabled = false
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                difficulty = position-1
                generate.isEnabled = position != 0
            }
        }

        // Generate route
        generate.setOnClickListener {
            // Get selected difficulty
            GlobalScope.launch(Dispatchers.IO) {
                // Generate route with selected difficulty, returns route id
                val result : Result<Int> = routeGenViewModel.generateRoute(difficulty)
                if (result is Result.Success) {
                    val routeID = result.data
                    var getRouteResult : Result<Int> = routeGenViewModel.getRoute(routeID)
                    if (getRouteResult is Result.Success) {
                        val resultImage : Result<Bitmap> = routeGenViewModel.getRouteImage(routeID)
                        // Update route image
                        if (resultImage is Result.Success){
                            runOnUiThread {
                                routeImage.setImageBitmap(resultImage.data)
                                // Enable "View route in AR" button
                                viewInAR.isEnabled = true
                            }
                            routeId = routeID
                        }
                    }
                    else {
                        runOnUiThread {
                            Toast.makeText(applicationContext, "Error updating route image", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(applicationContext, "Error generating route", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // Navigate to View in AR page
        viewInAR.setOnClickListener {
            // query route info
            GlobalScope.launch(Dispatchers.IO) {
                getRouteInfo(routeId)
            }
        }

    }

    /**
     * Fetch generated route info
     * */
    private suspend fun getRouteInfo(routeID: Int) {
        var routeInfo : Result<Array<HoldData>> = Result.Error(Exception("Error getting route info"))
        val value = GlobalScope.async {
            routeInfo = routeGenViewModel.getRouteInfo(routeID)
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
            runOnUiThread {
                Toast.makeText(
                    applicationContext,"Error retrieving route information", Toast.LENGTH_SHORT).show()
            }
        }
    }

}