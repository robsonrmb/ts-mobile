package br.com.topspin.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import br.com.topspin.R;
import br.com.topspin.constantes.Constantes;
import br.com.topspin.helper.Preferencias;

public class ConfiguracaoActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private String[] arrayQtdJogos = {"5", "6", "7", "8", "9", "10"};
    private String[] arrayTemas = {"Quadra rápida", "Saibro", "Grama"};
    private Button btnLimparDadosAcesso;
    private Spinner spinnerQtdJogos;
    private Spinner spinnerTemas;

    private Preferencias preferencias;
    private ArrayAdapter<String> adapterQtdJogos;
    private ArrayAdapter<String> adapterTemas;

    private boolean primeira_vez = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracao);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("CONFIGURAÇÕES");
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        preferencias = new Preferencias(ConfiguracaoActivity.this);
        if ("0".equals(preferencias.getTema())) {
            toolbar.setBackgroundColor(Color.parseColor(Constantes.QUADRA_RAPIDA));
        }else if ("1".equals(preferencias.getTema())) {
            toolbar.setBackgroundColor(Color.parseColor(Constantes.SAIBRO));
        }else{
            toolbar.setBackgroundColor(Color.parseColor(Constantes.GRAMA));
        }

        btnLimparDadosAcesso = (Button) findViewById(R.id.btnLimparDadosAcesso);
        spinnerQtdJogos = (Spinner) findViewById(R.id.spinnerQtdJogos);
        spinnerTemas = (Spinner) findViewById(R.id.spinnerTemas);

        adapterQtdJogos = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayQtdJogos);
        adapterQtdJogos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerQtdJogos.setAdapter(adapterQtdJogos);
        spinnerQtdJogos.setSelection(Integer.parseInt(preferencias.getQTD_ULTIMOS_JOGOS())-5);

        adapterTemas = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayTemas);
        adapterTemas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTemas.setAdapter(adapterTemas);
        spinnerTemas.setSelection(Integer.parseInt(preferencias.getTema()));

        //MUDANÇA DA QUANTIDADE DE JOGOS
        spinnerQtdJogos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                preferencias.salvarQtdUltimosJogos(String.valueOf(position+5));
                if (!primeira_vez) {
                    Toast.makeText(ConfiguracaoActivity.this, "Configuracao salva com sucesso!!!", Toast.LENGTH_SHORT).show();
                }
                primeira_vez = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //MUDANÇA DE TEMA
        spinnerTemas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                preferencias.salvarTema(String.valueOf(position));
                if (!primeira_vez) {
                    Toast.makeText(ConfiguracaoActivity.this, "Configuracao salva com sucesso!!!", Toast.LENGTH_SHORT).show();
                }
                primeira_vez = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //CLIQUE BOTAO LIMPAR DADOS DE ACESSO
        btnLimparDadosAcesso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferencias.limparDadosAcesso();
                Toast.makeText(ConfiguracaoActivity.this, "Operação bem sucedida!!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
