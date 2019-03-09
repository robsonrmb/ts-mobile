package br.com.topspin.helper;

import android.content.Context;
import android.content.SharedPreferences;

import br.com.topspin.model.Usuario;

public class Preferencias {

    private Context contexto;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String NOME_ARQUIVO = "actenis.preferencias";
    private int MODE = 0;

    private String ID_USUARIO_LOGADO = "id_usuario_logado";
    private String NOME_USUARIO_LOGADO = "nome_usuario_logado";
    private String APELIDO_USUARIO_LOGADO = "apelido_usuario_logado";
    private String EMAIL_ACESSO = "email_de_acesso";
    private String SENHA_ACESSO = "senha_de_acesso";
    private String QTD_ULTIMOS_JOGOS = "qtd_ultimos_jogos";
    private String TEMA = "tema";
    private String PRIMEIRO_ACESSO = "primeiro_acesso";

    public Preferencias(Context contextoParam) {
        contexto = contextoParam;
        preferences = contexto.getSharedPreferences(NOME_ARQUIVO, MODE);
        editor = preferences.edit();
    }

    //SALVA USUARIO LOGADO
    public void salvarIdUsuarioLogado(String texto) {
        editor.putString(ID_USUARIO_LOGADO, texto);
        editor.commit();
    }

    public String getIdUsuarioLogado() {
        return preferences.getString(ID_USUARIO_LOGADO, null);
    }

    //ULTIMOS JOGOS
    public void salvarQtdUltimosJogos(String qtd_ultimo_jogos) {
        editor.putString(QTD_ULTIMOS_JOGOS, qtd_ultimo_jogos);
        editor.commit();
    }

    public String getQTD_ULTIMOS_JOGOS() {
        return preferences.getString(QTD_ULTIMOS_JOGOS, "5");
    }

    //INFORMACOES DE ACESSO
    public void salvarInformacoesDeAcesso(Usuario usuario) {
        editor.putString(EMAIL_ACESSO, usuario.getEmail());
        editor.putString(SENHA_ACESSO, usuario.getSenha());
        editor.commit();
    }

    public void limparDadosAcesso() {
        if (preferences.contains(EMAIL_ACESSO)) {
            editor.remove(EMAIL_ACESSO);
        }
        if (preferences.contains(SENHA_ACESSO)) {
            editor.remove(SENHA_ACESSO);
        }
        editor.commit();
    }

    public Usuario getDadosDeAcesso() {
        if (preferences.contains(EMAIL_ACESSO)) {
            Usuario usuario = new Usuario();
            usuario.setEmail(preferences.getString(EMAIL_ACESSO, null));
            usuario.setSenha(preferences.getString(SENHA_ACESSO, null));
            return usuario;
        }else{
            return null;
        }
    }

    //NOME DO USUARIO LOGADO
    public void setNomeDoUsuarioLogado(String nome) {
        editor.putString(NOME_USUARIO_LOGADO, nome);
        editor.commit();
    }

    public String getNomeDoUsuarioLogado() {
        return preferences.getString(NOME_USUARIO_LOGADO, "");
    }

    //APELIDO DO USUARIO LOGADO
    public void setApelidoDoUsuarioLogado(String apelido) {
        editor.putString(APELIDO_USUARIO_LOGADO, apelido);
        editor.commit();
    }

    public String getApelidoDoUsuarioLogado() {
        return preferences.getString(APELIDO_USUARIO_LOGADO, "");
    }

    //TEMA
    public void salvarTema(String tema) {
        editor.putString(TEMA, tema);
        editor.commit();
    }

    public String getTema() {
        return preferences.getString(TEMA, "0");
    }

    //PRIMEIRO ACESSO
    public void salvarPrimeiroAcesso(boolean primeiroAcesso) {
        editor.putBoolean(PRIMEIRO_ACESSO, primeiroAcesso);
        editor.commit();
    }

    public boolean isPrimeiroAcesso() {
        return preferences.getBoolean(PRIMEIRO_ACESSO, true);
    }

}
