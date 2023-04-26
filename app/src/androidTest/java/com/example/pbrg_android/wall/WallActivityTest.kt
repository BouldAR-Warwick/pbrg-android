package com.example.pbrg_android.wall

import android.graphics.drawable.BitmapDrawable
import android.view.KeyEvent
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName
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
import org.hamcrest.Matchers.*
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
class WallActivityTest {
    @get:Rule
    var mIntentsRule: IntentsRule = IntentsRule()

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
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
    fun testWallRouteRouteGen() = runBlocking {
        // Login and navigate to wall page
        // Start up Tasks screen.
        val activityScenario = ActivityScenario.launch(LoginActivity::class.java)

        // Go to search activity
        onView(withId(R.id.username)).perform(typeText("testuser"))
        onView(withId(R.id.password)).perform(typeText("123456"), closeSoftKeyboard())
        onView(withId(R.id.login)).perform(click())
        delay(2000L)

        onView(withId(R.id.show_search_dialog)).perform(click())
        delay(600L)

        onView(withId(R.id.searchView)).perform(typeText("cv"))
        onView(withId(R.id.searchView)).perform(pressKey(KeyEvent.KEYCODE_ENTER))
        delay(600L)

        // Search dialog displayed
        onView(withId(R.id.searchView)).check(matches(isDisplayed()))
        // Select a gym
        onData(`is`("Warwick Sport Centre")).inAdapterView(withId(R.id.search_result_listview)).perform(click())
        delay(300L)
        // Test return to Main Activity
        intended(allOf(
            hasComponent(hasShortClassName(".main.MainActivity")),
            toPackage("com.example.pbrg_android")))
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

        // Navigate to RouteGenActivity
        onView(withId(R.id.generateNewRoute)).perform(click())
        // Verify intent
        delay(500L)
        intended(allOf(
            hasComponent(hasShortClassName(".routeGen.RouteGenActivity")),
            toPackage("com.example.pbrg_android")))

        // RouteGenActivity
        onView(withId(R.id.selectedGymName)).check(matches(withText("Warwick Sport Centre")))
        onView(withId(R.id.generate)).check(matches((isNotEnabled())))
        onView(withId(R.id.view_in_ar)).check(matches((isNotEnabled())))

        // Choose a difficulty
        onView(withId(R.id.select_difficulty)).check(matches(withSpinnerText(containsString("Select Difficulty"))))
        delay(500L)
        onView(withId(R.id.select_difficulty)).perform(click())
        delay(500L)
        onData(allOf(`is`(instanceOf(String::class.java)), `is`("10"))).perform(click())
        delay(500L)
        onView(withId(R.id.select_difficulty)).check(matches(withSpinnerText(containsString("10"))))
        delay(500L)

        // Verify "generate" is clickable and click
        onView(withId(R.id.generate)).check(matches((isEnabled())))
        delay(500L)
        onView(withId(R.id.generate)).perform(click())
        delay(3000L)

        // Verify route image change and "view_in_ar" button clickable
        onView(withId(R.id.view_in_ar)).check(matches((isEnabled())))
        onView(withId(R.id.route_image)).check(matches(drawableIsChanged(R.drawable.uni_board)))

        // Return to WallRoute activity
        onView(withContentDescription("Navigate up")).perform(click())
        delay(500L)

        // Verify the added route
        onView(withId(R.id.routeList)).check(matches(hasChildCount(4)))
        onData(anything()).inAdapterView(withId(R.id.routeList))
            .atPosition(3)
            .onChildView(withId(R.id.routeItemDifficulty))
            .check(matches(withText("V10")))
        delay(500L)

        // Navigate to RouteActivity
        onData(anything()).inAdapterView(withId(R.id.routeList))
            .atPosition(3)
            .onChildView(withId(R.id.routeItemDifficulty))
            .perform(click())
        intended(allOf(
            hasComponent(hasShortClassName(".route.RouteActivity")),
            toPackage("com.example.pbrg_android")))

        // Verify buttons are clickable
        onView(withId(R.id.difficulty)).check(matches(withText("V10")))
        onView(withId(R.id.viewAR)).check(matches(isEnabled()))
        onView(withId(R.id.deleteRoute)).check(matches(isEnabled()))

        onView(withId(R.id.deleteRoute)).perform(click())
        delay(1000L)

        // Verify the added route
        onView(withId(R.id.routeList)).check(matches(hasChildCount(3)))

        activityScenario.close()

    }

}