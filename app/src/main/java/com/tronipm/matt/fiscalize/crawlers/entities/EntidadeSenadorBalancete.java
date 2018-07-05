package com.tronipm.matt.fiscalize.crawlers.entities;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Matt
 */
public class EntidadeSenadorBalancete implements Serializable {

    public String ano = null;
    public String link = null;
    public ArrayList<EntidadeSenadorTabelaBalancete> tabela = null;

    public EntidadeSenadorBalancete() {
        tabela = new ArrayList<>();
    }

    @Override
    public String toString() {
        String a = "";
        a += "ano: " + ano + "\r\n";
        a += "link: " + link + "\r\n";
        if (tabela != null) {
            a += "tabela: \r\n" + tabela.toString() + "\r\n";
        } else {
            a += "tabela: null" + "\r\n";
        }

        return a;
    }

    public void add(EntidadeSenadorTabelaBalancete tab) {
        if (tabela == null) {
            tabela = new ArrayList<>();
        }

        boolean flag = true;
        for (int i = 0; i < tabela.size(); i++) {
            if (tabela.get(i).titulo != null
                    && tabela.get(i).titulo.equals(tab.titulo)) {
                tabela.set(i, tab);
                flag = false;
                break;
            }
        }
        if (flag) {
            tabela.add(tab);
        }
    }
}