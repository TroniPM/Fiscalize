package com.tronipm.matt.fiscalize.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
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
import com.tronipm.matt.fiscalize.crawlers.CrawlerSenador;
import com.tronipm.matt.fiscalize.crawlers.entities.EntidadeSenadorBalancete;
import com.tronipm.matt.fiscalize.database.TinyDB;
import com.tronipm.matt.fiscalize.entities.EntidadeSenador;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PMateus on 07/07/2018.
 * For project Fiscalize.
 * Contact: <paulomatew@gmail.com>
 */
public class SenadorBalanceteActivity extends AppCompatActivity {
    public static final String ARG_PARAM1 = "senador";
    public static final String ARG_PARAM2 = "ano";
    private CrawlerSenador crawler = null;
    private TinyDB db = null;
    private EntidadeSenador senador = null;
    private ProgressDialog dialog = null;
    private String ano = null;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SenadorFragmentPerfil fragOne = null;
    private SenadorFragmentGastos fragTwo = null;
    private ViewPagerAdapter adapter = null;

    @Override
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
            new RetrieveListTask(senador, ano).execute();
            return true;
        } else if (id == R.id.action_infos) {
            String date = null;
            for (EntidadeSenadorBalancete in : senador.getConteudoBalancete()) {
                if (in.ano.equals(ano)) {
                    date = in.date;
                    break;
                }
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Dados (" + ano + ") obtidos em:").setMessage(date)
                    .setPositiveButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
            // Create the AlertDialog object and return it
            builder.create().show();
            return true;
        } else if (id == R.id.action_abrir_web) {
            String link = null;
            for (EntidadeSenadorBalancete in : senador.getConteudoBalancete()) {
                if (in.ano.equals(ano)) {
                    link = in.link;
                    break;
                }
            }
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(ARG_PARAM1, senador);
        outState.putSerializable(ARG_PARAM2, ano);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senador);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new TinyDB(this);
        crawler = new CrawlerSenador();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().getSerializableExtra(ARG_PARAM1) != null) {
            senador = (EntidadeSenador) getIntent().getSerializableExtra(ARG_PARAM1);
            ano = getIntent().getStringExtra(ARG_PARAM2);
        } else {
            senador = (EntidadeSenador) getIntent().getExtras().getSerializable(ARG_PARAM1);
            ano = getIntent().getExtras().getString(ARG_PARAM2);
        }

        get();

        //APENAS PARA DEBUG
        if (senador != null) {
            if (senador.getConteudoBalancete() == null) {
                new RetrieveListTask(senador, null).execute();
            } else {
            }

//            System.out.println(senador);
        } else {
            Toast.makeText(this, "Ocorreu um erro. Resete o banco de dados.", Toast.LENGTH_SHORT).show();
            finish();
        }

        setTitle("Perfil");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence[] items = new CharSequence[senador.getAnosDisponiveis().size()];
                for (int i = 0; i < senador.getAnosDisponiveis().size(); i++) {
                    items[i] = senador.getAnosDisponiveis().get(i);
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(SenadorBalanceteActivity.this);
                builder.setTitle("Anos Disponíveis");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        //Apenas pega os dados do banco caso já tenha.
                        String ano = senador.getAnosDisponiveis().get(item);
                        boolean flag = true;
                        for (EntidadeSenadorBalancete in : senador.getConteudoBalancete()) {
                            if (in.ano.equals(ano)) {
                                flag = false;
                                break;
                            }
                        }
                        if (flag) {
                            new RetrieveListTask(senador, ano).execute();
                        } else {
                            SenadorBalanceteActivity.this.update(senador, ano);
                        }
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });

    }

    private void get() {
        if (ano == null && senador.getAnosDisponiveis() != null && senador.getAnosDisponiveis().size() > 0) {
            ano = senador.getAnosDisponiveis().get(senador.getAnosDisponiveis().size() - 1);
        }

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupViewPager(tabLayout);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        fragOne = SenadorFragmentPerfil.newInstance(senador, ano);
        fragTwo = SenadorFragmentGastos.newInstance(senador, ano);

        adapter.addFragment(fragOne, getResources().getString(R.string.informacoes).toUpperCase());
        adapter.addFragment(fragTwo, ano == null ? "ano" : ano);
        viewPager.setAdapter(adapter);
    }

    private void setupViewPager(TabLayout tabLayout) {
        tabLayout.getTabAt(0).setIcon(new IconicsDrawable(this).color(Color.WHITE).icon(FontAwesome.Icon.faw_user));
        //.color(Color.RED) .sizeDp(24)
        tabLayout.getTabAt(1).setIcon(new IconicsDrawable(this).color(Color.WHITE).icon(FontAwesome.Icon.faw_dollar_sign));
    }

    private void startDialog() {
        if (dialog == null) {
            dialog = new ProgressDialog(SenadorBalanceteActivity.this);
            dialog.setMessage("Carregando...");
            dialog.setIndeterminate(true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(true);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    Toast.makeText(SenadorBalanceteActivity.this, "Cancelado", Toast.LENGTH_SHORT).show();
                    SenadorBalanceteActivity.this.finish();
                }
            });
        }
        dialog.show();
    }

    private void stopDialog() {
        dialog.dismiss();
    }

    private void update(EntidadeSenador entd, String ano) {

        ((ViewPagerAdapter) adapter).getFragmentTitleList().set(1, ano);
        adapter.notifyDataSetChanged();
        setupViewPager(tabLayout);

        SenadorBalanceteActivity.this.senador = entd;
        SenadorBalanceteActivity.this.fragOne.setSenador(entd);
        SenadorBalanceteActivity.this.fragTwo.setSenador(entd);
        SenadorBalanceteActivity.this.ano = ano;
        SenadorBalanceteActivity.this.fragOne.setAno(ano);
        SenadorBalanceteActivity.this.fragTwo.setAno(ano);

        SenadorBalanceteActivity.this.fragOne.populate();
        SenadorBalanceteActivity.this.fragTwo.populate();

        SenadorBalanceteActivity.this.tabLayout.invalidate();
        SenadorBalanceteActivity.this.viewPager.invalidate();

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        public List<String> getFragmentTitleList() {
            return this.mFragmentTitleList;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public int getItemPosition(@NonNull Object item) {
            return POSITION_NONE;
//          return super.getItemPosition(item);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    class RetrieveListTask extends AsyncTask<String, Void, EntidadeSenador> {

        private EntidadeSenador senador;
        private String ano;

        protected RetrieveListTask(EntidadeSenador senador, String ano) {
            this.senador = senador;
            this.ano = ano;

            SenadorBalanceteActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    SenadorBalanceteActivity.this.startDialog();
                }
            });
        }

        protected EntidadeSenador doInBackground(String... url) {
            try {
                return crawler.conn_getSenador(senador, ano);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(final EntidadeSenador senadorDownloaded) {
            //https://stackoverflow.com/questions/2250770/how-to-refresh-android-listview
            SenadorBalanceteActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    SenadorBalanceteActivity.this.get();
                    db.putSenador(senadorDownloaded);
                    String anoAux = senadorDownloaded.getAnosDisponiveis().get(senadorDownloaded.getAnosDisponiveis().size() - 1);
                    SenadorBalanceteActivity.this.update(senadorDownloaded,
                            ano == null ? anoAux : ano);
                    SenadorBalanceteActivity.this.stopDialog();
//                    System.out.println(senadorDownloaded);
                }
            });
        }
    }
}
