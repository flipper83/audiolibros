package com.example.audiolibros;

import android.content.Intent;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.victoralbertos.device_animation_test_rule.DeviceAnimationTestRule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.example.audiolibros.testutils.ChildAtPositionMatcher.childAtPosition;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public IntentsTestRule<MainActivity> mActivityTestRule = new IntentsTestRule<>(MainActivity.class,
            false, false);

    @ClassRule static public DeviceAnimationTestRule
            deviceAnimationTestRule = new DeviceAnimationTestRule();

    @Before
    public void setUp() {

    }

    @Test
    public void shouldShowAnyBooksWhenOpenTheMainActivity() {
        startActivity();

        assertBookName("Kappa");
    }

    @Test
    public void shouldShowNewBooksWhenClickInNewTab() {
        startActivity();

        clickOnTab("Nuevos");
        assertBookName("Avecilla");
    }

    @Test
    public void shouldShowReadedBooksWhenClickInNewTab() {
        startActivity();

        clickOnTab("Leidos");
        assertBookName("Viejo Pancho, El");
    }

    @Test
    public void shouldOpenDetailScreenWhenClickOnARow() {
        startActivity();

        onView(childAtPosition(withId(R.id.recycler_view), 0)).perform(click());

        onView(withId(R.id.titulo)).check(matches(withText("Kappa")));
    }

    @Test
    public void shouldShowSearchedBookWhenSearchABook() {
        startActivity();

        writeOnSearch("Kappa");

        assertBookName("Kappa");
    }

    @Test
    public void shouldOpenPreferencesWhenClickOnPreferencesButton() {
        startActivity();

        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.menu_preferencias), withContentDescription("Preferencias"), isDisplayed()));
        actionMenuItemView.perform(click());

        intended(hasComponent(PreferenciasActivity.class.getCanonicalName()));
    }

    private void writeOnSearch(String textToSearch) {
        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.menu_buscar), withContentDescription("Buscar"), isDisplayed()));
        actionMenuItemView.perform(click());
        ViewInteraction searchAutoComplete = onView(
                allOf(withId(R.id.search_src_text),
                        withParent(allOf(withId(R.id.search_plate),
                                withParent(withId(R.id.search_edit_frame)))),
                        isDisplayed()));
        searchAutoComplete.perform(replaceText(textToSearch), closeSoftKeyboard());
    }

    private void startActivity() {
        mActivityTestRule.launchActivity(new Intent());
        mActivityTestRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Aplicacion application = (Aplicacion) mActivityTestRule.getActivity().getApplication();
                application.getAdaptador().reset();
            }
        });
    }

    private void assertBookName(String bookName) {
        ViewInteraction textView = onView(
                allOf(withId(R.id.titulo), withText(bookName),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.recycler_view),
                                        0),
                                1),
                        isDisplayed()));
        textView.check(matches(withText(bookName)));
    }

    private void clickOnTab(String tabName) {
        ViewInteraction appCompatTextView = onView(
                allOf(withText(tabName), isDisplayed()));
        appCompatTextView.perform(click());
    }
}
