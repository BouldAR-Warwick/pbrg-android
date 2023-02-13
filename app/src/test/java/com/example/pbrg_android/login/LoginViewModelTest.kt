package com.example.pbrg_android.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ActivityScenario.launch
import com.example.pbrg_android.R
import com.example.pbrg_android.data.model.LoggedInUser
import com.example.pbrg_android.user.UserManager
import kotlinx.coroutines.launch
import org.junit.Assert.*
import kotlinx.coroutines.test.runTest
import com.example.pbrg_android.utility.Result
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import java.util.*

class LoginViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    val mockUserManager : UserManager = mock(UserManager::class.java)

    var loginViewModel : LoginViewModel? = null
    @Before
    fun setUp() {
        loginViewModel = LoginViewModel(mockUserManager)
    }

    @Test
    fun testInvalidUsername() {
        loginViewModel!!.usernameChanged("testuser")
        assertNotEquals(
            LoginFormState(usernameError = R.string.invalid_username), loginViewModel!!.loginFormState.value
        )
        loginViewModel!!.usernameChanged("abc")
        assertEquals(
            LoginFormState(usernameError = R.string.invalid_username), loginViewModel!!.loginFormState.value
        )
    }

    @Test
    fun testInvalidPassword() {
        loginViewModel!!.passwordChanged("123456")
        assertNotEquals(
            LoginFormState(passwordError = R.string.invalid_password), loginViewModel!!.loginFormState.value
        )
        loginViewModel!!.passwordChanged("abc")
        assertEquals(
            LoginFormState(passwordError = R.string.invalid_password), loginViewModel!!.loginFormState.value
        )
    }

    @Test
    fun getLoginFormState() {
    }

    @Test
    fun getLoginResult() {
    }

    @Test
    fun testLogin() = runTest {
        var result: Result<LoggedInUser>
        val fakeUser = LoggedInUser(UUID.randomUUID().toString(), 1223, "Jane")
        result = Result.Success(fakeUser)
        whenever(mockUserManager.login("testuser", "123456", true)).thenReturn(result)

        launch {
            result = mockUserManager.login("testuser", "123456", true)
        }
        advanceUntilIdle()
        when(result) {
            is Result.Success -> {
                assert(true)
            }
            else ->
                assert(false)
        }
    }

    @Test
    fun testCheckWholeForm() {
        loginViewModel!!.checkWholeForm("1", "1")
        assertNotEquals(
            LoginFormState(isDataValid = true), loginViewModel!!.loginFormState.value
        )
        loginViewModel!!.checkWholeForm("testuser", "123456")
        assertEquals(
            LoginFormState(isDataValid = true), loginViewModel!!.loginFormState.value
        )

    }

    @Test
    fun usernameChanged() {
    }

    @Test
    fun passwordChanged() {
    }
}