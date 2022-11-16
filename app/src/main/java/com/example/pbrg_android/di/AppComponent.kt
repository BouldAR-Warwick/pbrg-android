package com.example.pbrg_android.di

import android.content.Context
import com.example.pbrg_android.login.LoginComponent
import com.example.pbrg_android.user.UserManager
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

// Definition of a Dagger component
@Singleton
@Component(modules = [AppSubcomponents::class])
interface AppComponent {
    // Factory to create instances of the AppComponent
    @Component.Factory
    interface Factory {
        // With @BindsInstance, the Context passed in will be available in the graph
        fun create(@BindsInstance context: Context): AppComponent
    }


    // Types that can be retrieved from the graph
    fun loginComponent(): LoginComponent.Factory
    fun userManager(): UserManager

}
