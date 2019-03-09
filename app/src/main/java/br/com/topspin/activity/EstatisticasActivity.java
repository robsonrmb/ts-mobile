package br.com.topspin.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import br.com.topspin.R;
import br.com.topspin.constantes.Constantes;
import br.com.topspin.firebase.dao.AvaliacaoDAO;
import br.com.topspin.firebase.dao.ResultadoJogoDAO;
import br.com.topspin.graficos.GraficoAvaliacoes;
import br.com.topspin.graficos.GraficoResultadoJogos;
import br.com.topspin.helper.Preferencias;

public class EstatisticasActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;

    private ProgressDialog progressDialog;
    private Toolbar toolbar;
    private TextView txtNomeJogador;
    private LinearLayout llCorpoPagina;

    private TextView txtNaoVD;
    private TextView txtNaoTie;

    private PieChart mChart1;
    private PieChart mChart2;
    private PieChart mChartSaque;
    private PieChart mChartForehand;
    private PieChart mChartBackhand;
    private PieChart mChartVoleio;
    private PieChart mChartSmash;
    private PieChart mChartOfensivo;
    private PieChart mChartDefensivo;
    private PieChart mChartTatico;
    private PieChart mChartCompetitivo;
    private PieChart mChartFisico;

    private String qtdVitorias;
    private String qtdDerrotas;
    private String qtdTieVencidos;
    private String qtdTiePerdidos;
    private String sequencia_saque;
    private String sequencia_forehand;
    private String sequencia_backhand;
    private String sequencia_voleio;
    private String sequencia_smash;
    private String sequencia_ofensivo;
    private String sequencia_defensivo;
    private String sequencia_tatico;
    private String sequencia_competitivo;
    private String sequencia_fisico;

    private Preferencias preferencias;
    private ResultadoJogoDAO resultadoJogoDAO = new ResultadoJogoDAO();
    private AvaliacaoDAO avaliacaoDAO = new AvaliacaoDAO();

    private String idDoUsuarioLogado;
    private String qtdAvaliacoesRecebidas;

    private ValueEventListener valueEventListenerSingleBuscaSequenciaDasEstatisticasSaque;
    private ValueEventListener valueEventListenerSingleBuscaSequenciaDasEstatisticasForehand;
    private ValueEventListener valueEventListenerSingleBuscaSequenciaDasEstatisticasBackhand;
    private ValueEventListener valueEventListenerSingleBuscaSequenciaDasEstatisticasVoleio;
    private ValueEventListener valueEventListenerSingleBuscaSequenciaDasEstatisticasSmash;
    private ValueEventListener valueEventListenerSingleBuscaSequenciaDasEstatisticasOfensivo;
    private ValueEventListener valueEventListenerSingleBuscaSequenciaDasEstatisticasDefensivo;
    private ValueEventListener valueEventListenerSingleBuscaSequenciaDasEstatisticasTatico;
    private ValueEventListener valueEventListenerSingleBuscaSequenciaDasEstatisticasCompetitivo;
    private ValueEventListener valueEventListenerSingleBuscaSequenciaDasEstatisticasFisico;

    private ValueEventListener valueEventListenerSingleQtdVitorias;
    private ValueEventListener valueEventListenerSingleQtdDerrotas;
    private ValueEventListener valueEventListenerSingleQtdTieVencidos;
    private ValueEventListener valueEventListenerSingleQtdTiePerdidos;

    private ValueEventListener valueEventListenerSingleQtdAvaliacoesRecebidas;

    @Override
    protected void onStop() {
        super.onStop();
        avaliacaoDAO.buscaSequenciaDasEstatisticasSaque().removeEventListener(valueEventListenerSingleBuscaSequenciaDasEstatisticasSaque);
        avaliacaoDAO.buscaSequenciaDasEstatisticasForehand().removeEventListener(valueEventListenerSingleBuscaSequenciaDasEstatisticasForehand);
        avaliacaoDAO.buscaSequenciaDasEstatisticasBackhand().removeEventListener(valueEventListenerSingleBuscaSequenciaDasEstatisticasBackhand);
        avaliacaoDAO.buscaSequenciaDasEstatisticasVoleio().removeEventListener(valueEventListenerSingleBuscaSequenciaDasEstatisticasVoleio);
        avaliacaoDAO.buscaSequenciaDasEstatisticasSmash().removeEventListener(valueEventListenerSingleBuscaSequenciaDasEstatisticasSmash);
        avaliacaoDAO.buscaSequenciaDasEstatisticasOfensivo().removeEventListener(valueEventListenerSingleBuscaSequenciaDasEstatisticasOfensivo);
        avaliacaoDAO.buscaSequenciaDasEstatisticasDefensivo().removeEventListener(valueEventListenerSingleBuscaSequenciaDasEstatisticasDefensivo);
        avaliacaoDAO.buscaSequenciaDasEstatisticasTatico().removeEventListener(valueEventListenerSingleBuscaSequenciaDasEstatisticasTatico);
        avaliacaoDAO.buscaSequenciaDasEstatisticasCompetitivo().removeEventListener(valueEventListenerSingleBuscaSequenciaDasEstatisticasCompetitivo);
        avaliacaoDAO.buscaSequenciaDasEstatisticasFisico().removeEventListener(valueEventListenerSingleBuscaSequenciaDasEstatisticasFisico);

        resultadoJogoDAO.buscaQtdVitoriasDoUsuario().removeEventListener(valueEventListenerSingleQtdVitorias);
        resultadoJogoDAO.buscaQtdDerrotasDoUsuario().removeEventListener(valueEventListenerSingleQtdDerrotas);
        resultadoJogoDAO.buscaQtdTieVencidosDoUsuario().removeEventListener(valueEventListenerSingleQtdTieVencidos);
        resultadoJogoDAO.buscaQtdTiePerdidosDoUsuario().removeEventListener(valueEventListenerSingleQtdTiePerdidos);

        avaliacaoDAO.buscaQtdAvaliacoesRecebidas().removeEventListener(valueEventListenerSingleQtdAvaliacoesRecebidas);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estatisticas);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("ESTATÍSTICAS");
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        progressDialog = ProgressDialog.show(EstatisticasActivity.this, "Aguarde!!!", "Processo em andamento.", true, false);

        preferencias = new Preferencias(EstatisticasActivity.this);
        idDoUsuarioLogado = preferencias.getIdUsuarioLogado();
        if ("0".equals(preferencias.getTema())) {
            toolbar.setBackgroundColor(Color.parseColor(Constantes.QUADRA_RAPIDA));
        }else if ("1".equals(preferencias.getTema())) {
            toolbar.setBackgroundColor(Color.parseColor(Constantes.SAIBRO));
        }else{
            toolbar.setBackgroundColor(Color.parseColor(Constantes.GRAMA));
        }

        llCorpoPagina = (LinearLayout) findViewById(R.id.corpoPagina);

        txtNomeJogador = (TextView) findViewById(R.id.txtNomeJogador);
        txtNomeJogador.setText("");

        mChart1 = (PieChart) findViewById(R.id.chart1);
        mChart2 = (PieChart) findViewById(R.id.chart2);
        mChartSaque = (PieChart) findViewById(R.id.chartSaque);
        mChartForehand = (PieChart) findViewById(R.id.chartForehand);
        mChartBackhand = (PieChart) findViewById(R.id.chartBackhand);
        mChartVoleio = (PieChart) findViewById(R.id.chartVoleio);
        mChartSmash = (PieChart) findViewById(R.id.chartSmash);
        mChartOfensivo = (PieChart) findViewById(R.id.chartOfensivo);
        mChartDefensivo = (PieChart) findViewById(R.id.chartDefensivo);
        mChartTatico = (PieChart) findViewById(R.id.chartTatico);
        mChartCompetitivo = (PieChart) findViewById(R.id.chartCompetitivo);
        mChartFisico = (PieChart) findViewById(R.id.chartFisico);

        txtNaoVD = (TextView) findViewById(R.id.txtNaoVD);
        txtNaoTie = (TextView) findViewById(R.id.txtNaoTie);

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            resultadoJogoDAO = new ResultadoJogoDAO(extra.getString("idDoUsuario"));
            avaliacaoDAO = new AvaliacaoDAO(extra.getString("idDoUsuario"));
            txtNomeJogador.setText("Estatísticas: " + extra.getString("nomeDoUsuario"));
        }else{
            resultadoJogoDAO = new ResultadoJogoDAO(preferencias.getIdUsuarioLogado());
            avaliacaoDAO = new AvaliacaoDAO(preferencias.getIdUsuarioLogado());
            txtNomeJogador.setText("Minhas estatísticas");
        }

        llCorpoPagina.setVisibility(View.INVISIBLE);

        //VITORIAS
        valueEventListenerSingleQtdVitorias = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                qtdVitorias = dataSnapshot.getValue(String.class);

                //DERROTAS
                valueEventListenerSingleQtdDerrotas = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        qtdDerrotas = dataSnapshot.getValue(String.class);
                        GraficoResultadoJogos.setPieChartResultadoJogos(EstatisticasActivity.this, mChart1, getVitorias(), getDerrotas(), txtNaoVD);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
                resultadoJogoDAO.buscaQtdDerrotasDoUsuario().addListenerForSingleValueEvent(valueEventListenerSingleQtdDerrotas);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        resultadoJogoDAO.buscaQtdVitoriasDoUsuario().addListenerForSingleValueEvent(valueEventListenerSingleQtdVitorias);

        //TIEBREAKS VENCIDOS
        valueEventListenerSingleQtdTieVencidos = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                qtdTieVencidos = dataSnapshot.getValue(String.class);

                //TIEBREAKS PERDIDOS
                valueEventListenerSingleQtdTiePerdidos = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        qtdTiePerdidos = dataSnapshot.getValue(String.class);

                        GraficoResultadoJogos.setPieChartTieBreak(EstatisticasActivity.this, mChart2, getTieVencidos(), getTiePerdidos(), txtNaoTie);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
                resultadoJogoDAO.buscaQtdTiePerdidosDoUsuario().addListenerForSingleValueEvent(valueEventListenerSingleQtdTiePerdidos);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        resultadoJogoDAO.buscaQtdTieVencidosDoUsuario().addListenerForSingleValueEvent(valueEventListenerSingleQtdTieVencidos);

        //SAQUE
        valueEventListenerSingleBuscaSequenciaDasEstatisticasSaque = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sequencia_saque = dataSnapshot.getValue(String.class);

                if (sequencia_saque != null) {
                    String[] array = sequencia_saque.split("#");
                    String seq = array[0] + array[1] + array[2] + array[3];
                    GraficoAvaliacoes.setPieChartAvaliacoesComQuatroRespostas(EstatisticasActivity.this, mChartSaque, seq, "SAQUE");

                }else{
                    mChartSaque.setVisibility(View.INVISIBLE);
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        avaliacaoDAO.buscaSequenciaDasEstatisticasSaque().addListenerForSingleValueEvent(valueEventListenerSingleBuscaSequenciaDasEstatisticasSaque);

        //FOREHAND
        valueEventListenerSingleBuscaSequenciaDasEstatisticasForehand = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sequencia_forehand = dataSnapshot.getValue(String.class);

                if (sequencia_forehand != null) {
                    String[] array = sequencia_forehand.split("#");
                    String seq = array[0] + array[1] + array[2] + array[3];
                    GraficoAvaliacoes.setPieChartAvaliacoesComQuatroRespostas(EstatisticasActivity.this, mChartForehand, seq, "FOREHAND");

                }else{
                    mChartForehand.setVisibility(View.INVISIBLE);
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        avaliacaoDAO.buscaSequenciaDasEstatisticasForehand().addListenerForSingleValueEvent(valueEventListenerSingleBuscaSequenciaDasEstatisticasForehand);

        //BACKHAND
        valueEventListenerSingleBuscaSequenciaDasEstatisticasBackhand = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sequencia_backhand = dataSnapshot.getValue(String.class);

                if (sequencia_backhand != null) {
                    String[] array = sequencia_backhand.split("#");
                    String seq = array[0] + array[1] + array[2] + array[3];
                    GraficoAvaliacoes.setPieChartAvaliacoesComQuatroRespostas(EstatisticasActivity.this, mChartBackhand, seq, "BACKHAND");

                }else{
                    mChartBackhand.setVisibility(View.INVISIBLE);
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        avaliacaoDAO.buscaSequenciaDasEstatisticasBackhand().addListenerForSingleValueEvent(valueEventListenerSingleBuscaSequenciaDasEstatisticasBackhand);

        //VOLEIO
        valueEventListenerSingleBuscaSequenciaDasEstatisticasVoleio = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sequencia_voleio = dataSnapshot.getValue(String.class);

                if (sequencia_voleio != null) {
                    String[] array = sequencia_voleio.split("#");
                    String seq = array[0] + array[1] + array[2] + array[3];
                    GraficoAvaliacoes.setPieChartAvaliacoesComQuatroRespostas(EstatisticasActivity.this, mChartVoleio, seq, "VOLEIO");

                }else{
                    mChartVoleio.setVisibility(View.INVISIBLE);
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        avaliacaoDAO.buscaSequenciaDasEstatisticasVoleio().addListenerForSingleValueEvent(valueEventListenerSingleBuscaSequenciaDasEstatisticasVoleio);

        //SMASH
        valueEventListenerSingleBuscaSequenciaDasEstatisticasSmash = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sequencia_smash = dataSnapshot.getValue(String.class);

                if (sequencia_smash != null) {
                    String[] array = sequencia_smash.split("#");
                    String seq = array[0] + array[1] + array[2] + array[3];
                    GraficoAvaliacoes.setPieChartAvaliacoesComQuatroRespostas(EstatisticasActivity.this, mChartSmash, seq, "SMASH");

                }else{
                    mChartSmash.setVisibility(View.INVISIBLE);
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        avaliacaoDAO.buscaSequenciaDasEstatisticasSmash().addListenerForSingleValueEvent(valueEventListenerSingleBuscaSequenciaDasEstatisticasSmash);

        //OFENSIVO
        valueEventListenerSingleBuscaSequenciaDasEstatisticasOfensivo = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sequencia_ofensivo = dataSnapshot.getValue(String.class);

                if (sequencia_ofensivo != null) {
                    String[] array = sequencia_ofensivo.split("#");
                    String seq = array[0] + array[1] + array[2];
                    GraficoAvaliacoes.setPieChartAvaliacoesComTresRespostas(EstatisticasActivity.this, mChartOfensivo, seq, "OFENSIVO");

                }else{
                    mChartOfensivo.setVisibility(View.INVISIBLE);
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        avaliacaoDAO.buscaSequenciaDasEstatisticasOfensivo().addListenerForSingleValueEvent(valueEventListenerSingleBuscaSequenciaDasEstatisticasOfensivo);

        //DEFENSIVO
        valueEventListenerSingleBuscaSequenciaDasEstatisticasDefensivo = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sequencia_defensivo = dataSnapshot.getValue(String.class);

                if (sequencia_defensivo != null) {
                    String[] array = sequencia_defensivo.split("#");
                    String seq = array[0] + array[1] + array[2];
                    GraficoAvaliacoes.setPieChartAvaliacoesComTresRespostas(EstatisticasActivity.this, mChartDefensivo, seq, "DEFENSIVO");

                }else{
                    mChartDefensivo.setVisibility(View.INVISIBLE);
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        avaliacaoDAO.buscaSequenciaDasEstatisticasDefensivo().addListenerForSingleValueEvent(valueEventListenerSingleBuscaSequenciaDasEstatisticasDefensivo);

        //TATICO
        valueEventListenerSingleBuscaSequenciaDasEstatisticasTatico = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sequencia_tatico = dataSnapshot.getValue(String.class);

                if (sequencia_tatico != null) {
                    String[] array = sequencia_tatico.split("#");
                    String seq = array[0] + array[1] + array[2];
                    GraficoAvaliacoes.setPieChartAvaliacoesComTresRespostas(EstatisticasActivity.this, mChartTatico, seq, "TATICO");

                }else{
                    mChartTatico.setVisibility(View.INVISIBLE);
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        avaliacaoDAO.buscaSequenciaDasEstatisticasTatico().addListenerForSingleValueEvent(valueEventListenerSingleBuscaSequenciaDasEstatisticasTatico);

        //COMPETITIVO
        valueEventListenerSingleBuscaSequenciaDasEstatisticasCompetitivo = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sequencia_competitivo = dataSnapshot.getValue(String.class);

                if (sequencia_competitivo != null) {
                    String[] array = sequencia_competitivo.split("#");
                    String seq = array[0] + array[1] + array[2];
                    GraficoAvaliacoes.setPieChartAvaliacoesComTresRespostas(EstatisticasActivity.this, mChartCompetitivo, seq, "COMPETITIVO");

                }else{
                    mChartCompetitivo.setVisibility(View.INVISIBLE);
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        avaliacaoDAO.buscaSequenciaDasEstatisticasCompetitivo().addListenerForSingleValueEvent(valueEventListenerSingleBuscaSequenciaDasEstatisticasCompetitivo);

        //FISICO
        valueEventListenerSingleBuscaSequenciaDasEstatisticasFisico = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sequencia_fisico = dataSnapshot.getValue(String.class);

                if (sequencia_fisico != null) {
                    String[] array = sequencia_fisico.split("#");
                    String seq = array[0] + array[1] + array[2] + array[3];
                    GraficoAvaliacoes.setPieChartAvaliacoesComQuatroRespostas(EstatisticasActivity.this, mChartFisico, seq, "FISICO");

                }else{
                    mChartFisico.setVisibility(View.INVISIBLE);
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        avaliacaoDAO.buscaSequenciaDasEstatisticasFisico().addListenerForSingleValueEvent(valueEventListenerSingleBuscaSequenciaDasEstatisticasFisico);

        //QUANTIDADE DE AVALIACOES RECEBIDAS
        valueEventListenerSingleQtdAvaliacoesRecebidas = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                qtdAvaliacoesRecebidas = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        avaliacaoDAO.buscaQtdAvaliacoesRecebidas().addListenerForSingleValueEvent(valueEventListenerSingleQtdAvaliacoesRecebidas);

        new Handler().postDelayed(new Runnable() {
            /*
             * Exibindo splash com um timer.
             */
            @Override
            public void run() {
                GraficoResultadoJogos.setPieChartResultadoJogos(EstatisticasActivity.this, mChart1, getVitorias(), getDerrotas(), txtNaoVD);
                GraficoResultadoJogos.setPieChartTieBreak(EstatisticasActivity.this, mChart2, getTieVencidos(), getTiePerdidos(), txtNaoTie);
                progressDialog.dismiss();
                if (qtdAvaliacoesRecebidas != null && Integer.parseInt(qtdAvaliacoesRecebidas) > 2) {
                    llCorpoPagina.setVisibility(View.VISIBLE);
                }else{
                    llCorpoPagina.setVisibility(View.INVISIBLE);
                    txtNomeJogador.setText("NÃO HÁ AVALIAÇÕES SUFICIENTES!!! \nPara visualizar as estatísticas, o atleta deverá ter recebido mais de duas (2) avaliações.");
                    Toast.makeText(EstatisticasActivity.this, "Não há avaliações suficientes para visualizar as estatísticas.", Toast.LENGTH_LONG).show();
                }
            }
        }, SPLASH_TIME_OUT);
    }

    private void abrirTelaPrincipal() {
        Intent intent = new Intent(EstatisticasActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private int getVitorias() {
        if (qtdVitorias == null || "".equals(qtdVitorias)) {
            qtdVitorias = "0";
        }
        return Integer.parseInt(qtdVitorias);
    }
    private int getDerrotas() {
        if (qtdDerrotas == null || "".equals(qtdDerrotas)) {
            qtdDerrotas = "0";
        }
        return Integer.parseInt(qtdDerrotas);
    }
    private int getTieVencidos() {
        if (qtdTieVencidos == null || "".equals(qtdTieVencidos)) {
            qtdTieVencidos = "0";
        }
        return Integer.parseInt(qtdTieVencidos);
    }
    private int getTiePerdidos() {
        if (qtdTiePerdidos == null || "".equals(qtdTiePerdidos)) {
            qtdTiePerdidos = "0";
        }
        return Integer.parseInt(qtdTiePerdidos);
    }
}
