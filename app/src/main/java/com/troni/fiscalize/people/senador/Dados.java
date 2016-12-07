package com.troni.fiscalize.people.senador;

public class Dados implements Cloneable {
    public String label = "", valor = "", link = "";

    public Dados(String label, String valor, String link) {
        this.label = label;
        this.valor = valor;
        this.link = link;
    }

    public Dados() {
    }


    @Override
    public Dados clone() {

        Dados dd = new Dados();
        dd.label = label;
        dd.valor = valor;
        dd.link = link;
        return dd;
    }
}