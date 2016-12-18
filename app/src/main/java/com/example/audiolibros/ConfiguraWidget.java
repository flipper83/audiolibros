package com.example.audiolibros;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Gonzalo on 19/01/2016.
 */
public class ConfiguraWidget extends Activity {
    int widgetId;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configura_widget);
        // si el usuario decide volver atrás el widget no se instalará
        setResult(RESULT_CANCELED);
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            finish();
        }
        widgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }
    }

    // Captura el evento onClick => al pulsar el botón se instala el widget
    public void instalarWidget(View view) {
        // Cuando un widget es añadido al escritorio se produce una primera
        // llamada a onUpdate(), pero si lo hacemos a través de una actividad
        // esta llamada no se produce. Para realizar la primera actualización
        // hacemos una llamada a actualizaWidget().
        MiAppWidgetProvider.actualizaWidget(this, widgetId);
        Intent resultValue = new Intent();
        // Una actividad de configuración de widget siempre ha de devolver el
        // extra EXTRA_APPWIDGET_ID con el id del widget.
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        // Finalmente, cambiamos el resultado de la actividad a RESULT_OK y la
        // cerramos.
        setResult(RESULT_OK, resultValue);
        finish();
    }
}
