package com.tronipm.matt.fiscalize.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.tronipm.matt.fiscalize.R;
import com.tronipm.matt.fiscalize.crawlers.CrawlerSenador;
import com.tronipm.matt.fiscalize.database.TinyDB;
import com.tronipm.matt.fiscalize.entities.EntidadeSenador;

import java.util.ArrayList;
import java.util.List;

public class SenadorActivity extends AppCompatActivity {
    public static final String PARAM1 = "senador";
    private CrawlerSenador crawler = null;
    private TinyDB db = null;
    private EntidadeSenador senador = null;

    private ProgressDialog dialog = null;


    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senador);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new TinyDB(this);
        crawler = new CrawlerSenador();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Perfil");

        senador = (EntidadeSenador) getIntent().getSerializableExtra(PARAM1);


        //APENAS PARA DEBUG
        if (senador != null) {
            if (senador.getTabelas() == null) {
                startDialog();
                new RetrieveListTask(senador).execute();
            } else {
                get();
            }
        }


    }

    private void get() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupViewPager(tabLayout);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        SenadorFragmentPerfil tabPerfil = SenadorFragmentPerfil.newInstance(senador);
        SenadorFragmentGastos tabGastos = SenadorFragmentGastos.newInstance(senador);

        adapter.addFragment(tabPerfil, "");
        adapter.addFragment(tabGastos, "");
        viewPager.setAdapter(adapter);
    }

    private void setupViewPager(TabLayout tabLayout) {
        tabLayout.getTabAt(0).setIcon(new IconicsDrawable(this).color(Color.WHITE).icon(FontAwesome.Icon.faw_user));
        //.color(Color.RED) .sizeDp(24)
        tabLayout.getTabAt(1).setIcon(new IconicsDrawable(this).color(Color.WHITE).icon(FontAwesome.Icon.faw_dollar_sign));
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

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
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
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
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
                    SenadorActivity.this.get();

                    System.out.println(senador);
                }
            });
        }
    }
}
