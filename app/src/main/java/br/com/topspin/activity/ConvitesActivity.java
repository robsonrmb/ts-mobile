package br.com.topspin.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.topspin.R;
import br.com.topspin.adapter.AdapterConvitesListView;
import br.com.topspin.constantes.Constantes;
import br.com.topspin.firebase.dao.ConviteDAO;
import br.com.topspin.firebase.dao.UsuarioDAO;
import br.com.topspin.helper.Preferencias;
import br.com.topspin.model.ConviteJogo;
import br.com.topspin.model.ConviteJogoAndamento;
import br.com.topspin.model.Usuario;

public class ConvitesActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button btnConvidar;
    private Button btnEnviados;
    private Button btnPendentes;
    private Button btnRecebidos;

    private ProgressDialog progressDialog;
    private int contador;
    private int contadorAuxiliar;

    private RelativeLayout relativeMsg;
    private TextView txtMsg;

    private Preferencias preferencias;
    private String idUsuarioLogado;
    private String andamentoDoConvite;
    private String chaveDoConvite;

    private ListView lv_convites;
    private List<ConviteJogoAndamento> listaDeConvites = new ArrayList<ConviteJogoAndamento>();
    private AdapterConvitesListView adapterConvitesLV;

    private ConviteJogo conviteJogo = new ConviteJogo();
    private ConviteJogo conviteJogoEnviado = new ConviteJogo();
    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private ConviteDAO conviteDAO = new ConviteDAO();
    private ValueEventListener valueEventListenerUsuarioPorId;
    private ValueEventListener valueEventListenerBuscaTodosOsConvitesEnviadosDeUmCliente;
    private ValueEventListener valueEventListenerBuscaTodosOsConvitesPendentesDeUmCliente;
    private ValueEventListener valueEventListenerBuscaTodosOsConvitesRecebidosDeUmCliente;
    private ValueEventListener valueEventListenerConviteEnviadoPorIdEChaveDoConvite;
    private ValueEventListener valueEventListenerConvitePendentePorIdEChaveDoConvite;
    private ValueEventListener valueEventListenerConviteRecebidoPorIdEChaveDoConvite;

    @Override
    protected void onStop() {
        super.onStop();
        /* todo usuario dinamico vindo de um loop/for.
        if (valueEventListenerUsuarioPorId != null) {
            usuarioDAO.buscaUsuarioPorId(cJogo.getId()).removeEventListener(valueEventListenerUsuarioPorId);
        }
        */
        if (valueEventListenerBuscaTodosOsConvitesEnviadosDeUmCliente != null) {
            conviteDAO.buscaTodosOsConvitesEnviadosDeUmUsuario(idUsuarioLogado).removeEventListener(valueEventListenerBuscaTodosOsConvitesEnviadosDeUmCliente);
        }
        if (valueEventListenerBuscaTodosOsConvitesPendentesDeUmCliente != null) {
            conviteDAO.buscaTodosOsConvitesPendentesDeUmUsuario(idUsuarioLogado).removeEventListener(valueEventListenerBuscaTodosOsConvitesPendentesDeUmCliente);
        }
        if (valueEventListenerBuscaTodosOsConvitesRecebidosDeUmCliente != null) {
            conviteDAO.buscaTodosOsConvitesRecebidosDeUmUsuario(idUsuarioLogado).removeEventListener(valueEventListenerBuscaTodosOsConvitesRecebidosDeUmCliente);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convites);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("MEUS CONVITES");
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        preferencias = new Preferencias(ConvitesActivity.this);
        idUsuarioLogado = preferencias.getIdUsuarioLogado();
        if ("0".equals(preferencias.getTema())) {
            toolbar.setBackgroundColor(Color.parseColor(Constantes.QUADRA_RAPIDA));
        }else if ("1".equals(preferencias.getTema())) {
            toolbar.setBackgroundColor(Color.parseColor(Constantes.SAIBRO));
        }else{
            toolbar.setBackgroundColor(Color.parseColor(Constantes.GRAMA));
        }

        btnConvidar = (Button) findViewById(R.id.btnConvidar);
        btnEnviados = (Button) findViewById(R.id.btnEnviados);
        btnPendentes = (Button) findViewById(R.id.btnPendentes);
        btnRecebidos = (Button) findViewById(R.id.btnRecebidos);

        relativeMsg = (RelativeLayout) findViewById(R.id.relativeMsg);
        relativeMsg.setVisibility(View.INVISIBLE);
        txtMsg = (TextView) findViewById(R.id.txtMsg);

        habilitarBotao(2);

        lv_convites = (ListView) findViewById(R.id.idListaConvites);
        adapterConvitesLV = new AdapterConvitesListView(listaDeConvites, this);
        lv_convites.setAdapter(adapterConvitesLV);

        //LISTENERS NA PROPRIA ACTIVITY
        valueEventListenerBuscaTodosOsConvitesPendentesDeUmCliente = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                contador = Integer.parseInt(dataSnapshot.getChildrenCount() + "");
                contadorAuxiliar = 0;
                for (final DataSnapshot dados: dataSnapshot.getChildren()) {
                    /*if (contadorAuxiliar == 0) {
                        progressDialog = ProgressDialog.show(ConvitesActivity.this, "Aguarde!!!", "Processo em andamento.", false, false);
                    }*/
                    final ConviteJogoAndamento cJogo = dados.getValue(ConviteJogoAndamento.class);
                    valueEventListenerUsuarioPorId = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Usuario usuario = dataSnapshot.getValue(Usuario.class);
                            if (dataSnapshot.getValue() != null) {
                                usuario.setId(dataSnapshot.getKey());
                                cJogo.setIdDoConvite(dados.getKey());
                                cJogo.setApelido(usuario.getApelido());
                                cJogo.setIdDaImagemDoStatus(recuperaIdDaImagemDoStatus(cJogo.getStatus(), "P"));
                                cJogo.setFraseDoConvite("LHE CONVIDOU PARA JOGAR.");
                                andamentoDoConvite = "P";
                                listaDeConvites.add(cJogo);

                                adapterConvitesLV.notifyDataSetChanged();

                                if (listaDeConvites == null || listaDeConvites.size() == 0) {
                                    relativeMsg.setVisibility(View.VISIBLE);
                                    txtMsg.setText("Nenhum convite pendente.");
                                }else{
                                    relativeMsg.setVisibility(View.INVISIBLE);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };
                    usuarioDAO.buscaUsuarioPorId(cJogo.getId()).addListenerForSingleValueEvent(valueEventListenerUsuarioPorId);
                    /*contadorAuxiliar++;
                    if (contador == contadorAuxiliar) {
                        progressDialog.dismiss();
                    }*/
                }
                if (listaDeConvites == null || listaDeConvites.size() == 0) {
                    relativeMsg.setVisibility(View.VISIBLE);
                    txtMsg.setText("Nenhum convite pendente.");
                }else{
                    relativeMsg.setVisibility(View.INVISIBLE);
                }

                //adapterNomes.notifyDataSetChanged();
                adapterConvitesLV = new AdapterConvitesListView(listaDeConvites, ConvitesActivity.this);
                lv_convites.setAdapter(adapterConvitesLV);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        conviteDAO.buscaTodosOsConvitesPendentesDeUmUsuario(idUsuarioLogado).addListenerForSingleValueEvent(valueEventListenerBuscaTodosOsConvitesPendentesDeUmCliente);

        //CLIQUE DO BOTÃO CONVIDAR
        btnConvidar.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(ConvitesActivity.this, AmigosActivity.class);
               startActivity(intent);
               finish();
           }
        });

        //CLIQUE DO BOTÃO ENVIADOS
        btnEnviados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                relativeMsg.setVisibility(View.INVISIBLE);
                if (listaDeConvites != null) {
                    listaDeConvites.clear();
                }
                habilitarBotao(1);
                valueEventListenerBuscaTodosOsConvitesEnviadosDeUmCliente = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        contador = Integer.parseInt(dataSnapshot.getChildrenCount() + "");
                        contadorAuxiliar = 0;
                        for (final DataSnapshot dados: dataSnapshot.getChildren()) {
                            //progressDialog = ProgressDialog.show(ConvitesActivity.this, "Aguarde!!!", "Processo em andamento.", false, false);
                            final ConviteJogoAndamento cJogo = dados.getValue(ConviteJogoAndamento.class);
                            valueEventListenerUsuarioPorId = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Usuario usuario = dataSnapshot.getValue(Usuario.class);
                                    if (dataSnapshot.getValue() != null) {
                                        usuario.setId(dataSnapshot.getKey());
                                        cJogo.setIdDoConvite(dados.getKey());
                                        cJogo.setApelido(usuario.getApelido());
                                        String valorStatus = "E";
                                        if ("EA".equals(cJogo.getStatus())) {
                                            valorStatus = "A"; //ACEITO
                                        }else if ("ER".equals(cJogo.getStatus())) {
                                            valorStatus = "R"; //RECUSADO
                                        }
                                        cJogo.setIdDaImagemDoStatus(recuperaIdDaImagemDoStatus(valorStatus, "E"));
                                        cJogo.setFraseDoConvite("FOI SEU CONVIDADO PARA JOGAR.");
                                        andamentoDoConvite = "E";
                                        listaDeConvites.add(cJogo);

                                        adapterConvitesLV.notifyDataSetChanged();

                                        if (listaDeConvites == null || listaDeConvites.size() == 0) {
                                            relativeMsg.setVisibility(View.VISIBLE);
                                            txtMsg.setText("Nenhum convite enviado.");
                                        }else{
                                            relativeMsg.setVisibility(View.INVISIBLE);
                                        }
                                    }
                                    /*contadorAuxiliar++;
                                    if (contador == contadorAuxiliar) {
                                        progressDialog.dismiss();
                                    }*/
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            };
                            usuarioDAO.buscaUsuarioPorId(cJogo.getId()).addListenerForSingleValueEvent(valueEventListenerUsuarioPorId);
                        }
                        if (listaDeConvites == null || listaDeConvites.size() == 0) {
                            relativeMsg.setVisibility(View.VISIBLE);
                            txtMsg.setText("Nenhum convite enviado.");
                        }else{
                            relativeMsg.setVisibility(View.INVISIBLE);
                        }
                        //adapterNomes.notifyDataSetChanged();
                        adapterConvitesLV = new AdapterConvitesListView(listaDeConvites, ConvitesActivity.this);
                        lv_convites.setAdapter(adapterConvitesLV);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
                conviteDAO.buscaTodosOsConvitesEnviadosDeUmUsuario(idUsuarioLogado).addListenerForSingleValueEvent(valueEventListenerBuscaTodosOsConvitesEnviadosDeUmCliente);
            }
        });

        //CLIQUE DO BOTÃO PENDENTES
        btnPendentes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                relativeMsg.setVisibility(View.INVISIBLE);
                if (listaDeConvites != null) {
                    listaDeConvites.clear();
                }
                habilitarBotao(2);
                valueEventListenerBuscaTodosOsConvitesPendentesDeUmCliente = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        contador = Integer.parseInt(dataSnapshot.getChildrenCount() + "");
                        contadorAuxiliar = 0;
                        for (final DataSnapshot dados: dataSnapshot.getChildren()) {
                            //progressDialog = ProgressDialog.show(ConvitesActivity.this, "Aguarde!!!", "Processo em andamento.", false, false);
                            final ConviteJogoAndamento cJogo = dados.getValue(ConviteJogoAndamento.class);
                            valueEventListenerUsuarioPorId = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Usuario usuario = dataSnapshot.getValue(Usuario.class);
                                    if (dataSnapshot.getValue() != null) {
                                        usuario.setId(dataSnapshot.getKey());
                                        cJogo.setIdDoConvite(dados.getKey());
                                        cJogo.setApelido(usuario.getApelido());
                                        cJogo.setIdDaImagemDoStatus(recuperaIdDaImagemDoStatus(cJogo.getStatus(), "P"));
                                        cJogo.setFraseDoConvite("LHE CONVIDOU PARA JOGAR.");
                                        andamentoDoConvite = "P";
                                        listaDeConvites.add(cJogo);

                                        adapterConvitesLV.notifyDataSetChanged();

                                        if (listaDeConvites == null || listaDeConvites.size() == 0) {
                                            relativeMsg.setVisibility(View.VISIBLE);
                                            txtMsg.setText("Nenhum convite pendente.");
                                        }else{
                                            relativeMsg.setVisibility(View.INVISIBLE);
                                        }
                                    }
                                    /*contadorAuxiliar++;
                                    if (contador == contadorAuxiliar) {
                                        progressDialog.dismiss();
                                    }*/
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            };
                            usuarioDAO.buscaUsuarioPorId(cJogo.getId()).addListenerForSingleValueEvent(valueEventListenerUsuarioPorId);
                        }
                        if (listaDeConvites == null || listaDeConvites.size() == 0) {
                            relativeMsg.setVisibility(View.VISIBLE);
                            txtMsg.setText("Nenhum convite pendente.");
                        }else{
                            relativeMsg.setVisibility(View.INVISIBLE);
                        }
                        //adapterNomes.notifyDataSetChanged();
                        adapterConvitesLV = new AdapterConvitesListView(listaDeConvites, ConvitesActivity.this);
                        lv_convites.setAdapter(adapterConvitesLV);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
                conviteDAO.buscaTodosOsConvitesPendentesDeUmUsuario(idUsuarioLogado).addListenerForSingleValueEvent(valueEventListenerBuscaTodosOsConvitesPendentesDeUmCliente);

            }
        });

        //CLIQUE DO BOTÃO RECEBIDOS
        btnRecebidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                relativeMsg.setVisibility(View.INVISIBLE);
                if (listaDeConvites != null) {
                    listaDeConvites.clear();
                }
                habilitarBotao(3);
                valueEventListenerBuscaTodosOsConvitesRecebidosDeUmCliente = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        contador = Integer.parseInt(dataSnapshot.getChildrenCount() + "");
                        contadorAuxiliar = 0;
                        for (final DataSnapshot dados: dataSnapshot.getChildren()) {
                            //progressDialog = ProgressDialog.show(ConvitesActivity.this, "Aguarde!!!", "Processo em andamento.", false, false);
                            final ConviteJogoAndamento cJogo = dados.getValue(ConviteJogoAndamento.class);
                            valueEventListenerUsuarioPorId = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Usuario usuario = dataSnapshot.getValue(Usuario.class);
                                    if (dataSnapshot.getValue() != null) {
                                        usuario.setId(dataSnapshot.getKey());
                                        cJogo.setIdDoConvite(dados.getKey());
                                        cJogo.setApelido(usuario.getApelido());
                                        cJogo.setIdDaImagemDoStatus(recuperaIdDaImagemDoStatus(cJogo.getStatus(), "R"));
                                        cJogo.setFraseDoConvite("LHE CONVIDOU PARA JOGAR.");
                                        andamentoDoConvite = "R";
                                        listaDeConvites.add(cJogo);

                                        adapterConvitesLV.notifyDataSetChanged();

                                        if (listaDeConvites == null || listaDeConvites.size() == 0) {
                                            relativeMsg.setVisibility(View.VISIBLE);
                                            txtMsg.setText("Nenhum convite pendente.");
                                        }else{
                                            relativeMsg.setVisibility(View.INVISIBLE);
                                        }
                                    }
                                    /*contadorAuxiliar++;
                                    if (contador == contadorAuxiliar) {
                                        progressDialog.dismiss();
                                    }*/
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            };
                            usuarioDAO.buscaUsuarioPorId(cJogo.getId()).addListenerForSingleValueEvent(valueEventListenerUsuarioPorId);
                        }
                        if (listaDeConvites == null || listaDeConvites.size() == 0) {
                            relativeMsg.setVisibility(View.VISIBLE);
                            txtMsg.setText("Nenhum convite recebido/confirmado.");
                        }else{
                            relativeMsg.setVisibility(View.INVISIBLE);
                        }
                        //adapterNomes.notifyDataSetChanged();
                        adapterConvitesLV = new AdapterConvitesListView(listaDeConvites, ConvitesActivity.this);
                        lv_convites.setAdapter(adapterConvitesLV);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
                conviteDAO.buscaTodosOsConvitesRecebidosDeUmUsuario(idUsuarioLogado).addListenerForSingleValueEvent(valueEventListenerBuscaTodosOsConvitesRecebidosDeUmCliente);
            }
        });

        //CLIQUE DO REGISTRO NO LISTVIEW
        lv_convites.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ConvitesActivity.this);
                alertDialog.setTitle("Convite");
                alertDialog.setMessage("Responda o convite para jogar.");
                alertDialog.setCancelable(true);

                if ("E".equals(andamentoDoConvite)) {
                    alertDialog.setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            chaveDoConvite = listaDeConvites.get(position).getIdDoConvite();
                            excluiConvite(chaveDoConvite, andamentoDoConvite);
                            listaDeConvites.remove(position);
                            adapterConvitesLV.notifyDataSetChanged();
                        }
                    });

                    alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                }else if ("P".equals(andamentoDoConvite)) {
                    alertDialog.setPositiveButton("Aceitar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            chaveDoConvite = listaDeConvites.get(position).getIdDoConvite();
                            valueEventListenerConvitePendentePorIdEChaveDoConvite = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    ConviteJogo cj = dataSnapshot.getValue(ConviteJogo.class);
                                    cj.setIdDoConvite(chaveDoConvite);
                                    conviteJogo = new ConviteJogo(cj);
                                    conviteJogo.setStatus("A"); //Aceito
                                    conviteJogo.setIdDeQuemConvidou(cj.getId());
                                    respondeConvite("A");

                                    listaDeConvites.remove(position);
                                    adapterConvitesLV.notifyDataSetChanged();

                                    valueEventListenerConviteEnviadoPorIdEChaveDoConvite = new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            ConviteJogo cj = dataSnapshot.getValue(ConviteJogo.class);
                                            conviteJogoEnviado = new ConviteJogo();
                                            conviteJogoEnviado.setIdDoConvite(chaveDoConvite);
                                            conviteJogoEnviado.setIdDeQuemConvidou(conviteJogo.getIdDeQuemConvidou());
                                            conviteJogoEnviado.setStatus("EA"); //Aceito
                                            alteraStatusConviteEnviado(conviteJogoEnviado);
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    };
                                    conviteDAO.buscaConviteEnviadoPorIdEChaveDoConvite(cj.getId(), chaveDoConvite).addListenerForSingleValueEvent(valueEventListenerConviteEnviadoPorIdEChaveDoConvite);

                                    if (listaDeConvites == null || listaDeConvites.size() == 0) {
                                        relativeMsg.setVisibility(View.VISIBLE);
                                        txtMsg.setText("Nenhum convite pendente.");
                                    }else{
                                        relativeMsg.setVisibility(View.INVISIBLE);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            };
                            conviteDAO.buscaConvitePendentePorIdEChaveDoConvite(idUsuarioLogado, chaveDoConvite).addListenerForSingleValueEvent(valueEventListenerConvitePendentePorIdEChaveDoConvite);
                        }
                    });

                    alertDialog.setNegativeButton("Recusar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            chaveDoConvite = listaDeConvites.get(position).getIdDoConvite();
                            valueEventListenerConvitePendentePorIdEChaveDoConvite = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    ConviteJogo cj = dataSnapshot.getValue(ConviteJogo.class);
                                    cj.setIdDoConvite(chaveDoConvite);
                                    conviteJogo = new ConviteJogo(cj);
                                    conviteJogo.setStatus("R"); //Recusado
                                    conviteJogo.setIdDeQuemConvidou(cj.getId());
                                    respondeConvite("R");

                                    listaDeConvites.remove(position);
                                    adapterConvitesLV.notifyDataSetChanged();

                                    valueEventListenerConviteEnviadoPorIdEChaveDoConvite = new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            ConviteJogo cj = dataSnapshot.getValue(ConviteJogo.class);
                                            conviteJogoEnviado = new ConviteJogo();
                                            conviteJogoEnviado.setIdDoConvite(chaveDoConvite);
                                            conviteJogoEnviado.setIdDeQuemConvidou(conviteJogo.getIdDeQuemConvidou());
                                            conviteJogoEnviado.setStatus("ER"); //Recusado
                                            alteraStatusConviteEnviado(conviteJogoEnviado);
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    };
                                    conviteDAO.buscaConviteEnviadoPorIdEChaveDoConvite(cj.getId(), chaveDoConvite).addListenerForSingleValueEvent(valueEventListenerConviteEnviadoPorIdEChaveDoConvite);

                                    if (listaDeConvites == null || listaDeConvites.size() == 0) {
                                        relativeMsg.setVisibility(View.VISIBLE);
                                        txtMsg.setText("Nenhum convite pendente.");
                                    }else{
                                        relativeMsg.setVisibility(View.INVISIBLE);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            };
                            conviteDAO.buscaConvitePendentePorIdEChaveDoConvite(idUsuarioLogado, chaveDoConvite).addListenerForSingleValueEvent(valueEventListenerConvitePendentePorIdEChaveDoConvite);

                        }
                    });
                }else{
                    alertDialog.setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            chaveDoConvite = listaDeConvites.get(position).getIdDoConvite();
                            excluiConvite(chaveDoConvite, andamentoDoConvite);
                            listaDeConvites.remove(position);
                            adapterConvitesLV.notifyDataSetChanged();
                        }
                    });

                    alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                }

                alertDialog.create();
                alertDialog.show();
            }
        });

    }

    private int recuperaIdDaImagemDoStatus(String status, String andamento) {

        int idImg = 0;
        if ("P".equals(andamento)) {
            idImg = getResources().getIdentifier("ic_help_outline", "drawable",  getPackageName());
        }else if ("R".equals(andamento) || "E".equals(andamento)) {
            if ("A".equals(status)) {
                idImg = getResources().getIdentifier("ic_thumb_up", "drawable", getPackageName());
            } else if ("R".equals(status)) {
                idImg = getResources().getIdentifier("ic_thumb_down", "drawable", getPackageName());
            } else {
                if ("E".equals(andamento)) {
                    idImg = getResources().getIdentifier("ic_help_outline", "drawable", getPackageName());
                }
            }
        }
        return idImg;
    }

    private void habilitarBotao(int valor) {

        btnEnviados.setBackgroundColor(Color.parseColor(Constantes.CINZA_CLARO));
        btnPendentes.setBackgroundColor(Color.parseColor(Constantes.CINZA_CLARO));
        btnRecebidos.setBackgroundColor(Color.parseColor(Constantes.CINZA_CLARO));
        if (valor == 1) {
            btnEnviados.setBackgroundColor(Color.parseColor(Constantes.WHITE));
        }else if(valor == 2) {
            btnPendentes.setBackgroundColor(Color.parseColor(Constantes.WHITE));
        }else{
            btnRecebidos.setBackgroundColor(Color.parseColor(Constantes.WHITE));
        }
    }

    public void respondeConvite(String andamento) {
        conviteJogo.responderConvite(idUsuarioLogado);
        if ("A".equals(andamento)) {
            Toast.makeText(ConvitesActivity.this, "Convite aceito com sucesso!!!", Toast.LENGTH_SHORT).show();
        }else if ("R".equals(andamento)) {
            Toast.makeText(ConvitesActivity.this, "Convite recusado com sucesso!!!", Toast.LENGTH_SHORT).show();
        }
    }

    public void alteraStatusConviteEnviado(ConviteJogo cj_enviado) {
        conviteJogoEnviado.alteraStatusConviteEnviado(cj_enviado);
    }

    public void excluiConvite(String idDoConvite, String andamento) {
        conviteJogo.excluirConvite(idUsuarioLogado, idDoConvite, andamento);
        Toast.makeText(ConvitesActivity.this, "Convite excluído com sucesso!!!", Toast.LENGTH_SHORT).show();
    }
}
