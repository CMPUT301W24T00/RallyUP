package com.example.rallyup;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.is;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.GrantPermissionRule;

import com.example.rallyup.uiReference.attendees.AttendeeHomepageActivity;
import com.example.rallyup.uiReference.splashScreen;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
/**
 * This class contains all attendee related intent tests
 */
public class attendeeHomepageTest {
    @Rule
    public ActivityScenarioRule<AttendeeHomepageActivity> scenario = new ActivityScenarioRule<>(AttendeeHomepageActivity.class);
    @Rule
    public GrantPermissionRule permissionCamera = GrantPermissionRule.grant("android.permission.CAMERA");

    /**
     * This method checks if the my event button goes to the correct activity
     */
    @Test
    public void testGoToMyEvents() {
        onView(withId(R.id.attendee_my_events_button)).perform(click());
        onView(withId(R.id.attendeeMyEvents)).check(matches(isDisplayed()));
    }
    /**
     * This method checks if the browse event button goes to the correct activity
     */
    @Test
    public void testGoToBrowseEvents() {
        onView(withId(R.id.attendee_browse_events_button)).perform(click());
        onView(withId(R.id.attendeeBrowseEvents)).check(matches(isDisplayed()));
    }

    /**
     * This method checks if the my event back button goes to the correct activity
     */
    @Test
    public void testReturnFromMyEvents() {
        onView(withId(R.id.attendee_my_events_button)).perform(click());
        onView(withId(R.id.browse_events_back_button)).perform(click());
        onView(withId(R.id.attendeeHomepage)).check(matches(isDisplayed()));
    }

    /**
     * This method checks if the browse event back button goes to the correct activity
     */
    @Test
    public void testReturnFromBrowseEvents() {
        onView(withId(R.id.attendee_browse_events_button)).perform(click());
        onView(withId(R.id.browse_events_back_button)).perform(click());
        onView(withId(R.id.attendeeHomepage)).check(matches(isDisplayed()));
    }

    /**
     * This method checks if the qr button goes to the correct activity
     */
    @Test
    public void testQRScanner() {
        onView(withId(R.id.QRScannerButton)).perform(click());
        onView(withId(R.id.scannerActivity)).check(matches(isDisplayed()));
    }

    /**
     * This method checks if the my event qr button goes to the correct activity
     */
    @Test
    public void testMyEventsQRScanner() {
        onView(withId(R.id.attendee_my_events_button)).perform(click());
        onView(withId(R.id.QRScannerButton)).perform(click());
        onView(withId(R.id.scannerActivity)).check(matches(isDisplayed()));
    }

    /**
     * This method checks if the edit profile button goes to the correct activity
     */
    @Test
    public void testGoToEditProfile() {
        onView(withId(R.id.edit_profile_button)).perform(click());
        onView(withId(R.id.attendeeUpdateInfo)).check(matches(isDisplayed()));
    }

    /**
     * This method checks if the edit profile back button goes to the correct activity
     */
    @Test
    public void testReturnFromEditProfile() {
        onView(withId(R.id.edit_profile_button)).perform(click());
        onView(withId(R.id.attendee_update_back_button)).perform(click());
        onView(withId(R.id.attendeeHomepage)).check(matches(isDisplayed()));
    }
}