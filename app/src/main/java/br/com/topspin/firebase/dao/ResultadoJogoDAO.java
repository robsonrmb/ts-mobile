package br.com.topspin.firebase.dao;

import com.google.firebase.database.DatabaseReference;

import br.com.topspin.config.ConfiguracaoFirebase;

public class ResultadoJogoDAO extends DAO {

    private DatabaseReference databaseReference;
    private String idDoUsuario;

    public ResultadoJogoDAO() {}
    public ResultadoJogoDAO(String idDoUsuario) {
        if (databaseReference == null) {
            databaseReference = ConfiguracaoFirebase.getDatabase();
        }
        this.idDoUsuario = idDoUsuario;
    }

    public DatabaseReference retornaReferencia() {
        return databaseReference;
    }

    public DatabaseReference buscaQtdVitoriasDoUsuario() {
        return databaseReference.child(getNoPrincipal()).child(idDoUsuario).child("estatisticas").child("qtdVitorias");
    }

    public DatabaseReference buscaQtdDerrotasDoUsuario() {
        return databaseReference.child(getNoPrincipal()).child(idDoUsuario).child("estatisticas").child("qtdDerrotas");
    }

    public DatabaseReference buscaQtdTieVencidosDoUsuario() {
        return databaseReference.child(getNoPrincipal()).child(idDoUsuario).child("estatisticas").child("qtdTieVencidos");
    }

    public DatabaseReference buscaQtdTiePerdidosDoUsuario() {
        return databaseReference.child(getNoPrincipal()).child(idDoUsuario).child("estatisticas").child("qtdTiePerdidos");
    }

    public DatabaseReference buscaResultadoUltimosJogosDoUsuario() {
        return databaseReference.child(getNoPrincipal()).child(idDoUsuario).child("estatisticas").child("ultimosJogos");
    }

    public DatabaseReference buscaQtdJogosDoUsuario() {
        return databaseReference.child(getNoPrincipal()).child(idDoUsuario).child("estatisticas").child("qtdJogos");
    }

}
