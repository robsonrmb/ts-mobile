package br.com.topspin.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import br.com.topspin.R;
import br.com.topspin.config.ConfiguracaoFirebase;
import br.com.topspin.constantes.Constantes;
import br.com.topspin.firebase.dao.UsuarioDAO;
import br.com.topspin.helper.Base64Custom;
import br.com.topspin.helper.Preferencias;
import br.com.topspin.model.Usuario;
import br.com.topspin.util.UtilACT;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private EditText nome;
    private EditText email;
    private EditText senha;

    private Spinner spinnerEstados;
    private ArrayAdapter<String> adapterEstados;

    private Button btnCadastrar;
    private ProgressDialog progressDialog;

    private Usuario usuario;

    private FirebaseAuth autenticacao;
    private Preferencias preferencias;

    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private ValueEventListener valueEventListenerUsuarioPorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        nome = (EditText) findViewById(R.id.editNome);
        email = (EditText) findViewById(R.id.editEmail);
        senha = (EditText) findViewById(R.id.editSenha);

        spinnerEstados = (Spinner) findViewById(R.id.spnEstados);
        adapterEstados = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Constantes.ESTADOS);
        spinnerEstados.setAdapter(adapterEstados);

        btnCadastrar = (Button) findViewById(R.id.btnCadastraUsuario);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = ProgressDialog.show(CadastroUsuarioActivity.this, "Aguarde!!!", "Processo em andamento.", true, false);
                usuario = new Usuario();
                usuario.setNome(nome.getText().toString());
                usuario.setEmail(email.getText().toString());
                usuario.setSenha(senha.getText().toString());
                usuario.setEstado(spinnerEstados.getSelectedItem().toString().toUpperCase().substring(0,2));
                cadastrarUsuario();
            }
        });
    }

    private void cadastrarUsuario() {
        autenticacao = ConfiguracaoFirebase.getAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(CadastroUsuarioActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //FirebaseUser user = task.getResult().getUser();

                    final String idUsuarioLogado = Base64Custom.codificarBase64(usuario.getEmail());

                    valueEventListenerUsuarioPorId = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Usuario usuario = dataSnapshot.getValue(Usuario.class);
                            if (dataSnapshot.getValue() == null) {
                                usuario = new Usuario();
                                usuario.setId(idUsuarioLogado);
                                usuario.setNome(nome.getText().toString());
                                usuario.setApelido(UtilACT.retornaApelidoPadraoAPartirDoNome(usuario.getNome()));
                                usuario.setEstado(spinnerEstados.getSelectedItem().toString().toUpperCase().substring(0,2));
                                usuario.salvar();
                            }
                            preferencias = new Preferencias(CadastroUsuarioActivity.this);
                            preferencias.salvarIdUsuarioLogado(idUsuarioLogado);

                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }

                            Intent intent = new Intent(CadastroUsuarioActivity.this, LoginActivity.class);
                            intent.putExtra("email", usuario.getEmail());
                            intent.putExtra("senha", usuario.getSenha());
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };
                    usuarioDAO.buscaUsuarioPorId(Base64Custom.codificarBase64(usuario.getEmail())).addListenerForSingleValueEvent(valueEventListenerUsuarioPorId);


                } else {
                    String mensagemDeErro = "";
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        mensagemDeErro = "Digite uma senha mais forte contendo caracteres e números.";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        mensagemDeErro = "Email inválido.";
                    } catch (FirebaseAuthUserCollisionException e) {
                        mensagemDeErro = "Email já cadastrado.";
                    } catch (Exception e) {
                        mensagemDeErro = "Erro ao efetuar o cadastro.";
                        e.printStackTrace();
                    }
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    Toast.makeText(CadastroUsuarioActivity.this, mensagemDeErro, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
