package com.troni.fiscalize.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.malinskiy.materialicons.IconDrawable;
import com.malinskiy.materialicons.Iconify;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.troni.fiscalize.activities.MainActivity;
import com.troni.fiscalize.R;
import com.troni.fiscalize.people.senador.Dados;
import com.troni.fiscalize.people.senador.ExpandableListView.ExpandableCollection;
import com.troni.fiscalize.people.senador.ExpandableListView.ExpandableListAdapter;
import com.troni.fiscalize.session.Session;
import com.troni.fiscalize.people.senador.Senador;
import com.troni.fiscalize.util.JSONConversor;
import com.troni.fiscalize.util.MLRoundedImageView;
import com.troni.fiscalize.util.Methods;
import com.vlonjatg.progressactivity.ProgressActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;


public class SenadorFragmentMain extends Fragment {

    public static final String ARG_PARAM1 = "id";

    private int mParam1 = 0;
    private WebView webView;
    private final String urlINIT = "http://www6g.";
    private final String KEYWORD_ID = "__NUMERO__";
    private final String KEYWORD_ANO = "__ANO__";
    private String MAIN_URL = urlINIT + "senado.leg.br/transparencia/sen/" + KEYWORD_ID;
    private final String scriptPath = "js/script_home.js";//assets/js/
    private static String scriptLoaded = null;
    public Senador senador = null;

    private boolean isShowingButtons = false;
    private View thisView = null;
    private ExpandableListView expListView;
    private ExpandableListAdapter adapter;
    private ProgressActivity pa;
    final Handler handlerChangeIcon = new Handler();

    private boolean porcent2 = false, porcent1 = false;

    public Activity getActivity2() {
        return Session.currentActivity;
    }

    @Override
    public void onDestroy() {
        try {
            if (webView != null)
                webView.stopLoading();
        } catch (Exception e) {
        }
        Session.createLog(SenadorFragmentMain.class.getName(), "onDestroy", null);
        try {
            handlerChangeIcon.removeCallbacks(null);
        } catch (Exception e) {

        }
        // webView = null;
        // senador = null;

        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Session.createLog(SenadorFragmentMain.class.getName(), "onDestroyView", null);
        try {
            handlerChangeIcon.removeCallbacks(null);
        } catch (Exception e) {

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Session.createLog(SenadorFragmentMain.class.getName(), "onAttach(context)", null);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Session.createLog(SenadorFragmentMain.class.getName(), "onDetach", null);
    }

    public SenadorFragmentMain() {
        // Required empty public constructor
    }

    public static SenadorFragmentMain newInstance(int param1) {
        SenadorFragmentMain fragment = new SenadorFragmentMain();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
            senador = ((MainActivity) getActivity2()).db_sql.selectSenadorById(mParam1);
        } else {
            senador = new Senador();
        }
        //Session.currentSenador = this.senador;


        if (scriptLoaded == null) {
            InputStream input = null;
            byte[] buffer = null;
            try {
                input = getActivity2().getAssets().open(scriptPath);
                buffer = new byte[input.available()];
                input.read(buffer);
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            scriptLoaded = Base64.encodeToString(buffer, Base64.NO_WRAP);
        }
    }

    private void initButtons(View v) {
        FloatingActionButton btn_more = (FloatingActionButton) v.findViewById(R.id.btn_mais);
        final FloatingActionButton btn_report = (FloatingActionButton) v.findViewById(R.id.btn_denunciar);
        final FloatingActionButton btn_refresh = (FloatingActionButton) v.findViewById(R.id.btn_atualizar);
        final FloatingActionButton btn_screenShot = (FloatingActionButton) v.findViewById(R.id.btn_screenshot);
        final FloatingActionButton btn_filtrar = (FloatingActionButton) v.findViewById(R.id.btn_filtrar);

        btn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                if (isShowingButtons) {
                    hideAllButtons(thisView);
                } else {
                    showAllButtons(thisView);
                }
            }
        });
        btn_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity2(), "->DENUNCIAR", Toast.LENGTH_SHORT).show();
                //DO ACTION
                hideAllButtons(thisView);
            }
        });
        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filtrar("", true, Session.currentSenador.link);
                hideAllButtons(thisView);
            }
        });
        btn_screenShot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity2(), "->SCREENSHOT", Toast.LENGTH_SHORT).show();
                //DO ACTION
                hideAllButtons(thisView);
            }
        });
        btn_filtrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideAllButtons(thisView);

                String[] anos = new String[senador.anos.size()];
                for (int i = 0; i < senador.anos.size(); i++) {
                    anos[i] = senador.anos.get(i).label;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity2());
                builder.setTitle("Escolha")
                        .setItems(anos, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                filtrar(senador.numero, true, senador.anos.get(which).link);
                            }
                        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.create().show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Session.createLog(SenadorFragmentMain.class.getName(), "onResume", null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Session.createLog(SenadorFragmentMain.class.getName(), "onCreateView", null);
        // Inflate the layout for this fragment
        // View v = inflater.inflate(R.layout.content_senador_main, container, false);
        final View v = inflater.inflate(R.layout.fragment_senador_main, container, false);
        thisView = v;

        final ImageView tip = (ImageView) v.findViewById(R.id.img_floating_tip);
        final LinearLayout linearLayout = (LinearLayout) v.findViewById(R.id.dragView);
        final SlidingUpPanelLayout up = (SlidingUpPanelLayout) v.findViewById(R.id.sliding_layout);
        final RelativeLayout content = (RelativeLayout) v.findViewById(R.id.floating_content_added);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (up.getPanelState().toString().equals("EXPANDED")) {
                    up.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    int imageResourceId = R.drawable.icon_cima_floating;
                    tip.setBackgroundResource(imageResourceId);
                    tip.setImageResource(imageResourceId);
                } else {
                    up.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                    int imageResourceId = R.drawable.icon_baixo_floating;
                    tip.setBackgroundResource(imageResourceId);
                    tip.setImageResource(imageResourceId);
                }
            }
        });

        Display display = getActivity2().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        //int width = size.x;
        final int height = size.y;

        final double porcentchange1 = 2.0 / 3.0;//Se >= 2/3 da tela mudará o ícone
        final double porcentchange2 = 1.0 / 4.0;//Se <= 1/4 da tela mudará o ícone

        Runnable r = new Runnable() {
            @Override
            public void run() {
                //Session.createLog(SenadorFragmentMain.class.getName(), "-> " + (linearLayout.getY() * 100 / (double) height) + "%", null);
                if (linearLayout.getY() >= (height * porcentchange1) && !porcent1) {
                    //Session.createLog(SenadorFragmentMain.class.getName(), "(linearLayout.getY() >= (height * porcentchange1) && !porcent1)", null);
                    int imageResourceId = R.drawable.icon_baixo_floating;
                    tip.setBackgroundResource(imageResourceId);
                    tip.setImageResource(imageResourceId);
                    porcent1 = true;
                    porcent2 = false;
                } else if (linearLayout.getY() <= (height * porcentchange2) && !porcent2) {
                    //Session.createLog(SenadorFragmentMain.class.getName(), "(linearLayout.getY() <= (height * porcentchange2) && !porcent2)", null);
                    int imageResourceId = R.drawable.icon_cima_floating;
                    tip.setBackgroundResource(imageResourceId);
                    tip.setImageResource(imageResourceId);
                    porcent1 = false;
                    porcent2 = true;
                }
                handlerChangeIcon.postDelayed(this, 500);
            }
        };
        handlerChangeIcon.post(r);

        pa = (ProgressActivity) v.findViewById(R.id.progress_senador);

        expListView = (ExpandableListView) v.findViewById(R.id.expandableListView);
        initButtons(v);
        initWebViews(v);

        if (!Session.currentSenador.nome.isEmpty()) {
            populateFilds(Session.currentSenador);
        } else {
            if (checarConexao()) {
                startLoadingContent(senador.numero, false, "");
                pa.showLoading();
            }
        }

        return v;
    }

    private void abrirLinkInBrowser(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        try {
            getActivity2().startActivity(i);
        } catch (Exception e) {
            Toast.makeText(getActivity2(), "Não foi possível abrir esta informação. Tente novamente.", Toast.LENGTH_LONG).show();
        }
    }

    private void filtrar(String numero, boolean trigger, String url) {
        if (checarConexao()) {
            startLoadingContent(numero, trigger, url);
            pa.showLoading();
        }
    }

    private void onErroWhileGettingData(String title, String body) {
        Drawable emptyDrawable = new IconDrawable(getActivity2(), Iconify.IconValue.zmdi_block).colorRes(R.color.colorErrorIcon);

        pa.showError(emptyDrawable, title, body, "Voltar", new View.OnClickListener() {
            public void onClick(View v) {
                getActivity2().onBackPressed();
            }
        });
    }

    private boolean checarConexao() {
        if (!Methods.isNetworkAvailable(getActivity2())) {
            Drawable errorDrawable = new IconDrawable(getActivity2(), Iconify.IconValue.zmdi_wifi_off).colorRes(R.color.colorErrorIcon);
            pa.showError(errorDrawable, "Sem conexão", "Ative a conexão de dados ou wifi para continuar...", "Voltar", new View.OnClickListener() {
                public void onClick(View v) {
                    getActivity2().onBackPressed();
                }
            });
            return false;
        }

        return true;
    }

    private void hideAllButtons(View v) {
        final FloatingActionButton fab1 = (FloatingActionButton) v.findViewById(R.id.btn_denunciar);
        final FloatingActionButton fab2 = (FloatingActionButton) v.findViewById(R.id.btn_atualizar);
        final FloatingActionButton fab3 = (FloatingActionButton) v.findViewById(R.id.btn_screenshot);
        final FloatingActionButton fab4 = (FloatingActionButton) v.findViewById(R.id.btn_filtrar);
        fab1.hide();
        fab2.hide();
        fab3.hide();
        fab4.hide();

        isShowingButtons = false;
    }

    private void showAllButtons(View v) {
        final FloatingActionButton fab1 = (FloatingActionButton) v.findViewById(R.id.btn_denunciar);
        final FloatingActionButton fab2 = (FloatingActionButton) v.findViewById(R.id.btn_atualizar);
        final FloatingActionButton fab3 = (FloatingActionButton) v.findViewById(R.id.btn_screenshot);
        final FloatingActionButton fab4 = (FloatingActionButton) v.findViewById(R.id.btn_filtrar);
        fab1.show();
        fab2.show();
        fab3.show();
        fab4.show();

        isShowingButtons = true;
    }

    private void initWebViews(View view) {
        //RelativeLayout layout = (RelativeLayout) ((ScrollView) view).getChildAt(0);
        ProgressActivity layout = (ProgressActivity) view;

        webView = new WebView(getActivity2());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            webView.addJavascriptInterface(new WebAppInterface(getActivity2()), "Android");
        webView.getSettings().setLoadsImagesAutomatically(false);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSaveFormData(false);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setDomStorageEnabled(true);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2)
            webView.getSettings().setSavePassword(false);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });
        webView.setPadding(0, 0, 0, 0);
        webView.setFocusable(false);
        webView.setWebViewClient(new BrowserMain());
        layout.addView(webView, 0, 0);
    }

    private void startLoadingContent(String id, boolean isFiltered, String url) {
        Session.createLog(SenadorFragmentMain.class.getName(), "startLoadingContent() id=" + id + " | isFiltered=" + isFiltered + " | url=" + url, null);
        try {
            webView.stopLoading();
        } catch (Exception e) {
        }
        String urlToLoad;
        if (isFiltered) {
            urlToLoad = url;
        } else {
            urlToLoad = MAIN_URL.replace(KEYWORD_ID, id);
        }
        webView.loadUrl(urlToLoad);
        //Toast.makeText(getActivity2(), R.string.carregando, Toast.LENGTH_SHORT).show();

        Methods.lockOrUnlockOrientation(getActivity2());
    }

    private void populateFilds(Senador senador) {
        Session.createLog(this.getClass().getName(), "populateFilds()", null);

        this.senador = senador;
        Session.currentSenador = this.senador;
        Session.currentSenador_dados = new ArrayList<>();

        TextView nome = (TextView) thisView.findViewById(R.id.textView_senador_nome);
        nome.setText(senador.nome);
        TextView nasc = (TextView) thisView.findViewById(R.id.textView_senador_nascimento);
        nasc.setText(senador.nascimento);
        TextView nat = (TextView) thisView.findViewById(R.id.textView_senador_naturalidade);
        nat.setText(senador.naturalidade);
        TextView gabinete = (TextView) thisView.findViewById(R.id.textView_senador_gabinete);
        gabinete.setText(senador.gabinete);
        TextView telefone = (TextView) thisView.findViewById(R.id.textView_senador_telefone);
        telefone.setText(senador.telefones);
        TextView fax = (TextView) thisView.findViewById(R.id.textView_senador_fax);
        fax.setText(senador.fax);
        TextView email = (TextView) thisView.findViewById(R.id.textView_senador_email);
        email.setText(senador.email);

        TextView f_nome = (TextView) thisView.findViewById(R.id.floating_nome_senador);
        f_nome.setText(senador.nome);
        TextView f_partido = (TextView) thisView.findViewById(R.id.floating_partido_senador);
        f_partido.setText(senador.partido);
        TextView f_ano = (TextView) thisView.findViewById(R.id.floating_ano_senador);
        f_ano.setText(senador.currentAno);

        //Se imagem já foi carregada previamente, só faço carregá-la
        if (senador.bitmapFoto == null) {
            if (senador.foto_url != null && !senador.foto_url.equals(""))
                if (senador.foto_url.endsWith("jpg") || senador.foto_url.endsWith("png"))
                    new DownloadImageTask((MLRoundedImageView) thisView.findViewById(R.id.imageview_senador_redondo))
                            .execute(senador.foto_url);
        } else {
            ((MLRoundedImageView) thisView.findViewById(R.id.imageview_senador_redondo)).setImageBitmap(senador.bitmapFoto);
        }

        expListView.setGroupIndicator(null);

        ExpandableCollection.key_value = new ArrayList<String>();
        ExpandableCollection.expandable_main_arr = new ArrayList<ExpandableCollection>();
        ExpandableCollection.expandable_hashmap = new HashMap<String, ArrayList<ExpandableCollection>>();

        for (int i = 0; i < senador.titulos.size(); i++) {
            //Crio o período
            ExpandableCollection.key_value.add(senador.titulos.get(i));
            ArrayList<ExpandableCollection> arr_obj = new ArrayList<ExpandableCollection>();

            ArrayList<Dados> array = new ArrayList<>();
            int tipo = 0;
            switch (i + 1) {
                case 1:
                    array = senador.dados1;
                    break;
                case 2:
                    array = senador.dados2;
                    break;
                case 3:
                    tipo = 1;
                    array = senador.dados3;
                    break;
                case 4:
                    tipo = 1;
                    array = senador.dados4;
                    break;
                case 5:
                    tipo = 1;
                    array = senador.dados5;
                    break;
            }
            for (int j = 0; j < array.size(); j++) {
                //Crio a Matéria
                arr_obj.add(new ExpandableCollection(array.get(j).label,
                        array.get(j).valor,
                        array.get(j).link, tipo));
            }
            ExpandableCollection.expandable_hashmap.put(ExpandableCollection.key_value.get(i), arr_obj);
        }

        adapter = new ExpandableListAdapter(getActivity2(),
                ExpandableCollection.key_value,
                ExpandableCollection.expandable_hashmap);

        expListView.setAdapter(adapter);

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                if (ExpandableCollection.key_value != null) {
                    if (ExpandableCollection.key_value.size() > groupPosition) {
                        String key = ExpandableCollection.key_value.get(groupPosition);

                        if (ExpandableCollection.expandable_hashmap.size() > 0) {
                            ExpandableCollection obj_exp = ExpandableCollection.expandable_hashmap
                                    .get(key).get(childPosition);

                            if (obj_exp.tipo == 1) {
                                //Abrir link em browser...
                                abrirLinkInBrowser(obj_exp.link);
                            } else {
                                if (obj_exp.link == null || obj_exp.link.isEmpty()) {
                                    //Se não tiver link, não faço action.
                                } else {
                                    SenadorFragmentMainYearDetail fragment = SenadorFragmentMainYearDetail.newInstance(obj_exp.link);

                                    ((MainActivity) getActivity2()).switchContent(fragment);
                                }
                            }
                        }
                    }
                }
                return false;
            }
        });

        notifyAdapter();

        pa.showContent();
    }

    private void notifyAdapter() {
        adapter.notifyDataSetChanged();
    }

    public class BrowserMain extends WebViewClient {
        boolean loadingFinished = true;
        boolean redirect = false;

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Session.createLog(this.getClass().getName(), "shouldOverrideUrlLoading(): redirecionando para url: " + url, null);

            if (!loadingFinished) {
                redirect = true;
            }
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed(); // Ignore SSL certificate errors
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Session.createLog(this.getClass().getName(), "shouldOverrideUrlLoading(): view.getUrl(): " + view.getUrl(), null);
            Session.createLog(this.getClass().getName(), "shouldOverrideUrlLoading(): failingUrl: " + failingUrl, null);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //Toast.makeText(getActivity2(), "Carregando: " + url, Toast.LENGTH_SHORT).show();
            Session.createLog(this.getClass().getName(), "onPageStarted(): url: " + url, null);
            loadingFinished = false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Session.createLog(this.getClass().getName(), "onPageStarted(): url: " + url + " | loadingFinished=" + loadingFinished + " | redirect=" + redirect, null);

            if (!redirect) {
                loadingFinished = true;
            }

            if (loadingFinished && !redirect) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        //Devices >= 4.4
                        injectJavaScriptFileByEvaluateJavascript("mainScript()");
                    } else {
                        //Devices < Android 4.4
                        //injectJavaScriptFileByLoad("mainScript(true)");
                    }
                } catch (Exception e) {
                    Session.createLog(this.getClass().getName(), "onPageFinished()", e);
                }
            } else {
                redirect = false;
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void injectJavaScriptFileByEvaluateJavascript(String scriptToRun) {
        Session.createLog(this.getClass().getName(), "injectJavaScriptFileByEvaluateJavascript() INSERINDO JS", null);

        try {

            //Esse if é para remover bug de quando tento verificar mais de um senador numa mesma "abertura" de app.
            if (webView == null) {
                initWebViews(thisView);

                if (checarConexao()) {
                    startLoadingContent(senador.numero, false, "");
                    pa.showLoading();
                }
                return;
            }

            webView.evaluateJavascript("(function() {" +
                    "var parent = document.getElementsByTagName('head')[0];" +
                    "if(parent!=null){" +
                    "var script = document.createElement('script');" +
                    "script.type = 'text/javascript';" +
                    "script.innerHTML = decodeURIComponent(escape(window.atob('" + scriptLoaded + "')));" +
                    "parent.appendChild(script);" +
                    "return " + scriptToRun + ";}})();", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    Session.createLog(this.getClass().getName(), "onReceiveValue() value -->>: " + value, null);

                    Methods.lockOrUnlockOrientation(getActivity2());

                    if (value != null && !value.equals("null")) {
                        String formatedValue = value.trim().replace("\\", "");
                        formatedValue = formatedValue.substring(1, formatedValue.length() - 1);

                        Session.createLog(this.getClass().getName(), "onReceiveValue() CONVERTIDO -->>: " + formatedValue, null);

                        //Recoloco o número
                        String num = ((MainActivity) getActivity2()).db_sql.selectSenadorById(mParam1).numero;
                        Senador senador = JSONConversor.converterStringToSenador(formatedValue);
                        senador.numero = num;
                        final Senador sen = senador;


                        if (senador != null) {
                            getActivity2().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //ACTION AFTER ALL DONE
                                    populateFilds(sen);
                                }
                            });
                        } else {
                            onErroWhileGettingData("Erro (id1) ao converter dados", "O app não conseguiu converter os dados obtidos no Portal da Transparência. Contate o suporte do app.");

                        }
                    } else {
                        onErroWhileGettingData("Erro (id2) ao obter dados", "O Portal da Transparência não respondeu como deveria. Tente novamente ou contate o suporte do app.");
                    }
                }
            });
        } catch (Exception e) {
            Session.createLog(this.getClass().getName(), "injectJavaScriptFileByEvaluateJavascript", e);
        }
    }

    public class WebAppInterface {
        Activity mContext;

        public WebAppInterface(Activity c) {
            mContext = c;
        }

        @JavascriptInterface
        public void getJSON(final String str) {
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Session.createLog(this.getClass().getName(), "getJSON() formatedValue -->> " + str, null);
                    getJSONfromWebView(str);
                }
            });

        }

    }

    private void getJSONfromWebView(String str) {
        Session.createLog(this.getClass().getName(), "getJSONfromWebView", null);
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Session.createLog(SenadorFragmentMain.class.getName(), "doInBackground()", e);
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);

            Session.currentSenador.bitmapFoto = result;
        }
    }
}
