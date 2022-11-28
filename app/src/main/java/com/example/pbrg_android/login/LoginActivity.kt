package com.example.pbrg_android.login

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.Observer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import com.example.pbrg_android.main.MainActivity
import com.example.pbrg_android.databinding.ActivityLoginBinding
import com.example.pbrg_android.Application
import com.example.pbrg_android.data.model.LoggedInUser
import com.example.pbrg_android.register.RegisterActivity
import javax.inject.Inject

const val EXTRA_MESSAGE = "com.example.pbrg_android.MESSAGE"

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    // @Inject annotated fields will be provided by Dagger
    @Inject
    lateinit var loginViewModel: LoginViewModel

    private val delay: Long = 600 // 600ms after user stops typing
    private var lastTextEdit: Long = 0
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        // Creates an instance of Login component by grabbing the factory from the app graph
        // and injects this activity to that Component
        (application as Application).appComponent.loginComponent().create().inject(this)

        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = binding.username
        val password = binding.password
        val login = binding.login
        val signup = binding.signup
        val loading = binding.loading
        val stayLoggedIn = binding.stayLoggedIn

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
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
                loginViewModel.usernameChanged(username.text.toString())
                loginViewModel.checkWholeForm(
                    username.text.toString(),
                    password.text.toString(),
                )
            }
        }

        val passwordChecker = Runnable{
            if (System.currentTimeMillis() > lastTextEdit + delay - 500) { loginViewModel.passwordChanged(password.text.toString())
                loginViewModel.passwordChanged(password.text.toString())

                loginViewModel.checkWholeForm(
                    username.text.toString(),
                    password.text.toString()
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

        signup!!.setOnClickListener{
            val intent: Intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        password.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE ->
                    loginViewModel.login(
                        username.text.toString(),
                        password.text.toString(),
                        stayLoggedIn!!.isChecked
                    )
            }
            false
        }


        login.setOnClickListener {
            loading.visibility = View.VISIBLE
            loginViewModel.login(username.text.toString(), password.text.toString(), stayLoggedIn!!.isChecked)
        }
    }

    private fun updateUiWithUser(loggedInUser: LoggedInUser) {
        // Initiate successful logged in experience
        val displayName = loggedInUser.displayName
        // display welcome popup
//        val welcome = getString(R.string.welcome)
//        Toast.makeText(
//            applicationContext,
//            "$welcome $displayName",
//            Toast.LENGTH_LONG
//        ).show()
        val intent = Intent(this, MainActivity::class.java).apply{
            putExtra(EXTRA_MESSAGE, displayName)
        }
        startActivity(intent)
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }


}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}