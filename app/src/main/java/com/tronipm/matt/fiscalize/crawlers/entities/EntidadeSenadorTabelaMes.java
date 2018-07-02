package com.tronipm.matt.fiscalize.crawlers.entities;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Matt
 */
public class EntidadeSenadorTabelaMes implements Serializable {

    public String titulo = null;
    public ArrayList<EntidadeSenadorTabelaMesLinha> linhas = new ArrayList<>();

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
        for (EntidadeSenadorTabelaMesLinha in : linhas) {
            a += in.toString();
        }
        return a;
    }
}
