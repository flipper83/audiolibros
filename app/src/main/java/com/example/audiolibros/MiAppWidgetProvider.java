package com.example.audiolibros;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * Created by Gonzalo on 19/01/2016.
 */
public class MiAppWidgetProvider extends AppWidgetProvider {

    // variable para controlar el click en la imagen del reproductor
    public static String ACTION_WIDGET_RECEIVER = "ActionReceiverWidget";
    // declaramos un audioplayer
    // otro modo de hacerlo sería mediante un servicio
    private static MediaPlayer mp;
    // declaramos un libro para cambios desde app con widget trabajando
    private static Libro lastBook = null;

    // debemos sobreescribir el metodo onUpdate
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        for (int widgetId : appWidgetIds) {
            actualizaWidget(context, widgetId);
        }
    }

    // Actualiza el contenido del widget
    public static void actualizaWidget(Context context, int widgetId) {
        Libro libro = getLastBookPlayed(context);

        // accedemos a las distintas representaciones de nuestro widget
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.widget);

        // sólo cambiamos si tenemos un último libro reproducido
        if (libro.recursoImagen >= 0) {
            // obtenemos la información del libro
            String titulo = libro.titulo;
            String autor = libro.autor;
            // cambiamos los valores
            remoteViews.setTextViewText(R.id.txtAutor, autor);
            remoteViews.setTextViewText(R.id.txtTitulo, titulo);
            // cuando cambia el libro requiere 2 pulsaciones
            // la primera cambiará el título y la segunda comenzará a reproducir
            if (lastBook != null) {
                if (!lastBook.titulo.equals(libro.titulo)) {
                    // actualizamos la referencia
                    lastBook = libro;
                    // cambiamos el media player
                    if (mp != null) {
                        mp.stop();
                        mp = null;
                    }
                }
            } else
                lastBook = libro;// actualizamos la referencia
        }

        // lanzamos la actividad principal al pulsar sobre la imagen del libro
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                intent, 0);
        remoteViews.setOnClickPendingIntent(R.id.imgOpenApp, pendingIntent);

        // Actuamos ante el pulsado del botón del reproductor
        // Declaramos a nuestra propia clase como gestora de dicho evento
        Intent player = new Intent(context, MiAppWidgetProvider.class);
        // añadimos la accion al intent antes de declarar el pendingIntent
        player.setAction(ACTION_WIDGET_RECEIVER);
        // en este caso declaramos un getBroadcast => debemos sobreescribir
        // onReceive
        PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context,
                0, player, 0);
        // asignamos el click
        remoteViews.setOnClickPendingIntent(R.id.imgPlayPause,
                actionPendingIntent);

        // cambio de icono según el estado de reproducción
        //usamos iconos del sistema
        if (mp != null)
            if (mp.isPlaying())
                remoteViews.setImageViewResource(R.id.imgPlayPause,
                        android.R.drawable.ic_media_pause);
            else
                remoteViews.setImageViewResource(R.id.imgPlayPause,
                        android.R.drawable.ic_media_play);

        // actualizamos la vistas remotas
        AppWidgetManager.getInstance(context).updateAppWidget(widgetId,
                remoteViews);

    }

    // Accedemos dentro de preferencias al último libro visitado
    private static Libro getLastBookPlayed(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(
                "com.example.audiolibros_internal", Context.MODE_PRIVATE);
        // leemos los datos del fichero de preferencias
        // en caso de no haber ningún libro, devolvemos el primero de ellos (es nuestra opción)
        int id = prefs.getInt("ultimo", 0);
        //obtenemos el libro
        Libro libro = ((Aplicacion) context.getApplicationContext()).getVectorLibros().elementAt(id);
        return libro;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // vemos el último libro pulsado
        Libro libro = getLastBookPlayed(context);

        // sólo actuamos si teníamos un libro pulsado
        if (libro.recursoImagen != -1) {
            // debemos declarar el audioplayer
            if (mp == null) {
                // obtenemos el libro
                Uri audio = Uri.parse(libro.urlAudio);
                mp = MediaPlayer.create(context.getApplicationContext(), audio);
            }

            // Obtenemos la acción que nos mandan realizar
            final String action = intent.getAction();
            // si es la acción de control del mediaplayer actuamos
            if (ACTION_WIDGET_RECEIVER.equals(action)) {
                if (mp.isPlaying())
                    mp.pause();
                else
                    mp.start();
            }
        } else
            Toast.makeText(context, "No hay ningún libro previo en memoria",
                    Toast.LENGTH_LONG).show();

        // Actualización
        AppWidgetManager appWidgetManager = AppWidgetManager
                .getInstance(context);
        ComponentName thisAppWidget = new ComponentName(
                context.getPackageName(), MiAppWidgetProvider.class.getName());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);
        // forzamos la actualización
        onUpdate(context, appWidgetManager, appWidgetIds);

        super.onReceive(context, intent);
    }
}

