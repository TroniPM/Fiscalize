package com.tronipm.matt.fiscalize.crawlers.entities;

import java.io.Serializable;

public class EntidadeSenadorTabelaMesLinha implements Serializable {

    public String doc = null;
    public String fornecedor = null;
    public String descricao = null;
    public String data = null;
    public String valor = null;

    @Override
    public String toString() {
        String a = "\r\n";
        a += "    doc: " + doc + "\r\n";
        a += "    fornecedor: " + fornecedor + "\r\n";
        a += "    descricao: " + descricao + "\r\n";
        a += "    data: " + data + "\r\n";
        a += "    valor: " + valor + "\r\n";

        return a;
    }
}
