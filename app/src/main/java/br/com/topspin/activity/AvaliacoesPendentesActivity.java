package br.com.topspin.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.topspin.R;
import br.com.topspin.adapter.AdapterAvaliacoesPendentes;
import br.com.topspin.bean.BeanAvaliacaoPendente;
import br.com.topspin.constantes.Constantes;
import br.com.topspin.firebase.dao.AvaliacaoDAO;
import br.com.topspin.firebase.dao.UsuarioDAO;
import br.com.topspin.helper.AvaliacaoUtil;
import br.com.topspin.helper.Preferencias;
import br.com.topspin.model.Avaliacao;
import br.com.topspin.model.AvaliacaoPendente;
import br.com.topspin.model.Usuario;

public class AvaliacoesPendentesActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView txtFrase;
    private ListView listViewAvaliacoesPendentes;

    private String idUsuarioLogado;
    private List<BeanAvaliacaoPendente> listaAvaliacoesPendentes = new ArrayList<BeanAvaliacaoPendente>();
    private BeanAvaliacaoPendente beanAvaliacaoPendente;

    private Preferencias preferencias;
    private Avaliacao avaliacao;
    private Usuario usuario;

    private AvaliacaoDAO avaliacaoDAO;
    private UsuarioDAO usuarioDAO;

    private String chaveDaAvaliacaoParaExclusao;

    private String qtdAvaliacoesRecebidas;
    private String qtdAvaliacoesPendentes;

    private ValueEventListener valueEventListenerAvaliacoesPendentesPorId;
    private ValueEventListener valueEventListenerAvaliacoesPendentesPorIdEChave;
    private ValueEventListener valueEventListenerSingleQtdAvaliacoesRecebidas;
    private ValueEventListener valueEventListenerSingleQtdAvaliacoesPendentes;
    private ValueEventListener valueEventListenerSingleUsuarios;

    private ValueEventListener valueEventListenerSaque;
    private ValueEventListener valueEventListenerForehand;
    private ValueEventListener valueEventListenerBackhand;
    private ValueEventListener valueEventListenerVoleio;
    private ValueEventListener valueEventListenerSmash;
    private ValueEventListener valueEventListenerOfensivo;
    private ValueEventListener valueEventListenerDefensivo;
    private ValueEventListener valueEventListenerTatico;
    private ValueEventListener valueEventListenerCompetitivo;
    private ValueEventListener valueEventListenerFisico;

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

    @Override
    protected void onStop() {
        super.onStop();
        avaliacaoDAO.buscaAvaliacoesPendentesPorId().removeEventListener(valueEventListenerAvaliacoesPendentesPorId);
        if (chaveDaAvaliacaoParaExclusao != null) {
            avaliacaoDAO.buscaAvaliacaoPendentesPorIdEChaveDaAvaliacao(chaveDaAvaliacaoParaExclusao).removeEventListener(valueEventListenerAvaliacoesPendentesPorIdEChave);
        }
        avaliacaoDAO.buscaQtdAvaliacoesRecebidas().removeEventListener(valueEventListenerSingleQtdAvaliacoesRecebidas);
        avaliacaoDAO.buscaQtdAvaliacoesPendentes().removeEventListener(valueEventListenerSingleQtdAvaliacoesPendentes);
        //usuarioDAO.buscaUsuarioPorId(idUsuarioLogado).removeEventListener(valueEventListenerSingleUsuarios);

        avaliacaoDAO.buscaSequenciaDasEstatisticasSaque().removeEventListener(valueEventListenerSaque);
        avaliacaoDAO.buscaSequenciaDasEstatisticasForehand().removeEventListener(valueEventListenerForehand);
        avaliacaoDAO.buscaSequenciaDasEstatisticasBackhand().removeEventListener(valueEventListenerBackhand);
        avaliacaoDAO.buscaSequenciaDasEstatisticasVoleio().removeEventListener(valueEventListenerVoleio);
        avaliacaoDAO.buscaSequenciaDasEstatisticasSmash().removeEventListener(valueEventListenerSmash);
        avaliacaoDAO.buscaSequenciaDasEstatisticasOfensivo().removeEventListener(valueEventListenerOfensivo);
        avaliacaoDAO.buscaSequenciaDasEstatisticasDefensivo().removeEventListener(valueEventListenerDefensivo);
        avaliacaoDAO.buscaSequenciaDasEstatisticasTatico().removeEventListener(valueEventListenerTatico);
        avaliacaoDAO.buscaSequenciaDasEstatisticasCompetitivo().removeEventListener(valueEventListenerCompetitivo);
        avaliacaoDAO.buscaSequenciaDasEstatisticasFisico().removeEventListener(valueEventListenerFisico);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avaliacoes_pendentes);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("AVALIAÇÕES PENDENTES");
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        preferencias = new Preferencias(AvaliacoesPendentesActivity.this);
        idUsuarioLogado = preferencias.getIdUsuarioLogado();
        if ("0".equals(preferencias.getTema())) {
            toolbar.setBackgroundColor(Color.parseColor(Constantes.QUADRA_RAPIDA));
        }else if ("1".equals(preferencias.getTema())) {
            toolbar.setBackgroundColor(Color.parseColor(Constantes.SAIBRO));
        }else{
            toolbar.setBackgroundColor(Color.parseColor(Constantes.GRAMA));
        }

        txtFrase = (TextView) findViewById(R.id.txtFrase);

        listViewAvaliacoesPendentes = (ListView) findViewById(R.id.idListaAvaliacoesPendentes);

        avaliacaoDAO = new AvaliacaoDAO(idUsuarioLogado);
        usuarioDAO = new UsuarioDAO();

        final AdapterAvaliacoesPendentes adapterAP = new AdapterAvaliacoesPendentes(listaAvaliacoesPendentes, this);

        valueEventListenerAvaliacoesPendentesPorId = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> avaliacoesPendentes = dataSnapshot.getChildren();
                if (avaliacoesPendentes != null) {
                    for (DataSnapshot dataSnapshot1: avaliacoesPendentes) {
                        if (dataSnapshot1.getValue() != null) {
                            AvaliacaoPendente avaliacaoPendente = dataSnapshot1.getValue(AvaliacaoPendente.class);

                            final String idDaAvaliacao = dataSnapshot1.getKey();
                            final String dataAvaliacaoPendente = avaliacaoPendente.getDataDaAvaliacao();

                            //USUARIOS
                            valueEventListenerSingleUsuarios = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    usuario = dataSnapshot.getValue(Usuario.class);
                                    beanAvaliacaoPendente = new BeanAvaliacaoPendente(idDaAvaliacao, usuario.getNome(), usuario.getApelido(), dataAvaliacaoPendente);
                                    listaAvaliacoesPendentes.add(beanAvaliacaoPendente);

                                    adapterAP.notifyDataSetChanged();

                                    if (listaAvaliacoesPendentes == null || listaAvaliacoesPendentes.size() == 0) {
                                        txtFrase.setText("NAO EXISTEM AVALIACOES PENDENTES.");
                                    }else{
                                        txtFrase.setText("Clique no registro para confirmar avaliacao.");
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            };
                            usuarioDAO.buscaUsuarioPorId(avaliacaoPendente.getIdDoAvaliador()).addListenerForSingleValueEvent(valueEventListenerSingleUsuarios);

                            /*
                            beanAvaliacaoPendente = new BeanAvaliacaoPendente(dataSnapshot1.getKey(), avaliacaoPendente.getNomeDoAvaliador(), avaliacaoPendente.getApelidoDoAvaliador(), avaliacaoPendente.getDataDaAvaliacao());
                            listaAvaliacoesPendentes.add(beanAvaliacaoPendente);
                            */
                        }
                    }
                }
                listViewAvaliacoesPendentes.setAdapter(adapterAP);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        avaliacaoDAO.buscaAvaliacoesPendentesPorId().addListenerForSingleValueEvent(valueEventListenerAvaliacoesPendentesPorId);
        if (listaAvaliacoesPendentes == null || listaAvaliacoesPendentes.size() == 0) {
            txtFrase.setText("NAO EXISTEM AVALIACOES PENDENTES.");
        }else{
            txtFrase.setText("Clique no registro para confirmar avaliacao.");
        }

        //QUANTIDADE DE AVALIACOES PENDENTES
        valueEventListenerSingleQtdAvaliacoesPendentes = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                qtdAvaliacoesPendentes = dataSnapshot.getValue(String.class);
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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        avaliacaoDAO.buscaQtdAvaliacoesRecebidas().addListenerForSingleValueEvent(valueEventListenerSingleQtdAvaliacoesRecebidas);

        //SEQUENCIA DAS ESTATISTICAS DE SAQUE
        valueEventListenerSaque = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sequencia_saque = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        avaliacaoDAO.buscaSequenciaDasEstatisticasSaque().addListenerForSingleValueEvent(valueEventListenerSaque);

        //SEQUENCIA DAS ESTATISTICAS DE FOREHAND
        valueEventListenerForehand = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sequencia_forehand = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        avaliacaoDAO.buscaSequenciaDasEstatisticasForehand().addListenerForSingleValueEvent(valueEventListenerForehand);

        //SEQUENCIA DAS ESTATISTICAS DE BACKHAND
        valueEventListenerBackhand = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sequencia_backhand = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        avaliacaoDAO.buscaSequenciaDasEstatisticasBackhand().addListenerForSingleValueEvent(valueEventListenerBackhand);

        //SEQUENCIA DAS ESTATISTICAS DE VOLEIO
        valueEventListenerVoleio = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sequencia_voleio = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        avaliacaoDAO.buscaSequenciaDasEstatisticasVoleio().addListenerForSingleValueEvent(valueEventListenerVoleio);

        //SEQUENCIA DAS ESTATISTICAS DE SMASH
        valueEventListenerSmash = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sequencia_smash = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        avaliacaoDAO.buscaSequenciaDasEstatisticasSmash().addListenerForSingleValueEvent(valueEventListenerSmash);

        //SEQUENCIA DAS ESTATISTICAS DE OFENSIVO
        valueEventListenerOfensivo = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sequencia_ofensivo = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        avaliacaoDAO.buscaSequenciaDasEstatisticasOfensivo().addListenerForSingleValueEvent(valueEventListenerOfensivo);

        //SEQUENCIA DAS ESTATISTICAS DE DEFENSIVO
        valueEventListenerDefensivo = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sequencia_defensivo = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        avaliacaoDAO.buscaSequenciaDasEstatisticasDefensivo().addListenerForSingleValueEvent(valueEventListenerDefensivo);

        //SEQUENCIA DAS ESTATISTICAS DE TATICO
        valueEventListenerTatico = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sequencia_tatico = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        avaliacaoDAO.buscaSequenciaDasEstatisticasTatico().addListenerForSingleValueEvent(valueEventListenerTatico);

        //SEQUENCIA DAS ESTATISTICAS DE COMPETITIVO
        valueEventListenerCompetitivo = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sequencia_competitivo = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        avaliacaoDAO.buscaSequenciaDasEstatisticasCompetitivo().addListenerForSingleValueEvent(valueEventListenerCompetitivo);

        //SEQUENCIA DAS ESTATISTICAS DE FISICO
        valueEventListenerFisico = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sequencia_fisico = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        avaliacaoDAO.buscaSequenciaDasEstatisticasFisico().addListenerForSingleValueEvent(valueEventListenerFisico);

        //CLIQUE DO REGISTRO NO LISTVIEW
        listViewAvaliacoesPendentes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                //Toast.makeText(AvaliacoesPendentesActivity.this, listaAvaliacoesPendentes.get(position).getIdDaAvaliacao(), Toast.LENGTH_LONG).show();

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(AvaliacoesPendentesActivity.this);
                alertDialog.setTitle("Confirma avaliação");
                alertDialog.setMessage("Você recebeu uma avalição feita por: "+listaAvaliacoesPendentes.get(position).getNomeDoAvaliador()+". Confirma?");
                alertDialog.setCancelable(false);

                alertDialog.setPositiveButton("Sim", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //pesquisar avaliacao pendente: idUsuarioLogado: listaAvaliacoesPendentes.get(position).getIdDaAvaliacao()
                        chaveDaAvaliacaoParaExclusao = listaAvaliacoesPendentes.get(position).getIdDaAvaliacao();
                        valueEventListenerAvaliacoesPendentesPorIdEChave = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                AvaliacaoPendente avaliacaoPend = dataSnapshot.getValue(AvaliacaoPendente.class);
                                avaliacao = new Avaliacao(avaliacaoPend);
                                avaliacao.setStatus("C");

                                avaliacao.setSequencia_saque(AvaliacaoUtil.montaSequenciaComQuatroRespostas(sequencia_saque, avaliacao.getSaque()));
                                avaliacao.setSequencia_forehand(AvaliacaoUtil.montaSequenciaComQuatroRespostas(sequencia_forehand, avaliacao.getForehand()));
                                avaliacao.setSequencia_backhand(AvaliacaoUtil.montaSequenciaComQuatroRespostas(sequencia_backhand, avaliacao.getBackhand()));
                                avaliacao.setSequencia_voleio(AvaliacaoUtil.montaSequenciaComQuatroRespostas(sequencia_voleio, avaliacao.getVoleio()));
                                avaliacao.setSequencia_smash(AvaliacaoUtil.montaSequenciaComQuatroRespostas(sequencia_smash, avaliacao.getSmash()));
                                avaliacao.setSequencia_ofensivo(AvaliacaoUtil.montaSequenciaComTresRespostas(sequencia_ofensivo, avaliacao.getOfensivo()));
                                avaliacao.setSequencia_defensivo(AvaliacaoUtil.montaSequenciaComTresRespostas(sequencia_defensivo, avaliacao.getDefensivo()));
                                avaliacao.setSequencia_tatico(AvaliacaoUtil.montaSequenciaComTresRespostas(sequencia_tatico, avaliacao.getTatico()));
                                avaliacao.setSequencia_competitivo(AvaliacaoUtil.montaSequenciaComTresRespostas(sequencia_competitivo, avaliacao.getCompetitivo()));
                                avaliacao.setSequencia_fisico(AvaliacaoUtil.montaSequenciaComQuatroRespostas(sequencia_fisico, avaliacao.getFisico()));
                                avaliacao.setQtdAvaliacoesPendentes(AvaliacaoUtil.confirmaAvaliacaoPendente(qtdAvaliacoesPendentes));
                                avaliacao.setQtdAvaliacoesRecebidas(AvaliacaoUtil.salvaAvaliacaoRecebida(qtdAvaliacoesRecebidas));

                                confirmarAvaliacao(idUsuarioLogado, chaveDaAvaliacaoParaExclusao);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        };
                        avaliacaoDAO.buscaAvaliacaoPendentesPorIdEChaveDaAvaliacao(chaveDaAvaliacaoParaExclusao).addListenerForSingleValueEvent(valueEventListenerAvaliacoesPendentesPorIdEChave);
                    }
                });

                alertDialog.setNegativeButton("Não", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(AvaliacoesPendentesActivity.this, "Cancelado!!!", Toast.LENGTH_SHORT).show();
                    }
                });
                alertDialog.create();
                alertDialog.show();

            }
        });
    }

    private void abrirTelaPrincipal() {
        Intent intent = new Intent(AvaliacoesPendentesActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void confirmarAvaliacao(String idUsuarioLogado,String chaveDaAvaliacaoParaExclusao) {
        avaliacao.confirmarAvaliacao(idUsuarioLogado, chaveDaAvaliacaoParaExclusao);

        Intent intent = new Intent(AvaliacoesPendentesActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        Toast.makeText(AvaliacoesPendentesActivity.this, "Avaliação confirmada com sucesso!!!", Toast.LENGTH_LONG).show();
    }
}
