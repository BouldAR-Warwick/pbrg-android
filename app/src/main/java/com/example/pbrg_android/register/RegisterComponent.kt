package com.example.pbrg_android.register

import com.example.pbrg_android.di.ActivityScope
import dagger.Subcomponent

// Scope annotation that the RegisterComponent uses
// Classes annotated with @ActivityScope will have a unique instance in this Component
@ActivityScope
// Definition of a Dagger subcomponent
@Subcomponent
interface RegisterComponent {

    // Factory to create instances of RegisterComponent
    @Subcomponent.Factory
    interface Factory {
        fun create(): RegisterComponent
    }

    // Classes that can be injected by this Component
    fun inject(activity: RegisterActivity)
}