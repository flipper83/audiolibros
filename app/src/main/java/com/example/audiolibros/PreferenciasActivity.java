package com.example.audiolibros;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class PreferenciasActivity extends AppCompatActivity {
  public static void open(AppCompatActivity activity) {
    Intent i = new Intent(activity, PreferenciasActivity.class);
    activity.startActivity(i);
  }

  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction().replace(android.R.id.
                content, new PreferenciasFragment()).commit();
    }
}
