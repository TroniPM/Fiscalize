package com.tronipm.matt.fiscalize.utils;

import com.tronipm.matt.fiscalize.entities.EntidadeSenador;

import java.util.Comparator;

/**
 * Created by PMateus on 07/07/2018.
 * For project Fiscalize.
 * Contact: <paulomatew@gmail.com>
 */
public class ComparatorAscSenadores implements Comparator<EntidadeSenador> {

    @Override
    public int compare(EntidadeSenador obj1, EntidadeSenador obj2) {
        if (obj1 == obj2) {
            return 0;
        }
        if (obj1 == null) {
            return -1;
        }
        if (obj2 == null) {
            return 1;
        }
        return obj1.getNomeCivil().compareTo(obj2.getNomeCivil());
    }
}
