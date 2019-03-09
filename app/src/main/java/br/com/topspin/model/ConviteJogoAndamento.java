package br.com.topspin.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import br.com.topspin.config.ConfiguracaoFirebase;

@IgnoreExtraProperties
public class ConviteJogoAndamento implements Serializable{

    private String idDoConvite; //chave gerada no firebase.
    private String idDeQuemConvidou;
    private String id;
    private String dataDoRegistro;
    private String dataDoJogo;
    private String localDoJogo;
    private String observacao;
    private String periodoDoJogo;
    private String status; //enviado(E), aceito(A) ou recusado(R)
    private String apelido;
    private int idDaImagemDoStatus;
    private String fraseDoConvite;

    public ConviteJogoAndamento() {}

    public String getIdDeQuemConvidou() {
        return idDeQuemConvidou;
    }

    public void setIdDeQuemConvidou(String idDeQuemConvidou) {
        this.idDeQuemConvidou = idDeQuemConvidou;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDataDoRegistro() {
        return dataDoRegistro;
    }

    public void setDataDoRegistro(String dataDoRegistro) {
        this.dataDoRegistro = dataDoRegistro;
    }

    public String getDataDoJogo() {
        return dataDoJogo;
    }

    public void setDataDoJogo(String dataDoJogo) {
        this.dataDoJogo = dataDoJogo;
    }

    public String getLocalDoJogo() {
        return localDoJogo;
    }

    public void setLocalDoJogo(String localDoJogo) {
        this.localDoJogo = localDoJogo;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getPeriodoDoJogo() {
        return periodoDoJogo;
    }

    public void setPeriodoDoJogo(String periodoDoJogo) {
        this.periodoDoJogo = periodoDoJogo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Exclude
    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    @Exclude
    public int getIdDaImagemDoStatus() {
        return idDaImagemDoStatus;
    }

    public void setIdDaImagemDoStatus(int idDaImagemDoStatus) {
        this.idDaImagemDoStatus = idDaImagemDoStatus;
    }

    @Exclude
    public String getFraseDoConvite() {
        return fraseDoConvite;
    }

    public void setFraseDoConvite(String fraseDoConvite) {
        this.fraseDoConvite = fraseDoConvite;
    }

    @Exclude
    public String getIdDoConvite() {
        return idDoConvite;
    }

    public void setIdDoConvite(String idDoConvite) {
        this.idDoConvite = idDoConvite;
    }
}
