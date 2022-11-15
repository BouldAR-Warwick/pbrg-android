package com.example.pbrg_android.ui.login

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.pbrg_android.data.LoginRepository
import com.example.pbrg_android.data.Result

import com.example.pbrg_android.R

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun checkLoginStatus() {

    }

    fun login(username: String, password: String) {
        // can be launched in a separate asynchronous job
        val result = loginRepository.login(username, password)

        if (result is Result.Success) {
            _loginResult.value =
                LoginResult(success = LoggedInUserView(displayName = result.data.displayName))
        } else {
            _loginResult.value = LoginResult(error = R.string.login_failed)
        }
    }

    fun checkWholeForm(username: String, password: String) {
        // check username, password
        val usernameValid = isUserNameValid(username)
        val passwordValid = isPasswordValid(password)

        val allValid = usernameValid && passwordValid

        // form is valid check
        if (allValid) {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    fun usernameChanged(username: String) {
        // check username
        val usernameValid = isUserNameValid(username)
        if (!usernameValid) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        }
    }

    fun passwordChanged(password: String) {
        // check password
        val passwordValid = isPasswordValid(password)
        if (!passwordValid) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}