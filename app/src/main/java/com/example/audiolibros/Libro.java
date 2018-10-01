package com.example.audiolibros;

import org.jetbrains.annotations.TestOnly;

import java.util.Vector;

/**
 * Created by Jesús Tomás on 24/01/2016.
 */
public class Libro {
  public String titulo;
  public String autor;
  public int recursoImagen;
  public String urlAudio;
  public String genero;   // Género literario
  public Boolean novedad; // Es una novedad
  public Boolean leido;   // Leído por el usuario
  public static final String CATEGORY_ALL = "";
  public final static String CATEGORY_ALL_LITERAL = "Todos los géneros";
  public final static String CATEGORY_EPIC = "Poema épico";
  public final static String CATEGORY_XIX = "Literatura siglo XIX";
  public final static String G_THRILLER = "Suspense";
  public final static String[] G_ARRAY = new String[]{
    CATEGORY_ALL_LITERAL, CATEGORY_EPIC, CATEGORY_XIX, G_THRILLER
  };

  public Libro(String titulo, String autor, int recursoImagen, String urlAudio, String genero,
               Boolean novedad, Boolean leido) {
    this.titulo = titulo;
    this.autor = autor;
    this.recursoImagen = recursoImagen;
    this.urlAudio = urlAudio;
    this.genero = genero;
    this.novedad = novedad;
    this.leido = leido;
  }

  private static Vector<Libro> initialBooks = null;
  private final static String SERVIDOR = "http://www.dcomg.upv.es/~jtomas/android/audiolibros/";


  public static Vector<Libro> ejemploLibros() {
    if (initialBooks == null) {
      initialBooks = new Vector<>();
      resetToInitialValues();
    }

    return initialBooks;
  }

  @TestOnly
  public static void setEjemploLibros(Vector<Libro> initialBooks) {
    Libro.initialBooks = initialBooks;
  }

  @TestOnly
  public static void resetToInitialValues() {
    initialBooks.clear();
    initialBooks.add(
      new Libro("Kappa", "Akutagawa", R.drawable.kappa, SERVIDOR + "kappa.mp3", Libro.CATEGORY_XIX,
        false, false));
    initialBooks.add(new Libro("Avecilla", "Alas Clarín, Leopoldo", R.drawable.avecilla,
      SERVIDOR + "avecilla.mp3", Libro.CATEGORY_XIX, true, false));
    initialBooks.add(new Libro("Divina Comedia", "Dante", R.drawable.divinacomedia,
      SERVIDOR + "divina_comedia.mp3", Libro.CATEGORY_EPIC, true, false));
    initialBooks.add(
      new Libro("Viejo Pancho, El", "Alonso y Trelles, José", R.drawable.viejo_pancho,
        SERVIDOR + "viejo_pancho.mp3", Libro.CATEGORY_XIX, true, true));
    initialBooks.add(new Libro("Canción de Rolando", "Anónimo", R.drawable.cancion_rolando,
      SERVIDOR + "cancion_rolando.mp3", Libro.CATEGORY_EPIC, false, true));
    initialBooks.add(
      new Libro("Matrimonio de sabuesos", "Agata Christie", R.drawable.matrimonio_sabuesos,
        SERVIDOR + "matrim_sabuesos.mp3", Libro.G_THRILLER, false, true));
    initialBooks.add(new Libro("La iliada", "Homero", R.drawable.iliada, SERVIDOR + "la_iliada.mp3",
      Libro.CATEGORY_EPIC, true, false));
  }
}
