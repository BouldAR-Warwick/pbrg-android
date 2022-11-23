package com.example.pbrg_android.register

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.pbrg_android.Application
import com.example.pbrg_android.databinding.ActivityLoginBinding
import com.example.pbrg_android.databinding.ActivityRegisterBinding
import com.example.pbrg_android.login.LoginViewModel
import javax.inject.Inject

class RegisterActivity  : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    // @Inject annotated fields will be provided by Dagger
    @Inject
    lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        // Creates an instance of Login component by grabbing the factory from the app graph
        // and injects this activity to that Component
        (application as Application).appComponent.registerComponent().create().inject(this)

        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = binding.registerUsername
        val password = binding.registerPassword
        val confirmPassword = binding.confirmPassword
        val email = binding.registerEmail
        val register = binding.register

        registerViewModel.registerFormState.observe(this@RegisterActivity, Observer {
            val registerState = it ?: return@Observer

            // disable login button unless both username / password is valid
            register.isEnabled = registerState.isDataValid

            if (registerState.usernameError != null) {
                username.error = getString(registerState.usernameError)
            }
            if (registerState.passwordError != null) {
                password.error = getString(registerState.passwordError)
            }
            if (registerState.emailError != null) {
                password.error = getString(registerState.emailError)
            }
            if (registerState.passwordMisMatch != null) {
                password.error = getString(registerState.passwordMisMatch)
            }
        })

    }
}