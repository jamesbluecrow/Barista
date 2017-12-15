package com.schibsted.spain.barista.interaction

import android.support.annotation.IdRes
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.matcher.ViewMatchers
import com.schibsted.spain.barista.internal.util.resourceMatcher

object BaristaSwipeInteractions {

  @JvmStatic
  fun swipeLeftOn(@IdRes resId: Int) {
    ViewActionExecutor.performAction(resId.resourceMatcher(), ViewActions.swipeLeft())
  }

  @JvmStatic
  fun swipeLeftOn(text: String) {
    ViewActionExecutor.performAction(ViewMatchers.withText(text), ViewActions.swipeLeft())
  }

  @JvmStatic
  fun swipeRightOn(@IdRes resId: Int) {
    ViewActionExecutor.performAction(resId.resourceMatcher(), ViewActions.swipeRight())
  }

  @JvmStatic
  fun swipeRightOn(text: String) {
    ViewActionExecutor.performAction(ViewMatchers.withText(text), ViewActions.swipeRight())
  }
}
