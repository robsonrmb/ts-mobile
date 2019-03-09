package br.com.topspin.bean;

public class BeanAvaliacaoPendente {

    private String idDaAvaliacao;
    private String nomeDoAvaliador;
    private String apelidoDoAvaliador;
    private String dataDaAvaliacao;

    public BeanAvaliacaoPendente() {}

    public BeanAvaliacaoPendente(String idDaAvaliacao, String nomeDoAvaliador, String dataDaAvaliacao) {
        this.idDaAvaliacao = idDaAvaliacao;
        this.nomeDoAvaliador = nomeDoAvaliador;
        this.dataDaAvaliacao = dataDaAvaliacao;
    }

    public BeanAvaliacaoPendente(String idDaAvaliacao, String nomeDoAvaliador, String apelidoDoAvaliador, String dataDaAvaliacao) {
        this.idDaAvaliacao = idDaAvaliacao;
        this.nomeDoAvaliador = nomeDoAvaliador;
        this.apelidoDoAvaliador = apelidoDoAvaliador;
        this.dataDaAvaliacao = dataDaAvaliacao;
    }

    public String getIdDaAvaliacao() {
        return idDaAvaliacao;
    }

    public void setIdDaAvaliacao(String idDaAvaliacao) {
        this.idDaAvaliacao = idDaAvaliacao;
    }

    public String getNomeDoAvaliador() {
        return nomeDoAvaliador;
    }

    public void setNomeDoAvaliador(String nomeDoAvaliador) {
        this.nomeDoAvaliador = nomeDoAvaliador;
    }

    public String getApelidoDoAvaliador() {
        return apelidoDoAvaliador;
    }

    public void setApelidoDoAvaliador(String apelidoDoAvaliador) {
        this.apelidoDoAvaliador = apelidoDoAvaliador;
    }

    public String getDataDaAvaliacao() {
        return dataDaAvaliacao;
    }

    public void setDataDaAvaliacao(String dataDaAvaliacao) {
        this.dataDaAvaliacao = dataDaAvaliacao;
    }
}
