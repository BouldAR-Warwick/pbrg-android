package com.example.pbrg_android.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.pbrg_android.Application
import com.example.pbrg_android.R
import com.example.pbrg_android.databinding.ActivityMainPageBinding
import com.example.pbrg_android.login.EXTRA_MESSAGE
import com.example.pbrg_android.search.SearchActivity
import com.example.pbrg_android.wall.WallActivity
import com.tencent.mmkv.MMKV
import org.json.JSONObject
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

        if (!userManager.isUserLoggedIn()) {
            // return to login page if user is not logged in
        } else {
            // Load main page
            setContentView(binding.root)
            var toolbar: Toolbar = findViewById(R.id.my_toolbar)
            toolbar.title = ""
            setSupportActionBar(findViewById(R.id.my_toolbar))
            // Display necessary message
            displayUsername()
            displaySelectedGym()

            // Search button
            search.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    onSearchRequested()
                }
            })

            // Wall button
            wall.setOnClickListener(object: View.OnClickListener {
                override fun onClick(v: View?) {
                    loadWall()
                }

            })

            // If the MainActivity needs to be displayed, we get the UserComponent
            // from the application graph and gets this Activity injected
            userManager.userComponent!!.inject(this)
        }

    }

    override fun onSearchRequested(): Boolean {
        startForResult.launch(Intent(this, SearchActivity::class.java))
        return true
    }

    private fun displayUsername() {
        // Extract the string from the Intent that started this activity
        val username = intent.getStringExtra(EXTRA_MESSAGE)
        if (username != null) {
            // Capture the layout's TextView and set the string as its text
            findViewById<TextView>(R.id.displayName).apply {
                text = username
            }
        }
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
        }
    }

    // Select wall
    private fun loadWall() {
        val intent: Intent = Intent(this, WallActivity::class.java).apply {
            val selectedGym: String? = intent.getStringExtra("selectedGym")
            putExtra("selectedGym", selectedGym)
        }
        startActivity(intent)
    }
}