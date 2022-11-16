package com.example.pbrg_android.user

import javax.inject.Inject

/**
 * UserDataRepository contains user-specific data such as username and unread notifications.
 *
 * This object will have a unique instance in a Component that is annotated with
 * @LoggedUserScope (i.e. only UserComponent in this case).
 */
@LoggedUserScope
class UserDataRepository @Inject constructor(private val userManager: UserManager) {

    val username: String
        get() = userManager.user!!.displayName

    var selectedGym: String = ""

    init {
//        selectedGym = datasource.getPrimaryGym()
    }

    fun changeSelectedGym() {

    }
}
