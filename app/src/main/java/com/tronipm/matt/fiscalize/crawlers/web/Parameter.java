package com.tronipm.matt.fiscalize.crawlers.web;

/**
 * Created by PMateus on 07/07/2018.
 * For project Fiscalize.
 * Contact: <paulomatew@gmail.com>
 */
public class Parameter {

    private String id;
    private String value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Parameter(String id, String value) {
        super();
        this.id = id;
        this.value = value;
    }

    @Override
    public String toString() {
        return getId() + "=" + getValue();
    }
}
