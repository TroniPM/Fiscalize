package com.tronipm.matt.fiscalize.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tronipm.matt.fiscalize.R;
import com.tronipm.matt.fiscalize.adapters.SenadorListCustomAdapter;
import com.tronipm.matt.fiscalize.crawlers.CrawlerSenador;
import com.tronipm.matt.fiscalize.database.TinyDB;
import com.tronipm.matt.fiscalize.entities.EntidadeSenador;

import java.util.ArrayList;

public class SenadorListActivity extends AppCompatActivity {

    private TinyDB db = null;
    private CrawlerSenador crawler = null;
    private ArrayList<EntidadeSenador> list = null;
    private SenadorListCustomAdapter adapter = null;
    private ProgressDialog dialog = null;
    private int tries = 1;
    private static final int limit = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senador_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new TinyDB(this);
        crawler = new CrawlerSenador();

        startDialog();

        list = db.getListSenador();
        if (list != null) {
            for (EntidadeSenador in : list) {
                System.out.println(in);
            }
        }
        refresh();
    }

    private void startDialog() {
        if (dialog == null) {
            dialog = new ProgressDialog(SenadorListActivity.this);
            dialog.setMessage("Carregando...");
            dialog.setIndeterminate(true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(true);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    Toast.makeText(SenadorListActivity.this, "Cancelado", Toast.LENGTH_SHORT).show();
                    SenadorListActivity.this.finish();
                }
            });
        }
        dialog.show();
    }

    private void stopDialog() {
        dialog.dismiss();
    }

    private void refresh() {
        if (list != null && !list.isEmpty()) {

            ListView listView = (ListView) findViewById(R.id.list);
            adapter = new SenadorListCustomAdapter(list, this);

            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    EntidadeSenador dataModel = list.get(position);

                    Snackbar.make(view, dataModel.getNomeCivil() + "\n" + dataModel.getPartido() + "\n" + dataModel.getId(), Snackbar.LENGTH_LONG)
                            .setAction("No action", null).show();
                }
            });

            stopDialog();
        } else {
            if ((tries++) <= limit) {
                new RetrieveListTask().execute();
            } else {
                Toast.makeText(this, "Não foi possível obter a lista de senadores. Ative sua conexão com a internet.", Toast.LENGTH_LONG).show();
            }
        }
    }

    //https://stackoverflow.com/questions/6343166/how-do-i-fix-android-os-networkonmainthreadexception
    class RetrieveListTask extends AsyncTask<String, Void, ArrayList<EntidadeSenador>> {

        private Exception exception;

        protected ArrayList<EntidadeSenador> doInBackground(String... urls) {
            try {
                return crawler.conn_getListSenadores();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(final ArrayList<EntidadeSenador> thislist) {
            //https://stackoverflow.com/questions/2250770/how-to-refresh-android-listview
            SenadorListActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    db.putListSenador(thislist);
                    SenadorListActivity.this.list = thislist;
                    SenadorListActivity.this.refresh();
                }
            });
        }
    }
}
