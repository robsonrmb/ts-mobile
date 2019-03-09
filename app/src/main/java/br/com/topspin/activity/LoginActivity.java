package br.com.topspin.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import br.com.topspin.R;
import br.com.topspin.config.ConfiguracaoFirebase;
import br.com.topspin.firebase.dao.UsuarioDAO;
import br.com.topspin.helper.Base64Custom;
import br.com.topspin.helper.Preferencias;
import br.com.topspin.helper.TS_Helper;
import br.com.topspin.model.Usuario;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 10;
    private static final String TAG = "LoginActivity";
    private FirebaseAuth.AuthStateListener mAuthListener;
    /*
    //VARIÁVEIS PARA ACESSO COM GMAIL
    private SignInButton mGoogleBtn;
    private GoogleApiClient mGoogleApiClient;
    GoogleSignInOptions gso;
    GoogleApiClient googleApiClient;
    */

    private RelativeLayout layoutLogin;
    private EditText email;
    private EditText senha;
    private Button btnLogar;
    private CheckBox ckbGuardar;
    private ProgressDialog progressDialog;
    private boolean progressEmFuncionamento;

    private Usuario usuario;
    private FirebaseAuth autenticacao;
    private Preferencias preferencias;

    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private ValueEventListener valueEventListenerUsuarioPorId;

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //autenticacao.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        /*
        if (mAuthListener != null) {
            autenticacao.removeAuthStateListener(mAuthListener);
        }
        */
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = ProgressDialog.show(LoginActivity.this, "Aguarde!!!", "Processo em andamento.", false, false);
        progressEmFuncionamento = true;

        /*
        initFirebase();
        initGoogleAuth();
        mGoogleBtn = (SignInButton) findViewById(R.id.mGoogleBtn);
        */

        TS_Helper.setExibirChamadaConvite(true);
        progressEmFuncionamento = false;

        //verificarUsuarioLogado();
        layoutLogin = (RelativeLayout) findViewById(R.id.activity_login);
        email = (EditText) findViewById(R.id.editEmail);
        senha = (EditText) findViewById(R.id.editSenha);
        btnLogar = (Button) findViewById(R.id.btnLogar);
        ckbGuardar = (CheckBox) findViewById(R.id.ckbGuardarInfo);

        //BUSCANDO INFORMACOES NO PREFERENCES
        preferencias = new Preferencias(LoginActivity.this);
        Usuario u = preferencias.getDadosDeAcesso();
        if (u != null) {
            if (u.getEmail() != null || !u.getEmail().isEmpty()) {
                email.setText(u.getEmail());
                senha.setText(u.getSenha());
                usuario = u;
                validarUsuario();
            }
        }
        if ("0".equals(preferencias.getTema())) {
            layoutLogin.setBackgroundResource(R.drawable.act_fundo_rapida);
        }else if ("1".equals(preferencias.getTema())) {
            layoutLogin.setBackgroundResource(R.drawable.act_fundo_saibro);
        }else{
            layoutLogin.setBackgroundResource(R.drawable.act_fundo_grama);
        }

        //BUSCANDO OS DADOS DE ACESSO APÓS O CADASTRO INICIAL
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            email.setText(extra.getString("email"));
            senha.setText(extra.getString("senha"));
        }

        if (progressDialog != null) {
            progressDialog.dismiss();
            progressEmFuncionamento = false;
        }

        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = ProgressDialog.show(LoginActivity.this, "Aguarde!!!", "Processo em andamento.", false, false);
                progressEmFuncionamento = true;
                usuario = new Usuario();
                usuario.setEmail(email.getText().toString());
                usuario.setSenha(senha.getText().toString());
                validarUsuario();
            }
        });

        /*
        mGoogleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = ProgressDialog.show(LoginActivity.this, "Aguarde!!!", "Processo em andamento.", false, false);
                progressEmFuncionamento = true;
                signIn();
            }
        });
        */
    }

    /*
    private void initFirebase() {
        autenticacao = ConfiguracaoFirebase.getAutenticacao();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    final String nomeAutenticado = firebaseAuth.getCurrentUser().getDisplayName();
                    final String emailAutenticado = firebaseAuth.getCurrentUser().getEmail();

                    valueEventListenerUsuarioPorId = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Usuario usuario = dataSnapshot.getValue(Usuario.class);
                            String idUsuarioLogado = Base64Custom.codificarBase64(emailAutenticado);
                            if (dataSnapshot.getValue() == null) {
                                usuario = new Usuario();
                                usuario.setNome(nomeAutenticado);
                                usuario.setEmail(emailAutenticado);
                                //usuario.setSenha();
                                usuario.setId(idUsuarioLogado);
                                usuario.salvar();
                            }
                            preferencias = new Preferencias(LoginActivity.this);
                            preferencias.salvarIdUsuarioLogado(idUsuarioLogado);

                            if (progressDialog != null) {
                                progressDialog.dismiss();
                                progressEmFuncionamento = false;
                            }

                            abrirTelaPrincipal();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };
                    usuarioDAO.buscaUsuarioPorId(Base64Custom.codificarBase64(firebaseAuth.getCurrentUser().getEmail())).addListenerForSingleValueEvent(valueEventListenerUsuarioPorId);
                }

            }
        };
    }

    private void initGoogleAuth() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(LoginActivity.this, "Erro ao logar com o google!!!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        autenticacao.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            String mensagemDeErro = "";
                            try {
                                Log.w(TAG, "signInWithCredential", task.getException());
                                throw task.getException();
                            } catch (FirebaseNetworkException e) {
                                mensagemDeErro = "Problemas na conexão com a rede.";
                            } catch (FirebaseAuthInvalidUserException e) {
                                mensagemDeErro = "Email desativado e/ou com problemas de autenticação.";
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                mensagemDeErro = "Dados de acesso incorretos.";
                            } catch (Exception e) {
                                mensagemDeErro = "Erro ao efetuar o login.";
                                e.printStackTrace();
                            }
                            Toast.makeText(LoginActivity.this, mensagemDeErro, Toast.LENGTH_LONG).show();
                            //autenticacao.signOut();
                        }
                        // ...
                    }
                });

    }
    */

    public void verificarUsuarioLogado() {
        autenticacao = ConfiguracaoFirebase.getAutenticacao();
        if (autenticacao.getCurrentUser() != null) {
            abrirTelaPrincipal();
        }
    }

    public void validarUsuario() {

        if (usuario.getEmail().isEmpty() || usuario.getSenha().isEmpty()) {
            Toast.makeText(LoginActivity.this, "Informe os dados de login!!!", Toast.LENGTH_SHORT).show();
            if (progressDialog != null) {
                progressDialog.dismiss();
                progressEmFuncionamento = false;
            }
        }else {
            //TODO so para testes.... excluir trecho de codigo
            /*if (usuario.getEmail().equals("l")) {
                usuario.setEmail("lilian@gmail.com");
                usuario.setSenha("lilian111");
            }else if (usuario.getEmail().equals("r")) {
                usuario.setEmail("robson.rmb@gmail.com");
                usuario.setSenha("robson111");
            }else if (usuario.getEmail().equals("p")) {
                usuario.setEmail("pedro@gmail.com");
                usuario.setSenha("pedro111");
            }else if (usuario.getEmail().equals("lu")) {
                usuario.setEmail("lucas@gmail.com");
                usuario.setSenha("lucas111");
            }else if (usuario.getEmail().equals("jv")) {
                usuario.setEmail("joaovictor@gmail.com");
                usuario.setSenha("joao111");
            }else if (usuario.getEmail().equals("j")) {
                usuario.setEmail("joao@gmail.com");
                usuario.setSenha("joao111");
            }else if (usuario.getEmail().equals("a")) {
                usuario.setEmail("alisson@gmail.com");
                usuario.setSenha("alisson111");
            }else if (usuario.getEmail().equals("m")) {
                usuario.setEmail("max@gmail.com");
                usuario.setSenha("max111");
            }else if (usuario.getEmail().equals("d")) {
                usuario.setEmail("davi@gmail.com");
                usuario.setSenha("davi111");
            }else if (usuario.getEmail().equals("i")) {
                usuario.setEmail("iuca@gmail.com");
                usuario.setSenha("iuca111");
            }else if (usuario.getEmail().equals("le")) {
                usuario.setEmail("leandro@gmail.com");
                usuario.setSenha("leandro111");
            }*/
            //=======================================================

            autenticacao = ConfiguracaoFirebase.getAutenticacao();
            autenticacao.signInWithEmailAndPassword(
                    usuario.getEmail(),
                    usuario.getSenha()
            ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (!task.isSuccessful()) {
                        String mensagemDeErro = "";
                        try {
                            throw task.getException();
                        } catch (FirebaseNetworkException e) {
                            mensagemDeErro = "Problemas na conexão com a rede.";
                        } catch (FirebaseAuthInvalidUserException e) {
                            mensagemDeErro = "Email não cadastrado ou com problemas de autenticação.";
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            mensagemDeErro = "Dados de acesso incorretos.";
                        } catch (Exception e) {
                            mensagemDeErro = "Erro ao efetuar o login.";
                            e.printStackTrace();
                        }
                        Toast.makeText(LoginActivity.this, mensagemDeErro, Toast.LENGTH_LONG).show();
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                            progressEmFuncionamento = false;
                        }
                    }else{
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                            progressEmFuncionamento = false;
                        }
                        guardarInformacoesDeAcesso();
                        abrirTelaPrincipal();
                    }
                }
            });
        }
    }

    public void guardarInformacoesDeAcesso() {
        preferencias = new Preferencias(LoginActivity.this);
        if (ckbGuardar.isChecked()) {
            preferencias.salvarInformacoesDeAcesso(usuario);
        }
        preferencias.salvarIdUsuarioLogado(Base64Custom.codificarBase64(usuario.getEmail()));
    }

    public void abrirTelaPrincipal() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void abrirCadastroUsuario(View v) {
        Intent intent = new Intent(LoginActivity.this, CadastroUsuarioActivity.class);
        startActivity(intent);
    }
}