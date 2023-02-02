package com.example.pbrg_android.di

import android.content.Context
import com.example.pbrg_android.login.LoginComponent
import com.example.pbrg_android.main.MainActivity
import com.example.pbrg_android.main.MainViewModel
import com.example.pbrg_android.register.RegisterComponent
import com.example.pbrg_android.route.RouteComponent
import com.example.pbrg_android.routeGen.RouteGenComponent
import com.example.pbrg_android.routeVis.RouteVisARComponent
import com.example.pbrg_android.search.SearchActivity
import com.example.pbrg_android.search.SearchViewModel
import com.example.pbrg_android.setting.SettingActivity
import com.example.pbrg_android.setting.SettingViewModel
import com.example.pbrg_android.user.UserManager
import com.example.pbrg_android.wall.WallComponent
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

    fun injectSearch(activity: SearchActivity)
    fun injectSetting(activity: SettingActivity)

    // Types that can be retrieved from the graph
    fun registerComponent(): RegisterComponent.Factory
    fun loginComponent(): LoginComponent.Factory
    fun wallComponent(): WallComponent.Factory
    fun routeComponent(): RouteComponent.Factory
    fun routeGenComponent(): RouteGenComponent.Factory
    fun routeVisARComponent(): RouteVisARComponent.Factory
    fun userManager(): UserManager
    fun searchViewModel(): SearchViewModel
    fun mainViewModel(): MainViewModel
    fun settingViewModel(): SettingViewModel
}
