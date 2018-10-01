package com.example.audiolibros;

import android.app.Activity;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import org.junit.Rule;
import org.junit.Test;

import java.util.Vector;

import static com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn;
import static com.schibsted.spain.barista.interaction.BaristaEditTextInteractions.writeTo;

public class MainActivityTest extends ScreenshotTest {

  @Rule public IntentsTestRule<MainActivity> activityRule =
    new IntentsTestRule<>(MainActivity.class, true, false);
  private final static Libro ANY_LIBRO = new Libro(
    "Kappa",
    "Akutagawa",
    R.drawable.kappa,
    "any server",
    Libro.G_S_XIX,
    false,
    false);

  private final static Libro ANY_OTHER_LIBRO = new Libro(
    "Avecilla",
    "Alas Clarín, Leopoldo",
    R.drawable.avecilla,
    "any server",
    Libro.G_S_XIX,
    true,
    false);

  @Test
  public void shouldShowAListTheBooksWhenOpenTheMainActivity() {
    Activity activity = startActivity();

    compareScreenshot(activity);
  }

  @Test
  public void shouldShowAnEmptyListWhenWeDontHaveBooks() {

    setEjemploLibros(new Vector<Libro>());

    Activity activity = startActivity();

    compareScreenshot(activity);
  }

  @Test
  public void shouldShowFilterBooksWhenWeWriteAFilteredBook() {
    Vector<Libro> anyLibros = new Vector<>();
    anyLibros.add(ANY_LIBRO);
    anyLibros.add(ANY_OTHER_LIBRO);

    setEjemploLibros(anyLibros);

    Activity activity = startActivity();

    clickOn(R.id.menu_buscar);
    writeTo(R.id.search_src_text, "Kappa");

    compareScreenshot(activity);
  }

  private Activity startActivity() {
    return activityRule.launchActivity(null);
  }
}