package com.example.pbrg_android.register

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers.*
import androidx.test.espresso.intent.rule.IntentsRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.pbrg_android.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers
import org.hamcrest.core.AllOf.allOf
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
class RegisterActivityTest {
    @get:Rule
    var mIntentsRule: IntentsRule = IntentsRule()

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun testRegisterPage() = runBlocking {
        // Start up Tasks screen.
        val activityScenario = ActivityScenario.launch(RegisterActivity::class.java)

        // Register button disabled on default
        onView(withId(R.id.register)).check(matches(isNotEnabled()))

        // Username too short
        onView(withId(R.id.registerUsername)).perform(typeText("t"))
        delay(600L) // add delay by hand
        onView(withId(R.id.registerUsername)).check(matches(hasErrorText("Not a valid username")))
        onView(withId(R.id.registerUsername)).perform(typeText("testuser"))
        // Register button still disabled
        onView(withId(R.id.register)).check(matches(isNotEnabled()))

        // Invalid Email
        onView(withId(R.id.registerEmail)).perform(typeText("a"))
        delay(600L)
        onView(withId(R.id.registerEmail)).check(matches(hasErrorText("Invalid email address")))
        onView(withId(R.id.registerEmail)).perform(typeText("a@mail.com"))
        // Register button still disabled
        onView(withId(R.id.register)).check(matches(isNotEnabled()))

        // Password too short
        onView(withId(R.id.registerPassword)).perform(typeText("12"))
        delay(600L)
        onView(withId(R.id.registerPassword)).check(matches(hasErrorText("Password must be >5 characters")))
        onView(withId(R.id.registerPassword)).perform(typeText("123456"))
        // Register button still disabled
        onView(withId(R.id.register)).check(matches(isNotEnabled()))

        // Inconsistent passwords
        onView(withId(R.id.confirmPassword)).perform(typeText("12"))
        delay(600L)
        onView(withId(R.id.confirmPassword)).check(matches(hasErrorText("Password entered is not same")))
        onView(withId(R.id.confirmPassword)).perform(typeText("123456"), closeSoftKeyboard())
        delay(600L)

        // Register button enabled
        onView(withId(R.id.register)).check(matches(isEnabled()))

        // Verify intent
        val resultData = Intent()
        resultData.putExtra("displayName", "Jane")
        val result = Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
        intending(toPackage("com.example.pbrg_android")).respondWith(result)

        onView(withId(R.id.register)).perform(click())

        intended(allOf(
                hasAction("android.intent.action.MAIN"),
                toPackage("com.example.pbrg_android")))

        // Make sure the activity is closed before resetting the db:
        activityScenario.close()
    }



    @Test
    fun getRegisterViewModel() {
    }

    @Test
    fun setRegisterViewModel() {
    }

    @Test
    fun onCreate() {
    }
}