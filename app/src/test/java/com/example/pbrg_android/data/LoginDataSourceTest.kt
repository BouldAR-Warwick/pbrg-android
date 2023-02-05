package com.example.pbrg_android.data

import android.content.Context
import com.example.pbrg_android.data.model.LoginData
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class LoginDataSourceTest {
    @Mock
    val mockContext : Context? = mock(Context::class.java)

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun testLogin() {
//        val loginData = LoginData("testuser", "123456", true)

    }

    @Test
    fun logout() {
    }

    @Test
    fun hash() {
    }
}