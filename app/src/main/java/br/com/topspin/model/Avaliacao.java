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
public class Avaliacao extends Model implements Serializable {

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

    private int saqueProgress;
    private int forehandProgress;
    private int backhandProgress;
    private int voleioProgress;
    private int smashProgress;
    private int ofensivoProgress;
    private int defensivoProgress;
    private int taticoProgress;
    private int competitivoProgress;
    private int fisicoProgress;

    private String sequencia_saque;
    private String sequencia_forehand;
    private String sequencia_backhand;
    private String sequencia_voleio;
    private String sequencia_smash;
    private String sequencia_ofensivo;
    private String sequencia_defensivo;
    private String sequencia_tatico;
    private String sequencia_competitivo;
    private String sequencia_fisico;

    private String qtdAvaliacoesRecebidas;
    private String qtdAvaliacoesPendentes;


    public Avaliacao() {}

    public Avaliacao(Avaliacao avaliacaoOriginal) {
        setIdDoAvaliador(avaliacaoOriginal.getIdDoAvaliador());
        setId(avaliacaoOriginal.getId());
        setDataDaAvaliacao(avaliacaoOriginal.getDataDaAvaliacao());
        setStatus(avaliacaoOriginal.getStatusDaAvaliacao());
        setSaque(avaliacaoOriginal.getSaque());
        setForehand(avaliacaoOriginal.getForehand());
        setBackhand(avaliacaoOriginal.getBackhand());
        setVoleio(avaliacaoOriginal.getVoleio());
        setSmash(avaliacaoOriginal.getSmash());
        setOfensivo(avaliacaoOriginal.getOfensivo());
        setDefensivo(avaliacaoOriginal.getDefensivo());
        setTatico(avaliacaoOriginal.getTatico());
        setCompetitivo(avaliacaoOriginal.getCompetitivo());
        setFisico(avaliacaoOriginal.getFisico());
    }

    public Avaliacao(AvaliacaoPendente avaliacaoPendente) {
        setIdDoAvaliador(avaliacaoPendente.getIdDoAvaliador());
        setDataDaAvaliacao(avaliacaoPendente.getDataDaAvaliacao());
        setStatus(avaliacaoPendente.getStatusDaAvaliacao());
        setSaque(avaliacaoPendente.getSaque());
        setForehand(avaliacaoPendente.getForehand());
        setBackhand(avaliacaoPendente.getBackhand());
        setVoleio(avaliacaoPendente.getVoleio());
        setSmash(avaliacaoPendente.getSmash());
        setOfensivo(avaliacaoPendente.getOfensivo());
        setDefensivo(avaliacaoPendente.getDefensivo());
        setTatico(avaliacaoPendente.getTatico());
        setCompetitivo(avaliacaoPendente.getCompetitivo());
        setFisico(avaliacaoPendente.getFisico());
    }

    public void salvar() {
        DatabaseReference databaseReference = ConfiguracaoFirebase.getDatabase();
        String chaveAvaliacao = databaseReference.child(getNoPrincipal()).child(getIdDoAvaliador()).child("avaliacao").push().getKey();

        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
        String dataDaAvaliacao = dt.format(new Date());

        Map<String, Object> postAvaliacao = this.toMap(dataDaAvaliacao);
        Map<String, Object> postPerformance = this.toMapPerformance(dataDaAvaliacao);

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/"+ getNoPrincipal() +"/" + getIdDoAvaliador() + "/avaliacao/" + chaveAvaliacao, postAvaliacao);
        childUpdates.put("/"+ getNoPrincipal() +"/" + getId() + "/performance/pendentes/" + chaveAvaliacao, postPerformance);
        childUpdates.put("/"+ getNoPrincipal() +"/" + getId() + "/estatisticas/qtd_avaliacoes_pendentes", getQtdAvaliacoesPendentes());

        databaseReference.updateChildren(childUpdates);
    }

    public void confirmarAvaliacao(String idUsuarioLogado, String chaveDaAvaliacaoParaExclusao) {
        DatabaseReference databaseReference = ConfiguracaoFirebase.getDatabase();
        //databaseReference.child(getNoPrincipal()).child(idUsuarioLogado).child("performance").child("confirmadas").setValue(this);
        String chaveAvaliacao = databaseReference.child(getNoPrincipal()).child(idUsuarioLogado).child("performance").child("confirmadas").push().getKey();

        Map<String, Object> postAvaliacaoConfirmada = this.toMapConfirmadas();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/"+ getNoPrincipal() +"/" + idUsuarioLogado + "/performance/confirmadas/" + chaveAvaliacao, postAvaliacaoConfirmada);
        childUpdates.put("/"+ getNoPrincipal() +"/" + idUsuarioLogado + "/performance/pendentes/" + chaveDaAvaliacaoParaExclusao, null);

        childUpdates.put("/"+ getNoPrincipal() +"/" + idUsuarioLogado + "/estatisticas/sequencia_saque", getSequencia_saque());
        childUpdates.put("/"+ getNoPrincipal() +"/" + idUsuarioLogado + "/estatisticas/sequencia_forehand", getSequencia_forehand());
        childUpdates.put("/"+ getNoPrincipal() +"/" + idUsuarioLogado + "/estatisticas/sequencia_backhand", getSequencia_backhand());
        childUpdates.put("/"+ getNoPrincipal() +"/" + idUsuarioLogado + "/estatisticas/sequencia_voleio", getSequencia_voleio());
        childUpdates.put("/"+ getNoPrincipal() +"/" + idUsuarioLogado + "/estatisticas/sequencia_smash", getSequencia_smash());
        childUpdates.put("/"+ getNoPrincipal() +"/" + idUsuarioLogado + "/estatisticas/sequencia_ofensivo", getSequencia_ofensivo());
        childUpdates.put("/"+ getNoPrincipal() +"/" + idUsuarioLogado + "/estatisticas/sequencia_defensivo", getSequencia_defensivo());
        childUpdates.put("/"+ getNoPrincipal() +"/" + idUsuarioLogado + "/estatisticas/sequencia_tatico", getSequencia_tatico());
        childUpdates.put("/"+ getNoPrincipal() +"/" + idUsuarioLogado + "/estatisticas/sequencia_competitivo", getSequencia_competitivo());
        childUpdates.put("/"+ getNoPrincipal() +"/" + idUsuarioLogado + "/estatisticas/sequencia_fisico", getSequencia_fisico());
        childUpdates.put("/"+ getNoPrincipal() +"/" + idUsuarioLogado + "/estatisticas/qtd_avaliacoes_pendentes", getQtdAvaliacoesPendentes());
        childUpdates.put("/"+ getNoPrincipal() +"/" + idUsuarioLogado + "/estatisticas/qtd_avaliacoes_recebidas", getQtdAvaliacoesRecebidas());

        databaseReference.updateChildren(childUpdates);
    }

    @Exclude
    public Map<String, Object> toMap(String dataDaAvaliacao) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("status", status);
        result.put("saque", saque);
        result.put("forehand", forehand);
        result.put("backhand", backhand);
        result.put("voleio", voleio);
        result.put("smash", smash);
        result.put("ofensivo", ofensivo);
        result.put("defensivo", defensivo);
        result.put("tatico", tatico);
        result.put("competitivo", competitivo);
        result.put("fisico", fisico);
        result.put("dataDaAvaliacao", dataDaAvaliacao);
        return result;
    }

    @Exclude
    public Map<String, Object> toMapPerformance(String dataDaAvaliacao) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("idDoAvaliador", idDoAvaliador);
        result.put("status", status);
        result.put("saque", saque);
        result.put("forehand", forehand);
        result.put("backhand", backhand);
        result.put("voleio", voleio);
        result.put("smash", smash);
        result.put("ofensivo", ofensivo);
        result.put("defensivo", defensivo);
        result.put("tatico", tatico);
        result.put("competitivo", competitivo);
        result.put("fisico", fisico);
        result.put("dataDaAvaliacao", dataDaAvaliacao);
        return result;
    }

    @Exclude
    public Map<String, Object> toMapConfirmadas() {
        HashMap<String, Object> result = new HashMap<>();
        //result.put("id", id);
        //result.put("nome", nome);
        result.put("idDoAvaliador", idDoAvaliador);
        result.put("status", status);
        result.put("saque", saque);
        result.put("forehand", forehand);
        result.put("backhand", backhand);
        result.put("voleio", voleio);
        result.put("smash", smash);
        result.put("ofensivo", ofensivo);
        result.put("defensivo", defensivo);
        result.put("tatico", tatico);
        result.put("competitivo", competitivo);
        result.put("fisico", fisico);
        result.put("dataDaAvaliacao", dataDaAvaliacao);
        return result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatusDaAvaliacao() {
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


    @Exclude
    public String getIdDoAvaliador() {
        return idDoAvaliador;
    }

    public void setIdDoAvaliador(String idDoAvaliador) {
        this.idDoAvaliador = idDoAvaliador;
    }

    public String getDataDaAvaliacao() {
        return dataDaAvaliacao;
    }

    public void setDataDaAvaliacao(String dataDaAvaliacao) {
        this.dataDaAvaliacao = dataDaAvaliacao;
    }

    @Exclude
    public int getSaqueProgress() {
        return saqueProgress;
    }

    public void setSaqueProgress(int saqueProgress) {
        this.saqueProgress = saqueProgress;
        setSaque(converteProgress(4, saqueProgress));
    }

    @Exclude
    public int getForehandProgress() {
        return forehandProgress;
    }

    public void setForehandProgress(int forehandProgress) {
        this.forehandProgress = forehandProgress;
        setForehand(converteProgress(4, forehandProgress));
    }

    @Exclude
    public int getBackhandProgress() {
        return backhandProgress;
    }

    public void setBackhandProgress(int backhandProgress) {
        this.backhandProgress = backhandProgress;
        setBackhand(converteProgress(4, backhandProgress));
    }

    @Exclude
    public int getVoleioProgress() {
        return voleioProgress;
    }

    public void setVoleioProgress(int voleioProgress) {
        this.voleioProgress = voleioProgress;
        setVoleio(converteProgress(4, voleioProgress));
    }

    @Exclude
    public int getSmashProgress() {
        return smashProgress;
    }

    public void setSmashProgress(int smashProgress) {
        this.smashProgress = smashProgress;
        setSmash(converteProgress(4, smashProgress));
    }

    @Exclude
    public int getOfensivoProgress() {
        return ofensivoProgress;
    }

    public void setOfensivoProgress(int ofensivoProgress) {
        this.ofensivoProgress = ofensivoProgress;
        setOfensivo(converteProgress(3, ofensivoProgress));
    }

    @Exclude
    public int getDefensivoProgress() {
        return defensivoProgress;
    }

    public void setDefensivoProgress(int defensivoProgress) {
        this.defensivoProgress = defensivoProgress;
        setDefensivo(converteProgress(3, defensivoProgress));
    }

    @Exclude
    public int getTaticoProgress() {
        return taticoProgress;
    }

    public void setTaticoProgress(int taticoProgress) {
        this.taticoProgress = taticoProgress;
        setTatico(converteProgress(3, taticoProgress));
    }

    @Exclude
    public int getCompetitivoProgress() {
        return competitivoProgress;
    }

    public void setCompetitivoProgress(int competitivoProgress) {
        this.competitivoProgress = competitivoProgress;
        setCompetitivo(converteProgress(3, competitivoProgress));
    }

    @Exclude
    public int getFisicoProgress() {
        return fisicoProgress;
    }

    public void setFisicoProgress(int fisicoProgress) {
        this.fisicoProgress = fisicoProgress;
        setFisico(converteProgress(4, fisicoProgress));
    }

    @Exclude
    private String converteProgress(int qtdRespostas, int valor) {
        if (qtdRespostas == 3) {
            return converteProgressDe3Repostas(valor);
        }else if (qtdRespostas == 4){
            return converteProgressDe4Repostas(valor);
        }else{
            return "";
        }
    }

    @Exclude
    public String getSequencia_saque() {
        return sequencia_saque;
    }

    public void setSequencia_saque(String sequencia_saque) {
        this.sequencia_saque = sequencia_saque;
    }

    @Exclude
    public String getSequencia_forehand() {
        return sequencia_forehand;
    }

    public void setSequencia_forehand(String sequencia_forehand) {
        this.sequencia_forehand = sequencia_forehand;
    }

    @Exclude
    public String getSequencia_backhand() {
        return sequencia_backhand;
    }

    public void setSequencia_backhand(String sequencia_backhand) {
        this.sequencia_backhand = sequencia_backhand;
    }

    @Exclude
    public String getSequencia_voleio() {
        return sequencia_voleio;
    }

    public void setSequencia_voleio(String sequencia_voleio) {
        this.sequencia_voleio = sequencia_voleio;
    }

    @Exclude
    public String getSequencia_smash() {
        return sequencia_smash;
    }

    public void setSequencia_smash(String sequencia_smash) {
        this.sequencia_smash = sequencia_smash;
    }

    @Exclude
    public String getSequencia_ofensivo() {
        return sequencia_ofensivo;
    }

    public void setSequencia_ofensivo(String sequencia_ofensivo) {
        this.sequencia_ofensivo = sequencia_ofensivo;
    }

    @Exclude
    public String getSequencia_defensivo() {
        return sequencia_defensivo;
    }

    public void setSequencia_defensivo(String sequencia_defensivo) {
        this.sequencia_defensivo = sequencia_defensivo;
    }

    @Exclude
    public String getSequencia_tatico() {
        return sequencia_tatico;
    }

    public void setSequencia_tatico(String sequencia_tatico) {
        this.sequencia_tatico = sequencia_tatico;
    }

    @Exclude
    public String getSequencia_competitivo() {
        return sequencia_competitivo;
    }

    public void setSequencia_competitivo(String sequencia_competitivo) {
        this.sequencia_competitivo = sequencia_competitivo;
    }

    @Exclude
    public String getSequencia_fisico() {
        return sequencia_fisico;
    }

    public void setSequencia_fisico(String sequencia_fisico) {
        this.sequencia_fisico = sequencia_fisico;
    }

    @Exclude
    public String getQtdAvaliacoesRecebidas() {
        return qtdAvaliacoesRecebidas;
    }

    public void setQtdAvaliacoesRecebidas(String qtdAvaliacoesRecebidas) {
        this.qtdAvaliacoesRecebidas = qtdAvaliacoesRecebidas;
    }

    @Exclude
    public String getQtdAvaliacoesPendentes() {
        return qtdAvaliacoesPendentes;
    }

    public void setQtdAvaliacoesPendentes(String qtdAvaliacoesPendentes) {
        this.qtdAvaliacoesPendentes = qtdAvaliacoesPendentes;
    }

    @Exclude
    private String converteProgressDe3Repostas(int valor) {
        switch (valor) {
            case 0:
                return "POUCO";
            case 1:
                return "NORMAL";
            case 2:
                return "MUITO";
            default:
                return "";
        }
    }

    @Exclude
    private String converteProgressDe4Repostas(int valor) {
        switch (valor) {
            case 0:
                return "RUIM";
            case 1:
                return "REGULAR";
            case 2:
                return "BOM";
            case 3:
                return "OTIMO";
            default:
                return "";
        }
    }

}
