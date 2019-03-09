package br.com.topspin.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.topspin.R;
import br.com.topspin.bean.BeanChaveValor;
import br.com.topspin.constantes.Constantes;
import br.com.topspin.firebase.dao.AvaliacaoDAO;
import br.com.topspin.firebase.dao.UsuarioDAO;
import br.com.topspin.helper.AvaliacaoUtil;
import br.com.topspin.helper.Preferencias;
import br.com.topspin.listeners.activity.AvaliacaoOnSeekBarChangeListeners;
import br.com.topspin.model.Avaliacao;
import br.com.topspin.model.Usuario;
import br.com.topspin.util.UtilACT;

public class AvaliacaoResumidaActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Spinner spinnerEstados;
    private Spinner spinnerJogadores;
    //private AutoCompleteTextView nomePesquisa;

    private ArrayAdapter<String> adapterEstados;
    private ArrayAdapter<String> adapterJogadores;
    private ArrayAdapter<String> adapterNomes;

    private TextView txtSaqueRuim;
    private TextView txtSaqueRegular;
    private TextView txtSaqueBom;
    private TextView txtSaqueOtimo;

    private TextView txtForehandRuim;
    private TextView txtForehandRegular;
    private TextView txtForehandBom;
    private TextView txtForehandOtimo;

    private TextView txtBackhandRuim;
    private TextView txtBackhandRegular;
    private TextView txtBackhandBom;
    private TextView txtBackhandOtimo;

    private TextView txtVoleioRuim;
    private TextView txtVoleioRegular;
    private TextView txtVoleioBom;
    private TextView txtVoleioOtimo;

    private TextView txtSmashRuim;
    private TextView txtSmashRegular;
    private TextView txtSmashBom;
    private TextView txtSmashOtimo;

    private TextView txtOfensivoPouco;
    private TextView txtOfensivoNormal;
    private TextView txtOfensivoMuito;

    private TextView txtDefensivoPouco;
    private TextView txtDefensivoNormal;
    private TextView txtDefensivoMuito;

    private TextView txtTaticoPouco;
    private TextView txtTaticoNormal;
    private TextView txtTaticoMuito;

    private TextView txtCompetitivoPouco;
    private TextView txtCompetitivoNormal;
    private TextView txtCompetitivoMuito;

    private TextView txtFisicoRuim;
    private TextView txtFisicoRegular;
    private TextView txtFisicoBom;
    private TextView txtFisicoOtimo;

    private SeekBar skbSaque;
    private SeekBar skbForehand;
    private SeekBar skbBackhand;
    private SeekBar skbVoleio;
    private SeekBar skbSmash;
    private SeekBar skbOfensivo;
    private SeekBar skbDefensivo;
    private SeekBar skbTatico;
    private SeekBar skbCompetitivo;
    private SeekBar skbFisico;

    private String qtdAvaliacoesPendentes;

    private Button btnSalvarAvaliacao;

    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private AvaliacaoDAO avaliacaoDAO;
    private List<String> listaDeNomes;
    private List<BeanChaveValor> listaDeJogadores;

    private Avaliacao avaliacao;
    private Preferencias preferencias;
    private String idDoAvaliado;
    private String nomeDoAvaliado;

    private String nomeExtra;
    private String estadoExtra;
    private String idUsuarioLogado;

    private ValueEventListener valueEventListenerBuscaTodosOsUsuarios;
    private ValueEventListener valueEventListenerSingleQtdAvaliacoesPendentes;

    @Override
    protected void onStop() {
        super.onStop();
        usuarioDAO.buscaTodosOsUsuarios().removeEventListener(valueEventListenerBuscaTodosOsUsuarios);
        if (avaliacaoDAO != null) {
            avaliacaoDAO.buscaQtdAvaliacoesPendentes().removeEventListener(valueEventListenerSingleQtdAvaliacoesPendentes);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avaliacao_resumida);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("AVALIAÇÃO");
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        preferencias = new Preferencias(AvaliacaoResumidaActivity.this);
        idUsuarioLogado = preferencias.getIdUsuarioLogado();
        if ("0".equals(preferencias.getTema())) {
            toolbar.setBackgroundColor(Color.parseColor(Constantes.QUADRA_RAPIDA));
        }else if ("1".equals(preferencias.getTema())) {
            toolbar.setBackgroundColor(Color.parseColor(Constantes.SAIBRO));
        }else{
            toolbar.setBackgroundColor(Color.parseColor(Constantes.GRAMA));
        }

        listaDeNomes = new ArrayList<String>();
        listaDeJogadores = new ArrayList<BeanChaveValor>();

        spinnerEstados = (Spinner) findViewById(R.id.spinnerEstados);
        adapterEstados = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Constantes.ESTADOS);
        adapterEstados.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEstados.setAdapter(adapterEstados);

        spinnerJogadores = (Spinner) findViewById(R.id.spinnerJogadores);
        adapterJogadores = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listaDeNomes);
        adapterJogadores.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJogadores.setAdapter(adapterJogadores);

        /*
        adapterNomes = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, listaDeNomes);
        nomePesquisa = (AutoCompleteTextView) findViewById(R.id.nomePesquisaAutoComplete);
        nomePesquisa.setAdapter(adapterNomes);
        */

        txtSaqueRuim = (TextView) findViewById(R.id.txtSaqueRuim);
        txtSaqueRegular = (TextView) findViewById(R.id.txtSaqueRegular);
        txtSaqueBom = (TextView) findViewById(R.id.txtSaqueBom);
        txtSaqueOtimo = (TextView) findViewById(R.id.txtSaqueOtimo);

        txtForehandRuim = (TextView) findViewById(R.id.txtForehandRuim);
        txtForehandRegular = (TextView) findViewById(R.id.txtForehandRegular);
        txtForehandBom = (TextView) findViewById(R.id.txtForehandBom);
        txtForehandOtimo = (TextView) findViewById(R.id.txtForehandOtimo);

        txtBackhandRuim = (TextView) findViewById(R.id.txtBackhandRuim);
        txtBackhandRegular = (TextView) findViewById(R.id.txtBackhandRegular);
        txtBackhandBom = (TextView) findViewById(R.id.txtBackhandBom);
        txtBackhandOtimo = (TextView) findViewById(R.id.txtBackhandOtimo);

        txtVoleioRuim = (TextView) findViewById(R.id.txtVoleioRuim);
        txtVoleioRegular = (TextView) findViewById(R.id.txtVoleioRegular);
        txtVoleioBom = (TextView) findViewById(R.id.txtVoleioBom);
        txtVoleioOtimo = (TextView) findViewById(R.id.txtVoleioOtimo);

        txtSmashRuim = (TextView) findViewById(R.id.txtSmashRuim);
        txtSmashRegular = (TextView) findViewById(R.id.txtSmashRegular);
        txtSmashBom = (TextView) findViewById(R.id.txtSmashBom);
        txtSmashOtimo = (TextView) findViewById(R.id.txtSmashOtimo);

        txtOfensivoPouco = (TextView) findViewById(R.id.txtOfensivoPouco);
        txtOfensivoNormal = (TextView) findViewById(R.id.txtOfensivoNormal);
        txtOfensivoMuito = (TextView) findViewById(R.id.txtOfensivoMuito);

        txtDefensivoPouco = (TextView) findViewById(R.id.txtDefensivoPouco);
        txtDefensivoNormal = (TextView) findViewById(R.id.txtDefensivoNormal);
        txtDefensivoMuito = (TextView) findViewById(R.id.txtDefensivoMuito);

        txtTaticoPouco = (TextView) findViewById(R.id.txtTaticoPouco);
        txtTaticoNormal = (TextView) findViewById(R.id.txtTaticoNormal);
        txtTaticoMuito = (TextView) findViewById(R.id.txtTaticoMuito);

        txtCompetitivoPouco = (TextView) findViewById(R.id.txtCompetitivoPouco);
        txtCompetitivoNormal = (TextView) findViewById(R.id.txtCompetitivoNormal);
        txtCompetitivoMuito = (TextView) findViewById(R.id.txtCompetitivoMuito);

        txtFisicoRuim = (TextView) findViewById(R.id.txtFisicoRuim);
        txtFisicoRegular = (TextView) findViewById(R.id.txtFisicoRegular);
        txtFisicoBom = (TextView) findViewById(R.id.txtFisicoBom);
        txtFisicoOtimo = (TextView) findViewById(R.id.txtFisicoOtimo);

        skbSaque = (SeekBar) findViewById(R.id.skbSaque);
        skbForehand = (SeekBar) findViewById(R.id.skbForehand);
        skbBackhand = (SeekBar) findViewById(R.id.skbBackhand);
        skbVoleio = (SeekBar) findViewById(R.id.skbVoleio);
        skbSmash = (SeekBar) findViewById(R.id.skbSmash);
        skbOfensivo = (SeekBar) findViewById(R.id.skbOfensivo);
        skbDefensivo = (SeekBar) findViewById(R.id.skbDefensivo);
        skbTatico = (SeekBar) findViewById(R.id.skbTatico);
        skbCompetitivo = (SeekBar) findViewById(R.id.skbCompetitivo);
        skbFisico = (SeekBar) findViewById(R.id.skbFisico);

        iniciarAvaliacoes();

        btnSalvarAvaliacao = (Button) findViewById(R.id.btnSalvarAvaliacao);

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            nomeExtra = extra.getString("nome");
            estadoExtra = extra.getString("estado").substring(0,2);
            spinnerEstados.setSelection(UtilACT.buscaIndiceDoEstado(estadoExtra));
            idDoAvaliado = extra.getString("id");
        }

        //QUANDO SELECIONAR UM VALOR DO SPINNER ESTADOS
        spinnerEstados.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //nomePesquisa.setText("");
                listaDeNomes = new ArrayList<String>();
                listaDeJogadores = new ArrayList<BeanChaveValor>();
                //usuariosPorEstadoChildEventListener = new UsuariosPorEstadoChildEventListener(spinnerEstados.getSelectedItemId(), listaDeNomes, listaDeJogadores);

                //LISTENERS NA PROPRIA ACTIVITY
                valueEventListenerBuscaTodosOsUsuarios = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String estadoSelecionado = UtilACT.buscaEstadoDoIndice(spinnerEstados.getSelectedItemId());
                        for (DataSnapshot dados: dataSnapshot.getChildren()) {
                            Usuario usuario = dados.getValue(Usuario.class);
                            String estadoDoUsuario = UtilACT.retornaDadoFormatadoParaEstado(usuario.getEstado(), true);
                            if (estadoSelecionado.equals(estadoDoUsuario)) {
                                if (!dados.getKey().equals(idUsuarioLogado)) {
                                    listaDeNomes.add(usuario.getNome());

                                    BeanChaveValor beanChaveValor = new BeanChaveValor();
                                    beanChaveValor.setChave(dados.getKey());
                                    beanChaveValor.setValor(usuario.getNome());
                                    listaDeJogadores.add(beanChaveValor);
                                }
                            }
                        }
                        //adapterNomes.notifyDataSetChanged();
                        adapterNomes = new ArrayAdapter<String>(AvaliacaoResumidaActivity.this, android.R.layout.simple_spinner_dropdown_item, listaDeNomes);
                        //nomePesquisa.setAdapter(adapterNomes);

                        adapterJogadores = new ArrayAdapter<String>(AvaliacaoResumidaActivity.this, android.R.layout.simple_spinner_dropdown_item, listaDeNomes);
                        spinnerJogadores.setAdapter(adapterJogadores);

                        if (nomeExtra != null) {
                            for (int i=0; i<listaDeNomes.size(); i++) {
                                String jogador = listaDeNomes.get(i);
                                if (jogador.equals(nomeExtra)) {
                                    spinnerJogadores.setSelection(i);
                                }
                            }
                        }

                        Log.w("", "getUser:onChildAdded");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
                usuarioDAO.buscaTodosOsUsuarios().addValueEventListener(valueEventListenerBuscaTodosOsUsuarios);

                Log.w("", "OnItemSelected.");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //QUANDO SELECIONAR UM VALOR DO SPINNER JOGADOR
        spinnerJogadores.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
               @Override
               public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                   if (listaDeJogadores.size() > 0) {
                       BeanChaveValor bcv = (BeanChaveValor) listaDeJogadores.get(position);
                       idDoAvaliado = bcv.getChave();
                       nomeDoAvaliado = bcv.getValor();
                   }
               }

               @Override
               public void onNothingSelected(AdapterView<?> parent) {

               }
           });


        //CLIQUE DO BOTAO SALVAR AVALIACAO
        btnSalvarAvaliacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (spinnerJogadores.getSelectedItem() == null || idDoAvaliado == null || "".equals(idDoAvaliado)) {
                    Toast.makeText(AvaliacaoResumidaActivity.this, "Selecione o atleta a ser avaliado!!!", Toast.LENGTH_LONG).show();

                }else {
                    avaliacao = new Avaliacao();
                    avaliacao.setIdDoAvaliador(preferencias.getIdUsuarioLogado());

                    avaliacao.setId(idDoAvaliado);
                    avaliacao.setStatus("P");
                    avaliacao.setSaqueProgress(skbSaque.getProgress());
                    avaliacao.setForehandProgress(skbForehand.getProgress());
                    avaliacao.setBackhandProgress(skbBackhand.getProgress());
                    avaliacao.setVoleioProgress(skbVoleio.getProgress());
                    avaliacao.setSmashProgress(skbSmash.getProgress());
                    avaliacao.setOfensivoProgress(skbOfensivo.getProgress());
                    avaliacao.setDefensivoProgress(skbDefensivo.getProgress());
                    avaliacao.setTaticoProgress(skbTatico.getProgress());
                    avaliacao.setCompetitivoProgress(skbCompetitivo.getProgress());
                    avaliacao.setFisicoProgress(skbFisico.getProgress());

                    //QUANTIDADE DE AVALIACOES PENDENTES
                    valueEventListenerSingleQtdAvaliacoesPendentes = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            qtdAvaliacoesPendentes = dataSnapshot.getValue(String.class);
                            avaliacao.setQtdAvaliacoesPendentes(AvaliacaoUtil.salvaAvaliacaoPendente(qtdAvaliacoesPendentes));
                            cadastrarAvaliacao();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };
                    avaliacaoDAO = new AvaliacaoDAO(idDoAvaliado);
                    avaliacaoDAO.buscaQtdAvaliacoesPendentes().addListenerForSingleValueEvent(valueEventListenerSingleQtdAvaliacoesPendentes);

                }

            }
        });

        //DINAMICAS DAS AVALIACOES
        AvaliacaoOnSeekBarChangeListeners avaliacaoOnSeekBarChangeListenersSaque = new AvaliacaoOnSeekBarChangeListeners(skbSaque, txtSaqueRuim, txtSaqueRegular, txtSaqueBom, txtSaqueOtimo, 4);
        skbSaque.setOnSeekBarChangeListener(avaliacaoOnSeekBarChangeListenersSaque);

        AvaliacaoOnSeekBarChangeListeners avaliacaoOnSeekBarChangeListenersForehand = new AvaliacaoOnSeekBarChangeListeners(skbForehand, txtForehandRuim, txtForehandRegular, txtForehandBom, txtForehandOtimo, 4);
        skbForehand.setOnSeekBarChangeListener(avaliacaoOnSeekBarChangeListenersForehand);

        AvaliacaoOnSeekBarChangeListeners avaliacaoOnSeekBarChangeListenersBackhand = new AvaliacaoOnSeekBarChangeListeners(skbBackhand, txtBackhandRuim, txtBackhandRegular, txtBackhandBom, txtBackhandOtimo, 4);
        skbBackhand.setOnSeekBarChangeListener(avaliacaoOnSeekBarChangeListenersBackhand);

        AvaliacaoOnSeekBarChangeListeners avaliacaoOnSeekBarChangeListenersVoleio = new AvaliacaoOnSeekBarChangeListeners(skbVoleio, txtVoleioRuim, txtVoleioRegular, txtVoleioBom, txtVoleioOtimo, 4);
        skbVoleio.setOnSeekBarChangeListener(avaliacaoOnSeekBarChangeListenersVoleio);

        AvaliacaoOnSeekBarChangeListeners avaliacaoOnSeekBarChangeListenersSmash = new AvaliacaoOnSeekBarChangeListeners(skbSmash, txtSmashRuim, txtSmashRegular, txtSmashBom, txtSmashOtimo, 4);
        skbSmash.setOnSeekBarChangeListener(avaliacaoOnSeekBarChangeListenersSmash);

        AvaliacaoOnSeekBarChangeListeners avaliacaoOnSeekBarChangeListenersOfensivo = new AvaliacaoOnSeekBarChangeListeners(skbOfensivo, txtOfensivoPouco, txtOfensivoNormal, txtOfensivoMuito, 3);
        skbOfensivo.setOnSeekBarChangeListener(avaliacaoOnSeekBarChangeListenersOfensivo);

        AvaliacaoOnSeekBarChangeListeners avaliacaoOnSeekBarChangeListenersDefensivo = new AvaliacaoOnSeekBarChangeListeners(skbDefensivo, txtDefensivoPouco, txtDefensivoNormal, txtDefensivoMuito, 3);
        skbDefensivo.setOnSeekBarChangeListener(avaliacaoOnSeekBarChangeListenersDefensivo);

        AvaliacaoOnSeekBarChangeListeners avaliacaoOnSeekBarChangeListenersTatico = new AvaliacaoOnSeekBarChangeListeners(skbTatico, txtTaticoPouco, txtTaticoNormal, txtTaticoMuito, 3);
        skbTatico.setOnSeekBarChangeListener(avaliacaoOnSeekBarChangeListenersTatico);

        AvaliacaoOnSeekBarChangeListeners avaliacaoOnSeekBarChangeListenersCompetitivo = new AvaliacaoOnSeekBarChangeListeners(skbCompetitivo, txtCompetitivoPouco, txtCompetitivoNormal, txtCompetitivoMuito, 3);
        skbCompetitivo.setOnSeekBarChangeListener(avaliacaoOnSeekBarChangeListenersCompetitivo);

        AvaliacaoOnSeekBarChangeListeners avaliacaoOnSeekBarChangeListenersFisico = new AvaliacaoOnSeekBarChangeListeners(skbFisico, txtFisicoRuim, txtFisicoRegular, txtFisicoBom, txtFisicoOtimo, 4);
        skbFisico.setOnSeekBarChangeListener(avaliacaoOnSeekBarChangeListenersFisico);

    }

    private String[] carregaJogadores(List<String> nomes, String[] jogadores) {
        for (int i=0; i<nomes.size(); i++) {
            String nome = nomes.get(i);
            jogadores[i] = nome;
        }
        return jogadores;
    }

    private void abrirTelaPrincipal() {
        Intent intent = new Intent(AvaliacaoResumidaActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void cadastrarAvaliacao() {
        avaliacao.salvar();

        finish();
        Toast.makeText(AvaliacaoResumidaActivity.this, "Avaliação salva com sucesso!!!", Toast.LENGTH_LONG).show();
    }

    private void iniciarAvaliacoes() {

        txtSaqueBom.setTextColor(Color.parseColor(Constantes.C_BOM));
        txtForehandBom.setTextColor(Color.parseColor(Constantes.C_BOM));
        txtBackhandBom.setTextColor(Color.parseColor(Constantes.C_BOM));
        txtVoleioBom.setTextColor(Color.parseColor(Constantes.C_BOM));
        txtSmashBom.setTextColor(Color.parseColor(Constantes.C_BOM));

        txtOfensivoNormal.setTextColor(Color.parseColor(Constantes.C_NORMAL));
        txtDefensivoNormal.setTextColor(Color.parseColor(Constantes.C_NORMAL));
        txtTaticoNormal.setTextColor(Color.parseColor(Constantes.C_NORMAL));
        txtCompetitivoNormal.setTextColor(Color.parseColor(Constantes.C_NORMAL));
        txtFisicoBom.setTextColor(Color.parseColor(Constantes.C_BOM));

        skbSaque.setProgress(2);
        skbForehand.setProgress(2);
        skbBackhand.setProgress(2);
        skbVoleio.setProgress(2);
        skbSmash.setProgress(2);
        skbOfensivo.setProgress(1);
        skbDefensivo.setProgress(1);
        skbTatico.setProgress(1);
        skbCompetitivo.setProgress(1);
        skbFisico.setProgress(2);
    }

}
