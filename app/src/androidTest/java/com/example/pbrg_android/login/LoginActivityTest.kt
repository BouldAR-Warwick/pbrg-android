package com.example.pbrg_android.login

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.view.KeyEvent
import android.view.View
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onData
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Assert.*
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName
import androidx.test.espresso.intent.matcher.IntentMatchers.*
import androidx.test.espresso.intent.rule.IntentsRule
import com.example.pbrg_android.R
import androidx.test.espresso.matcher.ViewMatchers.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers.*
import kotlinx.coroutines.test.runTest
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class LoginActivityTest {
    @get:Rule
    var mIntentsRule: IntentsRule = IntentsRule()
//    var mActivityTestRule: IntentsTestRule<LoginActivity> = IntentsTestRule(LoginActivity::class.java)

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun testSignupPage() = runBlocking {
        // Start up Tasks screen.
        val activityScenario = ActivityScenario.launch(LoginActivity::class.java)

        onView(withId(R.id.signup)).perform(click())
        delay(600L)

        intended(allOf(
            hasComponent(hasShortClassName(".register.RegisterActivity")),
            toPackage("com.example.pbrg_android")))

        // Make sure the activity is closed
        activityScenario.close()
    }

    @Test
    fun testLoginPage() = runBlocking {
        // Start up Tasks screen.
        val activityScenario = ActivityScenario.launch(LoginActivity::class.java)

        // Login button disabled on default
        onView(withId(R.id.login)).check(matches(isNotEnabled()))

        // Username too short
        onView(withId(R.id.username)).perform(typeText("t"))
        delay(600L) // add delay by hand
        onView(withId(R.id.username)).check(matches(hasErrorText("Not a valid username")))
        onView(withId(R.id.username)).perform(typeText("estuser"))

        // Login button still disabled
        onView(withId(R.id.login)).check(matches(isNotEnabled()))

        // Password too short
        onView(withId(R.id.password)).perform(typeText("12"))
        delay(600L)
        onView(withId(R.id.password)).check(matches(hasErrorText("Password must be >5 characters")))
        onView(withId(R.id.password)).perform(typeText("3456"), closeSoftKeyboard())

        // Login button enabled
        delay(600L)
        onView(withId(R.id.login)).check(matches(isEnabled()))

        // StayLoggedIn is checked by default
        onView(withId(R.id.stayLoggedIn)).check(matches(isChecked()))

        // StayLoggedIn is unchecked if clicked once
        onView(withId(R.id.stayLoggedIn)).perform(click())
        onView(withId(R.id.stayLoggedIn)).check(matches(isNotChecked()))

        // Verify intent
        onView(withId(R.id.login)).perform(click())
        delay(300L)
        intended(allOf(
            hasComponent(hasShortClassName(".main.MainActivity")),
            toPackage("com.example.pbrg_android")))

        // Main activity
        // Check resources loaded correctly
        onView(withId(R.id.show_search_dialog)).check(matches(isDisplayed()))
        onView(withId(R.id.setting)).check(matches(isDisplayed()))
        onView(withId(R.id.wall)).check(matches(isDisplayed()))
        onView(withId(R.id.displayName)).check(matches(withText("Testuser")))
        onView(withId(R.id.selected_gym)).check(matches(isDisplayed()))

        // Navigate to search
        onView(withId(R.id.show_search_dialog)).perform(click())
        delay(300L)
        intended(allOf(
            hasComponent(hasShortClassName(".search.SearchActivity")),
            toPackage("com.example.pbrg_android")))

        // Search activity
        // Check search
        onView(withId(R.id.searchView)).perform(typeText("ccc"))
        onView(withId(R.id.searchView)).perform(pressKey(KeyEvent.KEYCODE_ENTER))

        delay(600L)
        // Make sure the activity is closed
        activityScenario.close()
    }

    // Check if the drawable has changed from the default one
    private fun drawableIsChanged(@DrawableRes drawableResId: Int): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("with drawable from resource id: ")
                description.appendValue(drawableResId)
            }

            override fun matchesSafely(target: View?): Boolean {
                if (target !is ImageView) {
                    return false
                }
                if (drawableResId < 0) {
                    return target.drawable == null
                }
                val expectedDrawable = ContextCompat.getDrawable(target.context, drawableResId)
                    ?: return false

                val bitmap = (target.drawable as BitmapDrawable).bitmap
                val otherBitmap = (expectedDrawable as BitmapDrawable).bitmap
                return !bitmap.sameAs(otherBitmap)
            }
        }
    }

    @Test
    fun testSearchPage() = runBlocking {
        // Start up Tasks screen.
        val activityScenario = ActivityScenario.launch(LoginActivity::class.java)

        // Go to search activity
        onView(withId(R.id.username)).perform(typeText("testuser"))
        onView(withId(R.id.password)).perform(typeText("123456"), closeSoftKeyboard())
        onView(withId(R.id.login)).perform(click())
        delay(2000L)
        onView(withId(R.id.wall)).check(matches(drawableIsChanged(R.drawable.uni_board)))

        onView(withId(R.id.show_search_dialog)).perform(click())
        delay(600L)

        // Search dialog displayed
        onView(withId(R.id.searchView)).check(matches(isDisplayed()))

        // Test invalid search string
        onView(withId(R.id.searchView)).perform(typeText("cc"))
        onView(withId(R.id.searchView)).perform(pressKey(KeyEvent.KEYCODE_ENTER))
        delay(600L)
        onView(withId(R.id.search_result_listview)).check(matches(hasChildCount(0)))

        // Test valid search clause
        onView(withId(R.id.searchView)).perform(pressKey(KeyEvent.KEYCODE_DEL))
        onView(withId(R.id.searchView)).perform(pressKey(KeyEvent.KEYCODE_DEL))
        onView(withId(R.id.searchView)).perform(typeText("cv"))
        onView(withId(R.id.searchView)).perform(pressKey(KeyEvent.KEYCODE_ENTER))
        delay(600L)
        onView(withId(R.id.search_result_listview)).check(matches(hasChildCount(3)))

        // Select a gym
        onData(`is`("Warwick Sport Centre")).inAdapterView(withId(R.id.search_result_listview)).perform(click())
        delay(300L)
        // Test return to Main Activity
        intended(allOf(
            hasComponent(hasShortClassName(".main.MainActivity")),
            toPackage("com.example.pbrg_android")))

        // Test selected gym name changed
        onView(withId(R.id.selected_gym)).check(matches(withText("Warwick Sport Centre")))
        delay(2000L)
        // Test gym image changed
        onView(withId(R.id.wall)).check(matches(drawableIsChanged(R.drawable.wall2)))

        // Navigate to WallRoutes Activity
        // Check wall button
        onView(withId(R.id.wall)).check(matches(isClickable()))
        onView(withId(R.id.wall)).perform(click())
        // Verify intent
        delay(500L)
        intended(allOf(
            hasComponent(hasShortClassName(".main.MainActivity")),
            toPackage("com.example.pbrg_android")))
        // WallRoutes Activity
        onView(withId(R.id.selectedGymName)).check(matches(withText("Warwick Sport Centre")))
        onView(withId(R.id.generateNewRoute)).check(matches(isEnabled()))
        onView(withId(R.id.routeList)).check(matches(isDisplayed()))
        onView(withId(R.id.routeList)).check(matches(isClickable()))

        // Route List
        onData(anything()).inAdapterView(withId(R.id.routeList))
            .atPosition(0)
            .onChildView(withId(R.id.routeItemName))
            .check(matches(withText("Route #1")))
        onData(anything()).inAdapterView(withId(R.id.routeList))
            .atPosition(0)
            .onChildView(withId(R.id.routeItemDifficulty))
            .check(matches(withText("V5")))

        activityScenario.close()
    }

    @Test
    fun testNavigateToSetting() = runBlocking {
        val activityScenario = ActivityScenario.launch(LoginActivity::class.java)

        // Log in
        onView(withId(R.id.username)).perform(typeText("testuser"))
        onView(withId(R.id.password)).perform(typeText("123456"), closeSoftKeyboard())
        onView(withId(R.id.login)).perform(click())
        delay(500L)
        intended(allOf(
            hasComponent(hasShortClassName(".main.MainActivity")),
            toPackage("com.example.pbrg_android")))

        // Check account/setting icon
        onView(withId(R.id.setting)).check(matches(isClickable()))

        // Go to Setting
        onView(withId(R.id.setting)).perform(click())
        delay(500L)
        intended(allOf(
            hasComponent(hasShortClassName(".login.LoginActivity")),
            toPackage("com.example.pbrg_android")))

        // Log out is clickable
        onView(withId(R.id.logout)).check(matches(isClickable()))

        activityScenario.close()
    }

    @Test
    fun testNavigateToRoutes() = runBlocking {
        val activityScenario = ActivityScenario.launch(LoginActivity::class.java)

        // Log in
        onView(withId(R.id.username)).perform(typeText("testuser"))
        onView(withId(R.id.password)).perform(typeText("123456"), closeSoftKeyboard())
        onView(withId(R.id.login)).perform(click())
        delay(1000L)
        intended(allOf(
            hasComponent(hasShortClassName(".main.MainActivity")),
            toPackage("com.example.pbrg_android")))

        // Check wall button
        onView(withId(R.id.wall)).check(matches(isClickable()))
        onView(withId(R.id.wall)).perform(click())
        // Verify intent
        delay(1000L)
        intended(allOf(
            hasComponent(hasShortClassName(".main.MainActivity")),
            toPackage("com.example.pbrg_android")))
        // WallRoutes Activity
        onView(withId(R.id.selectedGymName)).check(matches(withText("Test Gym1")))
        onView(withId(R.id.generateNewRoute)).check(matches(isEnabled()))

        activityScenario.close()
    }

}