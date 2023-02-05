package com.example.pbrg_android.user

import com.example.pbrg_android.data.LoginDataSource
import com.example.pbrg_android.data.RegisterDataSource
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.kotlin.notNull
import org.mockito.kotlin.whenever

class UserManagerTest {
    @Mock
    val mockLoginDataSouce : LoginDataSource = mock(LoginDataSource::class.java)
    val mockRegisterDataSource : RegisterDataSource = mock(RegisterDataSource::class.java)
    val mockUserComponentFactory: UserComponent.Factory = mock(UserComponent.Factory::class.java)

    var userManager: UserManager? = null

    @Before
    fun setup() {
        userManager = UserManager(mockLoginDataSouce, mockRegisterDataSource, mockUserComponentFactory)
    }

    @Test
    fun testTest() {
        assertNotNull(userManager)
        whenever(mockLoginDataSouce.toString()).thenReturn("big step")
        assertEquals(mockLoginDataSouce.toString(), "big step")
    }

}
