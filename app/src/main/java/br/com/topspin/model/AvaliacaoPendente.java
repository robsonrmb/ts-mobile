package br.com.topspin.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class AvaliacaoPendente {

    private String idDoAvaliador;
    private String id;
    private String status;
    private String saque;
    private String forehand;
    private String backhand;
    private String voleio;
    private String smash;
    private String ofensivo;
    private String defensivo;
    private String tatico;
    private String competitivo;
    private String fisico;
    private String dataDaAvaliacao;
    private String statusDaAvaliacao;

    public String getIdDoAvaliador() {
        return idDoAvaliador;
    }

    public void setIdDoAvaliador(String idDoAvaliador) {
        this.idDoAvaliador = idDoAvaliador;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSaque() {
        return saque;
    }

    public void setSaque(String saque) {
        this.saque = saque;
    }

    public String getForehand() {
        return forehand;
    }

    public void setForehand(String forehand) {
        this.forehand = forehand;
    }

    public String getBackhand() {
        return backhand;
    }

    public void setBackhand(String backhand) {
        this.backhand = backhand;
    }

    public String getVoleio() {
        return voleio;
    }

    public void setVoleio(String voleio) {
        this.voleio = voleio;
    }

    public String getSmash() {
        return smash;
    }

    public void setSmash(String smash) {
        this.smash = smash;
    }

    public String getOfensivo() {
        return ofensivo;
    }

    public void setOfensivo(String ofensivo) {
        this.ofensivo = ofensivo;
    }

    public String getDefensivo() {
        return defensivo;
    }

    public void setDefensivo(String defensivo) {
        this.defensivo = defensivo;
    }

    public String getTatico() {
        return tatico;
    }

    public void setTatico(String tatico) {
        this.tatico = tatico;
    }

    public String getCompetitivo() {
        return competitivo;
    }

    public void setCompetitivo(String competitivo) {
        this.competitivo = competitivo;
    }

    public String getFisico() {
        return fisico;
    }

    public void setFisico(String fisico) {
        this.fisico = fisico;
    }

    public String getDataDaAvaliacao() {
        return dataDaAvaliacao;
    }

    public void setDataDaAvaliacao(String dataDaAvaliacao) {
        this.dataDaAvaliacao = dataDaAvaliacao;
    }

    public String getStatusDaAvaliacao() {
        return statusDaAvaliacao;
    }

    public void setStatusDaAvaliacao(String statusDaAvaliacao) {
        this.statusDaAvaliacao = statusDaAvaliacao;
    }
}
