package com.example.pbrg_android.setting

import androidx.lifecycle.ViewModel
import com.example.pbrg_android.user.UserManager
import javax.inject.Inject

class SettingViewModel @Inject constructor(private val userManager: UserManager): ViewModel() {
    fun logout() {
        userManager.logout()
    }
}