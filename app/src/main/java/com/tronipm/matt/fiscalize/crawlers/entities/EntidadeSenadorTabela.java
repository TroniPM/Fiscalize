package com.tronipm.matt.fiscalize.crawlers.entities;

import java.util.ArrayList;

/**
 *
 * @author Matt
 */
public class EntidadeSenadorTabela {

    public String titulo = null;
    public ArrayList<EntidadeSenadorTabelaLinha> linhas = new ArrayList<>();

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
        for (EntidadeSenadorTabelaLinha in : linhas) {
            a += in.toString();
        }
        return a;
    }
}
