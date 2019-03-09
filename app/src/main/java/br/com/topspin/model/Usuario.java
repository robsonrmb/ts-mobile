package br.com.topspin.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.Calendar;

import br.com.topspin.config.ConfiguracaoFirebase;

@IgnoreExtraProperties
public class Usuario extends Model implements Serializable {

    private String id;
    private String nome;
    private String email;
    private String senha;

    private String apelido;
    private String dataNascimento;
    private String localOndeJoga;
    private String tipo;
    private String nivel;
    private String estado;
    private String cidade;
    private String amigo;

    public Usuario() {}

    public void salvar() {
        DatabaseReference databaseReference = ConfiguracaoFirebase.getDatabase();
        databaseReference.child(getNoPrincipal()).child(getId()).child("nome").setValue(this.nome);
        databaseReference.child(getNoPrincipal()).child(getId()).child("apelido").setValue(this.apelido);
        databaseReference.child(getNoPrincipal()).child(getId()).child("dataNascimento").setValue(this.dataNascimento);
        databaseReference.child(getNoPrincipal()).child(getId()).child("cidade").setValue(this.cidade);
        databaseReference.child(getNoPrincipal()).child(getId()).child("estado").setValue(this.estado);
        databaseReference.child(getNoPrincipal()).child(getId()).child("localOndeJoga").setValue(this.localOndeJoga);
        databaseReference.child(getNoPrincipal()).child(getId()).child("nivel").setValue(this.nivel);
        databaseReference.child(getNoPrincipal()).child(getId()).child("tipo").setValue(this.tipo);
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getLocalOndeJoga() {
        return localOndeJoga;
    }

    public void setLocalOndeJoga(String localOndeJoga) {
        this.localOndeJoga = localOndeJoga;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    @Exclude
    public String getAmigo() {
        return amigo;
    }

    public void setAmigo(String amigo) {
        this.amigo = amigo;
    }

    @Exclude
    public String getIdade() {

        int idade = 0;
        if (getDataNascimento() != null) {
            int anoAtual = Calendar.getInstance().get(Calendar.YEAR);
            int mesAtual = Calendar.getInstance().get(Calendar.MONTH);
            int diaAtual = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

            idade = anoAtual - Integer.parseInt(getDataNascimento().substring(6));

            if (mesAtual < Integer.parseInt(dataNascimento.substring(3, 5))) {
                idade = idade - 1;

            } else if (mesAtual == Integer.parseInt(dataNascimento.substring(3, 5))) {
                if (diaAtual < Integer.parseInt(dataNascimento.substring(0, 2))) {
                    idade = idade - 1;
                }
            }
        }
        if (idade == 0) {
            return null;
        }else {
            return String.valueOf(idade);
        }
    }

}
