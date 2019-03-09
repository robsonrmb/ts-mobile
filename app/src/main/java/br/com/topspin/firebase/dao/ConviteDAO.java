package br.com.topspin.firebase.dao;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import br.com.topspin.config.ConfiguracaoFirebase;

public class ConviteDAO extends DAO {

    private DatabaseReference databaseReference;
    private String idDoUsuario;

    public ConviteDAO() {
        if (databaseReference == null) {
            databaseReference = ConfiguracaoFirebase.getDatabase();
        }
    }

    public ConviteDAO(String idDoUsuario) {
        if (databaseReference == null) {
            databaseReference = ConfiguracaoFirebase.getDatabase();
        }
        this.idDoUsuario = idDoUsuario;
    }

    public DatabaseReference retornaReferencia() {
        return databaseReference;
    }

    public DatabaseReference buscaTodosOsConvitesEnviadosDeUmUsuario(String idDoUsuario) {
        return databaseReference.child(getNoPrincipal()).child(idDoUsuario).child("convites").child("enviados");
    }

    public DatabaseReference buscaTodosOsConvitesPendentesDeUmUsuario(String idDoUsuario) {
        return databaseReference.child(getNoPrincipal()).child(idDoUsuario).child("convites").child("pendentes");
    }

    public DatabaseReference buscaTodosOsConvitesRecebidosDeUmUsuario(String idDoUsuario) {
        return databaseReference.child(getNoPrincipal()).child(idDoUsuario).child("convites").child("recebidos");
    }

    public DatabaseReference buscaConviteEnviadoPorIdEChaveDoConvite(String idDoUsuario, String chaveDoConvite) {
        return databaseReference.child(getNoPrincipal()).child(idDoUsuario).child("convites").child("enviados").child(chaveDoConvite);
    }

    public DatabaseReference buscaConvitePendentePorIdEChaveDoConvite(String idDoUsuario, String chaveDoConvite) {
        return databaseReference.child(getNoPrincipal()).child(idDoUsuario).child("convites").child("pendentes").child(chaveDoConvite);
    }

    public DatabaseReference buscaConviteRecebidoPorIdEChaveDoConvite(String idDoUsuario, String chaveDoConvite) {
        return databaseReference.child(getNoPrincipal()).child(idDoUsuario).child("convites").child("recebidos").child(chaveDoConvite);
    }

}
