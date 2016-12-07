package com.troni.fiscalize.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.troni.fiscalize.R;
import com.troni.fiscalize.database.SharedPreferencesManager;
import com.troni.fiscalize.database.SqliteManager;
import com.troni.fiscalize.fragment.HomeFragment;
import com.troni.fiscalize.fragment.PresidenteFragment;
import com.troni.fiscalize.fragment.SenadorFragmentList;
import com.troni.fiscalize.fragment.SenadorFragmentMain;
import com.troni.fiscalize.fragment.SenadorFragmentMainMonthDetail;
import com.troni.fiscalize.fragment.SenadorFragmentMainYearDetail;
import com.troni.fiscalize.fragment.SobreFragment;
import com.troni.fiscalize.session.Session;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public Fragment currentFragment = null;
    public Toolbar toolbar = null;
    public DrawerLayout drawer = null;
    public ActionBarDrawerToggle toggle = null;
    private long lastPress;
    public SqliteManager db_sql;
    public SharedPreferencesManager db_sharedPref;

    public static boolean orientationLocked = false;

    public ArrayList<Fragment> listaFragmentos = new ArrayList<>();
    private int qntd_fragmentos_empilhados = 5;//

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Session.createLog(this.getClass().getName(), "onConfigurationChanged()", null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Session.currentActivity = this;
        db_sharedPref = new SharedPreferencesManager(this);//Precisa ser antes do init no sqlite
        db_sql = new SqliteManager(this);


        //currentFragmentId = R.layout.content_main;
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        switchContent(new HomeFragment());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //Se estiver na tela MAIN do senador, volto pra anterior.
            if (currentFragment instanceof SenadorFragmentMain ||
                    currentFragment instanceof SenadorFragmentMainYearDetail ||
                    currentFragment instanceof SenadorFragmentMainMonthDetail) {
                //switchContent(new SenadorFragmentList());
                getPreviousFragment();
            } else {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastPress > 3000) {
                    Toast.makeText(getApplicationContext(), getString(R.string.sair), Toast.LENGTH_SHORT).show();
                    lastPress = currentTime;
                } else {
                    super.onBackPressed();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void switchContent(Fragment fragment) {
        if (this.currentFragment != null) {
            getSupportFragmentManager().beginTransaction().
                    remove(this.currentFragment).commit();
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.principal, fragment)
                .commit();

        listaFragmentos.add(fragment);
        this.currentFragment = fragment;
    }

    private void getPreviousFragment() {
        if (listaFragmentos != null && listaFragmentos.size() > 1) {
            getSupportFragmentManager().beginTransaction().
                    remove(this.currentFragment).commit();

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.principal, listaFragmentos.get(listaFragmentos.size() - 2))
                    .commit();
            //this.setContentView(listaFragmentos.get(listaFragmentos.size() - 1).getView());
            this.currentFragment = listaFragmentos.get(listaFragmentos.size() - 2);
            listaFragmentos.remove(listaFragmentos.size() - 1);

            while (listaFragmentos.size() > qntd_fragmentos_empilhados) {
                listaFragmentos.remove(0);
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            switchContent(new HomeFragment());
        } else if (id == R.id.nav_fav) {

        } else if (id == R.id.nav_presidente) {
            switchContent(new PresidenteFragment());
        } else if (id == R.id.nav_senador) {
            switchContent(new SenadorFragmentList());
        } else if (id == R.id.nav_deputado) {

        } else if (id == R.id.nav_compartilhar) {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "Vê só esse aplicativo!! " + "http://www.eu.com.br";
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Compartilhar via"));

        } else if (id == R.id.nav_feedback) {
            final ScrollView scrollView = new ScrollView(this);
            final EditText input = new EditText(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            input.setLayoutParams(lp);
            input.setSelected(true);
            input.setLines(6);
            input.setGravity(Gravity.TOP);
            scrollView.addView(input);

            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Fale onosco");
            alertDialog.setMessage("Insira sua mensagem");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Enviar",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (!input.getText().toString().equals("")) {
                                //Forço teclado a fechar
                                ((InputMethodManager) getApplication().getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                                sendEmail(input.getText().toString());
                                dialog.dismiss();
                            } else {
                                Toast.makeText(getApplication(), "Insira uma mensagem", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancelar",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ((InputMethodManager) getApplication().getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                            dialog.dismiss();
                        }
                    });
            alertDialog.setView(scrollView);
            alertDialog.show();

        } else if (id == R.id.nav_sobre) {
            switchContent(new SobreFragment());

        } else if (id == R.id.nav_config) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void sendEmail(String emailTxt) {
        Intent i = new Intent(Intent.ACTION_SEND);
        //i.setType("message/rfc822");
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"paulomatew@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, this.getResources().getString(R.string.app_name) + " - " + "Contato");
        i.putExtra(Intent.EXTRA_TEXT, emailTxt);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            this.startActivity(Intent.createChooser(i, "Enviar e-mail"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getApplication(), "Erro ao enviar email...", Toast.LENGTH_LONG).show();
        }
    }
}
