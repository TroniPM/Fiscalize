package com.troni.fiscalize.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.malinskiy.materialicons.IconDrawable;
import com.malinskiy.materialicons.Iconify;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.troni.fiscalize.R;
import com.troni.fiscalize.activities.MainActivity;
import com.troni.fiscalize.people.senador.Dados;
import com.troni.fiscalize.people.senador.ListAdapterYear;
import com.troni.fiscalize.session.Session;
import com.troni.fiscalize.util.JSONConversor;
import com.troni.fiscalize.util.MLRoundedImageView;
import com.troni.fiscalize.util.Methods;
import com.vlonjatg.progressactivity.ProgressActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.troni.fiscalize.session.Session.currentSenador;

public class SenadorFragmentMainYearDetail extends Fragment {

    public static final String ARG_PARAM1 = "id";

    private WebView webView;
    private String MAIN_URL = "";
    private final String scriptPath = "js/script_mes.js";//assets/js/
    private static String scriptLoaded = null;

    private boolean porcent2 = false, porcent1 = false;
    final Handler handlerChangeIcon = new Handler();

    private View thisView = null;

    private ProgressActivity pa;

    // private Senador senador;
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

        super.onDestroy();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Session.createLog(this.getClass().getName(), "onAttach(context)", null);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Session.createLog(this.getClass().getName(), "onDetach", null);
    }

    public SenadorFragmentMainYearDetail() {
        // Required empty public constructor
    }

    public static SenadorFragmentMainYearDetail newInstance(String param1) {
        SenadorFragmentMainYearDetail fragment = new SenadorFragmentMainYearDetail();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            MAIN_URL = getArguments().getString(ARG_PARAM1);
        }
        //this.senador = SenadorFragmentMain.senador;


        if (scriptLoaded == null) {
            scriptLoaded = getFile(scriptPath);
        }

        if (Session.script_jQuery_loaded == null) {
            Session.script_jQuery_loaded = getFile(Session.scriptJQuery_locale);
        }
        if (Session.script_canvgRGB_loaded == null) {
            Session.script_canvgRGB_loaded = getFile(Session.scriptCanvgRgb_locale);
        }
        if (Session.script_canvg_loaded == null) {
            Session.script_canvg_loaded = getFile(Session.scriptCanvg_locale);
        }
    }

    private String getFile(String url) {
        InputStream input = null;
        byte[] buffer = null;
        try {
            input = getActivity2().getAssets().open(url);
            buffer = new byte[input.available()];
            input.read(buffer);
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Base64.encodeToString(buffer, Base64.NO_WRAP);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // View v = inflater.inflate(R.layout.content_senador_main, container, false);
        View v = inflater.inflate(R.layout.fragment_senador_year_detail, container, false);
        thisView = v;

        pa = (ProgressActivity) v.findViewById(R.id.progress_senador_year);

        final ImageView tip = (ImageView) v.findViewById(R.id.img_floating_tip);
        final LinearLayout linearLayout = (LinearLayout) v.findViewById(R.id.dragView);
        final SlidingUpPanelLayout up = (SlidingUpPanelLayout) v.findViewById(R.id.sliding_layout);

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

        initSenadorData();

        initWebViews(v);


        if (!Session.currentSenador_dados.isEmpty()) {
            populateFilds(Session.currentSenador_dados);
        } else {
            if (checarConexao()) {
                startLoadingContent(MAIN_URL);
                pa.showLoading();
            }
        }

        //Toast.makeText(getActivity2(), MAIN_URL, Toast.LENGTH_SHORT).show();
        return v;
    }

    private ArrayList<Dados> copyArray(List<Dados> array) {
        ArrayList<Dados> novo = new ArrayList<Dados>();
        for (Dados in : array) {
            novo.add((in.clone()));
        }
        return novo;
    }

    /**
     * O primeiro indice é traz OUTRAS informações. Então remover o índice antes de chamar listview
     * label=img base 64
     * valor=titulo da despesa
     *
     * @param dados
     */
    private void initListView(List<Dados> dados) {
        Session.currentSenador_dados = dados;
        final ArrayList<Dados> copia = copyArray(dados);
        ListView yourListView = (ListView) thisView.findViewById(R.id.itemListView);

        TextView titulo = (TextView) thisView.findViewById(R.id.title_despesa_senador);
        titulo.setText(copia.get(0).valor);

        if (copia.get(0).label != null)
            try {
                showGrafico(dados.get(0).label);
            } catch (Exception e) {
                onErroWhileGettingData("Erro (id5) ao reler", "O app não conseguiu reutilizar os dados já obtidos. Contate o suporte do app.");
            }

        List<View.OnClickListener> lista = new ArrayList<>();
        lista.add(null);
        for (int i = 0; i < dados.size(); i++) {
            final int j = i;
            lista.add(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SenadorFragmentMainMonthDetail fragment = SenadorFragmentMainMonthDetail.newInstance(copia.get(j).link);
                    ((MainActivity) getActivity2()).switchContent(fragment);
                }
            });
        }

        ListAdapterYear customAdapter = new ListAdapterYear(getActivity2(), R.layout.senador_year_detail_itemlistrow, copia, lista);

        yourListView.setAdapter(customAdapter);

        customAdapter.notifyDataSetChanged();

    }

    public void showGrafico(String strBase64) throws Exception {
        strBase64 = strBase64.substring(strBase64.indexOf(",") + 1);
        byte[] decodedString = Base64.decode(strBase64, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);


        FrameLayout fm = new FrameLayout(Session.currentActivity);
        ImageView im = new ImageView(Session.currentActivity);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        params.setMargins(5, 5, 5, 5);
        fm.setLayoutParams(params);


        im.setImageBitmap(decodedByte);
        fm.addView(im);


        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(Session.currentActivity);

        dialogBuilder.setView(fm);
        dialogBuilder.setTitle("Gráfico");
        dialogBuilder.setPositiveButton("Voltar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }

        });
        android.app.AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private void onErroWhileGettingData(String title, String body) {
        try {
            Drawable emptyDrawable = new IconDrawable(getActivity2(), Iconify.IconValue.zmdi_block).colorRes(R.color.colorErrorIcon);

            pa.showError(emptyDrawable, title, body, "Voltar", new View.OnClickListener() {
                public void onClick(View v) {
                    getActivity2().onBackPressed();
                }
            });
        } catch (Exception e) {

        }
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
        webView.setWebViewClient(new BrowserGetYearDetail());
        layout.addView(webView, 0, 0);
    }

    private void startLoadingContent(String url) {
        try {
            webView.stopLoading();
        } catch (Exception e) {
        }

        webView.loadUrl(url);

        Methods.lockOrUnlockOrientation(getActivity2());
    }

    private void populateFilds(/*Senador senador*/List<Dados> dados) {
        Session.createLog(this.getClass().getName(), "populateFilds()", null);

        initSenadorData();

        initListView(dados);
        pa.showContent();
    }

    public void initSenadorData() {
        TextView nome = (TextView) thisView.findViewById(R.id.textView_senador_nome);
        nome.setText(currentSenador.nome);
        TextView nasc = (TextView) thisView.findViewById(R.id.textView_senador_nascimento);
        nasc.setText(currentSenador.nascimento);
        TextView nat = (TextView) thisView.findViewById(R.id.textView_senador_naturalidade);
        nat.setText(currentSenador.naturalidade);
        TextView gabinete = (TextView) thisView.findViewById(R.id.textView_senador_gabinete);
        gabinete.setText(currentSenador.gabinete);
        TextView telefone = (TextView) thisView.findViewById(R.id.textView_senador_telefone);
        telefone.setText(currentSenador.telefones);
        TextView fax = (TextView) thisView.findViewById(R.id.textView_senador_fax);
        fax.setText(currentSenador.fax);
        TextView email = (TextView) thisView.findViewById(R.id.textView_senador_email);
        email.setText(currentSenador.email);

        TextView f_nome = (TextView) thisView.findViewById(R.id.floating_nome_senador);
        f_nome.setText(currentSenador.nome);
        TextView f_partido = (TextView) thisView.findViewById(R.id.floating_partido_senador);
        f_partido.setText(currentSenador.partido);
        TextView f_ano = (TextView) thisView.findViewById(R.id.floating_ano_senador);
        f_ano.setText(currentSenador.currentAno);

        if (currentSenador.foto_url != null && !currentSenador.foto_url.equals(""))
            if (currentSenador.foto_url.endsWith("jpg") || currentSenador.foto_url.endsWith("png"))
                new DownloadImageTask((MLRoundedImageView) thisView.findViewById(R.id.imageview_senador_redondo))
                        .execute(currentSenador.foto_url);

        if (currentSenador.bitmapFoto == null) {
            if (currentSenador.foto_url != null && !currentSenador.foto_url.equals(""))
                if (currentSenador.foto_url.endsWith("jpg") || currentSenador.foto_url.endsWith("png"))
                    new DownloadImageTask((MLRoundedImageView) thisView.findViewById(R.id.imageview_senador_redondo))
                            .execute(currentSenador.foto_url);
        } else {
            ((MLRoundedImageView) thisView.findViewById(R.id.imageview_senador_redondo)).setImageBitmap(currentSenador.bitmapFoto);
        }
    }

    public class BrowserGetYearDetail extends WebViewClient {
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
            if (Session.DEBUG) {
                Session.createLog(this.getClass().getName(), "shouldOverrideUrlLoading(): view.getUrl(): " + view.getUrl(), null);
                Session.createLog(this.getClass().getName(), "shouldOverrideUrlLoading(): failingUrl: " + failingUrl, null);
            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //Toast.makeText(getActivity2(), "Carregando: " + url, Toast.LENGTH_SHORT).show();
            Session.createLog(this.getClass().getName(), "onPageStarted(): url: " + url, null);
            loadingFinished = false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Session.createLog(this.getClass().getName(), "onPageStarted(): url: " + url, null);

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
                    Session.createLog(this.getClass().getName(), "onPageFinished(): Exception", e);
                }
            } else {
                redirect = false;
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void injectJavaScriptFileByEvaluateJavascript(String scriptToRun) {
        Session.createLog(this.getClass().getName(), "injectJavaScriptFileByEvaluateJavascript(): INSERINDO JS", null);

        try {
            if (webView == null) {
                initWebViews(thisView);

                if (checarConexao()) {
                    startLoadingContent(MAIN_URL);
                    pa.showLoading();
                }
                return;
            }

            webView.evaluateJavascript("(function() {" +
                    "var parent = document.getElementsByTagName('head').item(0);" +
                    "if(parent != null){" +
                    "var script = document.createElement('script');" +
                    "script.type = 'text/javascript';" +
                    //"script.src = '" + scriptJQuery + "';" +
                    "script.innerHTML = decodeURIComponent(escape(window.atob('" + Session.script_jQuery_loaded + "')));" +
                    "parent.appendChild(script);" +

                    "script = document.createElement('script');" +
                    "script.type = 'text/javascript';" +
                    //"script.src = '" + scriptCanvgRgb + "';" +
                    "script.innerHTML = decodeURIComponent(escape(window.atob('" + Session.script_canvgRGB_loaded + "')));" +
                    "parent.appendChild(script);" +

                    "script = document.createElement('script');" +
                    "script.innerHTML = decodeURIComponent(escape(window.atob('" + Session.script_canvg_loaded + "')));" +
                    "script.type = 'text/javascript';" +
                    //"script.src = '" + scriptCanvg + "';" +
                    "parent.appendChild(script);" +

                    "script = document.createElement('script');" +
                    "script.type = 'text/javascript';" +
                    "script.innerHTML = decodeURIComponent(escape(window.atob('" + scriptLoaded + "')));" +
                    "parent.appendChild(script);" +
                    "return " + scriptToRun + ";}})();", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {

                    Session.createLog(this.getClass().getName(), "onReceiveValue(): value -->>: " + value, null);

                    Methods.lockOrUnlockOrientation(getActivity2());

                    if (value != null && !value.equals("null")) {
                        String formatedValue = value.trim().replace("\\", "");
                        formatedValue = formatedValue.substring(1, formatedValue.length() - 1);

                        Session.createLog(this.getClass().getName(), "onReceiveValue() CONVERTIDO -->>: " + formatedValue, null);

                        final ArrayList<Dados> dados = JSONConversor.converterStringToYearDetail(formatedValue);

                        if (dados.size() > 0) {
                            getActivity2().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //ACTION AFTER ALL DONE
                                    populateFilds(dados);
                                }
                            });
                        } else {
                            onErroWhileGettingData("Erro (id3) ao converter dados", "O app não conseguiu converter os dados obtidos no Portal da Transparência. Contate o suporte do app.");

                        }
                    } else {
                        onErroWhileGettingData("Erro (id4) ao obter dados", "O Portal da Transparência não respondeu como deveria. Tente novamente ou contate o suporte do app.");

                    }
                }
            });
        } catch (Exception e) {
            Session.createLog(this.getClass().getName(), "injectJavaScriptFileByEvaluateJavascript() Exception", e);
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
        Session.createLog(this.getClass().getName(), "getJSONfromWebView()", null);
        //tv.setText(str);
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
