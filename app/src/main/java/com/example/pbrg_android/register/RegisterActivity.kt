package com.example.pbrg_android.register

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import com.example.pbrg_android.Application
import com.example.pbrg_android.R
import com.example.pbrg_android.data.model.LoggedInUser
import com.example.pbrg_android.databinding.ActivityRegisterBinding
import com.example.pbrg_android.login.EXTRA_MESSAGE
import com.example.pbrg_android.login.hash
import com.example.pbrg_android.main.MainActivity
import javax.inject.Inject


class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    // @Inject annotated fields will be provided by Dagger
    @Inject
    lateinit var registerViewModel: RegisterViewModel

    private val delay: Long = 600 // 600ms after user stops typing
    private var lastTextEdit: Long = 0
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        // Creates an instance of Login component by grabbing the factory from the app graph
        // and injects this activity to that Component
        (application as Application).appComponent.registerComponent().create().inject(this)

        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Set toolbar with back button
        var toolbar: Toolbar = findViewById(R.id.register_toolbar)
        toolbar.title = ""
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)


        val username = binding.registerUsername
        val password = binding.registerPassword
        val confirmPassword = binding.confirmPassword
        val email = binding.registerEmail
        val register = binding.register

        registerViewModel.registerFormState.observe(this@RegisterActivity, Observer {
            val registerState = it ?: return@Observer

            // disable login button unless both username / password / email is valid
            register.isEnabled = registerState.isDataValid

            if (registerState.usernameError != null) {
                username.error = getString(registerState.usernameError)
            }
            if (registerState.emailError != null) {
                email.error = getString(registerState.emailError)
            }
            if (registerState.passwordError != null) {
                password.error = getString(registerState.passwordError)
            }
            if (registerState.passwordMisMatch != null) {
                confirmPassword.error = getString(registerState.passwordMisMatch)
            }
        })

        registerViewModel.loginResult.observe(this@RegisterActivity, Observer {
            val loginResult = it ?: return@Observer

            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)
            }
            setResult(Activity.RESULT_OK)

            //Complete and destroy login activity once successful
            finish()
        })

        val usernameChecker = Runnable{
            if (System.currentTimeMillis() > lastTextEdit + delay - 500) {
                registerViewModel.usernameChanged(username.text.toString())

                registerViewModel.checkWholeForm(
                    username.text.toString(),
                    password.text.toString(),
                    confirmPassword.text.toString(),
                    email.text.toString()
                )
            }
        }

        val passwordChecker = Runnable{
            if (System.currentTimeMillis() > lastTextEdit + delay - 500) {
                registerViewModel.passwordChanged(password.text.toString())

                registerViewModel.checkWholeForm(
                    username.text.toString(),
                    password.text.toString(),
                    confirmPassword.text.toString(),
                    email.text.toString()
                )
            }
        }

        val confirmPasswordChecker = Runnable{
            if (System.currentTimeMillis() > lastTextEdit + delay - 500) {
                registerViewModel.confirmPasswordChanged(password.text.toString(),confirmPassword.text.toString())

                registerViewModel.checkWholeForm(
                    username.text.toString(),
                    password.text.toString(),
                    confirmPassword.text.toString(),
                    email.text.toString()
                )
            }
        }

        val emailChecker = Runnable{
            if (System.currentTimeMillis() > lastTextEdit + delay - 500) {
                registerViewModel.emailChanged(email.text.toString())

                registerViewModel.checkWholeForm(
                    username.text.toString(),
                    password.text.toString(),
                    confirmPassword.text.toString(),
                    email.text.toString()
                )
            }
        }

        username.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                //avoid triggering event when text is empty
                if (s.isNotEmpty()) {
                    lastTextEdit = System.currentTimeMillis()
                    handler.postDelayed(usernameChecker,delay)}
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                handler.removeCallbacks(usernameChecker)
            }
            
            override fun beforeTextChanged(s: CharSequence, start: Int,count: Int, after: Int) {}
        })


        password.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                //avoid triggering event when text is empty
                if (s.isNotEmpty()) {
                    lastTextEdit = System.currentTimeMillis()
                    handler.postDelayed(passwordChecker,delay)}
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                handler.removeCallbacks(passwordChecker)
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,count: Int, after: Int) {}
        })

        confirmPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                //avoid triggering event when text is empty
                if (s.isNotEmpty()) {
                    lastTextEdit = System.currentTimeMillis()
                    handler.postDelayed(confirmPasswordChecker,delay)}
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                handler.removeCallbacks(confirmPasswordChecker)
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,count: Int, after: Int) {}
        })

        email.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                //avoid triggering event when text is empty
                if (s.isNotEmpty()) {
                    lastTextEdit = System.currentTimeMillis()
                    handler.postDelayed(emailChecker,delay)}
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                handler.removeCallbacks(emailChecker)
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,count: Int, after: Int) {}
        })

        register.setOnClickListener {
            registerViewModel.register(username.text.toString(),
                hash(password.text.toString()),
                email.text.toString())
        }
    }

    /**
     * Navigate to Main page
     * */
    private fun updateUiWithUser(loggedInUser: LoggedInUser) {
        // Initiate successful logged in experience
        val displayName = loggedInUser.displayName
        // Display welcome popup
        val welcome = getString(R.string.welcome)
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
        // Navigate to Main page
        val intent = Intent(this, MainActivity::class.java).apply{
            putExtra(EXTRA_MESSAGE, displayName)
        }
        startActivity(intent)
    }

    /**
     * Prompt register failed
     */
    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }

}