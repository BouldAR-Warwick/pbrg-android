package com.example.pbrg_android.wall

import com.example.pbrg_android.activities.BootActivity
import com.example.pbrg_android.di.ActivityScope
import com.example.pbrg_android.login.LoginActivity
import dagger.Subcomponent

// Scope annotation that the LoginComponent uses
// Classes annotated with @ActivityScope will have a unique instance in this Component

// Definition of a Dagger subcomponent
@Subcomponent
interface WallComponent {

    // Factory to create instances of LoginComponent
    @Subcomponent.Factory
    interface Factory {
        fun create(): WallComponent
    }

    // Classes that can be injected by this Component
    fun inject(activity: WallActivity)
}