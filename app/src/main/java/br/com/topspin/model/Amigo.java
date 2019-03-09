package br.com.topspin.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

import br.com.topspin.config.ConfiguracaoFirebase;

@IgnoreExtraProperties
public class Amigo extends Model implements Serializable{

    private String idUsuarioLogado;
    private String idDoAmigo;
    private String idGerado;

    public Amigo() {}

    public Amigo(String idUsuarioLogado, String idDoAmigo) {
        this.idUsuarioLogado = idUsuarioLogado;
        this.idDoAmigo = idDoAmigo;
    }

    public void salvar() {
        DatabaseReference databaseReference = ConfiguracaoFirebase.getDatabase();
        databaseReference.child(getNoPrincipal()).child(getIdUsuarioLogado()).child("amigos").push().setValue(getIdDoAmigo());
    }

    public void excluir() {
        DatabaseReference databaseReference = ConfiguracaoFirebase.getDatabase();
        databaseReference.child(getNoPrincipal()).child(getIdUsuarioLogado()).child("amigos").child(getIdGerado()).setValue(null);
    }

    @Exclude
    public String getIdUsuarioLogado() {
        return idUsuarioLogado;
    }

    public void setIdUsuarioLogado(String idUsuarioLogado) {
        this.idUsuarioLogado = idUsuarioLogado;
    }

    public String getIdDoAmigo() {
        return idDoAmigo;
    }

    public void setIdDoAmigo(String idDoAmigo) {
        this.idDoAmigo = idDoAmigo;
    }

    @Exclude
    public String getIdGerado() {
        return idGerado;
    }

    public void setIdGerado(String idGerado) {
        this.idGerado = idGerado;
    }
}
