package com.troni.fiscalize.people.senador;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Senador {
    public String nome = "", nascimento = "", naturalidade = "", gabinete = "", telefones = "";
    public String fax = "", email = "", foto_url = "", currentAno = "", partido = "", link = "";
    public int id = 0;//ID do SQLITE
    public String numero = "";//NUMERO DE ID NO PORTAL DA TRANSPARENCIA
    public ArrayList<String> titulos = new ArrayList<>();
    public ArrayList<Ano> anos = new ArrayList<>();
    public ArrayList<Dados> dados1 = new ArrayList<>();
    public ArrayList<Dados> dados2 = new ArrayList<>();
    public ArrayList<Dados> dados3 = new ArrayList<>();
    public ArrayList<Dados> dados4 = new ArrayList<>();
    public ArrayList<Dados> dados5 = new ArrayList<>();
    public Bitmap bitmapFoto = null;

    public Senador(String nome, String numero, int id) {
        this.nome = nome;
        this.numero = numero;
        this.id = id;
    }

    public Senador(String nome, String numero) {
        this.nome = nome;
        this.numero = numero;
    }

    public Senador() {
    }

    public void addTitulo(String str) {
        titulos.add(str);
    }

    public void addAno(String ano, String link) {
        anos.add(new Ano(ano, link));
    }

    public void addAno(Ano ano) {
        anos.add(ano);
    }

    public void addDados(int array, String label, String valor, String link) {
        switch (array) {
            case 1:
                dados1.add(new Dados(label, valor, link));
                break;
            case 2:
                dados2.add(new Dados(label, valor, link));
                break;
            case 3:
                dados3.add(new Dados(label, valor, link));
                break;
            case 4:
                dados4.add(new Dados(label, valor, link));
                break;
            case 5:
                dados5.add(new Dados(label, valor, link));
                break;
        }

    }

    public void addDados(int array, Dados dado) {
        switch (array) {
            case 1:
                dados1.add(dado);
                break;
            case 2:
                dados2.add(dado);
                break;
            case 3:
                dados3.add(dado);
                break;
            case 4:
                dados4.add(dado);
                break;
            case 5:
                dados5.add(dado);
                break;
        }
    }
}




