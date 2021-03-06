package com.tronipm.matt.fiscalize.crawlers.web;

import java.util.Comparator;

/**
 * Created by PMateus on 07/07/2018.
 * For project Fiscalize.
 * Contact: <paulomatew@gmail.com>
 */
public class ComparatorAsc implements Comparator<HTMLObject> {

    @Override
    public int compare(HTMLObject obj1, HTMLObject obj2) {
        if (obj1 == obj2) {
            return 0;
        }
        if (obj1 == null) {
            return -1;
        }
        if (obj2 == null) {
            return 1;
        }
        return (obj1.getLine_start() < obj2.getLine_start()) ? -1 : ((obj1.getLine_start() > obj2.getLine_start()) ? 1 : 0);
    }
}
