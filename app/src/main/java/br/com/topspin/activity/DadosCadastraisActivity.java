package br.com.topspin.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.topspin.R;
import br.com.topspin.config.ConfiguracaoFirebase;
import br.com.topspin.constantes.Constantes;
import br.com.topspin.firebase.dao.UsuarioDAO;
import br.com.topspin.helper.Preferencias;
import br.com.topspin.model.Usuario;
import br.com.topspin.util.UtilACT;

public class DadosCadastraisActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText nomeCompleto;
    private EditText apelido;
    private EditText dataNascimento;
    private EditText localOndeJoga;
    private EditText cidade;

    private Spinner spinnerEstados;
    private Spinner spinnerNivel;
    private Spinner spinnerTipo;
    private ArrayAdapter<String> adapterTipo;
    private ArrayAdapter<String> adapterNivel;
    private ArrayAdapter<String> adapterEstados;

    private Button btnSalva;
    private ProgressDialog progressDialog;

    private Usuario usuario;
    private String idUsuarioLogado;

    private FirebaseAuth autenticacao;
    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private ValueEventListener valueEventListenerUsuarioPorId;

    private Preferencias preferencias;

    @Override
    protected void onStop() {
        super.onStop();
        usuarioDAO.buscaUsuarioPorId(idUsuarioLogado).removeEventListener(valueEventListenerUsuarioPorId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dados_cadastrais);

        progressDialog = ProgressDialog.show(DadosCadastraisActivity.this, "Aguarde!!!", "Carregando informações.", true, false);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("DADOS CADASTRAIS");
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        preferencias = new Preferencias(DadosCadastraisActivity.this);
        idUsuarioLogado = preferencias.getIdUsuarioLogado();
        if ("0".equals(preferencias.getTema())) {
            toolbar.setBackgroundColor(Color.parseColor(Constantes.QUADRA_RAPIDA));
        }else if ("1".equals(preferencias.getTema())) {
            toolbar.setBackgroundColor(Color.parseColor(Constantes.SAIBRO));
        }else{
            toolbar.setBackgroundColor(Color.parseColor(Constantes.GRAMA));
        }

        nomeCompleto = (EditText) findViewById(R.id.editNomeCompleto);
        apelido = (EditText) findViewById(R.id.editApelido);
        dataNascimento = (EditText) findViewById(R.id.editDataNasc);
        localOndeJoga = (EditText) findViewById(R.id.editLocal);
        cidade = (EditText) findViewById(R.id.editCidade);
        btnSalva = (Button) findViewById(R.id.btnComplementaDados);

        spinnerTipo = (Spinner) findViewById(R.id.spnTipo);
        adapterTipo = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Constantes.TIPOS);
        spinnerTipo.setAdapter(adapterTipo);

        spinnerNivel = (Spinner) findViewById(R.id.spnNivel);
        adapterNivel = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Constantes.NIVEIS);
        spinnerNivel.setAdapter(adapterNivel);

        spinnerEstados = (Spinner) findViewById(R.id.spnEstados);
        adapterEstados = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Constantes.ESTADOS);
        spinnerEstados.setAdapter(adapterEstados);

        SimpleMaskFormatter simpleMaskData = new SimpleMaskFormatter("NN/NN/NNNN");
        MaskTextWatcher maskData = new MaskTextWatcher(dataNascimento, simpleMaskData);
        dataNascimento.addTextChangedListener(maskData);

        autenticacao = ConfiguracaoFirebase.getAutenticacao();

        valueEventListenerUsuarioPorId = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                if (dataSnapshot.getValue() != null) {
                    nomeCompleto.setText(usuario.getNome());
                    apelido.setText(usuario.getApelido());
                    dataNascimento.setText(usuario.getDataNascimento());
                    localOndeJoga.setText(usuario.getLocalOndeJoga());
                    spinnerTipo.setSelection(UtilACT.buscaTipo(usuario.getTipo()));
                    spinnerNivel.setSelection(UtilACT.buscaNivel(usuario.getNivel()));
                    spinnerEstados.setSelection(UtilACT.buscaIndiceDoEstado(usuario.getEstado()));
                    cidade.setText(usuario.getCidade());

                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        usuarioDAO.buscaUsuarioPorId(idUsuarioLogado).addListenerForSingleValueEvent(valueEventListenerUsuarioPorId);

        btnSalva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                usuario = new Usuario();
                usuario.setId(idUsuarioLogado);
                usuario.setNome(nomeCompleto.getText().toString().toUpperCase());
                usuario.setApelido(apelido.getText().toString().toUpperCase());
                usuario.setDataNascimento(dataNascimento.getText().toString());
                usuario.setLocalOndeJoga(localOndeJoga.getText().toString().toUpperCase());
                usuario.setCidade(cidade.getText().toString().toUpperCase());
                usuario.setTipo(spinnerTipo.getSelectedItem().toString().toUpperCase());
                usuario.setNivel(spinnerNivel.getSelectedItem().toString().toUpperCase());
                usuario.setEstado(spinnerEstados.getSelectedItem().toString().toUpperCase().substring(0,2));

                cadastrarUsuario();
            }
        });

    }

    private void cadastrarUsuario() {
        usuario.salvar();
        finish();
        Toast.makeText(DadosCadastraisActivity.this, "Dados atualizados com sucesso!!!", Toast.LENGTH_LONG).show();
    }

}
