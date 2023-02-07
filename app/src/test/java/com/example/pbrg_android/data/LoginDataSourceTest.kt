package com.example.pbrg_android.data

import com.example.pbrg_android.data.model.LoginData
import org.junit.Assert.*
import org.junit.Before
import org.junit.After
import org.junit.Test
import androidx.test.platform.app.InstrumentationRegistry
import com.example.pbrg_android.data.model.LoggedInUser
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import kotlinx.coroutines.test.runTest
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import com.example.pbrg_android.utility.Result
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle

@RunWith(RobolectricTestRunner::class)
class LoginDataSourceTest {
    @Before
    fun setUp() {

    }

    @After
    fun tearDown() {
    }

    @Test // test login request
    fun testLogin() = runTest {
        val mockWebServer : MockWebServer = MockWebServer()
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        MMKV.initialize(appContext)
        val loginDataSource : LoginDataSource = LoginDataSource(appContext)

        val loginData = LoginData("testuser", "123456", true)

        val mockLoginResponse : MockResponse = MockResponse()
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .addHeader("Set-Cookie", "JSESSIONID=asdasdasdas;")
            .setBody("{\\\"sessionId\\\": \\\"fakeSessionId\\\", " +
                    "\\\"uid\\\": \\\"fakeUid\\\", " +
                    "\\\"displayName\\\": \\\"fakeDisplayName\\\"}")
        mockWebServer.enqueue(mockLoginResponse)
        mockWebServer.start()

        var result: Result<LoggedInUser>
        val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), 1223, "Jane")
        result = Result.Success(fakeUser)
        var url : String = mockWebServer.url("").toString()
        url = url.substring(0, url.length-1)
        launch {
            result = loginDataSource.login(url, loginData)
        }
        advanceUntilIdle()
        when(result) {
            is Result.Success -> {
                assertEquals((result as Result.Success<LoggedInUser>).data.sessionId, "fakeSessionId")
                assertEquals((result as Result.Success<LoggedInUser>).data.uid, "fakeUid")
                assertEquals((result as Result.Success<LoggedInUser>).data.displayName, "fakeDisplayName")
            }
            else ->
                assertEquals(result, "fakeUid")
        }

        mockWebServer.shutdown()

    }

    @Test
    fun logout() {
    }

    @Test
    fun hash() {
    }
}