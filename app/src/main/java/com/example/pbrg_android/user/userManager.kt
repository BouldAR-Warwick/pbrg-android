package com.example.pbrg_android.user

import android.widget.Toast
import com.example.pbrg_android.data.LoginDataSource
import com.example.pbrg_android.data.RegisterDataSource
import com.example.pbrg_android.utility.Result
import com.example.pbrg_android.data.model.LoggedInUser
import com.example.pbrg_android.data.model.LoginData
import com.example.pbrg_android.data.model.RegisterData
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handles User lifecycle. Manages registrations, logs in and logs out.
 * Knows when the user is logged in.
 *
 * Marked with @Singleton since we only one an instance of UserManager in the application graph.
 */
@Singleton
class UserManager @Inject constructor(
    private val loginDataSource: LoginDataSource,
    private val registerDataSource: RegisterDataSource,
    // Since UserManager will be in charge of managing the UserComponent lifecycle,
    // it needs to know how to create instances of it
    private val userComponentFactory: UserComponent.Factory
) {

    /**
     *  UserComponent is specific to a logged in user. Holds an instance of UserComponent.
     *  This determines if the user is logged in or not, when the user logs in,
     *  a new Component will be created. When the user logs out, this will be null.
     */
    var userComponent: UserComponent? = null
        private set

    var user: LoggedInUser? = null
        private set

    fun isUserLoggedIn() = userComponent != null

    suspend fun registerUser(username: String, password: String, email: String): Result<LoggedInUser> {
        return withContext(Dispatchers.IO) {

            val result = registerDataSource.register(RegisterData(username, password, email))
            if (result is Result.Success) {
                setLoggedInUser(result.data)
                userJustLoggedIn()
            }
            result
        }
    }

    suspend fun login(username: String, password: String, stayLoggedIn: Boolean): Result<LoggedInUser> {
        return withContext(Dispatchers.IO) {
            // call dataSource login method
            val baseUrl: String = "https://grabourg.dcs.warwick.ac.uk/webservices-1.0-SNAPSHOT"
            val result = loginDataSource.login(baseUrl, LoginData(username, password, stayLoggedIn))
            // return true if result contains LoggedInUser data
            if (result is Result.Success) {
                setLoggedInUser(result.data)
                userJustLoggedIn()
            }

            result
        }
    }

    fun logout() {
        // When the user logs out, we remove the instance of UserComponent from memory
        user = null
        userComponent = null
        MMKV.defaultMMKV().clearAll()
    }

    private fun userJustLoggedIn() {
        // When the user logs in, we create a new instance of UserComponent
        userComponent = userComponentFactory.create()
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
    }
}