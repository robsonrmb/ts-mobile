package br.com.topspin.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.topspin.R;
import br.com.topspin.adapter.AdapterJogadoresListView;
import br.com.topspin.constantes.Constantes;
import br.com.topspin.firebase.dao.UsuarioDAO;
import br.com.topspin.helper.Preferencias;
import br.com.topspin.model.ConviteJogo;
import br.com.topspin.model.Usuario;
import br.com.topspin.util.UtilACT;

public class QueroJogarActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private EditText dataDoJogo;
    private EditText localDoJogo;
    private EditText obsDoJogo;
    private Spinner spinnerPeriodo;
    private ArrayAdapter<String> adapterPeriodo;

    private Button btnEnviarConvite;

    private Preferencias preferencias;
    private String idUsuarioLogado;

    private ConviteJogo conviteJogo;

    private String nomeExtra;
    private String idDoAmigoExtra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quero_jogar);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("QUERO JOGAR");
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        dataDoJogo = (EditText) findViewById(R.id.editDataDoJogo);
        localDoJogo = (EditText) findViewById(R.id.editLocalDoJogo);
        obsDoJogo = (EditText) findViewById(R.id.editObsDoJogo);

        btnEnviarConvite = (Button) findViewById(R.id.btnEnviarConvite);

        spinnerPeriodo = (Spinner) findViewById(R.id.spnPeriodo);
        adapterPeriodo = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Constantes.PERIODOS);
        spinnerPeriodo.setAdapter(adapterPeriodo);

        preferencias = new Preferencias(QueroJogarActivity.this);
        idUsuarioLogado = preferencias.getIdUsuarioLogado();
        if ("0".equals(preferencias.getTema())) {
            toolbar.setBackgroundColor(Color.parseColor(Constantes.QUADRA_RAPIDA));
        }else if ("1".equals(preferencias.getTema())) {
            toolbar.setBackgroundColor(Color.parseColor(Constantes.SAIBRO));
        }else{
            toolbar.setBackgroundColor(Color.parseColor(Constantes.GRAMA));
        }

        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
        String dataAtual = dt.format(new Date());

        SimpleMaskFormatter simpleMaskData = new SimpleMaskFormatter("NN/NN/NNNN");
        MaskTextWatcher maskData = new MaskTextWatcher(dataDoJogo, simpleMaskData);
        dataDoJogo.addTextChangedListener(maskData);
        dataDoJogo.setText(dataAtual);

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            idDoAmigoExtra = extra.getString("idDoUsuario");
            nomeExtra = extra.getString("nomeDoUsuario");
        }

        btnEnviarConvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                conviteJogo = new ConviteJogo();
                conviteJogo.setIdDeQuemConvidou(idUsuarioLogado);
                conviteJogo.setId(idDoAmigoExtra);
                conviteJogo.setDataDoJogo(dataDoJogo.getText().toString().toUpperCase());
                conviteJogo.setLocalDoJogo(localDoJogo.getText().toString().toUpperCase());
                conviteJogo.setObservacao(obsDoJogo.getText().toString());
                conviteJogo.setPeriodoDoJogo(spinnerPeriodo.getSelectedItem().toString().toUpperCase());

                cadastrarConviteJogo();
            }
        });
    }

    private void cadastrarConviteJogo() {
        conviteJogo.salvar();

        finish();
        Toast.makeText(QueroJogarActivity.this, "Convite realizado com sucesso!!!", Toast.LENGTH_LONG).show();
    }
}
