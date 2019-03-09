package br.com.topspin.firebase.dao;

import com.google.firebase.database.DatabaseReference;

import br.com.topspin.config.ConfiguracaoFirebase;

public class AvaliacaoDAO extends DAO {

    private DatabaseReference databaseReference;
    private String idDoUsuario;

    public AvaliacaoDAO() {
        if (databaseReference == null) {
            databaseReference = ConfiguracaoFirebase.getDatabase();
        }
    }

    public AvaliacaoDAO(String idDoUsuario) {
        if (databaseReference == null) {
            databaseReference = ConfiguracaoFirebase.getDatabase();
        }
        this.idDoUsuario = idDoUsuario;
    }

    public DatabaseReference retornaReferencia() {
        return databaseReference;
    }

    public DatabaseReference buscaAvaliacoesPendentesPorId() {
        return databaseReference.child(getNoPrincipal()).child(idDoUsuario).child("performance").child("pendentes");
    }

    public DatabaseReference buscaAvaliacoesConfirmadasPorId() {
        return databaseReference.child(getNoPrincipal()).child(idDoUsuario).child("performance").child("confirmadas");
    }

    public DatabaseReference buscaAvaliacaoPendentesPorIdEChaveDaAvaliacao(String chaveDaAvaliacao) {
        return databaseReference.child(getNoPrincipal()).child(idDoUsuario).child("performance").child("pendentes").child(chaveDaAvaliacao);
    }

    public DatabaseReference buscaSequenciaDasEstatisticas() {
        return databaseReference.child(getNoPrincipal()).child(idDoUsuario).child("estatisticas").child("sequencia");
    }

    public DatabaseReference buscaSequenciaDasEstatisticasSaque() {
        return databaseReference.child(getNoPrincipal()).child(idDoUsuario).child("estatisticas").child("sequencia_saque");
    }

    public DatabaseReference buscaSequenciaDasEstatisticasForehand() {
        return databaseReference.child(getNoPrincipal()).child(idDoUsuario).child("estatisticas").child("sequencia_forehand");
    }

    public DatabaseReference buscaSequenciaDasEstatisticasBackhand() {
        return databaseReference.child(getNoPrincipal()).child(idDoUsuario).child("estatisticas").child("sequencia_backhand");
    }

    public DatabaseReference buscaSequenciaDasEstatisticasVoleio() {
        return databaseReference.child(getNoPrincipal()).child(idDoUsuario).child("estatisticas").child("sequencia_voleio");
    }

    public DatabaseReference buscaSequenciaDasEstatisticasSmash() {
        return databaseReference.child(getNoPrincipal()).child(idDoUsuario).child("estatisticas").child("sequencia_smash");
    }

    public DatabaseReference buscaSequenciaDasEstatisticasOfensivo() {
        return databaseReference.child(getNoPrincipal()).child(idDoUsuario).child("estatisticas").child("sequencia_ofensivo");
    }

    public DatabaseReference buscaSequenciaDasEstatisticasDefensivo() {
        return databaseReference.child(getNoPrincipal()).child(idDoUsuario).child("estatisticas").child("sequencia_defensivo");
    }

    public DatabaseReference buscaSequenciaDasEstatisticasTatico() {
        return databaseReference.child(getNoPrincipal()).child(idDoUsuario).child("estatisticas").child("sequencia_tatico");
    }

    public DatabaseReference buscaSequenciaDasEstatisticasCompetitivo() {
        return databaseReference.child(getNoPrincipal()).child(idDoUsuario).child("estatisticas").child("sequencia_competitivo");
    }

    public DatabaseReference buscaSequenciaDasEstatisticasFisico() {
        return databaseReference.child(getNoPrincipal()).child(idDoUsuario).child("estatisticas").child("sequencia_fisico");
    }

    public DatabaseReference buscaQtdAvaliacoesRecebidas() {
        return databaseReference.child(getNoPrincipal()).child(idDoUsuario).child("estatisticas").child("qtd_avaliacoes_recebidas");
    }

    public DatabaseReference buscaQtdAvaliacoesPendentes() {
        return databaseReference.child(getNoPrincipal()).child(idDoUsuario).child("estatisticas").child("qtd_avaliacoes_pendentes");
    }
}
