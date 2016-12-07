package com.troni.fiscalize.people.senador;

/**
 * Created by Mateus on 14/10/2016.
 */

public class Nota {
    public String identificacao = "", fornecedor = "", descricao = "", data = "", valor = "";

    public Nota(String identificacao, String fornecedor, String descricao, String data, String valor) {
        this.identificacao = identificacao;
        this.fornecedor = fornecedor;
        this.descricao = descricao;
        this.data = data;
        this.valor = valor;
    }

    public Nota() {
    }
}
