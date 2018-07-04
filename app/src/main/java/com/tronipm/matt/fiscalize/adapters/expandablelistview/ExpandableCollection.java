package com.tronipm.matt.fiscalize.adapters.expandablelistview;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by PMateus on 16/09/2015.
 * For project SIG@Viewer.
 * Contact: <paulomatew@gmail.com>
 */
public class ExpandableCollection {

    public String nome = "";
    public String valor = "";
    public String link = "";
    public int tipo = 0;
    public static HashMap<String, ArrayList<ExpandableCollection>> expandable_hashmap;
    public static ArrayList<ExpandableCollection> expandable_main_arr = null;
    public static ArrayList<String> key_value = null;

    /*public ExpandableCollection(String title, String prof, String situacao, String faltas, ArrayList<String> notas, ArrayList<String> avaliacao, String valor) {

        this.nome = title;
        this.prof = (prof == null) ? "-" : prof;
        this.situacao = (situacao == null) ? "-" : situacao;
        this.faltas = (faltas == null) ? "-" : faltas;
        this.valor = (valor == null) ? "-" : valor;

        for (int i = 0; i < notas.size(); i++)
            this.notas.add((notas.get(i) == null) ? "-" : notas.get(i));

        for (int i = 0; i < avaliacao.size(); i++)
            this.avaliacao.add((avaliacao.get(i) == null) ? "-" : avaliacao.get(i));
    }*/

    public ExpandableCollection(String nome, String valor, String link) {
        this.nome = nome;
        this.valor = valor;
        this.link = link;
    }
}
