package com.tronipm.matt.fiscalize.crawlers.entities;


import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by PMateus on 07/07/2018.
 * For project Fiscalize.
 * Contact: <paulomatew@gmail.com>
 */
public class EntidadeSenadorTabelaResumo implements Serializable {

    public String titulo = null;
    public ArrayList<EntidadeSenadorTabelaResumoLinha> linhas = new ArrayList<>();

    public boolean canSave() {
        if ((titulo == null || titulo.trim().isEmpty()) && linhas.isEmpty()) {
            return false;
        }
        if (linhas == null || linhas.isEmpty()) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        String a = "";
        a += "titulo: " + titulo;
        for (EntidadeSenadorTabelaResumoLinha in : linhas) {
            a += in.toString();
        }
        return a;
    }
}

