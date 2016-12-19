package com.example.audiolibros;

import android.content.Context;

import java.util.Vector;

/**
 * Created by Jesús Tomás on 27/01/2016.
 */
public class AdaptadorLibrosFiltro extends AdaptadorLibros {
    private Vector<Libro> vectorSinFiltro;// Vector con todos los libros
    private Vector<Integer> indiceFiltro; // Índice en vectorSinFiltro de
    // Cada elemento de vectorLibros
    private String busqueda = "";         // Búsqueda sobre autor o título
    private String genero = "";           // Género seleccionado
    private boolean novedad = false;      // Si queremos ver solo novedades
    private boolean leido = false;        // Si queremos ver solo leidos

    public AdaptadorLibrosFiltro(Context contexto,
                                 Vector<Libro> vectorLibros) {
        super(contexto, vectorLibros);
        vectorSinFiltro = vectorLibros;
        recalculaFiltro();
    }

    public void reset() {
        busqueda = "";
        genero = "";
        novedad = false;
        leido = false;
        recalculaFiltro();
        notifyDataSetChanged();
    }

    public void setBusqueda(String busqueda) {
        this.busqueda = busqueda.toLowerCase();
        recalculaFiltro();
    }

    public void setGenero(String genero) {
        this.genero = genero;
        recalculaFiltro();
    }

    public void setNovedad(boolean novedad) {
        this.novedad = novedad;
        recalculaFiltro();
    }

    public void setLeido(boolean leido) {
        this.leido = leido;
        recalculaFiltro();
    }

    public void recalculaFiltro() {
        vectorLibros = new Vector<Libro>();
        indiceFiltro = new Vector<Integer>();
        for (int i = 0; i < vectorSinFiltro.size(); i++) {
            Libro libro = vectorSinFiltro.elementAt(i);
            if ((libro.titulo.toLowerCase().contains(busqueda) ||
                    libro.autor.toLowerCase().contains(busqueda))
                    && (libro.genero.startsWith(genero))
                    && (!novedad || (novedad && libro.novedad))
                    && (!leido || (leido && libro.leido))) {
                vectorLibros.add(libro);
                indiceFiltro.add(i);
            }
        }
    }

    public Libro getItem(int posicion) {
        return vectorSinFiltro.elementAt(indiceFiltro.elementAt(posicion));
    }

    public long getItemId(int posicion) {
        return indiceFiltro.elementAt(posicion);
    }

    public void borrar(int posicion){
        vectorSinFiltro.remove((int)getItemId(posicion));
        recalculaFiltro();
    }

    public void insertar(Libro libro){
        vectorSinFiltro.add(0,libro);
        recalculaFiltro();
    }
}
