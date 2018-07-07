package com.tronipm.matt.fiscalize.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.tronipm.matt.fiscalize.R;
import com.tronipm.matt.fiscalize.adapters.SenadorListCustomAdapter;
import com.tronipm.matt.fiscalize.crawlers.CrawlerSenador;
import com.tronipm.matt.fiscalize.database.TinyDB;
import com.tronipm.matt.fiscalize.entities.EntidadeSenador;

import java.util.ArrayList;

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

    @Override
    protected void onResume() {
        super.onResume();

        refresh();
    }

    private TextDrawable.IBuilder mDrawableBuilder = null;

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
                    db.putListSenador(thislist);
                    SenadorListActivity.this.list = thislist;
                    SenadorListActivity.this.refresh();
                }
            });
        }
    }
}