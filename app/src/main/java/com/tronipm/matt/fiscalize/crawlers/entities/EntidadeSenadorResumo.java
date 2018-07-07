package com.tronipm.matt.fiscalize.crawlers.entities;

import java.io.Serializable;

/**
 * Created by PMateus on 07/07/2018.
 * For project Fiscalize.
 * Contact: <paulomatew@gmail.com>
 */
public class EntidadeSenadorResumo implements Serializable {

    public String titulo = null;
    public EntidadeSenadorTabelaResumo tabela = null;
    public String imagem = null;

    @Override
    public String toString() {
        String a = "";
        a += "titulo: " + titulo + "\r\n";
        a += "imagem: " + imagem + "\r\n";
        if (tabela != null) {
            a += "tabela: \r\n" + tabela.toString() + "\r\n";
        } else {
            a += "tabela: null" + "\r\n";
        }

        return a;
    }
}
