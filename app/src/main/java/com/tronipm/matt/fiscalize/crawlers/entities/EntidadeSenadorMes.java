package com.tronipm.matt.fiscalize.crawlers.entities;

public class EntidadeSenadorMes {

    public String titulo = null;
    public EntidadeSenadorTabelaMes tabela = null;

    @Override
    public String toString() {
        String a = "";
        a += "titulo: " + titulo + "\r\n";
        if (tabela != null) {
            a += "tabela: \r\n" + tabela.toString() + "\r\n";
        } else {
            a += "tabela: null" + "\r\n";
        }

        return a;
    }
}
