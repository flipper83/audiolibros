package com.example.audiolibros.fragments;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;

import com.example.audiolibros.Aplicacion;
import com.example.audiolibros.Libro;
import com.example.audiolibros.MainActivity;
import com.example.audiolibros.R;
import com.example.audiolibros.ZoomSeekBar;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Jesús Tomás on 26/01/2016.
 */
public class DetalleFragment extends Fragment implements
        View.OnTouchListener, MediaPlayer.OnPreparedListener,
        MediaController.MediaPlayerControl, ZoomSeekBar.OnClickListener {
    public static String ARG_ID_LIBRO = "id_libro";
    MediaPlayer mediaPlayer;
    MediaController mediaController;
    private ZoomSeekBar zSeekBar;
    private Libro libro;
    private int puntoReproduccion = 0;

  public static DetalleFragment create(int id) {
    DetalleFragment detailFrament = new DetalleFragment();
    Bundle args = new Bundle();
    args.putInt(DetalleFragment.ARG_ID_LIBRO, id);
    detailFrament.setArguments(args);
    return detailFrament;
  }


  @Override public View onCreateView(LayoutInflater inflador, ViewGroup
            contenedor, Bundle savedInstanceState) {
        View vista = inflador.inflate(R.layout.fragment_detalle,
                contenedor, false);

        // colocamos la ZoomSeekBar
        zSeekBar = (ZoomSeekBar) vista.findViewById(R.id.zoomSeekBar);
        // le asignamos un escuchador
        zSeekBar.setOnClickListener(this);
        // de primeras no visible (hasta que el mediaplayer no esté listo)
        zSeekBar.setVisibility(View.INVISIBLE);

        Bundle args = getArguments();
        if (args != null) {
            int position = args.getInt(ARG_ID_LIBRO);
            ponInfoLibro(position, vista);
        } else {
            ponInfoLibro(0, vista);
        }
        setHasOptionsMenu(true);
        return vista;
    }

    private void ponInfoLibro(int id, View vista) {
        /*Libro*/ libro = ((Aplicacion) getActivity().getApplication())
                .getVectorLibros().elementAt(id);
        ((TextView) vista.findViewById(R.id.titulo)).setText(libro.titulo);
        ((TextView) vista.findViewById(R.id.autor)).setText(libro.autor);
        ((ImageView) vista.findViewById(R.id.portada))
                .setImageResource(libro.recursoImagen);
        vista.setOnTouchListener(this);
        if (mediaPlayer != null){
            mediaPlayer.release();
        }
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(this);
        mediaController = new MediaController(getActivity());
        Uri audio = Uri.parse(libro.urlAudio);
        try {
            mediaPlayer.setDataSource(getActivity(), audio);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            Log.e("Audiolibros", "ERROR: No se puede reproducir "+audio,e);
        }
    }

    public void ponInfoLibro(int id) {
        ponInfoLibro(id, getView());
    }

    @Override public void onPrepared(MediaPlayer mediaPlayer) {
        //Log.d("Audiolibros", "Entramos en onPrepared de MediaPlayer");
        SharedPreferences preferencias = PreferenceManager
                .getDefaultSharedPreferences(getActivity());
        if (preferencias.getBoolean("pref_autoreproducir", true)) {
            mediaPlayer.start();
        }
        mediaController.setMediaPlayer(this);
        mediaController.setAnchorView(getView());

//        mediaController.setAnchorView(getView().findViewById(
//                R.id.fragment_detalle));

        //esto sobra con la zoomSeekBar
        //mediaController.setEnabled(true);
        //mediaController.show();
        int duracionAudio = getDuration() / 1000;
        // Log.i(TAG, "Audio preparado, duracion: " + duracionAudio);

        zSeekBar.setValMin(0);
        zSeekBar.setEscalaMin(0);
        zSeekBar.setValMax(duracionAudio);
        zSeekBar.setEscalaMax(duracionAudio);
        zSeekBar.setEscalaRaya(duracionAudio / 50);
        zSeekBar.setEscalaRayaLarga(10);
        // la hacemos visible ya que esta preparado
        zSeekBar.setVisibility(View.VISIBLE);

        //nos ubicamos
        seekTo(puntoReproduccion);
        zSeekBar.setVal(puntoReproduccion / 1000);

        // controlamos la posición de la barra de acuerdo al segundo de
        // reproducción
        // usamos un ScheduleExecutorService => tareas periódicas
        // No permite actualizar elementos gráficos directamente, por ello
        // debemos
        // utilizar conjuntamente un handler de mensajes
        ScheduledExecutorService myScheduledExecutorService = Executors
                .newScheduledThreadPool(1);

        myScheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
                                                              @Override
                                                              public void run() {
                                                                  monitorHandler.sendMessage(monitorHandler.obtainMessage());
                                                              }
                                                          }, 1, // initialDelay
                1, // delay
                TimeUnit.SECONDS);

    }

    // handler para el procedimiento anterior
    Handler monitorHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mediaPlayerMonitor();
        }
    };

    // Nuestra barra actua de acuerdo al mediaplayer
    private void mediaPlayerMonitor() {
        if (mediaPlayer != null) {
            try {
                if (isPlaying())
                    if (zSeekBar.getVal() != zSeekBar.getValMax() - 1)
                        zSeekBar.setVal(getCurrentPosition() / 1000);// ms2sec
                    else
                        zSeekBar.setVal(zSeekBar.getValMax());// avanzo el
                // ultimo
                // segundo
            } catch (Exception e) {
                // ignoramos el error de illegalstateexception del último
                // isPlaying
            }

        }
    }

    @Override public boolean onTouch(View vista, MotionEvent evento) {
        mediaController.show();
        return false;
    }

    @Override public void onStop() {
        mediaController.hide();
        try {
            mediaPlayer.stop();
            mediaPlayer.release();
        } catch (Exception e) {
            Log.d("Audiolibros", "Error en mediaPlayer.stop()");
        }
        super.onStop();
    }

    @Override public boolean canPause() {
        return true;
    }

    @Override public boolean canSeekBackward() {
        return true;
    }

    @Override public boolean canSeekForward() {
        return true;
    }

    @Override public int getBufferPercentage() {
        return 0;
    }

    @Override public int getCurrentPosition() {
        try {
            return mediaPlayer.getCurrentPosition();
        } catch (Exception e) {
            return 0;
        }
    }

    @Override public int getDuration() {
        return mediaPlayer.getDuration();
    }

    @Override public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    @Override public void pause() {
        mediaPlayer.pause();
    }

    @Override public void seekTo(int pos) {
        mediaPlayer.seekTo(pos);
    }

    @Override public void start() {
        mediaPlayer.start();
    }

    @Override public int getAudioSessionId() {
        return 0;
    }

/*    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        DetalleFragment detalleFragment = (DetalleFragment)
                getFragmentManager().findFragmentById(R.id.detalle_fragment);
        if (detalleFragment == null ) {

            Toast.makeText(getActivity(), "NO MOstrar" , Toast.LENGTH_LONG).show();

            ((MainActivity) getActivity()).showDrawer(false);
        }
    }*/

/*    @Override
    public void onAttach(Context contexto) {
        super.onAttach(contexto);
        Toast.makeText(getActivity(), "oAttach" , Toast.LENGTH_LONG).show();
        DetalleFragment detalleFragment = (DetalleFragment)
                getFragmentManager().findFragmentById(R.id.detalle_fragment);
        if (detalleFragment == null ) {

            Toast.makeText(getActivity(), "NO MOstrar" , Toast.LENGTH_LONG).show();

            ((MainActivity) getActivity()).showDrawer(false);
        }
    }*/

    @Override
    public void onClick(View arg0) {
        // gestionamos el evento onclick sobre la barra
        // desplazando el reproductor a este punto
        // Actúa también ante el desplazamiento de la barra.
        // (Esto falla a veces debido al procedimiento periódico empleado
        // para la actualización de su valor (ScheduleExecutorService)
        // ya que nosotros movemos hacia un punto y el scheduler lo vuele atrás
        // sólo hay que insistir
        seekTo(zSeekBar.getVal() * 1000);// ms2sec
    }

    //debemos tener cuidado con el mediaPlayer tras cambiar de orientación
    //ya que se habrá parado y al parse se pierde de acuerdo a como está
    //definido

    @Override public void onResume(){
        //Toast.makeText(getActivity(), "onResume" , Toast.LENGTH_LONG).show();
        DetalleFragment detalleFragment = (DetalleFragment)
                getFragmentManager().findFragmentById(R.id.detalle_fragment);
        if (detalleFragment == null ) {
            //Toast.makeText(getActivity(), "NO MOstrar" , Toast.LENGTH_LONG).show();
            ((MainActivity) getActivity()).showDrawer(false);
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnPreparedListener(this);

            Uri audio = Uri.parse(libro.urlAudio);
            try {
                mediaPlayer.setDataSource(getActivity(), audio);
                mediaPlayer.prepareAsync();
            } catch (IOException e) {
                Log.e("Audiolibros", "ERROR: No se puede reproducir " + audio, e);
            }
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        //la actividad es ocultada al pasar al segundo plano
        super.onPause();
        //cogemos el punto por el que va
        puntoReproduccion = mediaPlayer.getCurrentPosition();
        //lo paramos
        pause();
    }


/*    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //aquí guardamos el puntero (lo usaremos en el onPause)
        this.actividad=activity;
    }*/

}
