package com.tronipm.matt.fiscalize.crawlers.entities;


import java.io.Serializable;

/**
 * Created by PMateus on 07/07/2018.
 * For project Fiscalize.
 * Contact: <paulomatew@gmail.com>
 */
public class EntidadeSenadorTabelaResumoLinha implements Serializable {

    public String label = null;
    public String conteudo = null;
    public String link = null;
    public boolean openWeb = false;

    @Override
    public String toString() {
        String a = "";
        a += "\r\n    label: " + label;
        a += "\r\n    openWeb: " + openWeb;
        a += "\r\n        conteudo: " + conteudo;
        a += "\r\n        link: " + link + "\r\n";

        return a;
    }
}
