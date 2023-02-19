package com.example.pbrg_android.user

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import com.example.pbrg_android.data.LoginDataSource
import com.example.pbrg_android.data.RegisterDataSource
import com.example.pbrg_android.data.model.LoggedInUser
import com.example.pbrg_android.data.model.RegisterData
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import com.example.pbrg_android.utility.Result
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.kotlin.*
import org.robolectric.RobolectricTestRunner
import java.util.*


@RunWith(RobolectricTestRunner::class)
class UserManagerTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    val mockLoginDataSource : LoginDataSource = mock(LoginDataSource::class.java)
    val mockRegisterDataSource : RegisterDataSource = mock(RegisterDataSource::class.java)
    val mockUserComponentFactory: UserComponent.Factory = mock(UserComponent.Factory::class.java)

    var userManager: UserManager? = null

    @Before
    fun setup() {
        userManager = UserManager(mockLoginDataSource, mockRegisterDataSource, mockUserComponentFactory)
        var result: Result<LoggedInUser>
        val fakeUser = LoggedInUser(UUID.randomUUID().toString(), 1223, "Jane")
        result = Result.Success(fakeUser)
//        mockRegisterDataSource.stub {
//            onBlocking {
//                register(any())
//            }.doReturn(result)
//        }
    }

    @kotlinx.coroutines.ExperimentalCoroutinesApi
    @Test
    fun testBeforeLogin() = runTest {
        assertNotNull(userManager)
        assertFalse(userManager!!.isUserLoggedIn())

        var result1: Result<LoggedInUser>
        val fakeUser1 = LoggedInUser(UUID.randomUUID().toString(), 1223, "ABC")
        result1 = Result.Success(fakeUser1)
        `when`(mockRegisterDataSource.register(any())).thenReturn(result1)

        launch {
            result1 = userManager!!.registerUser("testuser", "123456", "a@m.com")
        }
        advanceUntilIdle()

        println("Hello ---------------------------------")
        println((result1 as Result.Success<LoggedInUser>).data.displayName)
        println("Hello ---------------------------------")
        assertFalse(userManager!!.isUserLoggedIn())
//        assertNotNull(userManager!!.user)
    }

}
