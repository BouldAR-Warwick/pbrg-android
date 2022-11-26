package com.example.pbrg_android.setting

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.pbrg_android.Application
import com.example.pbrg_android.R
import com.example.pbrg_android.databinding.ActivitySettingBinding
import com.example.pbrg_android.login.LoginActivity

import javax.inject.Inject

class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding

    // @Inject annotated fields will be provided by Dagger
    @Inject
    lateinit var settingViewModel: SettingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        // Creates an instance of Login component by grabbing the factory from the app graph
        // and injects this activity to that Component
        (application as Application).appComponent.injectSetting(this)

        super.onCreate(savedInstanceState)

        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var toolbar: Toolbar = findViewById(R.id.setting_toolbar)
        toolbar.title = ""
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        val logout = binding.logout
        val username = binding.displayName
        username.text = intent.getStringExtra("username")

        logout.setOnClickListener {
            settingViewModel.logout()
            val intent: Intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }
}