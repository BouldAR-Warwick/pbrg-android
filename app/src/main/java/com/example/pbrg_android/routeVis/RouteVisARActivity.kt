package com.example.pbrg_android.routeVis

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pbrg_android.Application
import com.example.pbrg_android.R
import com.example.pbrg_android.databinding.ActivityRouteVisArBinding
import com.tencent.mmkv.MMKV
import javax.inject.Inject

class RouteVisARActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRouteVisArBinding

    // @Inject annotated fields will be provided by Dagger
    @Inject
    lateinit var routeVisARViewModel: RouteVisARViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        // Creates an instance of Login component by grabbing the factory from the app graph
        // and injects this activity to that Component
        (application as Application).appComponent.routeVisARComponent().create().inject(this)

        super.onCreate(savedInstanceState)

        binding = ActivityRouteVisArBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.back.setOnClickListener {
            //TODO: generate route
        }
    }
}