package com.tronipm.matt.fiscalize.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.tronipm.matt.fiscalize.R;
import com.tronipm.matt.fiscalize.crawlers.CrawlerSenador;
import com.tronipm.matt.fiscalize.crawlers.entities.EntidadeSenadorDetalhe;
import com.tronipm.matt.fiscalize.database.TinyDB;
import com.tronipm.matt.fiscalize.entities.EntidadeSenador;

public class SenadorDetalheActivity extends AppCompatActivity {
    private CrawlerSenador crawlerSenador = null;
    private TinyDB db = null;

    private static EntidadeSenador senador = null;
    private static EntidadeSenadorDetalhe senadorDetalhe = null;
    private static String link = null;
    private static String titulo = null;
    private ProgressDialog dialog = null;

    public static void setSenador(EntidadeSenador senador) {
        SenadorDetalheActivity.senador = senador;
    }

    public static void setLink(String link) {
        SenadorDetalheActivity.link = link;
    }

    public static void setTitulo(String titulo) {
        SenadorDetalheActivity.titulo = titulo;
    }

    public static void setDados() {
        SenadorDetalheActivity.titulo = null;
        SenadorDetalheActivity.link = null;
        SenadorDetalheActivity.senador = null;
    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.senador_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_atualizar) {
            new RetrieveListTask(senador, link, titulo).execute();
            return true;
        } else if (id == R.id.action_infos) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Dados (" + senadorResumo.titulo + ") obtidos em:").setMessage(senadorResumo.date)
                    .setPositiveButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
            // Create the AlertDialog object and return it
            builder.create().show();
            return true;
        } else if (id == R.id.action_abrir_web) {
            if (link != null && !link.isEmpty()) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                startActivity(browserIntent);
            } else {
                Toast.makeText(this, "Informação sem link anexado.", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senador_detalhe);
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

        System.out.println(">> " + titulo);
        System.out.println(">> " + link);
    }

}
