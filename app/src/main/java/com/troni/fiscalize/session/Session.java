package com.troni.fiscalize.session;

import android.app.Activity;
import android.util.Log;

import com.troni.fiscalize.people.senador.Dados;
import com.troni.fiscalize.people.senador.Senador;

import java.util.ArrayList;
import java.util.List;

public class Session {
    public static boolean DEBUG = true;

    public static boolean canEnterMainScreen = false;

    public static Senador currentSenador = new Senador();//inicializo para evitar crash
    public static List<Dados> currentSenador_dados = new ArrayList<>();//inicializo para evitar crash

    public static Activity currentActivity = null;

    public static String script_jQuery_loaded = null;
    public static String script_canvgRGB_loaded = null;
    public static String script_canvg_loaded = null;
    public static final String scriptJQuery = "https://code.jquery.com/jquery-3.1.1.min.js";
    public static final String scriptCanvg = "http://canvg.github.io/canvg/canvg.js";
    public static final String scriptCanvgRgb = "http://canvg.github.io/canvg/rgbcolor.js";
    public static final String scriptJQuery_locale = "js/jquery-3.1.1.min.js";
    public static final String scriptCanvg_locale = "js/canvg.js";
    public static final String scriptCanvgRgb_locale = "js/rgbcolor.js";

    public static void createLog(String tag, String str, Exception e) {
        if (DEBUG)
            Log.v(tag, str, e);
    }
}
