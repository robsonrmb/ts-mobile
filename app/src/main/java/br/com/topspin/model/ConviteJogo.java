package br.com.topspin.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import br.com.topspin.config.ConfiguracaoFirebase;

@IgnoreExtraProperties
public class ConviteJogo extends Model implements Serializable{

    private String idDeQuemConvidou;
    private String id;
    private String dataDoRegistro;
    private String dataDoJogo;
    private String localDoJogo;
    private String observacao;
    private String periodoDoJogo;
    private String status; //enviado(E), aceito(A) ou recusado(R)
    private String idDoConvite;

    private String qtdConvitesPendentes;
    private String qtdConvitesRecebidos;

    public ConviteJogo() {}

    public ConviteJogo(ConviteJogo cj) {
        this.idDeQuemConvidou = cj.getIdDeQuemConvidou();
        this.id = cj.getId();
        this.dataDoRegistro = cj.getDataDoRegistro();
        this.dataDoJogo = cj.getDataDoJogo();
        this.localDoJogo = cj.getLocalDoJogo();
        this.observacao = cj.getObservacao();
        this.periodoDoJogo = cj.getPeriodoDoJogo();
        this.status = cj.getStatus();
        this.idDoConvite = cj.getIdDoConvite();
    }

    public void salvar() {
        DatabaseReference databaseReference = ConfiguracaoFirebase.getDatabase();
        String chaveConvite = databaseReference.child("usuarios").child(getIdDeQuemConvidou()).child("convites").push().getKey();

        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
        String dataReg = dt.format(new Date());

        Map<String, Object> postConvite = this.toConvite(dataReg);
        Map<String, Object> postConvidado = this.toConvidado(dataReg);

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/"+ getNoPrincipal() +"/" + getIdDeQuemConvidou() + "/convites/enviados/" + chaveConvite, postConvite);
        childUpdates.put("/"+ getNoPrincipal() +"/" + getId() + "/convites/pendentes/" + chaveConvite, postConvidado);
        childUpdates.put("/"+ getNoPrincipal() +"/" + getId() + "/estatisticas/qtd_convites_pendentes", getQtdConvitesPendentes());

        databaseReference.updateChildren(childUpdates);
    }

    public void responderConvite(String idUsuarioLogado) {
        DatabaseReference databaseReference = ConfiguracaoFirebase.getDatabase();
        Map<String, Object> postConviteResposta = this.toResposta();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/"+ getNoPrincipal() +"/" + idUsuarioLogado + "/convites/recebidos/" + idDoConvite, postConviteResposta);
        childUpdates.put("/"+ getNoPrincipal() +"/" + idUsuarioLogado + "/convites/pendentes/" + idDoConvite, null);

        /*childUpdates.put("/"+ getNoPrincipal() +"/" + idUsuarioLogado + "/estatisticas/qtd_convites_pendentes", getQtdConvitesPendentes());
        childUpdates.put("/"+ getNoPrincipal() +"/" + idUsuarioLogado + "/estatisticas/qtd_convites_recebidos", getQtdConvitesRecebidos());*/

        databaseReference.updateChildren(childUpdates);
    }

    public void alteraStatusConviteEnviado(ConviteJogo cj_enviado) {
        DatabaseReference databaseReference = ConfiguracaoFirebase.getDatabase();
        Map<String, Object> postConviteResposta = this.toResposta();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/"+ getNoPrincipal() +"/" + cj_enviado.getIdDeQuemConvidou() + "/convites/enviados/" + cj_enviado.getIdDoConvite() + "/status", cj_enviado.getStatus());

        databaseReference.updateChildren(childUpdates);
    }

    public void excluirConvite(String idUsuarioLogado, String idDoConv, String andamento) {
        DatabaseReference databaseReference = ConfiguracaoFirebase.getDatabase();
        Map<String, Object> childUpdates = new HashMap<>();
        if ("E".equals(andamento)) {
            childUpdates.put("/"+ getNoPrincipal() +"/" + idUsuarioLogado + "/convites/enviados/" + idDoConv, null);
        }else if ("R".equals(andamento)) {
            childUpdates.put("/"+ getNoPrincipal() +"/" + idUsuarioLogado + "/convites/recebidos/" + idDoConv, null);
        }
        databaseReference.updateChildren(childUpdates);
    }

    @Exclude
    public Map<String, Object> toConvite(String dataDoRegistro) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("dataDoRegistro", dataDoRegistro);
        result.put("dataDoJogo", dataDoJogo);
        result.put("localDoJogo", localDoJogo);
        result.put("observacao", observacao);
        result.put("periodoDoJogo", periodoDoJogo);
        result.put("status", "E");
        return result;
    }

    @Exclude
    public Map<String, Object> toConvidado(String dataDoRegistro) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", idDeQuemConvidou);//idDeQuemConvidou
        result.put("dataDoRegistro", dataDoRegistro);
        result.put("dataDoJogo", dataDoJogo);
        result.put("localDoJogo", localDoJogo);
        result.put("observacao", observacao);
        result.put("periodoDoJogo", periodoDoJogo);
        result.put("status", "E");
        return result;
    }

    @Exclude
    public Map<String, Object> toResposta() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", idDeQuemConvidou);//idDeQuemConvidou
        result.put("dataDoRegistro", dataDoRegistro);
        result.put("dataDoJogo", dataDoJogo);
        result.put("localDoJogo", localDoJogo);
        result.put("observacao", observacao);
        result.put("periodoDoJogo", periodoDoJogo);
        result.put("status", status);
        return result;
    }

    @Exclude
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
    public String getQtdConvitesPendentes() {
        return qtdConvitesPendentes;
    }

    public void setQtdConvitesPendentes(String qtdConvitesPendentes) {
        this.qtdConvitesPendentes = qtdConvitesPendentes;
    }

    @Exclude
    public String getQtdConvitesRecebidos() {
        return qtdConvitesRecebidos;
    }

    public void setQtdConvitesRecebidos(String qtdConvitesRecebidos) {
        this.qtdConvitesRecebidos = qtdConvitesRecebidos;
    }

    @Exclude
    public String getIdDoConvite() {
        return idDoConvite;
    }

    public void setIdDoConvite(String idDoConvite) {
        this.idDoConvite = idDoConvite;
    }
}
