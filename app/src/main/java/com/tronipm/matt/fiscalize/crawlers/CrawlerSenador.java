package com.tronipm.matt.fiscalize.crawlers;

import com.tronipm.matt.fiscalize.crawlers.entities.EntidadeSenadorBalancete;
import com.tronipm.matt.fiscalize.crawlers.entities.EntidadeSenadorTabelaBalancete;
import com.tronipm.matt.fiscalize.crawlers.entities.EntidadeSenadorTabelaBalanceteLinha;
import com.tronipm.matt.fiscalize.crawlers.entities.EntidadeSenadorResumo;
import com.tronipm.matt.fiscalize.crawlers.entities.EntidadeSenadorTabelaResumo;
import com.tronipm.matt.fiscalize.crawlers.entities.EntidadeSenadorTabelaResumoLinha;
import com.tronipm.matt.fiscalize.crawlers.entities.EntidadeSenadorDetalhe;
import com.tronipm.matt.fiscalize.crawlers.entities.EntidadeSenadorTabelaDetalhe;
import com.tronipm.matt.fiscalize.crawlers.entities.EntidadeSenadorTabelaDetalheLinha;
import com.tronipm.matt.fiscalize.entities.EntidadeSenador;
import com.tronipm.matt.fiscalize.crawlers.web.Browser;
import com.tronipm.matt.fiscalize.crawlers.web.HTMLObject;
import com.tronipm.matt.fiscalize.crawlers.web.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by PMateus on 07/07/2018.
 * For project Fiscalize.
 * Contact: <paulomatew@gmail.com>
 */
public class CrawlerSenador {

    private static final boolean DEBUG = false;

    private static final String PARAM_ID = "IDDD";
    private static final String PARAM_ANO = "ANOOO";
    private static final String SENADOR_LIST = "https://www25.senado.leg.br/web/transparencia/sen/";
    private static final String SENADOR_LINK1 = "http://www6g.senado.leg.br/transparencia/sen/" + PARAM_ID + "/";
    private static final String SENADOR_LINK2 = "http://www6g.senado.leg.br/transparencia/sen/" + PARAM_ID + "/?ano=" + PARAM_ANO;

//    public static void main(String[] args) {
//        CrawlerSenador c = new CrawlerSenador();
//    //Pegar lista de senadores (id, nome, partido)
//        try {
//            ArrayList<EntidadeSenador> senadores = c.conn_getListSenadores();
//            for (EntidadeSenador in : senadores) {
//                System.out.println(in);
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    pegar dados do senador
//        EntidadeSenador senador = c.conn_getSenador("4981");
//        EntidadeSenador senador = c.conn_getSenador("4981");
//        senador = c.conn_getSenadorAno(senador, senador.getConteudoBalancete().get(0).tabela.get(0).linhas.get(0).link);
//        senador = c.conn_getSenadorMes(senador, senador.getConteudoResumo().get(0).tabela.linhas.get(0).link);
//        System.out.println(senador);
//        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
//
//        String ano = senador.getAnosDisponiveis().get(senador.getAnosDisponiveis().size() - 2);
//        senador = c.conn_getSenador(senador, ano);
//        senador = c.conn_getSenadorAno(senador, senador.getConteudoBalancete().get(1).tabela.get(0).linhas.get(0).link);
//        senador = c.conn_getSenadorMes(senador, senador.getConteudoResumo().get(1).tabela.linhas.get(0).link);
//
//        System.out.println(senador);
//    }

    public EntidadeSenador conn_getSenadorAno(EntidadeSenador senador, String url) {
        if (url == null || url.trim().isEmpty()) {
            return senador;
        }
        HTMLObject pagina;
        Browser b = new Browser();
        if (DEBUG) {
//            pagina = HTMLObject.parse(Util.carregarAno());
        } else {
            String html = b.get(url);
            pagina = HTMLObject.parse(html);
//            Util.escreverEmArquivo("./senadorano.txt", html, false);
        }

        try {
            senador = getTabelaAno(url, senador, pagina);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return senador;
    }

    public EntidadeSenador conn_getSenadorMes(EntidadeSenador senador, String url) {
        if (url == null || url.trim().isEmpty()) {
            return senador;
        }
        HTMLObject pagina;
        Browser b = new Browser();
        if (DEBUG) {
//            pagina = HTMLObject.parse(Util.carregarMes());
        } else {
            String html = b.get(url);
            pagina = HTMLObject.parse(html);
//            Util.escreverEmArquivo("./senadormes.txt", html, false);
        }

        try {
            senador = getTabelaMes(url, senador, pagina);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return senador;
    }

    public EntidadeSenador conn_getSenador(EntidadeSenador senador, String ano) {
        if (senador.getId() == null || senador.getId().trim().isEmpty()) {
            return null;
        }
//        EntidadeSenador senador = new EntidadeSenador();
//        senador.setId(id);

        Date date = new Date();
        HTMLObject pagina;

        Browser b = new Browser();

        String url;
        if (ano == null) {
            url = SENADOR_LINK1.replace(PARAM_ID, senador.getId());
        } else {
            url = SENADOR_LINK2.replace(PARAM_ID, senador.getId()).replace(PARAM_ANO, ano);
        }
        senador.setLinkPortalTransparencia(url);

        if (DEBUG) {
//            pagina = HTMLObject.parse(Util.carregar());
        } else {
            String html = b.get(url);
            pagina = HTMLObject.parse(html);
//            Util.escreverEmArquivo("./senador.txt", html, false);
        }
        try {
            senador = getElementoDadosPessoais(senador, pagina);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            senador = getElementoPartido(senador, pagina);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            senador = getElementoLinks(senador, pagina);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            senador = getElementoAnos(senador, pagina);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            senador = getElementoFoto(senador, pagina);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        EntidadeSenadorBalancete balancete = new EntidadeSenadorBalancete();
        balancete.link = url;
        balancete.date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(date);
        balancete.ano = ano != null ? ano
                : senador.getAnosDisponiveis().get(senador.getAnosDisponiveis().size() - 1);
        senador.add(balancete);

        try {
            EntidadeSenadorTabelaBalancete tab = getTabelaCeaps(senador, pagina);
            balancete.add(tab);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            EntidadeSenadorTabelaBalancete tab = getTabelaOutrosGastos(senador, pagina);
            balancete.add(tab);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            EntidadeSenadorTabelaBalancete tab = getTabelaBeneficios(senador, pagina);
            balancete.add(tab);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            EntidadeSenadorTabelaBalancete tab = getTabelaPessoal(senador, pagina);
            balancete.add(tab);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            EntidadeSenadorTabelaBalancete tab = getTabelaSubsidios(senador, pagina);
            balancete.add(tab);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return senador;
    }

    public EntidadeSenador conn_getSenador(EntidadeSenador senador) {
        return conn_getSenador(senador, null);
    }

    public EntidadeSenador conn_getSenador(String id) {
        EntidadeSenador senador = new EntidadeSenador();
        senador.setId(id);
        return conn_getSenador(senador, null);
    }

    public ArrayList<EntidadeSenador> conn_getListSenadores() throws Exception {
        ArrayList<EntidadeSenador> arr = new ArrayList<>();
        Browser b = new Browser();
        String html = b.get(SENADOR_LIST);
        HTMLObject pagina = HTMLObject.parse(html);
        HTMLObject select = pagina.getObjectsByTag("select").get(0);
        for (HTMLObject in : select.getChildrens()) {
            String value = in.getAttribute("value");
            if (value == null || value.trim().isEmpty()) {
                continue;
            }
            EntidadeSenador s = new EntidadeSenador();
            s.setId(value);
            String aux = space(in.getHtmlSourceAsText());
            try {
                s.setNomeCivil(aux.split("-")[0].trim());
                s.setPartido(aux.split("-")[1].trim());
            } catch (Exception e) {
                s.setNomeCivil(aux.trim());
            }
            arr.add(s);
        }

        return arr;
    }

    private EntidadeSenadorTabelaBalancete getTabelaCeaps(EntidadeSenador senador, HTMLObject pagina) {
        HTMLObject dados = pagina.getObjectsById("accordion-ceaps").get(0).getChildrens().get(0);
        EntidadeSenadorTabelaBalancete tabela = new EntidadeSenadorTabelaBalancete();
        try {
            tabela.titulo = space(dados.getChildrens().get(0).getHtmlSourceAsText());
        } catch (Exception e) {
        }
        //linhas
        try {
            dados = pagina.getObjectsById("collapse-ceaps").get(0).getChildrens().get(0).getChildrens().get(0);
            dados = dados.getChildrens().get(2);
            for (HTMLObject in : dados.getChildrens()) {
                if (in.getTag().equals("tr")) {
                    EntidadeSenadorTabelaBalanceteLinha linha = new EntidadeSenadorTabelaBalanceteLinha();
                    String titulo = space(in.getChildrens().get(0).getHtmlSourceAsText());
                    String conteudo = space(in.getChildrens().get(1).getHtmlSourceAsText());

                    linha.label = titulo;
                    linha.conteudo = conteudo;

                    try {
                        String link = space(in.getChildrens().get(1).getChildrens().get(0).getAttribute("href"));
                        if (link != null) {
                            linha.link = (link.contains("http") ? "" : senador.getLinkPortalTransparencia()) + link;
                        }

                    } catch (Exception e) {
                    }

                    tabela.linhas.add(linha);
                }
            }
        } catch (Exception e) {
        }
        //totais
        try {
            dados = pagina.getObjectsById("collapse-ceaps").get(0).getChildrens().get(0).getChildrens().get(0);
            dados = dados.getChildrens().get(3);

            EntidadeSenadorTabelaBalanceteLinha linha = new EntidadeSenadorTabelaBalanceteLinha();
            String titulo = space(dados.getChildrens().get(0).getChildrens().get(0).getHtmlSourceAsText());
            String conteudo = space(dados.getChildrens().get(0).getChildrens().get(1).getHtmlSourceAsText());
            linha.label = titulo;
            linha.conteudo = conteudo;
            tabela.linhas.add(linha);
        } catch (Exception e) {
        }
        if (tabela.canSave()) {
            return tabela;
        }
        return null;
    }

    private EntidadeSenadorTabelaBalancete getTabelaOutrosGastos(EntidadeSenador senador, HTMLObject pagina) {
        HTMLObject dados = pagina.getObjectsById("accordion-outros-gastos").get(0).getChildrens().get(0);
        EntidadeSenadorTabelaBalancete tabela = new EntidadeSenadorTabelaBalancete();
        try {
            tabela.titulo = space(dados.getChildrens().get(0).getHtmlSourceAsText());
        } catch (Exception e) {
        }
        //linhas
        try {
            dados = pagina.getObjectsById("collapse-outros-gastos").get(0).getChildrens().get(0).getChildrens().get(0);
            dados = dados.getChildrens().get(2);
            for (HTMLObject in : dados.getChildrens()) {
                if (in.getTag().equals("tr")) {
                    EntidadeSenadorTabelaBalanceteLinha linha = new EntidadeSenadorTabelaBalanceteLinha();
                    String titulo = space(in.getChildrens().get(0).getHtmlSourceAsText());
                    String conteudo = space(in.getChildrens().get(1).getHtmlSourceAsText());
                    linha.label = titulo;
                    linha.conteudo = conteudo;

                    try {
                        String link = space(in.getChildrens().get(1).getChildrens().get(0).getAttribute("href"));
                        if (link != null) {
                            linha.link = (link.contains("http") ? "" : senador.getLinkPortalTransparencia()) + link;
                        }
                    } catch (Exception e) {
                    }

                    tabela.linhas.add(linha);
                }
            }
        } catch (Exception e) {
        }

        //totais
        try {
            dados = pagina.getObjectsById("collapse-outros-gastos").get(0).getChildrens().get(0).getChildrens().get(0);
            dados = dados.getChildrens().get(3);

            EntidadeSenadorTabelaBalanceteLinha linha = new EntidadeSenadorTabelaBalanceteLinha();
            String titulo = space(dados.getChildrens().get(0).getChildrens().get(0).getHtmlSourceAsText());
            String conteudo = space(dados.getChildrens().get(0).getChildrens().get(1).getHtmlSourceAsText());
            linha.label = titulo;
            linha.conteudo = conteudo;
            tabela.linhas.add(linha);
        } catch (Exception e) {
        }
        if (tabela.canSave()) {
            return tabela;
        }
        return null;
    }

    private EntidadeSenadorTabelaBalancete getTabelaBeneficios(EntidadeSenador senador, HTMLObject pagina) {
        HTMLObject dados = pagina.getObjectsById("accordion-outros").get(0).getChildrens().get(0);
        EntidadeSenadorTabelaBalancete tabela = new EntidadeSenadorTabelaBalancete();
        try {
            tabela.titulo = space(dados.getChildrens().get(0).getHtmlSourceAsText());
        } catch (Exception e) {
        }
        //linhas
        try {
            dados = pagina.getObjectsById("collapse-outros").get(0).getChildrens().get(0).getChildrens().get(0);
            dados = dados.getChildrens().get(2);
            for (HTMLObject in : dados.getChildrens()) {
                if (in.getTag().equals("tr")) {
                    EntidadeSenadorTabelaBalanceteLinha linha = new EntidadeSenadorTabelaBalanceteLinha();
                    String titulo = space(in.getChildrens().get(0).getHtmlSourceAsText());
                    String conteudo = space(in.getChildrens().get(1).getHtmlSourceAsText());
                    linha.label = titulo;
                    linha.conteudo = conteudo;
                    try {
                        String link = space(in.getChildrens().get(1).getChildrens().get(0).getAttribute("href"));
                        if (link != null) {
                            linha.link = (link.contains("http") ? "" : senador.getLinkPortalTransparencia()) + link;
                        }
                    } catch (Exception e) {
                    }

                    tabela.linhas.add(linha);
                }
            }
        } catch (Exception e) {
        }

        if (tabela.canSave()) {
            return tabela;
        }
        return null;
    }

    private EntidadeSenadorTabelaBalancete getTabelaPessoal(EntidadeSenador senador, HTMLObject pagina) {
        HTMLObject dados = pagina.getObjectsById("accordion-pessoal").get(0).getChildrens().get(0);
        EntidadeSenadorTabelaBalancete tabela = new EntidadeSenadorTabelaBalancete();
        try {
            tabela.titulo = space(dados.getChildrens().get(0).getHtmlSourceAsText());
        } catch (Exception e) {
        }
        //linhas
        try {
            dados = pagina.getObjectsById("collapse-pessoal").get(0).getChildrens().get(0).getChildrens().get(0);
            dados = dados.getChildrens().get(2);
            for (HTMLObject in : dados.getChildrens()) {
                if (in.getTag().equals("tr")) {
                    EntidadeSenadorTabelaBalanceteLinha linha = new EntidadeSenadorTabelaBalanceteLinha();
                    String titulo = space(in.getChildrens().get(0).getHtmlSourceAsText());
                    String conteudo = space(in.getChildrens().get(1).getHtmlSourceAsText());
                    linha.label = titulo;
                    linha.conteudo = conteudo;
                    try {
                        String link = space(in.getChildrens().get(1).getChildrens().get(0).getAttribute("href"));
                        if (link != null) {
                            linha.link = (link.contains("http") ? "" : senador.getLinkPortalTransparencia()) + link;
                        }
                    } catch (Exception e) {
                    }

                    tabela.linhas.add(linha);
                }
            }
        } catch (Exception e) {
        }

        if (tabela.canSave()) {
            return tabela;
        }
        return null;
    }

    private EntidadeSenadorTabelaBalancete getTabelaSubsidios(EntidadeSenador senador, HTMLObject pagina) {
        HTMLObject dados = pagina.getObjectsById("accordion-subsidios").get(0).getChildrens().get(0);
        EntidadeSenadorTabelaBalancete tabela = new EntidadeSenadorTabelaBalancete();
        try {
            tabela.titulo = space(dados.getChildrens().get(0).getHtmlSourceAsText());
        } catch (Exception e) {
        }
        //linhas
        try {
            dados = pagina.getObjectsById("collapse-subsidios").get(0).getChildrens().get(0).getChildrens().get(0);
            dados = dados.getChildrens().get(2);
            for (HTMLObject in : dados.getChildrens()) {
                if (in.getTag().equals("tr")) {
                    EntidadeSenadorTabelaBalanceteLinha linha = new EntidadeSenadorTabelaBalanceteLinha();
                    String titulo = space(in.getChildrens().get(0).getHtmlSourceAsText());
                    String conteudo = space(in.getChildrens().get(1).getHtmlSourceAsText());
                    linha.label = titulo;
                    linha.conteudo = conteudo;
                    try {
                        String link = space(in.getChildrens().get(1).getChildrens().get(0).getAttribute("href"));
                        if (link != null) {
                            linha.link = (link.contains("http") ? "" : senador.getLinkPortalTransparencia()) + link;
                        }
                    } catch (Exception e) {
                    }

                    tabela.linhas.add(linha);
                }
            }
        } catch (Exception e) {
        }

        if (tabela.canSave()) {
            return tabela;
        }
        return null;
    }

    private EntidadeSenador getElementoDadosPessoais(EntidadeSenador senador, HTMLObject pagina) throws Exception {
        HTMLObject dados = pagina.getObjectsByClass("dadosPessoais").get(0).getChildrens().get(1);
        for (int i = 0; i < dados.getChildrens().size(); i++) {
            HTMLObject in = dados.getChildrens().get(i);
            String content = space(in.getHtmlSourceAsText()).toLowerCase();
            if (in.getTag().contains("dt")
                    && content.contains("nome")) {
                senador.setNomeCivil(space(dados.getChildrens().get(i + 1).getHtmlSourceAsText()));
            } else if (in.getTag().contains("dt")
                    && content.contains("data")
                    && content.contains("nascimento")) {
                senador.setDataNascimento(space(dados.getChildrens().get(i + 1).getHtmlSourceAsText()));
            } else if (in.getTag().contains("dt")
                    && content.contains("naturalidade")) {
                senador.setNaturalidade(space(dados.getChildrens().get(i + 1).getHtmlSourceAsText()));
            } else if (in.getTag().contains("dt")
                    && content.contains("gabinete")) {
                senador.setGabinete(space(dados.getChildrens().get(i + 1).getHtmlSourceAsText()));
            } else if (in.getTag().contains("dt")
                    && content.contains("telefone")) {
                senador.setTelefones(space(dados.getChildrens().get(i + 1).getHtmlSourceAsText()));
            } else if (in.getTag().contains("dt")
                    && content.contains("fax")) {
                senador.setFax(space(dados.getChildrens().get(i + 1).getHtmlSourceAsText()));
            } else if (in.getTag().contains("dt")
                    && (content.contains("e-mail") || content.contains("email"))) {
                senador.setEmail(space(dados.getChildrens().get(i + 1).getHtmlSourceAsText()));
            } else if (in.getTag().contains("dt")
                    && content.contains("site")
                    && content.contains("pessoal")) {
                HTMLObject site = dados.getChildrens().get(i + 1);
                senador.setSitePessoal(site.getAttribute("href"));
            } else if (in.getTag().contains("dt")
                    && content.contains("escrit")
                    && content.contains("apoio")) {
                senador.setEscritorioApoio(space(dados.getChildrens().get(i + 1).getHtmlSourceAsText()));
            }
        }

        return senador;
    }

    private EntidadeSenador getElementoLinks(EntidadeSenador senador, HTMLObject pagina) throws Exception {
        ArrayList<HTMLObject> dadosArr = pagina.getObjectsByClass("botaoNavegacao").get(0).getChildrens();
        for (int i = 0; i < dadosArr.size(); i++) {
            HTMLObject in = dadosArr.get(i);
            String content = space(in.getHtmlSourceAsText()).toLowerCase();
            if (content.contains("proposições")) {
                senador.setLinkProposicoes(in.getAttribute("href"));
            } else if (content.contains("pronunciamentos")) {
                senador.setLinkPronunciamentos(in.getAttribute("href"));
            } else if (content.contains("matérias")
                    && content.contains("relatadas")) {
                senador.setLinkMateriasRelatadas(in.getAttribute("href"));
            } else if (content.contains("votações")) {
                senador.setLinkVotacoes(in.getAttribute("href"));
            } else if (content.contains("página")
                    && content.contains("institucional")) {
                senador.setLinkInstitucional(in.getAttribute("href"));
            }
        }
        return senador;
    }

    private EntidadeSenador getElementoAnos(EntidadeSenador senador, HTMLObject pagina) throws Exception {
        String nova = pagina.getObjectsById("conteudo_transparencia").get(0).getHtmlSourceAsHtml();
        HTMLObject nPagina = HTMLObject.parse(nova);
        nova = space(nPagina.getObjectsByClass("dropdown-menu").get(0).getHtmlSourceAsText());

        ArrayList<String> anosDisponiveis = new ArrayList<>();
        for (String in : nova.split(" ")) {
            anosDisponiveis.add(in);
        }
        senador.setAnosDisponiveis(anosDisponiveis);

        return senador;
    }

    private EntidadeSenador getElementoPartido(EntidadeSenador senador, HTMLObject pagina) throws Exception {
        String nova = pagina.getObjectsByTag("h1").get(0).getChildrens().get(3).getHtmlSourceAsText();
        senador.setPartido(space(nova));

        return senador;
    }

    private EntidadeSenador getElementoFoto(EntidadeSenador senador, HTMLObject pagina) {
        HTMLObject nova = pagina.getObjectsByClass("foto").get(0).getChildrens().get(0); //.getHtmlSourceAsText();//.get(0);//.getObjectsByTag("ul");//.getObjectsByClass("dropdown-menu");
        String link = nova.getAttribute("src");
        senador.setLinkFoto(link);

        return senador;
    }

    private String space(String txt) {
        if (txt == null) {
            return null;
        }
        return txt.replaceAll("&nbsp;", "").trim().replaceAll("\r\n+", " ").replaceAll("\t+", " ").replaceAll(" +", " ");
    }

    public CrawlerSenador() {
    }

    private EntidadeSenador getTabelaAno(String url, EntidadeSenador senador, HTMLObject pagina) {
        HTMLObject dados = pagina.getObjectsById("conteudo_transparencia").get(0);//.getChildrens().get(0);
        EntidadeSenadorResumo outter = new EntidadeSenadorResumo();
        outter.date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
        EntidadeSenadorTabelaResumo tabela = new EntidadeSenadorTabelaResumo();
        try {
            outter.titulo = space(dados.getChildrens().get(0).getHtmlSourceAsText());
            outter.link = url;
        } catch (Exception e) {
        }
        HTMLObject nPagina = HTMLObject.parse(dados.getHtmlSourceAsHtml());
        HTMLObject tabelaHTML = nPagina.getObjectsByTag("table").get(0).getChildrens().get(2);

        //Colocando um lixo na string para remover futuramente os parâmetros do fim
        String aux = replaceLast(url, "/", "/AABBCCDDEE");
        aux = aux.split("AABBCCDDEE")[0];

        for (HTMLObject in : tabelaHTML.getChildrens()) {
            if (in.getTag().equals("tr")) {
                EntidadeSenadorTabelaResumoLinha linha = new EntidadeSenadorTabelaResumoLinha();
                linha.label = space(in.getChildrens().get(0).getHtmlSourceAsText());
                linha.conteudo = space(in.getChildrens().get(1).getHtmlSourceAsText());
                try {
                    String link = space(in.getChildrens().get(1).getChildrens().get(0).getAttribute("href"));
                    if (link != null) {
                        linha.link = (link.contains("http") ? "" : aux) + link;
                    }
                } catch (Exception e) {
                }
                tabela.linhas.add(linha);
            }
        }
        if (tabela.canSave()) {
            outter.tabela = tabela;

            senador.add(outter);
        }
//        System.out.println(inner);
        return senador;
    }

    private String replaceLast(String string, String substring, String replacement) {
        int index = string.lastIndexOf(substring);
        if (index == -1) {
            return string;
        }
        return string.substring(0, index) + replacement
                + string.substring(index + substring.length());
    }

    private EntidadeSenador getTabelaMes(String url, EntidadeSenador senador, HTMLObject pagina) {
        HTMLObject dados = pagina.getObjectsById("conteudo_transparencia").get(0);//.getChildrens().get(0);
        EntidadeSenadorDetalhe inner = new EntidadeSenadorDetalhe();
        EntidadeSenadorTabelaDetalhe tabela = new EntidadeSenadorTabelaDetalhe();
        try {
            inner.titulo = space(dados.getChildrens().get(0).getHtmlSourceAsText());
        } catch (Exception e) {
        }
        HTMLObject nPagina = HTMLObject.parse(dados.getHtmlSourceAsHtml());
        HTMLObject tabelaHTML = nPagina.getObjectsByTag("table").get(0).getChildrens().get(2);

        for (HTMLObject in : tabelaHTML.getChildrens()) {
            if (in.getTag().equals("tr")) {
                EntidadeSenadorTabelaDetalheLinha linha = new EntidadeSenadorTabelaDetalheLinha();
                linha.doc = space(in.getChildrens().get(0).getHtmlSourceAsText());
                linha.fornecedor = space(in.getChildrens().get(1).getHtmlSourceAsText());
                linha.descricao = space(in.getChildrens().get(2).getHtmlSourceAsText());
                linha.data = space(in.getChildrens().get(3).getHtmlSourceAsText());
                linha.valor = space(in.getChildrens().get(4).getHtmlSourceAsText());

                tabela.linhas.add(linha);
            }
        }
        if (tabela.canSave()) {
            inner.tabela = tabela;

            senador.add(inner);
        }

        return senador;
    }
}