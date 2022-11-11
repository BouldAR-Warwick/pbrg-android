package com.example.pbrg_android.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.pbrg_android.R

class MainPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)
        setSupportActionBar(findViewById(R.id.my_toolbar))
        displayUsername()

    }



    private fun displayUsername() {
        // Extract the string from the Intent that started this activity
        val username = intent.getStringExtra(com.example.pbrg_android.ui.login.EXTRA_MESSAGE)

        // Capture the layout's TextView and set the string as its text
        val textView = findViewById<TextView>(R.id.displayName).apply {
            text = username
        }
    }
}