package com.tronipm.matt.fiscalize.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.tronipm.matt.fiscalize.R;
import com.tronipm.matt.fiscalize.adapters.ViewPagerAdapter;
import com.tronipm.matt.fiscalize.crawlers.CrawlerSenador;
import com.tronipm.matt.fiscalize.crawlers.entities.EntidadeSenadorResumo;
import com.tronipm.matt.fiscalize.database.TinyDB;
import com.tronipm.matt.fiscalize.entities.EntidadeSenador;
import com.tronipm.matt.fiscalize.fragments.SenadorFragmentResumoGrafico;
import com.tronipm.matt.fiscalize.fragments.SenadorFragmentResumoGastos;

public class SenadorResumoActivity extends AppCompatActivity {
    private CrawlerSenador crawlerSenador = null;
    private TinyDB db = null;

    private static EntidadeSenador senador = null;
    private static EntidadeSenadorResumo senadorResumo = null;
    private static String link = null;
    private static String titulo = null;
    private static String tituloBalancete = null;
    private ProgressDialog dialog = null;

    private ViewPagerAdapter adapter = null;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private SenadorFragmentResumoGrafico fragOne = null;
    private SenadorFragmentResumoGastos fragTwo = null;

    public static void setDados() {
        SenadorResumoActivity.senador = null;
        SenadorResumoActivity.link = null;
        SenadorResumoActivity.titulo = null;
        SenadorResumoActivity.senadorResumo = null;
    }

    public static void setSenador(EntidadeSenador senador) {
        SenadorResumoActivity.senador = senador;
    }

    public static void setLink(String link) {
        SenadorResumoActivity.link = link;
    }

    public static void setTitulo(String titulo) {
        SenadorResumoActivity.titulo = titulo;
    }

    public static void setTituloBalancete(String tituloBalancete) {
        SenadorResumoActivity.tituloBalancete = tituloBalancete;
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
            new RetrieveListTask(senador, link, titulo, tituloBalancete).execute();
            return true;
        } else if (id == R.id.action_infos) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder
                    .setTitle("Informações")
                    .setMessage("Dados sobre \"" + senadorResumo.titulo + "\" atualizado em:\r\n" + senadorResumo.date + ".")
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
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        fragOne = SenadorFragmentResumoGrafico.newInstance(senador, senadorResumo);
        fragTwo = SenadorFragmentResumoGastos.newInstance(senador, senadorResumo);

        adapter.addFragment(fragOne, getResources().getString(R.string.grafico).toUpperCase());
        adapter.addFragment(fragTwo, getResources().getString(R.string.valores).toUpperCase());
        viewPager.setAdapter(adapter);
    }

    private void setupViewPager(TabLayout tabLayout) {
        tabLayout.getTabAt(0).setIcon(new IconicsDrawable(this).color(Color.WHITE).icon(FontAwesome.Icon.faw_chart_bar));
        //.color(Color.RED) .sizeDp(24)
        tabLayout.getTabAt(1).setIcon(new IconicsDrawable(this).color(Color.WHITE).icon(FontAwesome.Icon.faw_dollar_sign));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senador_resumo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        crawlerSenador = new CrawlerSenador();
        db = new TinyDB(this);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Resumo");


        //APENAS PARA DEBUG
        if (senador != null) {
            boolean flag = true;
            for (int i = 0; senador.getConteudoResumo() != null && i < senador.getConteudoResumo().size(); i++) {
                if (senador.getConteudoResumo().get(i).link.equals(link)) {
                    senadorResumo = senador.getConteudoResumo().get(i);

                    startDialog();
                    init();
                    stopDialog();

                    flag = false;
                    break;
                }
            }

            if (flag) {
                new RetrieveListTask(senador, link, titulo, tituloBalancete).execute();
            }
        } else {
            Toast.makeText(this, "Ocorreu um erro. Resete o banco de dados.", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private void init() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupViewPager(tabLayout);
    }

    private void startDialog() {
        if (dialog == null) {
            dialog = new ProgressDialog(SenadorResumoActivity.this);
            dialog.setMessage("Carregando...");
            dialog.setIndeterminate(true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(true);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    Toast.makeText(SenadorResumoActivity.this, "Cancelado", Toast.LENGTH_SHORT).show();
                    SenadorResumoActivity.this.finish();
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
        private String tituloBalancete;

        protected RetrieveListTask(EntidadeSenador senador, String link, String titulo, String tituloBalancete) {
            this.senador = senador;
            this.link = link;
            this.titulo = titulo;
            this.tituloBalancete = tituloBalancete;

            SenadorResumoActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    SenadorResumoActivity.this.startDialog();
                }
            });
        }

        protected EntidadeSenador doInBackground(String... url) {
            try {
                return crawlerSenador.conn_getSenadorAno(senador, link, tituloBalancete);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(final EntidadeSenador senadorDownloaded) {
            //https://stackoverflow.com/questions/2250770/how-to-refresh-android-listview
            SenadorResumoActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    db.putSenador(senadorDownloaded);
                    SenadorResumoActivity.setSenador(senadorDownloaded);

                    EntidadeSenadorResumo resumo = null;
                    for (int i = 0; i < SenadorResumoActivity.senador.getConteudoResumo().size(); i++) {
                        if (senador.getConteudoResumo().get(i).link.equals(link)) {
                            resumo = senador.getConteudoResumo().get(i);
                            break;
                        }
                    }
                    SenadorResumoActivity.senadorResumo = resumo;
                    init();
                    //preencher campos
                    SenadorResumoActivity.this.stopDialog();
                }
            });
        }
    }
}
