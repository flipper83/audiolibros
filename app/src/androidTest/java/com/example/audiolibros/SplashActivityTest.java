package com.example.audiolibros;

import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.example.audiolibros.testutils.ElapsedTimeIdlingResource;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SplashActivityTest {
    public static final int HACK_WATTING_TIME = 1800;

    @Rule
    public IntentsTestRule<SplashActivity> mActivityTestRule = new IntentsTestRule<>(SplashActivity.class, false,
            false);

    @Test
    public void shouldOpenMainActivityAfterSplashTimeFinish() {
        mActivityTestRule.launchActivity(new Intent());
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(HACK_WATTING_TIME);
        Espresso.registerIdlingResources(idlingResource);

        intended(hasComponent(MainActivity.class.getCanonicalName()));
        Espresso.unregisterIdlingResources(idlingResource);
    }

}
