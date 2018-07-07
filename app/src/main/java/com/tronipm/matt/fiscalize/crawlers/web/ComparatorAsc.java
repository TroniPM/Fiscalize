package com.tronipm.matt.fiscalize.crawlers.web;

import java.util.Comparator;

/**
 * Created by PMateus on 07/07/2018.
 * For project Fiscalize.
 * Contact: <paulomatew@gmail.com>
 */
public class ComparatorAsc implements Comparator<HTMLObject> {

    @Override
    public int compare(HTMLObject o1, HTMLObject o2) {
        return (o1.getLine_start() < o2.getLine_start()) ? -1 : ((o1.getLine_start() > o2.getLine_start()) ? 1 : 0);
    }
}
