package com.tronipm.matt.fiscalize.crawlers.entities;

import java.io.Serializable;

public class EntidadeSenadorTabelaAnoLinha implements Serializable {

    public String label = null;
    public String conteudo = null;
    public String link = null;

    @Override
    public String toString() {
        String a = "";
        a += "\r\n    label: " + label;
        a += "\r\n        conteudo: " + conteudo;
        a += "\r\n        link: " + link + "\r\n";

        return a;
    }
}
