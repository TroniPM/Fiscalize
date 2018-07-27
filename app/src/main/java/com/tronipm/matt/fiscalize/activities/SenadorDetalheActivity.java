package com.tronipm.matt.fiscalize.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.tronipm.matt.fiscalize.R;
import com.tronipm.matt.fiscalize.adapters.RecyclerViewAdapter;
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

    public static void setDados() {
        senador = null;
        link = null;
        titulo = null;
        senadorDetalhe = null;
    }

    public static void setSenador(EntidadeSenador senador) {
        SenadorDetalheActivity.senador = senador;
    }

    public static void setLink(String link) {
        SenadorDetalheActivity.link = link;
    }

    public static void setTitulo(String titulo) {
        SenadorDetalheActivity.titulo = titulo;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.senador_menu, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        SenadorDetalheActivity.setDados();
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
            String t = senadorDetalhe.titulo == null ? titulo : senadorDetalhe.titulo;
            String d = senadorDetalhe.date == null ? "NULL" : senadorDetalhe.date;
            String txt = "Dados sobre \"" + t + "\"" + " atualizado em:\r\n" + d + ".";
            builder.setTitle("Informações")
                    .setMessage(txt)
                    .setPositiveButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
            // Create the AlertDialog object and return it
            builder.create().show();
            return true;
        } else if (id == R.id.action_abrir_web) {
            if (senadorDetalhe.link != null && !senadorDetalhe.link.isEmpty()) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(senadorDetalhe.link));
                startActivity(browserIntent);
            } else {
                Toast.makeText(this, "Informação sem link anexado.", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senador_detalhe);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        crawlerSenador = new CrawlerSenador();
        db = new TinyDB(this);

//        TextView tv = (TextView) findViewById(R.id.textView);
//        tv.setText(titulo);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Detalhes");

        //APENAS PARA DEBUG
        if (senador != null) {
            boolean flag = true;
            for (int i = 0; senador.getConteudoDetalhe() != null && i < senador.getConteudoDetalhe().size(); i++) {
                if (senador.getConteudoDetalhe().get(i).link.equals(link)) {
                    senadorDetalhe = senador.getConteudoDetalhe().get(i);
                    System.out.println(senadorDetalhe);
                    populate();
                    flag = false;
                    break;
                }
            }

            if (flag) {
                new RetrieveListTask(senador, link, titulo).execute();
            }
        } else {
            Toast.makeText(this, "Ocorreu um erro. Resete o banco de dados.", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private void populate() {
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        RecyclerViewAdapter mAdapter = new RecyclerViewAdapter(senadorDetalhe);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void startDialog() {
        if (dialog == null) {
            dialog = new ProgressDialog(SenadorDetalheActivity.this);
            dialog.setMessage("Carregando...");
            dialog.setIndeterminate(true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(true);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    Toast.makeText(SenadorDetalheActivity.this, "Cancelado", Toast.LENGTH_SHORT).show();
                    SenadorDetalheActivity.this.finish();
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
        private String link;
        private String titulo;

        protected RetrieveListTask(EntidadeSenador senador, String link, String titulo) {
            this.senador = senador;
            this.link = link;
            this.titulo = titulo;

            SenadorDetalheActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    SenadorDetalheActivity.this.startDialog();
                }
            });
        }

        protected EntidadeSenador doInBackground(String... url) {
            try {
                return crawlerSenador.conn_getSenadorMes(senador, link);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(final EntidadeSenador senadorDownloaded) {
            //https://stackoverflow.com/questions/2250770/how-to-refresh-android-listview
            SenadorDetalheActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    db.putSenador(senadorDownloaded);
                    SenadorDetalheActivity.setSenador(senadorDownloaded);

                    EntidadeSenadorDetalhe resumo = null;
                    if (SenadorDetalheActivity.senador.getConteudoDetalhe() != null) {
                        for (int i = 0; i < SenadorDetalheActivity.senador.getConteudoDetalhe().size(); i++) {
                            if (SenadorDetalheActivity.senador.getConteudoDetalhe().get(i).link.equals(link)) {
                                resumo = SenadorDetalheActivity.senador.getConteudoDetalhe().get(i);
                                break;
                            }
                        }
                    } else {
                        Toast.makeText(SenadorDetalheActivity.this, "Não existem lançamentos.", Toast.LENGTH_SHORT).show();
                        SenadorDetalheActivity.this.finish();
                    }
                    SenadorDetalheActivity.senadorDetalhe = resumo;

                    System.out.println(resumo);
                    populate();
                    //preencher campos
                    SenadorDetalheActivity.this.stopDialog();
                }
            });
        }
    }
}
