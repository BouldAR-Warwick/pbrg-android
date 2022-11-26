package com.example.pbrg_android.routeGen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.pbrg_android.Application
import com.example.pbrg_android.R
import com.example.pbrg_android.databinding.ActivityRouteGenBinding
import com.tencent.mmkv.MMKV
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
        val spinner: Spinner = binding.selectDifficulty
        val generate = binding.generate
        val selectedGymName = binding.selectedGymName
        var routeImage = binding.routeImage
        selectedGymName.text = intent.getStringExtra("selectedGym")

        // Create an ArrayAdapter using the string array and a default spinner layout
        // Populate difficulties choices 0-13
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

        // Set up wall image
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

        generate.setOnClickListener {
            //TODO: generate route
        }
    }
}