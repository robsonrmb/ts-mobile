package br.com.topspin.bean;


public class BeanAvaliacao {

    private boolean botaoRuimMarcado;
    private boolean botaoRegularMarcado;
    private boolean botaoBomMarcado;
    private boolean botaoOtimoMarcado;

    private int contador;
    private String textoAvaliacao;

    public BeanAvaliacao() {}

    public BeanAvaliacao(int contador) {
        this.contador = contador;
        this.textoAvaliacao = "SAQUE";
        this.botaoRuimMarcado = false;
        this.botaoRegularMarcado = false;
        this.botaoBomMarcado = false;
        this.botaoOtimoMarcado = false;
    }

    public boolean isBotaoRuimMarcado() {
        return botaoRuimMarcado;
    }

    public void setBotaoRuimMarcado(boolean botaoRuimMarcado) {
        this.botaoRuimMarcado = botaoRuimMarcado;
    }

    public boolean isBotaoRegularMarcado() {
        return botaoRegularMarcado;
    }

    public void setBotaoRegularMarcado(boolean botaoRegularMarcado) {
        this.botaoRegularMarcado = botaoRegularMarcado;
    }

    public boolean isBotaoBomMarcado() {
        return botaoBomMarcado;
    }

    public void setBotaoBomMarcado(boolean botaoBomMarcado) {
        this.botaoBomMarcado = botaoBomMarcado;
    }

    public boolean isBotaoOtimoMarcado() {
        return botaoOtimoMarcado;
    }

    public void setBotaoOtimoMarcado(boolean botaoOtimoMarcado) {
        this.botaoOtimoMarcado = botaoOtimoMarcado;
    }

    public int getContador() {
        return contador;
    }

    public void setContador(int contador) {
        this.contador = contador;
    }

    public String getTextoAvaliacao() {
        return textoAvaliacao;
    }

    public void setTextoAvaliacao(String textoAvaliacao) {
        this.textoAvaliacao = textoAvaliacao;
    }
}
