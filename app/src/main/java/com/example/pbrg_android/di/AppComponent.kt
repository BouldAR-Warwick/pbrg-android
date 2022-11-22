package com.example.pbrg_android.di

import android.content.Context
import com.example.pbrg_android.login.LoginComponent
import com.example.pbrg_android.main.MainActivity
import com.example.pbrg_android.main.MainViewModel
import com.example.pbrg_android.search.SearchActivity
import com.example.pbrg_android.search.SearchViewModel
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

    fun injectMain(activity: MainActivity)
    fun mainViewModel(): MainViewModel

    fun injectSearch(activity: SearchActivity)
    fun searchViewModel(): SearchViewModel

    // Types that can be retrieved from the graph
    fun loginComponent(): LoginComponent.Factory
    fun userManager(): UserManager
}
