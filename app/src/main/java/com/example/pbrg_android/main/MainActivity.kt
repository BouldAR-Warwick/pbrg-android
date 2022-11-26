package com.example.pbrg_android.main

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Parcel
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.pbrg_android.Application
import com.example.pbrg_android.R
import com.example.pbrg_android.databinding.ActivityMainPageBinding
import com.example.pbrg_android.login.EXTRA_MESSAGE
import com.example.pbrg_android.search.SearchActivity
import com.example.pbrg_android.setting.SettingActivity
import com.example.pbrg_android.utility.Result
import com.example.pbrg_android.wall.WallActivity
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.IOException
import javax.inject.Inject

class MainActivity : AppCompatActivity(){
    private lateinit var binding: ActivityMainPageBinding

    @Inject
    lateinit var mainViewModel: MainViewModel

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            //  you will get result here in result.data
            intent = result.data
            displaySelectedGym()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {

        // Ask Dagger to inject our dependencies
        (application as Application).appComponent.injectMain(this)

        super.onCreate(savedInstanceState)

        val userManager = (application as Application).appComponent.userManager()

        binding = ActivityMainPageBinding.inflate(layoutInflater)
        val search = binding.showSearchDialog
        val wall = binding.wall
        val setting = binding.setting
        val displayName = binding.displayName
        if (!userManager.isUserLoggedIn()) {
            // return to login page if user is not logged in
        } else {
            // Load main page
            setContentView(binding.root)
            var toolbar: Toolbar = findViewById(R.id.my_toolbar)
            toolbar.title = ""
            setSupportActionBar(findViewById(R.id.my_toolbar))
            // Display necessary message
            displayName.text = intent.getStringExtra(EXTRA_MESSAGE)
            displaySelectedGym()

            // Search button
            search.setOnClickListener { onSearchRequested() }

            // Wall button
            wall.setOnClickListener { gotoWall() }

            displayName.setOnClickListener { gotoSetting(displayName.text.toString()) }

            setting.setOnClickListener { gotoSetting(displayName.text.toString()) }

            // If the MainActivity needs to be displayed, we get the UserComponent
            // from the application graph and gets this Activity injected
            userManager.userComponent!!.inject(this)
        }

    }

    override fun onSearchRequested(): Boolean {
        startForResult.launch(Intent(this, SearchActivity::class.java))
        return true
    }

    private fun gotoSetting(username: String) {
        val intent: Intent = Intent(this, SettingActivity::class.java).apply {
            putExtra("username", username)
        }
        startActivity(intent)
    }

    // Display the primary gym or selected gym from searched result
    private fun displaySelectedGym() {
        // TODO: display primary gym
        val selectedGym: String? = intent.getStringExtra("selectedGym")
        println("selected gym is $selectedGym")

        if (selectedGym != null) {
            findViewById<TextView>(R.id.selected_gym).apply {
                text = selectedGym
            }
            getGym(selectedGym)
        }
    }

    private fun getGym(selectedGym: String) {
        GlobalScope.launch(Dispatchers.IO) {
            // get selected gym
            var result: Result<Int> = Result.Success(0)
            result = mainViewModel.getGym(selectedGym)
            if (result is Result.Success) {
                println("got selected gym")
            } else {
                println("Error getting selected gym")
            }

            // display wall image of the gym
            var imageResult: Result<Bitmap> = Result.Error(IOException("Error loading image"))
            imageResult = mainViewModel.getWallImage()

            if (imageResult is Result.Success) {
                mainViewModel.storeWallImage(imageResult.data)
                runOnUiThread {
                    val imageView = findViewById<ImageView>(R.id.wall) as ImageView
                    imageView.setImageBitmap(imageResult.data)
                }
            } else {
                println("Error displaying image!")
            }
        }
    }


    // Select wall
    private fun gotoWall() {
        val intent: Intent = Intent(this, WallActivity::class.java).apply {
            val selectedGym: String? = intent.getStringExtra("selectedGym")
            putExtra("selectedGym", selectedGym)
        }
        startActivity(intent)
    }

}