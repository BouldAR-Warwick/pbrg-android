package com.example.pbrg_android.login

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Assert.*
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
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
import org.hamcrest.Matchers.allOf
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
        onView(withId(R.id.username)).perform(typeText("test1"))

        // Login button still disabled
        onView(withId(R.id.login)).check(matches(isNotEnabled()))

        // Password too short
        onView(withId(R.id.password)).perform(typeText("12"))
        delay(600L)
        onView(withId(R.id.password)).check(matches(hasErrorText("Password must be >5 characters")))
        onView(withId(R.id.password)).perform(typeText("123456"), closeSoftKeyboard())

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
        delay(600L)
        intended(allOf(
            hasComponent(hasShortClassName(".main.MainActivity")),
            toPackage("com.example.pbrg_android")))

        // Make sure the activity is closed
        activityScenario.close()
    }

}