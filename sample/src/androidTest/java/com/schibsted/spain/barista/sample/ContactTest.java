package com.schibsted.spain.barista.sample;

import android.Manifest;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.schibsted.spain.barista.interaction.PermissionGranter;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed;
import static com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn;

@RunWith(AndroidJUnit4.class)
public class ContactTest {

  @Rule
  public IntentsTestRule<ContactActivity> activityRule = new IntentsTestRule<>(ContactActivity.class);

  @Test
  public void pickContactAndShowInfo() {
    PermissionGranter.allowPermissionsIfNeeded(Manifest.permission.READ_CONTACTS);

    clickOn(R.id.pick_contact);

    assertDisplayed("Alorma");
    assertDisplayed("+34661121243");
  }

}