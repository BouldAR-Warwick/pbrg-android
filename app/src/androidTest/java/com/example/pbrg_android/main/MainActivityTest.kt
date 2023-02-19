package com.example.pbrg_android.main

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Assert.*
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName
import androidx.test.espresso.intent.matcher.IntentMatchers.*
import androidx.test.espresso.intent.rule.IntentsRule
import com.example.pbrg_android.R
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import com.example.pbrg_android.login.EXTRA_MESSAGE
import com.example.pbrg_android.login.LoginActivity
import com.example.pbrg_android.setting.SettingActivity
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
class MainActivityTest {
    private val intent: Intent = Intent(InstrumentationRegistry.getInstrumentation().targetContext, MainActivity::class.java).apply {
        putExtra(EXTRA_MESSAGE, "Jane")
    }
    @get:Rule
    var mActivityScenarioRule : ActivityScenarioRule<MainActivity> = ActivityScenarioRule<MainActivity>(intent)

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun testNavigateToSearch() = runBlocking {
        val intent: Intent = Intent(InstrumentationRegistry.getInstrumentation().targetContext, MainActivity::class.java).apply {
            putExtra(EXTRA_MESSAGE, "Jane")
        }
        val activityScenario: ActivityScenario<MainActivity> =
            ActivityScenario.launch<MainActivity>(intent)
        delay(1000L)
        onView(withId(R.id.show_search_dialog)).perform(click())
        delay(600L)

        intended(
            allOf(
                hasComponent(hasShortClassName(".search.SearchActivity")),
                toPackage("com.example.pbrg_android")
            )
        )

        activityScenario.close()
    }
    @Test
    fun testNavigateToSetting() = runBlocking {
        // Start up Tasks screen.
        val activityScenario: ActivityScenario<MainActivity> = ActivityScenario.launch(MainActivity::class.java)


        delay(600L)
        onView(withId(R.id.setting)).perform(ViewActions.click())
        delay(600L)

        intended(allOf(
            hasComponent(hasShortClassName(".setting.SettingActivity")),
            toPackage("com.example.pbrg_android")))

        // Make sure the activity is closed
        activityScenario.close()
    }

    @Test
    fun testMainPage() = runBlocking {
        // Start up Tasks screen.
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        // Make sure the activity is closed
        activityScenario.close()
    }

    @Test
    fun setMainViewModel() {
    }

    @Test
    fun onCreate() {
    }

    @Test
    fun onSearchRequested() {
    }
}