package com.schibsted.spain.barista.sample;

import android.net.Uri;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed;
import static com.schibsted.spain.barista.intents.BaristaIntents.BARISTA_TEST_CONTACT_EMAIL;
import static com.schibsted.spain.barista.intents.BaristaIntents.BARISTA_TEST_CONTACT_NAME;
import static com.schibsted.spain.barista.intents.BaristaIntents.BARISTA_TEST_CONTACT_PHONE;
import static com.schibsted.spain.barista.intents.BaristaIntents.clearContactData;
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
    Uri contactUri = mockContactIntent();

    clickOn(R.id.pick_contact);

    assertDisplayed(BARISTA_TEST_CONTACT_NAME);
    assertDisplayed(BARISTA_TEST_CONTACT_PHONE);
    assertDisplayed(BARISTA_TEST_CONTACT_EMAIL);

    clearContactData(contactUri);
  }

}