package com.tronipm.matt.fiscalize.entities;

import com.tronipm.matt.fiscalize.crawlers.entities.EntidadeSenadorAno;
import com.tronipm.matt.fiscalize.crawlers.entities.EntidadeSenadorMes;
import com.tronipm.matt.fiscalize.crawlers.entities.EntidadeSenadorTabela;

import java.io.Serializable;
import java.util.ArrayList;

public class EntidadeSenador implements Serializable {

    private String id = null;
    private String nomeCivil = null;
    private String partido = null;
    private String dataNascimento = null;
    private String naturalidade = null;
    private String gabinete = null;
    private String telefones = null;
    private String fax = null;
    private String email = null;
    private String sitePessoal = null;
    private String escritorioApoio = null;

    private String linkFoto = null;
    private String linkInstitucional = null;
    private String linkProposicoes = null;

    private String linkPronunciamentos = null;
    private String linkMateriasRelatadas = null;
    private String linkVotacoes = null;
    private String linkPortalTransparencia = null;

    private ArrayList<String> anosDisponiveis = null;
    private ArrayList<EntidadeSenadorTabela> tabelas = null;
    private ArrayList<EntidadeSenadorAno> conteudoAno = null;
    private ArrayList<EntidadeSenadorMes> conteudoMes = null;

    public void add(EntidadeSenadorTabela tabela) {
        if (tabelas == null) {
            tabelas = new ArrayList<>();
        }

        tabelas.add(tabela);
    }

    public void add(EntidadeSenadorAno ano) {
        if (conteudoAno == null) {
            conteudoAno = new ArrayList<>();
        }

        conteudoAno.add(ano);
    }

    public void add(EntidadeSenadorMes mes) {
        if (conteudoMes == null) {
            conteudoMes = new ArrayList<>();
        }

        conteudoMes.add(mes);
    }

    @Override
    public String toString() {
        String a = "";

        a += "id: " + id + "\r\n";
        a += "nomeCivil: " + nomeCivil + "\r\n";
        a += "partido: " + partido + "\r\n";
        a += "dataNascimento: " + dataNascimento + "\r\n";
        a += "naturalidade: " + naturalidade + "\r\n";
        a += "gabinete: " + gabinete + "\r\n";
        a += "telefones: " + telefones + "\r\n";
        a += "fax: " + fax + "\r\n";
        a += "email: " + email + "\r\n";
        a += "sitePessoal: " + sitePessoal + "\r\n";
        a += "escritorioApoio: " + escritorioApoio + "\r\n";
        a += "anosDisponiveis: ";
        if (anosDisponiveis != null) {
            for (String in : anosDisponiveis) {
                a += in + " ";
            }
        }
        a += "\r\n";
        a += "\r\n";
        a += "linkFoto: " + linkFoto + "\r\n";
        a += "linkInstitucional: " + linkInstitucional + "\r\n";
        a += "linkProposicoes: " + linkProposicoes + "\r\n";
        a += "linkPronunciamentos: " + linkPronunciamentos + "\r\n";
        a += "linkMateriasRelatadas: " + linkMateriasRelatadas + "\r\n";
        a += "linkVotacoes: " + linkVotacoes + "\r\n";
        a += "linkPortalTransparencia: " + linkPortalTransparencia + "\r\n";

        a += "ANO: ";
        if (conteudoAno != null) {
            a += "\r\n";
            for (EntidadeSenadorAno in : conteudoAno) {
                a += in.toString();
            }
        } else {
            a += "null";
        }

        a += "MES: ";
        if (conteudoMes != null) {
            a += "\r\n";
            for (EntidadeSenadorMes in : conteudoMes) {
                a += in.toString();
            }
        } else {
            a += "null";
        }

        return a;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNomeCivil() {
        return nomeCivil;
    }

    public void setNomeCivil(String nomeCivil) {
        this.nomeCivil = nomeCivil;
    }

    public String getPartido() {
        return partido;
    }

    public void setPartido(String partido) {
        this.partido = partido;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getNaturalidade() {
        return naturalidade;
    }

    public void setNaturalidade(String naturalidade) {
        this.naturalidade = naturalidade;
    }

    public String getGabinete() {
        return gabinete;
    }

    public void setGabinete(String gabinete) {
        this.gabinete = gabinete;
    }

    public String getTelefones() {
        return telefones;
    }

    public void setTelefones(String telefones) {
        this.telefones = telefones;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSitePessoal() {
        return sitePessoal;
    }

    public void setSitePessoal(String sitePessoal) {
        this.sitePessoal = sitePessoal;
    }

    public String getEscritorioApoio() {
        return escritorioApoio;
    }

    public void setEscritorioApoio(String escritorioApoio) {
        this.escritorioApoio = escritorioApoio;
    }

    public String getLinkFoto() {
        return linkFoto;
    }

    public void setLinkFoto(String linkFoto) {
        this.linkFoto = linkFoto;
    }

    public String getLinkInstitucional() {
        return linkInstitucional;
    }

    public void setLinkInstitucional(String linkInstitucional) {
        this.linkInstitucional = linkInstitucional;
    }

    public String getLinkProposicoes() {
        return linkProposicoes;
    }

    public void setLinkProposicoes(String linkProposicoes) {
        this.linkProposicoes = linkProposicoes;
    }

    public String getLinkPronunciamentos() {
        return linkPronunciamentos;
    }

    public void setLinkPronunciamentos(String linkPronunciamentos) {
        this.linkPronunciamentos = linkPronunciamentos;
    }

    public String getLinkMateriasRelatadas() {
        return linkMateriasRelatadas;
    }

    public void setLinkMateriasRelatadas(String linkMateriasRelatadas) {
        this.linkMateriasRelatadas = linkMateriasRelatadas;
    }

    public String getLinkVotacoes() {
        return linkVotacoes;
    }

    public void setLinkVotacoes(String linkVotacoes) {
        this.linkVotacoes = linkVotacoes;
    }

    public String getLinkPortalTransparencia() {
        return linkPortalTransparencia;
    }

    public void setLinkPortalTransparencia(String linkPortalTransparencia) {
        this.linkPortalTransparencia = linkPortalTransparencia;
    }

    public ArrayList<String> getAnosDisponiveis() {
        return anosDisponiveis;
    }

    public void setAnosDisponiveis(ArrayList<String> anosDisponiveis) {
        this.anosDisponiveis = anosDisponiveis;
    }

    public ArrayList<EntidadeSenadorTabela> getTabelas() {
        return tabelas;
    }

    public void setTabelas(ArrayList<EntidadeSenadorTabela> tabelas) {
        this.tabelas = tabelas;
    }

    public ArrayList<EntidadeSenadorAno> getConteudoAno() {
        return conteudoAno;
    }

    public void setConteudoAno(ArrayList<EntidadeSenadorAno> conteudoAno) {
        this.conteudoAno = conteudoAno;
    }

    public ArrayList<EntidadeSenadorMes> getConteudoMes() {
        return conteudoMes;
    }

    public void setConteudoMes(ArrayList<EntidadeSenadorMes> conteudoMes) {
        this.conteudoMes = conteudoMes;
    }

    public EntidadeSenador() {
    }
}
