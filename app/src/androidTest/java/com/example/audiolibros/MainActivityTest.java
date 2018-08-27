package com.example.audiolibros;

import android.app.Activity;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import org.junit.Rule;
import org.junit.Test;

public class MainActivityTest extends ScreenshotTest {

  @Rule public IntentsTestRule<MainActivity> activityRule =
      new IntentsTestRule<>(MainActivity.class, true, false);

  @Test public void shouldShowAListTheBooksWhenOpenTheMainActivity() {
    Activity activity = startActivity();

    compareScreenshot(activity);
  }

  private Activity startActivity() {
    return activityRule.launchActivity(null);
  }
}