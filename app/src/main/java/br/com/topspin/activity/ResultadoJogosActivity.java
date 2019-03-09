package br.com.topspin.activity;

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
import android.widget.TextView;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.topspin.R;
import br.com.topspin.constantes.Constantes;
import br.com.topspin.firebase.dao.ResultadoJogoDAO;
import br.com.topspin.helper.Preferencias;
import br.com.topspin.model.ResultadoJogo;

public class ResultadoJogosActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView dataDoJogo;
    private Spinner spinnerResultado;
    private Spinner spinnerPlacar;
    private Spinner spinnerTipo;
    private TextView editTieVencidos;
    private TextView editTiePerdidos;
    private Button btnSalvar;

    private ArrayAdapter<String> adapterResultado;
    private ArrayAdapter<String> adapterPlacar;
    private ArrayAdapter<String> adapterTipo;

    private String qtdVitorias;
    private String qtdDerrotas;
    private String qtdTieVencidos;
    private String qtdTiePerdidos;
    private String resultadoUltimosJogos;
    private String qtdJogos;

    private ResultadoJogo resultadoJogo;
    private Preferencias preferencias;
    private ResultadoJogoDAO resultadoJogoDAO;

    private ValueEventListener valueEventListenerSingleQtdVitorias;
    private ValueEventListener valueEventListenerSingleQtdDerrotas;
    private ValueEventListener valueEventListenerSingleQtdTieVencidos;
    private ValueEventListener valueEventListenerSingleQtdTiePerdidos;
    private ValueEventListener valueEventListenerSingleResultadoUltimosJogos;
    private ValueEventListener valueEventListenerSingleQtdJogos;

    @Override
    protected void onStop() {
        super.onStop();
        resultadoJogoDAO.buscaQtdVitoriasDoUsuario().removeEventListener(valueEventListenerSingleQtdVitorias);
        resultadoJogoDAO.buscaQtdDerrotasDoUsuario().removeEventListener(valueEventListenerSingleQtdDerrotas);
        resultadoJogoDAO.buscaQtdTieVencidosDoUsuario().removeEventListener(valueEventListenerSingleQtdTieVencidos);
        resultadoJogoDAO.buscaQtdTiePerdidosDoUsuario().removeEventListener(valueEventListenerSingleQtdTiePerdidos);
        resultadoJogoDAO.buscaResultadoUltimosJogosDoUsuario().removeEventListener(valueEventListenerSingleResultadoUltimosJogos);
        resultadoJogoDAO.buscaQtdJogosDoUsuario().removeEventListener(valueEventListenerSingleQtdJogos);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado_jogos);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("RESULTADO DE JOGOS");
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        preferencias = new Preferencias(ResultadoJogosActivity.this);
        if ("0".equals(preferencias.getTema())) {
            toolbar.setBackgroundColor(Color.parseColor(Constantes.QUADRA_RAPIDA));
        }else if ("1".equals(preferencias.getTema())) {
            toolbar.setBackgroundColor(Color.parseColor(Constantes.SAIBRO));
        }else{
            toolbar.setBackgroundColor(Color.parseColor(Constantes.GRAMA));
        }

        dataDoJogo = (EditText) findViewById(R.id.editDataJogo);
        SimpleMaskFormatter simpleMaskData = new SimpleMaskFormatter("NN/NN/NNNN");
        MaskTextWatcher maskData = new MaskTextWatcher(dataDoJogo, simpleMaskData);
        dataDoJogo.addTextChangedListener(maskData);

        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
        dataDoJogo.setText(dt.format(new Date()));

        spinnerResultado = (Spinner) findViewById(R.id.spinnerResultado);
        adapterResultado = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Constantes.RESULTADOS);
        adapterResultado.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerResultado.setAdapter(adapterResultado);

        spinnerPlacar = (Spinner) findViewById(R.id.spinnerPlacar);
        adapterPlacar = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Constantes.PLACARES);
        adapterPlacar.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlacar.setAdapter(adapterPlacar);

        spinnerTipo = (Spinner) findViewById(R.id.spinnerTipo);
        adapterTipo = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Constantes.TIPOS_DE_JOGOS);
        adapterTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(adapterTipo);

        editTieVencidos = (EditText) findViewById(R.id.editTieVencidos);
        editTiePerdidos = (EditText) findViewById(R.id.editTiePerdidos);

        btnSalvar = (Button) findViewById(R.id.btnSalvar);

        resultadoJogoDAO = new ResultadoJogoDAO(preferencias.getIdUsuarioLogado());

        //BUSCA QUANTIDADE DE VITÓRIAS
        valueEventListenerSingleQtdVitorias = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                qtdVitorias = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        resultadoJogoDAO.buscaQtdVitoriasDoUsuario().addListenerForSingleValueEvent(valueEventListenerSingleQtdVitorias);

        //BUSCA QUANTIDADE DE DERROTAS
        valueEventListenerSingleQtdDerrotas = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                qtdDerrotas = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        resultadoJogoDAO.buscaQtdDerrotasDoUsuario().addListenerForSingleValueEvent(valueEventListenerSingleQtdDerrotas);

        //BUSCA QUANTIDADE DE TIEBREAK VENCIDOS
        valueEventListenerSingleQtdTieVencidos = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                qtdTieVencidos = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        resultadoJogoDAO.buscaQtdTieVencidosDoUsuario().addListenerForSingleValueEvent(valueEventListenerSingleQtdTieVencidos);

        //BUSCA QUANTIDADE DE TIEBREAK PERDIDOS
        valueEventListenerSingleQtdTiePerdidos = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                qtdTiePerdidos = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        resultadoJogoDAO.buscaQtdTiePerdidosDoUsuario().addListenerForSingleValueEvent(valueEventListenerSingleQtdTiePerdidos);

        //BUSCA RESULTADO DOS ULTIMOS JOGOS
        valueEventListenerSingleResultadoUltimosJogos = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                resultadoUltimosJogos = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        resultadoJogoDAO.buscaResultadoUltimosJogosDoUsuario().addListenerForSingleValueEvent(valueEventListenerSingleResultadoUltimosJogos);

        //BUSCA QUANTIDADE DE JOGOS
        valueEventListenerSingleQtdJogos = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                qtdJogos = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        resultadoJogoDAO.buscaQtdJogosDoUsuario().addListenerForSingleValueEvent(valueEventListenerSingleQtdJogos);

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resultadoJogo = new ResultadoJogo();
                resultadoJogo.setIdDoUsuarioLogado(preferencias.getIdUsuarioLogado());
                resultadoJogo.setData(dataDoJogo.getText().toString());
                resultadoJogo.setResultado(spinnerResultado.getSelectedItem().toString());
                resultadoJogo.setPlacar(spinnerPlacar.getSelectedItem().toString());
                resultadoJogo.setTipo(spinnerTipo.getSelectedItem().toString());
                resultadoJogo.setQtdTieBreakVencidos(editTieVencidos.getText().toString());
                resultadoJogo.setQtdTieBreakPerdidos(editTiePerdidos.getText().toString());

                if (qtdJogos == null) {
                    resultadoJogo.setQtdJogosParaEstatisticas("1");
                }else {
                    resultadoJogo.setQtdJogosParaEstatisticas((Integer.parseInt(qtdJogos) + 1) + "");
                }

                if (spinnerResultado.getSelectedItemPosition() == 1) {
                    //VITORIAS
                    if (qtdVitorias != null && !qtdVitorias.equals("")) {
                        resultadoJogo.setQtdVitoriasParaEstatisticas((Integer.parseInt(qtdVitorias) + 1) + "");
                    }else{
                        resultadoJogo.setQtdVitoriasParaEstatisticas("1");
                    }

                }else if (spinnerResultado.getSelectedItemPosition() == 2) {
                    //DERROTAS
                    if (qtdDerrotas != null && !qtdDerrotas.equals("")) {
                        resultadoJogo.setQtdDerrotasParaEstatisticas((Integer.parseInt(qtdDerrotas) + 1) + "");
                    }else{
                        resultadoJogo.setQtdDerrotasParaEstatisticas("1");
                    }
                }
                //TIE VENCIDOS
                if (qtdTieVencidos != null && !"".equals(qtdTieVencidos)) {
                    qtdTieVencidos = (Integer.parseInt(qtdTieVencidos) + getEditTieVencidosFormatado())+"";
                }else{
                    qtdTieVencidos = getEditTieVencidosFormatado()+"";
                }
                resultadoJogo.setQtdTieVencidosParaEstatisticas(qtdTieVencidos);

                //TIE PERDIDOS
                if (qtdTiePerdidos != null && !"".equals(qtdTiePerdidos)) {
                    qtdTiePerdidos = (Integer.parseInt(qtdTiePerdidos) + getEditTiePerdidosFormatado())+"";
                }else{
                    qtdTiePerdidos = getEditTiePerdidosFormatado()+"";
                }
                resultadoJogo.setQtdTiePerdidosParaEstatisticas(qtdTiePerdidos);

                //ULTIMOSJOGOS
                String ultJogo = "";
                if (spinnerResultado.getSelectedItemPosition() == 1) {
                    ultJogo = "V";
                }else if (spinnerResultado.getSelectedItemPosition() == 2) {
                    ultJogo = "D";
                }
                if (resultadoUltimosJogos != null && !"".equals(resultadoUltimosJogos)) {
                    if (resultadoUltimosJogos.length() < Constantes.QTD_MAXIMA_ULTIMOS_JOGOS) {
                        resultadoUltimosJogos = ultJogo + resultadoUltimosJogos;
                    }else{
                        resultadoUltimosJogos = ultJogo + resultadoUltimosJogos.substring(0, resultadoUltimosJogos.length()-1);
                    }
                }else{
                    resultadoUltimosJogos = ultJogo;
                }
                resultadoJogo.setResultadoUltimosJogosParaEstatisticas(resultadoUltimosJogos);

                String msg = validarJogo();
                if (msg == null) {
                    cadastrarJogo();
                }else{
                    Toast.makeText(ResultadoJogosActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void abrirTelaPrincipal() {
        Intent intent = new Intent(ResultadoJogosActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private String validarJogo() {
        return resultadoJogo.validar();
    }

    private void cadastrarJogo() {
        resultadoJogo.salvar();

        Intent intent = new Intent(ResultadoJogosActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        Toast.makeText(ResultadoJogosActivity.this, "Dados do jogo salvo com sucesso!!!", Toast.LENGTH_LONG).show();
    }

    private int getEditTieVencidosFormatado() {
        if (editTieVencidos.getText() == null || "".equals(editTieVencidos.getText().toString())) {
            return 0;
        }else{
            return Integer.parseInt(editTieVencidos.getText().toString());
        }
    }

    private int getEditTiePerdidosFormatado() {
        if (editTiePerdidos.getText() == null || "".equals(editTiePerdidos.getText().toString())) {
            return 0;
        }else{
            return Integer.parseInt(editTiePerdidos.getText().toString());
        }
    }
}
