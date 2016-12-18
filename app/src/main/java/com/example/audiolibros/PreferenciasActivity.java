package com.example.audiolibros;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Jesús Tomás on 30/01/2016.
 */
public class PreferenciasActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction().replace(android.R.id.
                content, new PreferenciasFragment()).commit();
    }
}
