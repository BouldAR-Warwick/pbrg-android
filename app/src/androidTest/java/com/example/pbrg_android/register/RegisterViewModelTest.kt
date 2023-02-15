package com.example.pbrg_android.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.pbrg_android.R
import com.example.pbrg_android.user.UserManager
import org.junit.Assert.*
import android.text.TextUtils
import android.util.Patterns
import androidx.test.core.app.ApplicationProvider
import com.example.pbrg_android.data.LoginDataSource
import com.example.pbrg_android.data.RegisterDataSource
import com.example.pbrg_android.user.UserComponent
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito.mock
import java.util.*


class RegisterViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    val mockUserManager : UserManager = mock(UserManager::class.java)

    var registerViewModel : RegisterViewModel? = null
    @Before
    fun setUp() {
        registerViewModel = RegisterViewModel(mockUserManager)
    }

    @Test
    fun testRegister() {
        registerViewModel!!.checkWholeForm("testuser", "123456", "123456", "test@mail.com")
        assertNotEquals(
            RegisterFormState(isDataValid = false), registerViewModel!!.registerFormState.value
        )
        registerViewModel!!.checkWholeForm("testuser", "123456", "123", "test@mail.com")
        assertEquals(
            RegisterFormState(isDataValid = true), registerViewModel!!.registerFormState.value
        )
    }

    @Test
    fun testInvalidUsername() {
        registerViewModel!!.usernameChanged("testuser")
        assertNotEquals(
            RegisterFormState(usernameError = R.string.invalid_username), registerViewModel!!.registerFormState.value
        )
        registerViewModel!!.usernameChanged("abc")
        assertEquals(
            RegisterFormState(usernameError = R.string.invalid_username), registerViewModel!!.registerFormState.value
        )
    }

    @Test
    fun testInvalidPassword() {
        registerViewModel!!.passwordChanged("123456")
        assertNotEquals(
            RegisterFormState(passwordError = R.string.invalid_password), registerViewModel!!.registerFormState.value
        )
        registerViewModel!!.passwordChanged("abc")
        assertEquals(
            RegisterFormState(passwordError = R.string.invalid_password), registerViewModel!!.registerFormState.value
        )
    }

    @Test
    fun testInvalidEmail() {
        registerViewModel!!.emailChanged("test@mail.com")
        assertNotEquals(
            RegisterFormState(emailError = R.string.invalid_email), registerViewModel!!.registerFormState.value
        )
        registerViewModel!!.emailChanged("testmail.com")
        assertEquals(
            RegisterFormState(emailError = R.string.invalid_email), registerViewModel!!.registerFormState.value
        )
    }

    @Test
    fun testPasswordMatches() {
        registerViewModel!!.confirmPasswordChanged("123456", "123456")
        assertNotEquals(
            RegisterFormState(passwordMisMatch = R.string.password_mismatch), registerViewModel!!.registerFormState.value
        )
        registerViewModel!!.confirmPasswordChanged("123456", "12345")
        assertEquals(
            RegisterFormState(passwordMisMatch = R.string.password_mismatch), registerViewModel!!.registerFormState.value
        )
    }

}