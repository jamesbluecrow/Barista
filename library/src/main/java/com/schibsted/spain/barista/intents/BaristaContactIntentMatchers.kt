package com.schibsted.spain.barista.intents

import android.content.Intent
import android.net.Uri
import android.support.test.espresso.core.internal.deps.guava.base.Preconditions
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.TypeSafeMatcher

internal object BaristaContactIntentMatchers {

    @JvmStatic
    fun captureContact(): Matcher<Intent> {
        return hasAction(`is`(Intent.ACTION_PICK))
    }

    private fun hasAction(actionMatcher: Matcher<String>): Matcher<Intent> {
        Preconditions.checkNotNull(actionMatcher)

        return object : TypeSafeMatcher<Intent>() {
            override fun describeTo(description: Description) {
                description.appendText("has action: ")
                description.appendDescriptionOf(actionMatcher)
            }

            public override fun matchesSafely(intent: Intent): Boolean = actionMatcher.matches(intent.action)

        }
    }
}

data class MockIntent(val matcher: Matcher<Intent>, val uri: Uri)