package com.example.audiolibros;

import android.app.Activity;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import com.example.audiolibros.mother.BookMother;

import org.junit.Rule;
import org.junit.Test;

import java.util.Vector;

import static com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn;
import static com.schibsted.spain.barista.interaction.BaristaDrawerInteractions.openDrawer;
import static com.schibsted.spain.barista.interaction.BaristaEditTextInteractions.writeTo;
import static com.schibsted.spain.barista.interaction.BaristaMenuClickInteractions.clickMenu;

public class MainActivityTest extends ScreenshotTest {

  @Rule public IntentsTestRule<MainActivity> activityRule =
    new IntentsTestRule<>(MainActivity.class, true, false);

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
    givenBooks(BookMother.ANY_LIBRO, BookMother.ANY_OTHER_LIBRO);
    Activity activity = startActivity();

    clickOn(R.id.menu_buscar);
    writeTo(R.id.search_src_text, "Kappa");

    compareScreenshot(activity);
  }

  @Test
  public void shouldShowNewBooksWhenWePressInNewTab() {
    givenBooks(BookMother.ANY_NEW_BOOK, BookMother.ANY_LIBRO);
    Activity activity = startActivity();

    clickOn(R.string.tab_title_new);

    compareScreenshot(activity);
  }

  @Test
  public void shouldShowNewBooksWhenWePressInReadTab() {
    givenBooks(BookMother.ANY_READ_BOOK, BookMother.ANY_LIBRO);
    Activity activity = startActivity();

    clickOn(R.string.tab_title_read);

    compareScreenshot(activity);
  }

  @Test
  public void shouldShowNewBooksWhenWePressInCategory() {
    givenBooks(BookMother.ANY_THRILLER_BOOK, BookMother.ANY_LIBRO);
    Activity activity = startActivity();

    openDrawer();
    clickOn(Libro.G_THRILLER);

    compareScreenshot(activity);
  }

  private Vector<Libro> givenBooks(Libro... books) {
    Vector<Libro> anyLibros = new Vector<>();
    for (Libro book : books) {
      anyLibros.add(book);
    }
    setEjemploLibros(anyLibros);
    return anyLibros;
  }


  private Activity startActivity() {
    return activityRule.launchActivity(null);
  }
}