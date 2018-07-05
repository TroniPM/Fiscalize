package com.tronipm.matt.fiscalize.crawlers.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Util {
/*
    public static void escreverEmArquivo(String path, String content, boolean isAppend) {
        if (true) {
            return;
        }
        FileOutputStream fop = null;
        File file;
        try {
            file = new File(path);
            fop = new FileOutputStream(file, isAppend);
            if (!file.exists()) {
                file.createNewFile();
            }
            byte[] contentInBytes = content.getBytes();
            fop.write(contentInBytes);
            fop.flush();
            fop.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fop != null) {
                    fop.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String[] carregarArquivoRetornaString(String arquivo) {
        String[] linhas = null;
        try {
            FileInputStream fin = new FileInputStream(arquivo);
            byte[] a = new byte[fin.available()];
            fin.read(a);
            fin.close();
            linhas = new String(a).split("\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return linhas;
    }

    public static String carregar() {
        String linhas = null;
        try {
            FileInputStream fin = new FileInputStream("./senador.txt");
            byte[] a = new byte[fin.available()];
            fin.read(a);
            fin.close();
            linhas = new String(a);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return linhas;
    }

    public static String carregarAno() {
        String linhas = null;
        try {
            FileInputStream fin = new FileInputStream("./senadorano.txt");
            byte[] a = new byte[fin.available()];
            fin.read(a);
            fin.close();
            linhas = new String(a);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return linhas;
    }

    public static String carregarMes() {
        String linhas = null;
        try {
            FileInputStream fin = new FileInputStream("./senadormes.txt");
            byte[] a = new byte[fin.available()];
            fin.read(a);
            fin.close();
            linhas = new String(a);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return linhas;
    }*/
}