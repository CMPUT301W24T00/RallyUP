package com.example.rallyup;


import static androidx.test.espresso.Espresso.onData;
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

import com.example.rallyup.uiReference.attendees.AttendeeHomepageActivity;
import com.example.rallyup.uiReference.organizers.OrganizerEventListActivity;
import com.example.rallyup.uiReference.splashScreen;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * This class tests all organizer related intent tests
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class organizerEventListTest {
    /**
     * creates a new scenario to test the organizer event list activity
     */
    @Rule
    public ActivityScenarioRule<OrganizerEventListActivity> scenario = new ActivityScenarioRule<>(OrganizerEventListActivity.class);

    /**
     * This method checks if the clicking an event in the list view goes to the correct activity
     */
    @Test
    public void testGoToOrgEventDetails() {
        onData(anything()).inAdapterView(withId(R.id.org_events_list)).atPosition(0).perform(click());
        onView(withId(R.id.activityOrgEventDetails)).check(matches(isDisplayed()));
    }

    /**
     * This method checks if the back button from an event in the list view goes to the correct activity
     */
    @Test
    public void testReturnFromEventDetails() {
        onData(anything()).inAdapterView(withId(R.id.org_events_list)).atPosition(0).perform(click());
        onView(withId(R.id.organizer_details_back_button)).perform(click());
        onView(withId(R.id.activityOrganizerEventList)).check(matches(isDisplayed()));
    }

    /**
     * This method checks if the clicking the add event button goes to the correct activity
     */
    @Test
    public void testGoToAddEvent() {
        onView(withId(R.id.createEventButton)).perform(click());
        onView(withId(R.id.addEvent)).check(matches(isDisplayed()));
    }
    /**
     * This method checks if the clicking the back button from the add event activity goes to the correct activity
     */
    @Test
    public void testReturnAddEvent() {
        onView(withId(R.id.createEventButton)).perform(click());
        onView(withId(R.id.backButton)).perform(click());
        onView(withId(R.id.activityOrganizerEventList)).check(matches(isDisplayed()));
    }
    /**
     * This method checks if the clicking the view attendees within an event's details goes to the correct activity
     */
    @Test
    public void testGoToEventViewAttendees() {
        onData(anything()).inAdapterView(withId(R.id.org_events_list)).atPosition(0).perform(click());
        onView(withId(R.id.event_attendees_button)).perform(click());
        onView(withId(R.id.activityEventAttendeesInfo)).check(matches(isDisplayed()));
    }

    /**
     * This method checks if the clicking the back button within the view attendees goes to the correct activity
     */
    @Test
    public void testReturnFromEventViewAttendees() {
        onData(anything()).inAdapterView(withId(R.id.org_events_list)).atPosition(0).perform(click());
        onView(withId(R.id.event_attendees_button)).perform(click());
        onView(withId(R.id.event_attendees_back_button)).perform(click());
        onView(withId(R.id.activityOrgEventDetails)).check(matches(isDisplayed()));
    }
}

