package com.troni.fiscalize.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.troni.fiscalize.R;
import com.troni.fiscalize.database.SharedPreferencesManager;
import com.troni.fiscalize.database.SqliteManager;
import com.troni.fiscalize.session.Session;
import com.vlonjatg.progressactivity.ProgressActivity;

import java.util.ArrayList;
import java.util.List;

public class SplashScreen extends AppCompatActivity {
    private ProgressActivity pa = null;
    private List<Integer> skipIds = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Session.createLog(this.getClass().getName(), "onCreate()", null);

        Session.currentActivity = this;
        //this._context = this;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);

        pa = (ProgressActivity) findViewById(R.id.progress_init);

        skipIds = new ArrayList<>();
        skipIds.add(R.id.relative_splas_screen);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Session.createLog(this.getClass().getName(), "onResume()", null);

        new SetupDataBaseWorker(this, pa, skipIds).execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.createLog(this.getClass().getName(), "onDestroy()", null);
    }

    private class SetupDataBaseWorker extends AsyncTask<Void, Void, Void> {
        private List<Integer> skipIds;
        private ProgressActivity pa;
        private SqliteManager db_sql;//= new SqliteManager(getApplicationContext());
        private SharedPreferencesManager db_sharedPref;//= new SharedPreferencesManager(getApplicationContext());
        private Activity activity;

        public SetupDataBaseWorker(Activity activity, ProgressActivity pa, List<Integer> skipIds) {
            Session.createLog(this.getClass().getName(), "CONSTRUCTOR SetupDataBaseWorker()", null);
            this.activity = activity;
            this.pa = pa;
            this.skipIds = skipIds;

            //db_sharedPref = new SharedPreferencesManager(this.activity);
            //db_sql = new SqliteManager(this.activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pa.showLoading(skipIds);
            pa.showLoading();
        }

        @Override
        protected Void doInBackground(Void... urls) {
            Session.createLog(this.getClass().getName(), "doInBackground()", null);
            db_sharedPref = new SharedPreferencesManager(this.activity);
            db_sql = new SqliteManager(this.activity);

            if (db_sharedPref != null && db_sql != null) {
                Session.createLog(this.getClass().getName(), "doInBackground() db_sharedPref != null AND db_sql != null", null);
                if (!db_sharedPref.getBancoJaFoiInstalado()) {
                    db_sql.insertSenadores();
                    db_sharedPref.setBancoJaFoiInstalado();

                    Session.canEnterMainScreen = true;
                } else {
                    Session.canEnterMainScreen = true;
                }
            } else {
                Session.createLog(this.getClass().getName(), "doInBackground() db_sharedPref==null AND db_sql=null", null);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Session.createLog(this.getClass().getName(), "onPostExecute()", null);

            this.activity.startActivity(new Intent(SplashScreen.this, MainActivity.class));
            this.activity.finish();
        }
    }
}
