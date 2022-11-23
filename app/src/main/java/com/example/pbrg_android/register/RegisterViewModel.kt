package com.example.pbrg_android.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pbrg_android.R
import com.example.pbrg_android.data.model.LoggedInUser
import com.example.pbrg_android.login.LoginFormState
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
}