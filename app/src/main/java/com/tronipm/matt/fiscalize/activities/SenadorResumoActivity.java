package com.tronipm.matt.fiscalize.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.tronipm.matt.fiscalize.R;
import com.tronipm.matt.fiscalize.entities.EntidadeSenador;

public class SenadorResumoActivity extends AppCompatActivity {
    public static final String ARG_PARAM1 = "senador";
    public static final String ARG_PARAM2 = "param2";

    private EntidadeSenador senador;
    private String titulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senador_resumo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Resumo");

        senador = (EntidadeSenador) getIntent().getSerializableExtra(ARG_PARAM1);
        titulo = getIntent().getStringExtra(ARG_PARAM2);

        System.out.println(">>>>>>> " + titulo);
        System.out.println(">>>>>>> " + senador);
    }
}
