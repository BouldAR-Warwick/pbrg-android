package com.example.pbrg_android.di

import com.example.pbrg_android.login.LoginComponent
import com.example.pbrg_android.user.UserComponent
import com.example.pbrg_android.wall.WallComponent
import dagger.Module

// This module tells a Component which are its subcomponents
@Module(subcomponents = [
    WallComponent::class,
    LoginComponent::class,
    UserComponent::class])
class AppSubcomponents
