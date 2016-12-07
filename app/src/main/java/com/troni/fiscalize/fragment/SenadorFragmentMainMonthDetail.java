package com.troni.fiscalize.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.malinskiy.materialicons.IconDrawable;
import com.malinskiy.materialicons.Iconify;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.troni.fiscalize.R;
import com.troni.fiscalize.people.senador.Dados;
import com.troni.fiscalize.people.senador.ListAdapterMonth;
import com.troni.fiscalize.people.senador.ListAdapterYear;
import com.troni.fiscalize.people.senador.Nota;
import com.troni.fiscalize.session.Session;
import com.troni.fiscalize.util.JSONConversor;
import com.troni.fiscalize.util.MLRoundedImageView;
import com.troni.fiscalize.util.Methods;
import com.vlonjatg.progressactivity.ProgressActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static com.troni.fiscalize.session.Session.currentSenador;

public class SenadorFragmentMainMonthDetail extends Fragment {

    public static final String ARG_PARAM1 = "id";

    private int mParam1 = 0;
    private WebView webView;
    private String MAIN_URL = "";
    private final String scriptPath = "js/script_especifico.js";//assets/js/
    //private final String scriptJQuery = "https://code.jquery.com/jquery-3.1.1.min.js";
    //private final String scriptJQuery_locale = "js/jquery-3.1.1.min.js";
    private static String scriptLoaded = null;
    //private static String script_jQuery_Loaded = null;

    private boolean isShowing = false;

    private View thisView = null;

    private ProgressActivity pa;
    private boolean porcent2 = false, porcent1 = false;
    final Handler handlerChangeIcon = new Handler();
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

    public SenadorFragmentMainMonthDetail() {
        // Required empty public constructor
    }

    public static SenadorFragmentMainMonthDetail newInstance(String param1) {
        SenadorFragmentMainMonthDetail fragment = new SenadorFragmentMainMonthDetail();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Session.createLog(this.getClass().getName(), "onCreate()", null);
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
        Session.createLog(this.getClass().getName(), "onCreateView()", null);

        View v = inflater.inflate(R.layout.fragment_senador_month_detail, container, false);
        thisView = v;

        pa = (ProgressActivity) v.findViewById(R.id.progress_senador_month);

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

        if (checarConexao()) {
            startLoadingContent(MAIN_URL);
            pa.showLoading();
        }
        return v;
    }

    /**
     * O primeiro indice é traz OUTRAS informações. Então remover o índice antes de chamar listview
     * label=img base 64
     * valor=titulo da despesa
     *
     * @param dados
     */
    private void initListView(ArrayList<Nota> dados) {
        ListView yourListView = (ListView) thisView.findViewById(R.id.itemListView);

        TextView titulo = (TextView) thisView.findViewById(R.id.title_despesa_senador);
        titulo.setText(dados.get(0).identificacao);
        dados.remove(0);

        ListAdapterMonth customAdapter = new ListAdapterMonth(getActivity2(), R.layout.senador_year_detail_itemlistrow, dados, null);

        yourListView.setAdapter(customAdapter);

        customAdapter.notifyDataSetChanged();

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
        Session.createLog(this.getClass().getName(), "startLoadingContent() url:" + url, null);
        try {
            webView.stopLoading();
        } catch (Exception e) {
        }

        webView.loadUrl(url);

        Methods.lockOrUnlockOrientation(getActivity2());
    }

    private void populateFilds(/*Senador senador*/ArrayList<Nota> dados) {
        Session.createLog(this.getClass().getName(), "populateFilds()", null);

        //this.senador = senador;
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
                    Session.createLog(this.getClass().getName(), "onPageFinished() Exception", e);
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

                        final ArrayList<Nota> dados = JSONConversor.converterStringToMonthDetail(formatedValue);

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
            Session.createLog(this.getClass().getName(), "injectJavaScriptFileByEvaluateJavascript Exception", e);
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
        Session.createLog(this.getClass().getName(), "getJSONfromWebView() " + str, null);
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
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
