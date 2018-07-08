package com.tronipm.matt.fiscalize.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.tronipm.matt.fiscalize.R;
import com.tronipm.matt.fiscalize.adapters.SenadorListCustomAdapter;
import com.tronipm.matt.fiscalize.crawlers.CrawlerSenador;
import com.tronipm.matt.fiscalize.crawlers.entities.EntidadeSenadorBalancete;
import com.tronipm.matt.fiscalize.database.TinyDB;
import com.tronipm.matt.fiscalize.entities.EntidadeSenador;
import com.tronipm.matt.fiscalize.utils.ComparatorAscSenadores;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by PMateus on 07/07/2018.
 * For project Fiscalize.
 * Contact: <paulomatew@gmail.com>
 */
public class SenadorListActivity extends AppCompatActivity {
    private ProgressDialog dialog = null;
    private TinyDB db = null;
    private CrawlerSenador crawler = null;
    private ArrayList<EntidadeSenador> list = null;

    private int tries = 1;
    private static final int limit = 3;

    private TextDrawable.IBuilder mDrawableBuilder = null;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.senador_list_menu, menu);
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
            startDialog();
            new RetrieveListTask().execute();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();

        refresh();
        SenadorBalanceteActivity.setDados();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senador_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        startDialog();

        db = new TinyDB(this);
        crawler = new CrawlerSenador();


        list = db.getListSenador();
//        if (list != null) {
//            for (EntidadeSenador in : list) {
//                System.out.println(in);
//            }
//        }

        // initialize the builder based on the "TYPE"
//        int type = intent.getIntExtra(MainActivity.TYPE, DrawableProvider.SAMPLE_RECT);
//        switch (type) {
//            case DrawableProvider.SAMPLE_RECT:
//                mDrawableBuilder = TextDrawable.builder()
//                        .rect();
//                break;
//            case DrawableProvider.SAMPLE_ROUND_RECT:
//                mDrawableBuilder = TextDrawable.builder()
//                        .roundRect(10);
//                break;
//            case DrawableProvider.SAMPLE_ROUND:
//                mDrawableBuilder = TextDrawable.builder()
//                        .round();
//                break;
//            case DrawableProvider.SAMPLE_RECT_BORDER:
//                mDrawableBuilder = TextDrawable.builder()
//                        .beginConfig()
//                        .withBorder(4)
//                        .endConfig()
//                        .rect();
//                break;
//            case DrawableProvider.SAMPLE_ROUND_RECT_BORDER:
//                mDrawableBuilder = TextDrawable.builder()
//                        .beginConfig()
//                        .withBorder(4)
//                        .endConfig()
//                        .roundRect(10);
//                break;
//            case DrawableProvider.SAMPLE_ROUND_BORDER:
//                mDrawableBuilder = TextDrawable.builder()
//                        .beginConfig()
//                        .withBorder(4)
//                        .endConfig()
//                        .round();
//                break;
//        }
        mDrawableBuilder = TextDrawable.builder()
                .beginConfig()
                .withBorder(4)
                .endConfig()
                .roundRect(10);
        // init the list view and its adapter
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
            listView.setAdapter(new SenadorListCustomAdapter(mDrawableBuilder, new TinyDB(this).getListSenador(), this));

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
                    ArrayList<EntidadeSenador> senadores = db.getListSenador();
                    if (senadores != null) {
                        for (int i = 0; i < senadores.size(); i++) {
                            boolean flag = true;
                            for (int j = 0; j < thislist.size(); j++) {
                                if (thislist.get(i).getId().equals(senadores.get(j).getId())) {
                                    flag = false;
                                    break;
                                }
                            }
                            //Caso senador não exista, adiciono ele
                            if (flag) {
                                senadores.add(thislist.get(i));
                            }
                        }
                    } else {
                        senadores = thislist;
                    }
                    //Ordenando
                    Collections.sort(senadores, new ComparatorAscSenadores());

                    db.putListSenador(senadores);
                    SenadorListActivity.this.list = senadores;
                    SenadorListActivity.this.refresh();
                }
            });
        }
    }
}