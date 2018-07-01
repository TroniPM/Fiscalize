package com.tronipm.matt.fiscalize.crawlers.entities;

public class EntidadeSenadorTabelaAnoLinha {

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
