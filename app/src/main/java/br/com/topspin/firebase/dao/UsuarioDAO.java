package br.com.topspin.firebase.dao;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import br.com.topspin.config.ConfiguracaoFirebase;

public class UsuarioDAO extends DAO {

    private DatabaseReference databaseReference;
    private String idDoUsuario;

    public UsuarioDAO() {
        if (databaseReference == null) {
            databaseReference = ConfiguracaoFirebase.getDatabase();
        }
    }

    public UsuarioDAO(String idDoUsuario) {
        if (databaseReference == null) {
            databaseReference = ConfiguracaoFirebase.getDatabase();
        }
        this.idDoUsuario = idDoUsuario;
    }

    public DatabaseReference retornaReferencia() {
        return databaseReference;
    }

    public DatabaseReference buscaTodosOsUsuarios() {
        return databaseReference.child("usuarios");
    }

    public DatabaseReference buscaUsuarioPorId(String id) {
        return databaseReference.child(getNoPrincipal()).child(id);
    }

    public DatabaseReference buscaTodosOsUsuariosDeUmEstado(String estado) {
        return databaseReference.child(getNoPrincipal());
    }

    public Query buscaUsuariosPorEstado(String estado) {
        return databaseReference.child(getNoPrincipal()).orderByChild("estado").equalTo(estado);
    }

    public Query buscaUsuarioPorEstadoENome(String estado, String nome) {
        return databaseReference.child(getNoPrincipal()).orderByChild("nome").equalTo(nome);
    }

    public DatabaseReference buscaAmigoDeUmUsuario(String id, String idDoAmigo) {
        return databaseReference.child(getNoPrincipal()).child(id).child("amigos").child(idDoAmigo);
    }

    public DatabaseReference buscaTodosOsAmigosDeUmUsuario(String id) {
        return databaseReference.child(getNoPrincipal()).child(id).child("amigos");
    }

}
