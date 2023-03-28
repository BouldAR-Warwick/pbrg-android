package com.example.pbrg_android.main

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Parcel
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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

    private var currentGym: String = ""

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
            // Return to login page if user is not logged in
            println("==========================================================")
            finish()

        } else {
            // Load main page
            setContentView(binding.root)
            var toolbar: Toolbar = findViewById(R.id.my_toolbar)
            toolbar.title = ""
            setSupportActionBar(findViewById(R.id.my_toolbar))
            // Display necessary information on the page
            displayName.text = intent.getStringExtra(EXTRA_MESSAGE)
            displaySelectedGym()

            // Search button
            search.setOnClickListener { onSearchRequested() }

            // Wall button
            wall.setOnClickListener { gotoWall() }

            // Setting|displayName
            displayName.setOnClickListener { gotoSetting(displayName.text.toString()) }
            setting.setOnClickListener { gotoSetting(displayName.text.toString()) }

            // If the MainActivity needs to be displayed, we get the UserComponent
            // from the application graph and gets this Activity injected
            userManager.userComponent!!.inject(this)
        }

    }
    /***
     * Navigate to Search page
     */
    override fun onSearchRequested(): Boolean {
        startForResult.launch(Intent(this, SearchActivity::class.java))
        return true
    }

    /***
     * Navigate to Setting page
     */
    private fun gotoSetting(username: String) {
        val intent: Intent = Intent(this, SettingActivity::class.java).apply {
            putExtra("username", username)
        }
        startActivity(intent)
    }

    /***
     * Display the primary gym or selected gym from searched result
     */
    private fun displaySelectedGym() {
        var selectedGym: String? = intent.getStringExtra("selectedGym")
//        println("selected gym is $selectedGym") // for debugging

        // Display selected gym from search result
        if (selectedGym != null) {
            findViewById<TextView>(R.id.selected_gym).apply {
                text = selectedGym
            }
            getGym(selectedGym!!)
            currentGym = selectedGym!!
        }
        // Display primary gym
        else {
            // Display primary gym
            GlobalScope.launch(Dispatchers.IO) {
                var result: Result<String> = mainViewModel.getPrimaryGym()
                if (result is Result.Success) {
                    selectedGym = result.data
                    findViewById<TextView>(R.id.selected_gym).apply {
                        text = selectedGym
                    }
                    getGym(selectedGym!!)
                    currentGym = selectedGym!!
                } else {
                    runOnUiThread {
                        Toast.makeText(applicationContext, "Error getting primary gym!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    /**
     * Fetch the selected gym info
     * */
    private fun getGym(selectedGym: String) {
        GlobalScope.launch(Dispatchers.IO) {
            // Fetch selected gym
            var result: Result<Int> = Result.Success(0)
            result = mainViewModel.getGym(selectedGym)
            if (result is Result.Error) {
                runOnUiThread {
                    Toast.makeText(applicationContext, "Error fetching selected gym!", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Fetch wall image of selected gym
                var imageResult: Result<Bitmap> = mainViewModel.getWallImage()
                // Display wall image of selected gym
                if (imageResult is Result.Success) {
                    mainViewModel.storeWallImage(imageResult.data)
                    runOnUiThread {
                        val imageView = findViewById<ImageView>(R.id.wall)
                        imageView.setImageBitmap(imageResult.data)
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(applicationContext, "Error displaying image!", Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }
    }

    /**
     * Navigate to Wall page
     * */
    private fun gotoWall() {
        val intent: Intent = Intent(this, WallActivity::class.java).apply {
            val selectedGym: String? = intent.getStringExtra("selectedGym")
            putExtra("selectedGym", currentGym)
        }
        startActivity(intent)
    }

}