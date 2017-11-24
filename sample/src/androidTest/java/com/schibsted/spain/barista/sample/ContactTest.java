package com.schibsted.spain.barista.sample;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed;
import static com.schibsted.spain.barista.intents.BaristaIntents.mockContactIntent;
import static com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn;

@RunWith(AndroidJUnit4.class)
public class ContactTest {

  @Rule
  public GrantPermissionRule permissionRule = GrantPermissionRule.grant(android.Manifest.permission.WRITE_CONTACTS);

  @Rule
  public IntentsTestRule<ContactActivity> activityRule = new IntentsTestRule<>(ContactActivity.class);

  @Test
  public void pickContactAndShowInfo() {
    mockContactIntent("Barista Test User");

    clickOn(R.id.pick_contact);

    assertDisplayed("Barista Test User");
  }

}