package com.example.pbrg_android.search

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.view.KeyEvent
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.ComponentNameMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers.*
import androidx.test.espresso.intent.rule.IntentsRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.pbrg_android.R
import com.example.pbrg_android.login.LoginActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.AllOf.allOf
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
class SearchActivityTest {
    @get:Rule
    var mIntentsRule: IntentsRule = IntentsRule()

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
        onView(withId(R.id.searchView)).perform(ViewActions.pressKey(KeyEvent.KEYCODE_ENTER))
        delay(600L)
        onView(withId(R.id.search_result_listview)).check(matches(hasChildCount(0)))

        // Test valid search clause
        onView(withId(R.id.searchView)).perform(ViewActions.pressKey(KeyEvent.KEYCODE_DEL))
        onView(withId(R.id.searchView)).perform(ViewActions.pressKey(KeyEvent.KEYCODE_DEL))
        onView(withId(R.id.searchView)).perform(typeText("cv"))
        onView(withId(R.id.searchView)).perform(ViewActions.pressKey(KeyEvent.KEYCODE_ENTER))
        delay(600L)
        onView(withId(R.id.search_result_listview)).check(matches(hasChildCount(3)))

        // Select a gym
        Espresso.onData(Matchers.`is`("Warwick Sport Centre")).inAdapterView(withId(R.id.search_result_listview)).perform(click())
        delay(300L)
        // Test return to Main Activity
        intended(
            Matchers.allOf(
                hasComponent(ComponentNameMatchers.hasShortClassName(".main.MainActivity")),
                toPackage("com.example.pbrg_android")
            )
        )

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
        intended(
            Matchers.allOf(
                hasComponent(ComponentNameMatchers.hasShortClassName(".main.MainActivity")),
                toPackage("com.example.pbrg_android")
            )
        )

        // WallRoutes Activity
        onView(withId(R.id.selectedGymName)).check(matches(withText("Warwick Sport Centre")))
        onView(withId(R.id.generateNewRoute)).check(matches(isEnabled()))
        onView(withId(R.id.routeList)).check(matches(isDisplayed()))
        onView(withId(R.id.routeList)).check(matches(isClickable()))

        // Route List
        Espresso.onData(Matchers.anything()).inAdapterView(withId(R.id.routeList))
            .atPosition(0)
            .onChildView(withId(R.id.routeItemName))
            .check(matches(withText("Route #1")))
        Espresso.onData(Matchers.anything()).inAdapterView(withId(R.id.routeList))
            .atPosition(0)
            .onChildView(withId(R.id.routeItemDifficulty))
            .check(matches(withText("V5")))

        activityScenario.close()
    }
}