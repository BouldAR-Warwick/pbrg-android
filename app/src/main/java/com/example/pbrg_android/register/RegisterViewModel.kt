package com.example.pbrg_android.register

import android.text.TextUtils
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pbrg_android.R
import com.example.pbrg_android.data.model.LoggedInUser
import com.example.pbrg_android.login.LoginResult
import com.example.pbrg_android.user.UserManager
import com.example.pbrg_android.utility.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class RegisterViewModel @Inject constructor(private val userManager: UserManager) : ViewModel() {
    private val _registerForm = MutableLiveData<RegisterFormState>()
    val registerFormState: LiveData<RegisterFormState> = _registerForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun register(username: String, password: String, email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when(val result = userManager.registerUser(username, password, email)) {
                is Result.Success ->
                    _loginResult.postValue(LoginResult(success = LoggedInUser(
                        sessionId = result.data.sessionId,
                        uid = result.data.uid,
                        displayName = result.data.displayName)
                    ))
                else ->
                    // todo: split error type
                    _loginResult.postValue(LoginResult(error = R.string.login_failed))
            }
        }

    }

    fun checkWholeForm(username: String, password: String, confirmPassword: String, email: String) {
        // check username, password
        val usernameValid = isUserNameValid(username)
        val passwordValid = isPasswordValid(password)
        val confirmPasswordValid = isConfirmPasswordValid(password,confirmPassword)
        val emailValid = isPasswordValid(email)

        val allValid = usernameValid && passwordValid && confirmPasswordValid && emailValid

        // form is valid check
        if (allValid) {
            _registerForm.value = RegisterFormState(isDataValid = true)
        }
    }

    fun usernameChanged(username: String) {
        // check username
        val usernameValid = isUserNameValid(username)
        if (!usernameValid) {
            _registerForm.value = RegisterFormState(usernameError = R.string.invalid_username)
        }
    }

    fun passwordChanged(password: String) {
        // check password
        val passwordValid = isPasswordValid(password)
        if (!passwordValid) {
            _registerForm.value = RegisterFormState(passwordError = R.string.invalid_password)
        }
    }

    fun confirmPasswordChanged(password: String, confirmPassword: String) {
        // check password
        val confirmPasswordValid = isConfirmPasswordValid(password, confirmPassword)
        if (!confirmPasswordValid) {
            _registerForm.value = RegisterFormState(passwordMisMatch = R.string.password_mismatch)
        }
    }

    fun emailChanged(email: String) {
        // check password
        val emailValid = isEmailValid(email)
        if (!emailValid) {
            _registerForm.value = RegisterFormState(emailError = R.string.invalid_email)
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isUserNameValid(username: String): Boolean {
        return username.length >= 6
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    private fun isConfirmPasswordValid(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword
    }

}