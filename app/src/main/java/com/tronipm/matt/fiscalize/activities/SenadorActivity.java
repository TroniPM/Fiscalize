package com.tronipm.matt.fiscalize.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.tronipm.matt.fiscalize.R;
import com.tronipm.matt.fiscalize.crawlers.CrawlerSenador;
import com.tronipm.matt.fiscalize.database.TinyDB;
import com.tronipm.matt.fiscalize.entities.EntidadeSenador;

import java.util.ArrayList;

public class SenadorActivity extends AppCompatActivity {
    public static final String PARAM1 = "senador";
    private CrawlerSenador crawler = null;
    private TinyDB db = null;
    private EntidadeSenador senador = null;

    private ProgressDialog dialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senador);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new TinyDB(this);
        crawler = new CrawlerSenador();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Perfil");

        senador = (EntidadeSenador) getIntent().getSerializableExtra(PARAM1);

        startDialog();
        new RetrieveListTask(senador).execute();
    }

    private void startDialog() {
        if (dialog == null) {
            dialog = new ProgressDialog(SenadorActivity.this);
            dialog.setMessage("Carregando...");
            dialog.setIndeterminate(true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(true);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    Toast.makeText(SenadorActivity.this, "Cancelado", Toast.LENGTH_SHORT).show();
                    SenadorActivity.this.finish();
                }
            });
        }
        dialog.show();
    }

    private void stopDialog() {
        dialog.dismiss();
    }

    class RetrieveListTask extends AsyncTask<String, Void, EntidadeSenador> {

        private EntidadeSenador senador;

        protected RetrieveListTask(EntidadeSenador senador) {
            this.senador = senador;
        }

        protected EntidadeSenador doInBackground(String... url) {
            try {
                return crawler.conn_getSenador(senador);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(final EntidadeSenador thislist) {
            //https://stackoverflow.com/questions/2250770/how-to-refresh-android-listview
            SenadorActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    SenadorActivity.this.senador = thislist;
                    db.putSenador(thislist);

                    SenadorActivity.this.stopDialog();

                    System.out.println(senador);
                }
            });
        }
    }
}
