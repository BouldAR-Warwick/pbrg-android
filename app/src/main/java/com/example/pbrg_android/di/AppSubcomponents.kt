package com.example.pbrg_android.di

import com.example.pbrg_android.login.LoginComponent
import com.example.pbrg_android.register.RegisterComponent
import com.example.pbrg_android.route.RouteComponent
import com.example.pbrg_android.routeGen.RouteGenComponent
import com.example.pbrg_android.routeVis.RouteVisARComponent
import com.example.pbrg_android.user.UserComponent
import com.example.pbrg_android.wall.WallComponent
import dagger.Module

// This module tells a Component which are its subcomponents
@Module(subcomponents = [
    RegisterComponent::class,
    WallComponent::class,
    LoginComponent::class,
    RouteComponent::class,
    RouteGenComponent::class,
    RouteVisARComponent::class,
    UserComponent::class])
class AppSubcomponents
