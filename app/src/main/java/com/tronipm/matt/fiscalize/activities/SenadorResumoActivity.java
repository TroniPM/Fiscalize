package com.tronipm.matt.fiscalize.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.anychart.anychart.AnyChart;
import com.anychart.anychart.AnyChartView;
import com.anychart.anychart.Cartesian;
import com.anychart.anychart.CartesianSeriesColumn;
import com.anychart.anychart.DataEntry;
import com.anychart.anychart.EnumsAnchor;
import com.anychart.anychart.HoverMode;
import com.anychart.anychart.Position;
import com.anychart.anychart.TooltipPositionMode;
import com.anychart.anychart.ValueDataEntry;
import com.tronipm.matt.fiscalize.R;
import com.tronipm.matt.fiscalize.crawlers.CrawlerSenador;
import com.tronipm.matt.fiscalize.crawlers.entities.EntidadeSenadorResumo;
import com.tronipm.matt.fiscalize.database.TinyDB;
import com.tronipm.matt.fiscalize.entities.EntidadeSenador;

import java.util.ArrayList;
import java.util.List;

public class SenadorResumoActivity extends AppCompatActivity {
    private CrawlerSenador crawlerSenador = null;
    private TinyDB db = null;

    private static EntidadeSenador senador = null;
    private static EntidadeSenadorResumo senadorResumo = null;
    private static String link = null;
    private static String titulo = null;
    private ProgressDialog dialog = null;

    public static void setSenador(EntidadeSenador senador) {
        SenadorResumoActivity.senador = senador;
    }

    public static void setSenador() {
        SenadorResumoActivity.senador = null;
    }

    public static void setLink(String link) {
        SenadorResumoActivity.link = link;
    }

    public static void setLink() {
        SenadorResumoActivity.link = null;
    }

    public static void setTitulo(String titulo) {
        SenadorResumoActivity.titulo = titulo;
    }

    public static void setTitulo() {
        SenadorResumoActivity.titulo = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senador_resumo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        crawlerSenador = new CrawlerSenador();
        db = new TinyDB(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Resumo");

        //APENAS PARA DEBUG
        if (senador != null) {
            boolean flag = true;
            for (int i = 0; senador.getConteudoResumo() != null && i < senador.getConteudoResumo().size(); i++) {
                if (senador.getConteudoResumo().get(i).link.equals(link)) {
                    senadorResumo = senador.getConteudoResumo().get(i);
                    criarGrafico(senadorResumo);
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

    private void criarGrafico(EntidadeSenadorResumo resumo) {
        if (resumo == null) {
            Toast.makeText(this, "Erro ao consultar informações", Toast.LENGTH_SHORT).show();
            this.finish();
            return;
        }
        AnyChartView anyChartView = findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar));

        Cartesian cartesian = AnyChart.column();

        List<DataEntry> data = new ArrayList<>();
        for (int i = 0; i < resumo.tabela.linhas.size(); i++) {
            double vr = Double.parseDouble(resumo.tabela.linhas.get(i).conteudo
                    .replaceAll("\\.", "")
                    .replaceAll(",", "."));
            data.add(new ValueDataEntry(resumo.tabela.linhas.get(i).label,
                    vr));
        }

        CartesianSeriesColumn column = cartesian.column(data);
        //TODO CORRIGIR DEPOIS a formatação do DECIMAL com.anychart.anychart.Tooltip >> https://docs.anychart.com/Common_Settings/Text_Formatters
        String formatModel = "R$ {%Value}{groupsSeparator:., decimalsCount:2}";//%.2f
        column.getTooltip()
                .setTitleFormat("{%X}")
                .setPosition(Position.CENTER_BOTTOM)
                .setAnchor(EnumsAnchor.CENTER_BOTTOM)
                .setOffsetX(0d)
                .setOffsetY(5d)
                .setFormat(formatModel);

        cartesian.setAnimation(true);
        cartesian.setTitle(resumo.titulo);

        cartesian.getYScale().setMinimum(0d);

        cartesian.getYAxis().getLabels().setFormat(formatModel);

        cartesian.getTooltip().setPositionMode(TooltipPositionMode.POINT);
        cartesian.getInteractivity().setHoverMode(HoverMode.BY_X);

        cartesian.getXAxis().setTitle("Mês");
        cartesian.getYAxis().setTitle("Valor");

        anyChartView.setChart(cartesian);
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

        protected RetrieveListTask(EntidadeSenador senador, String link, String titulo) {
            this.senador = senador;
            this.link = link;
            this.titulo = titulo;

            SenadorResumoActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    SenadorResumoActivity.this.startDialog();
                }
            });
        }

        protected EntidadeSenador doInBackground(String... url) {
            try {
                return crawlerSenador.conn_getSenadorAno(senador, link);
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
                    for (int i = 0; i < senador.getConteudoResumo().size(); i++) {
                        if (senador.getConteudoResumo().get(i).link.equals(link)) {
                            resumo = senador.getConteudoResumo().get(i);
                            break;
                        }
                    }
                    SenadorResumoActivity.this.criarGrafico(resumo);
                    //preencher campos
                    SenadorResumoActivity.this.stopDialog();
                }
            });
        }
    }
}
