package com.example.pbrg_android.route

import com.example.pbrg_android.di.ActivityScope
import com.example.pbrg_android.register.RegisterActivity
import dagger.Subcomponent

// Scope annotation that the LoginComponent uses
// Classes annotated with @ActivityScope will have a unique instance in this Component
@ActivityScope
// Definition of a Dagger subcomponent
@Subcomponent
interface RouteComponent {

    // Factory to create instances of LoginComponent
    @Subcomponent.Factory
    interface Factory {
        fun create(): RouteComponent
    }

    // Classes that can be injected by this Component
    fun inject(activity: RouteActivity)
}