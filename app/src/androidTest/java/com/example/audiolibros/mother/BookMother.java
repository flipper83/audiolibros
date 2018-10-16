package com.example.audiolibros.mother;

import com.example.audiolibros.Libro;
import com.example.audiolibros.R;

public class BookMother {
  public final static Libro ANY_BOOK = new Libro(
    "Kappa",
    "Akutagawa",
    R.drawable.kappa,
    "any server",
    Libro.CATEGORY_XIX,
    false,
    false);

  public final static Libro ANY_OTHER_BOOK = new Libro(
    "Avecilla",
    "Alas Clarín, Leopoldo",
    R.drawable.avecilla,
    "any server",
    Libro.CATEGORY_XIX,
    true,
    false);

  public final static Libro ANY_READ_BOOK = new Libro(
    "Read",
    "Akutagawa",
    R.drawable.kappa,
    "any server",
    Libro.CATEGORY_XIX,
    false,
    true);

  public final static Libro ANY_NEW_BOOK = new Libro(
    "New",
    "Akutagawa",
    R.drawable.kappa,
    "any server",
    Libro.CATEGORY_XIX,
    true,
    false);

  public final static Libro ANY_THRILLER_BOOK = new Libro(
    "Thriller",
    "Akutagawa",
    R.drawable.kappa,
    "any server",
    Libro.G_THRILLER,
    false,
    false);
}
