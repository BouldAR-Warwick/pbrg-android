package com.example.pbrg_android.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pbrg_android.utility.Result

import javax.inject.Inject

import com.example.pbrg_android.R
import com.example.pbrg_android.data.model.LoggedInUser
import com.example.pbrg_android.user.UserManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel @Inject constructor(private val userManager: UserManager) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun checkLoginStatus() {

    }

    fun login(username: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            // can be launched in a separate asynchronous job
            val result = userManager.login(username, password)

            when(result) {
                is Result.Success ->
                    _loginResult.postValue(LoginResult(success = LoggedInUserView(displayName = result.data.displayName)))
                else ->
                    _loginResult.postValue(LoginResult(error = R.string.login_failed))
            }
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