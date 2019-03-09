package br.com.topspin.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import br.com.topspin.R;
import br.com.topspin.config.ConfiguracaoFirebase;
import br.com.topspin.constantes.Constantes;
import br.com.topspin.firebase.dao.AvaliacaoDAO;
import br.com.topspin.firebase.dao.ConviteDAO;
import br.com.topspin.firebase.dao.ResultadoJogoDAO;
import br.com.topspin.firebase.dao.UsuarioDAO;
import br.com.topspin.helper.Preferencias;
import br.com.topspin.helper.TS_Helper;
import br.com.topspin.model.Usuario;
import br.com.topspin.util.UtilACT;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    //private FirebaseAuth.AuthStateListener mAuthListener;

    private Toolbar toolbar;
    private LinearLayout layoutLinearMain;

    private TextView txtApelido;
    private TextView txtQuantidadesRecebidas;
    private TextView txtQuantidadesPendentes;
    private TextView txtQuantidadesJogos;
    private ImageView imgTipo;
    private TextView txtQtdAvalRecebida;
    private TextView txtQtdAvalPendente;
    private TextView txtQtdJogos;
    private TextView txtUltimosJogos;

    private int qtdUltimosJogos;
    private String qtdJogos;
    private String qtdAvaliacoesRecebidas;
    private String qtdAvaliacoesPendentes;

    private LinearLayout layoutDados;
    private LinearLayout layoutConvites;
    private LinearLayout layoutJogos;
    private LinearLayout layoutAvaliacoes;
    private LinearLayout layoutPendentes;
    private LinearLayout layoutEstatisticas;

    private ImageView imgJogo1;
    private ImageView imgJogo2;
    private ImageView imgJogo3;
    private ImageView imgJogo4;
    private ImageView imgJogo5;
    private ImageView imgJogo6;
    private ImageView imgJogo7;
    private ImageView imgJogo8;
    private ImageView imgJogo9;
    private ImageView imgJogo10;

    private String sequenciaUltimosJogos;

    private String idUsuarioLogado;

    private Preferencias preferencias;
    private UsuarioDAO usuarioDAO;
    private ResultadoJogoDAO resultadoJogoDAO;
    private AvaliacaoDAO avaliacaoDAO;
    private ConviteDAO conviteDAO = new ConviteDAO();

    private ValueEventListener valueEventListenerSingleUsuarios;
    private ValueEventListener valueEventListenerSingleUltimosJogos;
    private ValueEventListener valueEventListenerSingleQtdAvaliacoesRecebidas;
    private ValueEventListener valueEventListenerSingleQtdAvaliacoesPendentes;
    private ValueEventListener valueEventListenerSingleQtdJogos;
    private ValueEventListener valueEventListenerBuscaTodosOsConvitesPendentesDeUmCliente;

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        autenticacao.signOut();
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //autenticacao.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        resultadoJogoDAO.buscaResultadoUltimosJogosDoUsuario().removeEventListener(valueEventListenerSingleUltimosJogos);
        usuarioDAO.buscaUsuarioPorId(idUsuarioLogado).removeEventListener(valueEventListenerSingleUsuarios);
        avaliacaoDAO.buscaQtdAvaliacoesRecebidas().removeEventListener(valueEventListenerSingleQtdAvaliacoesRecebidas);
        avaliacaoDAO.buscaQtdAvaliacoesPendentes().removeEventListener(valueEventListenerSingleQtdAvaliacoesPendentes);
        resultadoJogoDAO.buscaQtdJogosDoUsuario().removeEventListener(valueEventListenerSingleQtdJogos);
        if (valueEventListenerBuscaTodosOsConvitesPendentesDeUmCliente != null) {
            conviteDAO.buscaTodosOsConvitesPendentesDeUmUsuario(idUsuarioLogado).removeEventListener(valueEventListenerBuscaTodosOsConvitesPendentesDeUmCliente);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        autenticacao = ConfiguracaoFirebase.getAutenticacao();
        /*
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }

            }
        };*/

        preferencias = new Preferencias(MainActivity.this);
        if (preferencias.isPrimeiroAcesso()) {
            preferencias.salvarPrimeiroAcesso(false);
            startActivity(new Intent(MainActivity.this, PassosActivity.class));
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("TOPSPIN");
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(toolbar);

        layoutLinearMain = (LinearLayout) findViewById(R.id.layoutLinearMain);

        txtApelido = (TextView) findViewById(R.id.txtApelido);
        txtQuantidadesRecebidas = (TextView) findViewById(R.id.txtQuantidadesRecebidas);
        txtQuantidadesPendentes = (TextView) findViewById(R.id.txtQuantidadesPendentes);
        txtQuantidadesJogos = (TextView) findViewById(R.id.txtQuantidadesJogos);
        imgTipo = (ImageView) findViewById(R.id.imgTipo);

        txtQtdAvalRecebida = (TextView) findViewById(R.id.txtQtdAvalRecebida);
        txtQtdAvalPendente = (TextView) findViewById(R.id.txtQtdAvalPendente);
        txtQtdJogos = (TextView) findViewById(R.id.txtQtdJogos);
        txtUltimosJogos = (TextView) findViewById(R.id.txtUltimosJogos);

        layoutDados = (LinearLayout) findViewById(R.id.layout_dados);
        layoutConvites = (LinearLayout) findViewById(R.id.layout_convites);
        layoutJogos = (LinearLayout) findViewById(R.id.layout_jogos);
        layoutAvaliacoes = (LinearLayout) findViewById(R.id.layout_avaliacoes);
        layoutPendentes = (LinearLayout) findViewById(R.id.layout_pendentes);
        layoutEstatisticas = (LinearLayout) findViewById(R.id.layout_estatisticas);

        if ("0".equals(preferencias.getTema())) {
            layoutLinearMain.setBackgroundResource(R.drawable.act_dados_rapida);
            txtApelido.setTextColor(Color.parseColor(Constantes.WHITE));
            txtQuantidadesRecebidas.setTextColor(Color.parseColor(Constantes.WHITE));
            txtQuantidadesPendentes.setTextColor(Color.parseColor(Constantes.WHITE));
            txtQuantidadesJogos.setTextColor(Color.parseColor(Constantes.WHITE));
            txtQtdAvalRecebida.setTextColor(Color.parseColor(Constantes.WHITE));
            txtQtdAvalPendente.setTextColor(Color.parseColor(Constantes.WHITE));
            txtQtdJogos.setTextColor(Color.parseColor(Constantes.WHITE));
            toolbar.setBackgroundColor(Color.parseColor(Constantes.QUADRA_RAPIDA));

        }else if ("1".equals(preferencias.getTema())) {
            layoutLinearMain.setBackgroundResource(R.drawable.act_dados_saibro);
            txtApelido.setTextColor(Color.parseColor(Constantes.BLACK));
            txtQuantidadesRecebidas.setTextColor(Color.parseColor(Constantes.BLACK));
            txtQuantidadesPendentes.setTextColor(Color.parseColor(Constantes.BLACK));
            txtQuantidadesJogos.setTextColor(Color.parseColor(Constantes.BLACK));
            txtQtdAvalRecebida.setTextColor(Color.parseColor(Constantes.BLACK));
            txtQtdAvalPendente.setTextColor(Color.parseColor(Constantes.BLACK));
            txtQtdJogos.setTextColor(Color.parseColor(Constantes.BLACK));
            toolbar.setBackgroundColor(Color.parseColor(Constantes.SAIBRO));

        }else{
            layoutLinearMain.setBackgroundResource(R.drawable.act_dados_grama);
            txtApelido.setTextColor(Color.parseColor(Constantes.WHITE));
            txtQuantidadesRecebidas.setTextColor(Color.parseColor(Constantes.WHITE));
            txtQuantidadesPendentes.setTextColor(Color.parseColor(Constantes.WHITE));
            txtQuantidadesJogos.setTextColor(Color.parseColor(Constantes.WHITE));
            txtQtdAvalRecebida.setTextColor(Color.parseColor(Constantes.WHITE));
            txtQtdAvalPendente.setTextColor(Color.parseColor(Constantes.WHITE));
            txtQtdJogos.setTextColor(Color.parseColor(Constantes.WHITE));
            toolbar.setBackgroundColor(Color.parseColor(Constantes.GRAMA));
        }

        idUsuarioLogado = preferencias.getIdUsuarioLogado();

        usuarioDAO = new UsuarioDAO();
        resultadoJogoDAO = new ResultadoJogoDAO(idUsuarioLogado);
        avaliacaoDAO = new AvaliacaoDAO(idUsuarioLogado);

        //USUARIOS
        valueEventListenerSingleUsuarios = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                if (dataSnapshot.getValue() != null) {
                    txtApelido.setText(UtilACT.retornaApelidoPadraoAPartirDoNome(usuario.getNome()));
                    //txtNome.setText(UtilACT.retornaDadoFormatadoParaDoisValores(usuario.getNome(), UtilACT.retornaDadoFormatado(usuario.getIdade(), " ANOS", null), null));
                    //txtCidade.setText(UtilACT.retornaDadoFormatadoParaEstado(usuario.getEstado(), false));
                    //txtEstado.setText(usuario.getCidade());
                    int idDaImagem;
                    if (usuario.getTipo() == null) {
                        idDaImagem = getResources().getIdentifier("act_destro", "drawable",  getPackageName());
                    }else if ("DESTRO".equals(usuario.getTipo().toUpperCase())) {
                        idDaImagem = getResources().getIdentifier("act_destro", "drawable",  getPackageName());
                    }else {
                        idDaImagem = getResources().getIdentifier("act_canhoto", "drawable",  getPackageName());
                    }
                    //imgTipo.setImageResource(idDaImagem);

                    preferencias.setNomeDoUsuarioLogado(usuario.getNome());
                    preferencias.setApelidoDoUsuarioLogado(usuario.getApelido());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        usuarioDAO.buscaUsuarioPorId(idUsuarioLogado).addListenerForSingleValueEvent(valueEventListenerSingleUsuarios);

        imgJogo1 = (ImageView) findViewById(R.id.imgJogo1);
        imgJogo2 = (ImageView) findViewById(R.id.imgJogo2);
        imgJogo3 = (ImageView) findViewById(R.id.imgJogo3);
        imgJogo4 = (ImageView) findViewById(R.id.imgJogo4);
        imgJogo5 = (ImageView) findViewById(R.id.imgJogo5);
        imgJogo6 = (ImageView) findViewById(R.id.imgJogo6);
        imgJogo7 = (ImageView) findViewById(R.id.imgJogo7);
        imgJogo8 = (ImageView) findViewById(R.id.imgJogo8);
        imgJogo9 = (ImageView) findViewById(R.id.imgJogo9);
        imgJogo10 = (ImageView) findViewById(R.id.imgJogo10);
        qtdUltimosJogos = Integer.parseInt(preferencias.getQTD_ULTIMOS_JOGOS());

        resultadoJogoDAO = new ResultadoJogoDAO(preferencias.getIdUsuarioLogado());

        //ULTIMOS JOGOS
        valueEventListenerSingleUltimosJogos = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sequenciaUltimosJogos = dataSnapshot.getValue(String.class);
                imgJogo1.setVisibility(View.INVISIBLE);
                imgJogo2.setVisibility(View.INVISIBLE);
                imgJogo3.setVisibility(View.INVISIBLE);
                imgJogo4.setVisibility(View.INVISIBLE);
                imgJogo5.setVisibility(View.INVISIBLE);
                imgJogo6.setVisibility(View.INVISIBLE);
                imgJogo7.setVisibility(View.INVISIBLE);
                imgJogo8.setVisibility(View.INVISIBLE);
                imgJogo9.setVisibility(View.INVISIBLE);
                imgJogo10.setVisibility(View.INVISIBLE);
                if (sequenciaUltimosJogos != null && !sequenciaUltimosJogos.isEmpty()) {
                    for (int i = 0; i < sequenciaUltimosJogos.length(); i++) {
                        int idDaImagem;
                        if (i == 0) {
                            if (sequenciaUltimosJogos.substring(i, i+1).equals("V")) {
                                idDaImagem = getResources().getIdentifier("ic_vitoria", "drawable",  getPackageName());
                            } else {
                                idDaImagem = getResources().getIdentifier("ic_derrota", "drawable",  getPackageName());
                            }
                            imgJogo1.setImageResource(idDaImagem);
                            imgJogo1.setVisibility(View.VISIBLE);
                            if (qtdUltimosJogos == (i + 1)) {
                                break;
                            }
                        }else if (i == 1) {
                            if (sequenciaUltimosJogos.substring(i, i+1).equals("V")) {
                                idDaImagem = getResources().getIdentifier("ic_vitoria", "drawable",  getPackageName());
                            } else {
                                idDaImagem = getResources().getIdentifier("ic_derrota", "drawable",  getPackageName());
                            }
                            imgJogo2.setImageResource(idDaImagem);
                            imgJogo2.setVisibility(View.VISIBLE);
                            if (qtdUltimosJogos == (i + 1)) {
                                break;
                            }
                        }else if (i == 2) {
                            if (sequenciaUltimosJogos.substring(i, i+1).equals("V")) {
                                idDaImagem = getResources().getIdentifier("ic_vitoria", "drawable",  getPackageName());
                            } else {
                                idDaImagem = getResources().getIdentifier("ic_derrota", "drawable",  getPackageName());
                            }
                            imgJogo3.setImageResource(idDaImagem);
                            imgJogo3.setVisibility(View.VISIBLE);
                            if (qtdUltimosJogos == (i + 1)) {
                                break;
                            }
                        }else if (i == 3) {
                            if (sequenciaUltimosJogos.substring(i, i+1).equals("V")) {
                                idDaImagem = getResources().getIdentifier("ic_vitoria", "drawable",  getPackageName());
                            } else {
                                idDaImagem = getResources().getIdentifier("ic_derrota", "drawable",  getPackageName());
                            }
                            imgJogo4.setImageResource(idDaImagem);
                            imgJogo4.setVisibility(View.VISIBLE);
                            if (qtdUltimosJogos == (i + 1)) {
                                break;
                            }
                        }else if (i == 4) {
                            if (sequenciaUltimosJogos.substring(i, i+1).equals("V")) {
                                idDaImagem = getResources().getIdentifier("ic_vitoria", "drawable",  getPackageName());
                            } else {
                                idDaImagem = getResources().getIdentifier("ic_derrota", "drawable",  getPackageName());
                            }
                            imgJogo5.setImageResource(idDaImagem);
                            imgJogo5.setVisibility(View.VISIBLE);
                            if (qtdUltimosJogos == (i + 1)) {
                                break;
                            }
                        }else if (i == 5) {
                            if (sequenciaUltimosJogos.substring(i, i+1).equals("V")) {
                                idDaImagem = getResources().getIdentifier("ic_vitoria", "drawable",  getPackageName());
                            } else {
                                idDaImagem = getResources().getIdentifier("ic_derrota", "drawable",  getPackageName());
                            }
                            imgJogo6.setImageResource(idDaImagem);
                            imgJogo6.setVisibility(View.VISIBLE);
                            if (qtdUltimosJogos == (i + 1)) {
                                break;
                            }
                        }else if (i == 6) {
                            if (sequenciaUltimosJogos.substring(i, i+1).equals("V")) {
                                idDaImagem = getResources().getIdentifier("ic_vitoria", "drawable",  getPackageName());
                            } else {
                                idDaImagem = getResources().getIdentifier("ic_derrota", "drawable",  getPackageName());
                            }
                            imgJogo7.setVisibility(View.VISIBLE);
                            if (qtdUltimosJogos == (i + 1)) {
                                break;
                            }
                        }else if (i == 7) {
                            if (sequenciaUltimosJogos.substring(i, i+1).equals("V")) {
                                idDaImagem = getResources().getIdentifier("ic_vitoria", "drawable",  getPackageName());
                            } else {
                                idDaImagem = getResources().getIdentifier("ic_derrota", "drawable",  getPackageName());
                            }
                            imgJogo8.setImageResource(idDaImagem);
                            imgJogo8.setVisibility(View.VISIBLE);
                            if (qtdUltimosJogos == (i + 1)) {
                                break;
                            }
                        }else if (i == 8) {
                            if (sequenciaUltimosJogos.substring(i, i+1).equals("V")) {
                                idDaImagem = getResources().getIdentifier("ic_vitoria", "drawable",  getPackageName());
                            } else {
                                idDaImagem = getResources().getIdentifier("ic_derrota", "drawable",  getPackageName());
                            }
                            imgJogo9.setImageResource(idDaImagem);
                            imgJogo9.setVisibility(View.VISIBLE);
                            if (qtdUltimosJogos == (i + 1)) {
                                break;
                            }
                        }else if (i == 9) {
                            if (sequenciaUltimosJogos.substring(i).equals("V")) {
                                idDaImagem = getResources().getIdentifier("ic_vitoria", "drawable",  getPackageName());
                            } else {
                                idDaImagem = getResources().getIdentifier("ic_derrota", "drawable",  getPackageName());
                            }
                            imgJogo10.setImageResource(idDaImagem);
                            imgJogo10.setVisibility(View.VISIBLE);
                            if (qtdUltimosJogos == (i + 1)) {
                                break;
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        resultadoJogoDAO.buscaResultadoUltimosJogosDoUsuario().addListenerForSingleValueEvent(valueEventListenerSingleUltimosJogos);

        //QUANTIDADE DE AVALIACOES PENDENTES
        valueEventListenerSingleQtdAvaliacoesPendentes = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                qtdAvaliacoesPendentes = dataSnapshot.getValue(String.class);
                if (qtdAvaliacoesPendentes == null) {
                    txtQtdAvalPendente.setText("0");
                }else {
                    txtQtdAvalPendente.setText(qtdAvaliacoesPendentes);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        avaliacaoDAO.buscaQtdAvaliacoesPendentes().addListenerForSingleValueEvent(valueEventListenerSingleQtdAvaliacoesPendentes);

        //QUANTIDADE DE AVALIACOES RECEBIDAS
        valueEventListenerSingleQtdAvaliacoesRecebidas = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                qtdAvaliacoesRecebidas = dataSnapshot.getValue(String.class);
                if (qtdAvaliacoesRecebidas == null) {
                    txtQtdAvalRecebida.setText("0");
                }else {
                    txtQtdAvalRecebida.setText(qtdAvaliacoesRecebidas);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        avaliacaoDAO.buscaQtdAvaliacoesRecebidas().addListenerForSingleValueEvent(valueEventListenerSingleQtdAvaliacoesRecebidas);

        //QUANTIDADE DE JOGOS
        valueEventListenerSingleQtdJogos = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                qtdJogos = dataSnapshot.getValue(String.class);
                if (qtdJogos == null) {
                    txtQtdJogos.setText("0");
                    txtUltimosJogos.setText("Nenhum jogo cadastrado!!!");
                }else {
                    txtQtdJogos.setText(qtdJogos);
                    txtUltimosJogos.setText("Últimos jogos");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        resultadoJogoDAO.buscaQtdJogosDoUsuario().addListenerForSingleValueEvent(valueEventListenerSingleQtdJogos);

        //LISTENERS NA PROPRIA ACTIVITY
        valueEventListenerBuscaTodosOsConvitesPendentesDeUmCliente = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0 && TS_Helper.isExibirChamadaConvite()) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                    alertDialog.setTitle("Convite para jogar!!!");
                    alertDialog.setMessage("Voce recebeu um convite para jogar. Clique para visualizar.");
                    alertDialog.setCancelable(true);

                    alertDialog.setPositiveButton("Visualizar", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            TS_Helper.setExibirChamadaConvite(false);
                            Intent intent = new Intent(MainActivity.this, ConvitesActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });

                    alertDialog.setNegativeButton("Fechar", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            TS_Helper.setExibirChamadaConvite(false);
                        }
                    });

                    alertDialog.create();
                    alertDialog.show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        conviteDAO.buscaTodosOsConvitesPendentesDeUmUsuario(idUsuarioLogado).addListenerForSingleValueEvent(valueEventListenerBuscaTodosOsConvitesPendentesDeUmCliente);

        //CLIQUE DO LINK DE QTD DE AVALIAÇÕES RECEBIDAS
        txtQtdAvalRecebida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirTelaEstatisticas();
            }
        });
        txtQuantidadesRecebidas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirTelaEstatisticas();
            }
        });

        //CLIQUE DO LINK DE QTD DE AVALIAÇÕES PENDENTES
        txtQtdAvalPendente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {abrirTelaAvaliacoesPendentes();
            }
        });
        txtQuantidadesPendentes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirTelaAvaliacoesPendentes();
            }
        });

        //CLIQUE DO LINK DE QTD DE JOGOS
        txtQtdJogos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirTelaResultadoDeJogos();
            }
        });
        txtQuantidadesJogos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirTelaResultadoDeJogos();
            }
        });

        //CLIQUE DO LINK DE DADOS CADASTRAIS
        layoutDados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirTelaDadosCadastrais();
            }
        });

        //CLIQUE DO LINK DE CONVITES
        layoutConvites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirTelaConvites();
            }
        });

        //CLIQUE DO LINK DE ULTIMOS JOGOS
        layoutJogos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirTelaResultadoDeJogos();
            }
        });

        //CLIQUE DO LINK DE AVALIACOES
        layoutAvaliacoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirTelaAvaliacao();
            }
        });

        //CLIQUE DO LINK DE AVALIACOES PENDENTES
        layoutPendentes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {abrirTelaAvaliacoesPendentes();
            }
        });

        //CLIQUE DO LINK DE ESTATISTICAS
        layoutEstatisticas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirTelaEstatisticas();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_pesquisa:
                abrirTelaPesquisa();
                return true;
            case R.id.action_amigos:
                abrirTelaAmigos();
                return true;
            case R.id.action_settings:
                abrirTelaDadosConfiguracoes();
                return true;
            case R.id.action_passos:
                abrirTelaPrimeirosPassos();
                return true;
            case R.id.action_sair:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void abrirTelaLogin() {

        autenticacao.signOut();

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void abrirTelaPesquisa() {
        Intent intent = new Intent(MainActivity.this, PesquisaActivity.class);
        startActivity(intent);
    }

    public void abrirTelaAvaliacao() {
        Intent intent = new Intent(MainActivity.this, AvaliacaoResumidaActivity.class);
        startActivity(intent);
    }

    public void abrirTelaAvaliacoesPendentes() {
        Intent intent = new Intent(MainActivity.this, AvaliacoesPendentesActivity.class);
        startActivity(intent);
    }

    public void abrirTelaResultadoDeJogos() {
        Intent intent = new Intent(MainActivity.this, ResultadoJogosActivity.class);
        startActivity(intent);
    }

    public void abrirTelaAmigos() {
        Intent intent = new Intent(MainActivity.this, AmigosActivity.class);
        startActivity(intent);
    }

    public void abrirTelaConvites() {
        Intent intent = new Intent(MainActivity.this, ConvitesActivity.class);
        startActivity(intent);
    }

    public void abrirTelaDadosCadastrais() {
        Intent intent = new Intent(MainActivity.this, DadosCadastraisActivity.class);
        startActivity(intent);
    }

    public void abrirTelaEstatisticas() {
        Intent intent = new Intent(MainActivity.this, EstatisticasActivity.class);
        startActivity(intent);
    }

    public void abrirTelaDadosConfiguracoes() {
        Intent intent = new Intent(MainActivity.this, ConfiguracaoActivity.class);
        startActivity(intent);
    }

    public void abrirTelaPrimeirosPassos() {
        Intent intent = new Intent(MainActivity.this, PassosActivity.class);
        startActivity(intent);
    }
}
