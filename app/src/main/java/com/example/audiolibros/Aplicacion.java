package com.example.audiolibros;

import android.app.Application;

import java.util.Vector;

/**
 * Created by Jesús Tomás on 24/01/2016.
 */
public class Aplicacion extends Application {

    private Vector<Libro> vectorLibros;
    private AdaptadorLibrosFiltro adaptador;

    @Override
    public void onCreate() {
        super.onCreate();
        vectorLibros = Libro.ejemploLibros();
        adaptador = new AdaptadorLibrosFiltro(this, vectorLibros);
        adaptador.reset();
    }

    public AdaptadorLibrosFiltro getAdaptador() {
        return adaptador;
    }

    public Vector<Libro> getVectorLibros() {
        return vectorLibros;
    }
}
