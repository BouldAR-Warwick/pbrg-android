package com.example.pbrg_android.register

/**
 * Data validation state of the register form.
 */
data class RegisterFormState(
    val usernameError: Int? = null,
    val passwordError: Int? = null,
    val passwordMisMatch: Int? = null,
    val emailError: Int? = null,
    val isDataValid: Boolean = false
)