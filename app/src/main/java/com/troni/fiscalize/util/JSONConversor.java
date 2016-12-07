package com.troni.fiscalize.util;

import android.util.Log;

import com.troni.fiscalize.people.senador.Ano;
import com.troni.fiscalize.people.senador.Dados;
import com.troni.fiscalize.people.senador.Nota;
import com.troni.fiscalize.people.senador.Senador;
import com.troni.fiscalize.session.Session;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JSONConversor {
    public static Senador converterStringToSenador(String str) {
        JSONObject jObj = null;
        Senador senador = new Senador();
        str = str.trim();

        if (!str.equals("")) {
            try {
                jObj = new JSONObject(str);

                //GET main data
                senador.nome = jObj.getString("nome");
                senador.nascimento = jObj.getString("nascimento");
                senador.naturalidade = jObj.getString("naturalidade");
                senador.gabinete = jObj.getString("gabinete");
                senador.telefones = jObj.getString("telefones");
                senador.fax = jObj.getString("fax");
                senador.email = jObj.getString("email");
                senador.foto_url = jObj.getString("foto_url");
                senador.partido = jObj.getString("partido");
                senador.currentAno = jObj.getString("currentAno");
                senador.link = jObj.getString("link");

                //Getting anos
                for (int i = 0; i < jObj.getJSONArray("anos").length(); i++) {
                    JSONObject jAno = ((JSONObject) jObj.getJSONArray("anos").get(i));

                    String label = jAno.getString("label");
                    String link = jAno.getString("link");
                    senador.addAno(new Ano(label, link));
                }
                //Getting titulos
                senador.addTitulo(jObj.getString("titulo_ceap"));
                senador.addTitulo(jObj.getString("titulo_outros"));
                senador.addTitulo(jObj.getString("titulo_beneficios"));
                senador.addTitulo(jObj.getString("titulo_pessoal"));
                senador.addTitulo(jObj.getString("titulo_subsidio"));

                //Getting data
                for (int i = 0; i < jObj.getJSONArray("dados1").length(); i++) {
                    JSONObject dados = ((JSONObject) jObj.getJSONArray("dados1").get(i));

                    String label = dados.getString("label");
                    String valor = dados.getString("valor");
                    //Faço isso apenas no link pq ele pode ser NULL
                    String link = (dados.isNull("link")) ? "" : dados.getString("link");
                    if (!link.isEmpty() && !link.startsWith("http")) {
                        link = senador.link.split("\\?")[0] + link;
                    }
                    senador.addDados(1, new Dados(label, valor, link));
                }
                for (int i = 0; i < jObj.getJSONArray("dados2").length(); i++) {
                    JSONObject dados = ((JSONObject) jObj.getJSONArray("dados2").get(i));

                    String label = dados.getString("label");
                    String valor = dados.getString("valor");
                    String link = (dados.isNull("link")) ? "" : dados.getString("link");
                    if (!link.isEmpty() && !link.startsWith("http")) {
                        link = senador.link.split("\\?")[0] + link;
                    }
                    senador.addDados(2, new Dados(label, valor, link));
                }
                for (int i = 0; i < jObj.getJSONArray("dados3").length(); i++) {
                    JSONObject dados = ((JSONObject) jObj.getJSONArray("dados3").get(i));

                    String label = dados.getString("label");
                    String valor = dados.getString("valor");
                    String link = (dados.isNull("link")) ? "" : dados.getString("link");
                    if (!link.isEmpty() && !link.startsWith("http")) {
                        link = senador.link.split("\\?")[0] + link;
                    }
                    senador.addDados(3, new Dados(label, valor, link));
                }
                for (int i = 0; i < jObj.getJSONArray("dados4").length(); i++) {
                    JSONObject dados = ((JSONObject) jObj.getJSONArray("dados4").get(i));

                    String label = dados.getString("label");
                    String valor = dados.getString("valor");
                    String link = (dados.isNull("link")) ? "" : dados.getString("link");
                    if (!link.isEmpty() && !link.startsWith("http")) {
                        link = senador.link.split("\\?")[0] + link;
                    }
                    senador.addDados(4, new Dados(label, valor, link));
                }
                for (int i = 0; i < jObj.getJSONArray("dados5").length(); i++) {
                    JSONObject dados = ((JSONObject) jObj.getJSONArray("dados5").get(i));

                    String label = dados.getString("label");
                    String valor = dados.getString("valor");
                    String link = (dados.isNull("link")) ? "" : dados.getString("link");
                    if (!link.isEmpty() && !link.startsWith("http")) {
                        link = senador.link.split("\\?")[0] + link;
                    }
                    senador.addDados(5, new Dados(label, valor, link));
                }

            } catch (JSONException e) {
                Log.v(JSONConversor.class.getName(), "DEU ERRADO ALGO...", e);
                senador = null;
            }
        }

        return senador;
    }

    /**
     * O primeiro INDICE do array será label=imgbase64 e valor=titulo da despesa
     *
     * @param str
     * @return
     */
    public static ArrayList<Dados> converterStringToYearDetail(String str) {
        JSONObject jObj = null;
        ArrayList<Dados> dados = new ArrayList<Dados>();
        str = str.trim();


        if (!str.equals("")) {
            try {
                jObj = new JSONObject(str);
                Session.createLog(JSONConversor.class.getName(), jObj.getString("grafico"), null);
                Session.createLog(JSONConversor.class.getClass().getName(), jObj.getString("titulo"), null);

                dados.add(new Dados(jObj.getString("grafico"), jObj.getString("titulo"), null));

                String linkPage = (jObj.isNull("link")) ? "" : jObj.getString("link");


                //Getting anos
                for (int i = 0; i < jObj.getJSONArray("meses").length(); i++) {
                    JSONObject jMeses = ((JSONObject) jObj.getJSONArray("meses").get(i));

                    String label = jMeses.getString("label");
                    String valor = jMeses.getString("valor");
                    String link = jMeses.getString("link");

                    if (!link.isEmpty() && !link.startsWith("http")) {
                        link = linkPage.split("\\?")[0] + link;
                    }
                    dados.add(new Dados(label, valor, link));
                }


            } catch (JSONException e) {
                Log.v(JSONConversor.class.getName(), "DEU ERRADO ALGO...", e);
            }
        }

        return dados;
    }

    /**
     * O primeiro INDICE do array será ident=TITULO DA DESPESA
     *
     * @param str
     * @return
     */
    public static ArrayList<Nota> converterStringToMonthDetail(String str) {
        JSONObject jObj = null;
        ArrayList<Nota> dados = new ArrayList<Nota>();
        str = str.trim();


        if (!str.equals("")) {
            try {
                jObj = new JSONObject(str);
                Session.createLog(JSONConversor.class.getClass().getName(), jObj.getString("titulo"), null);
                Nota n = new Nota();
                n.identificacao = jObj.getString("titulo");
                dados.add(n);


                //Getting anos
                for (int i = 0; i < jObj.getJSONArray("lancamentos").length(); i++) {
                    JSONObject jLancamentos = ((JSONObject) jObj.getJSONArray("lancamentos").get(i));

                    String ident = jLancamentos.getString("ident");
                    String fornecedor = jLancamentos.getString("fornecedor");
                    String descricao = jLancamentos.getString("descricao");
                    String data = jLancamentos.getString("data");
                    String valor = jLancamentos.getString("valor");


                    dados.add(new Nota(ident, fornecedor, descricao, data, valor));
                }


            } catch (JSONException e) {
                Log.v(JSONConversor.class.getName(), "converterStringToMonthDetail() DEU ERRADO ALGO...", e);
            }
        }

        return dados;
    }
}
