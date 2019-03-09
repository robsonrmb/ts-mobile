package br.com.topspin.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.topspin.R;
import br.com.topspin.adapter.AdapterJogadoresListView;
import br.com.topspin.constantes.Constantes;
import br.com.topspin.firebase.dao.UsuarioDAO;
import br.com.topspin.helper.Preferencias;
import br.com.topspin.model.Amigo;
import br.com.topspin.model.Usuario;

public class AmigosActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ProgressDialog progressDialog;
    private TextView txtFrase;
    private int contador;
    private int contadorAuxiliar;

    private ListView lv_jogadores;
    private List<Usuario> listaDeJogadores = new ArrayList<Usuario>();
    private AdapterJogadoresListView adapterJogadoresLV;

    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private ValueEventListener valueEventListenerBuscaTodosOsAmigosDeUmUsuario;
    private ValueEventListener valueEventListenerUsuarioPorId;

    private Preferencias preferencias;
    private String idUsuarioLogado;

    @Override
    protected void onStop() {
        super.onStop();
        if (valueEventListenerUsuarioPorId != null) {
            usuarioDAO.buscaUsuarioPorId(idUsuarioLogado).removeEventListener(valueEventListenerUsuarioPorId);
        }
        if (valueEventListenerBuscaTodosOsAmigosDeUmUsuario != null) {
            usuarioDAO.buscaTodosOsAmigosDeUmUsuario(idUsuarioLogado).removeEventListener(valueEventListenerBuscaTodosOsAmigosDeUmUsuario);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amigos);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("MEUS AMIGOS");
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        txtFrase = (TextView) findViewById(R.id.txtFrase);

        lv_jogadores = (ListView) findViewById(R.id.idListaJogadores);
        adapterJogadoresLV = new AdapterJogadoresListView(listaDeJogadores, this);
        lv_jogadores.setAdapter(adapterJogadoresLV);

        preferencias = new Preferencias(AmigosActivity.this);
        idUsuarioLogado = preferencias.getIdUsuarioLogado();
        if ("0".equals(preferencias.getTema())) {
            toolbar.setBackgroundColor(Color.parseColor(Constantes.QUADRA_RAPIDA));
        }else if ("1".equals(preferencias.getTema())) {
            toolbar.setBackgroundColor(Color.parseColor(Constantes.SAIBRO));
        }else{
            toolbar.setBackgroundColor(Color.parseColor(Constantes.GRAMA));
        }

        //LISTENERS NA PROPRIA ACTIVITY
        progressDialog = ProgressDialog.show(AmigosActivity.this, "Aguarde!!!", "Processo em andamento.", false, false);
        valueEventListenerBuscaTodosOsAmigosDeUmUsuario = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                contador = Integer.parseInt(dataSnapshot.getChildrenCount() + "");
                contadorAuxiliar = 0;
                for (DataSnapshot dados: dataSnapshot.getChildren()) {
                    String idDoAmigo = dados.getValue(String.class);
                    valueEventListenerUsuarioPorId = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Usuario usuario = dataSnapshot.getValue(Usuario.class);
                            if (dataSnapshot.getValue() != null) {
                                usuario.setId(dataSnapshot.getKey());
                                usuario.setAmigo("S");
                                listaDeJogadores.add(usuario);

                                adapterJogadoresLV.notifyDataSetChanged();

                                contadorAuxiliar++;
                                if (contador == contadorAuxiliar) {
                                    progressDialog.dismiss();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };
                    usuarioDAO.buscaUsuarioPorId(idDoAmigo).addListenerForSingleValueEvent(valueEventListenerUsuarioPorId);
                    txtFrase.setText("Veja a lista de seus amigos abaixo.");
                }

                if (contador == 0) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    txtFrase.setText("Adicione um amigo na tela de pesquisa.");
                }

                //adapterNomes.notifyDataSetChanged();
                adapterJogadoresLV = new AdapterJogadoresListView(listaDeJogadores, AmigosActivity.this);
                lv_jogadores.setAdapter(adapterJogadoresLV);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        usuarioDAO.buscaTodosOsAmigosDeUmUsuario(idUsuarioLogado).addListenerForSingleValueEvent(valueEventListenerBuscaTodosOsAmigosDeUmUsuario);

        //CLIQUE DO REGISTRO NO LISTVIEW
        lv_jogadores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(AmigosActivity.this);
                alertDialog.setTitle("Jogador: " + listaDeJogadores.get(position).getNome());
                alertDialog.setMessage("Convide seu amigo para jogar.");
                alertDialog.setCancelable(true);

                Button btnConvite = new Button(AmigosActivity.this);
                btnConvite.setText("Convidar");
                alertDialog.setView(btnConvite);

                alertDialog.setNegativeButton("Retirar como amigo.", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        valueEventListenerBuscaTodosOsAmigosDeUmUsuario = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String keyIdAmigo = "";
                                for (DataSnapshot dados: dataSnapshot.getChildren()) {
                                    String idAmigoDaLista = dados.getValue(String.class);
                                    if (listaDeJogadores.get(position).getId().equals(idAmigoDaLista)) {
                                        keyIdAmigo = dados.getKey();
                                        break;
                                    }
                                }
                                Amigo amigo = new Amigo(idUsuarioLogado, null);
                                amigo.setIdGerado(keyIdAmigo);
                                amigo.excluir();

                                listaDeJogadores.remove(position);
                                adapterJogadoresLV.notifyDataSetChanged();

                                Toast.makeText(AmigosActivity.this, "Amigo retirado com sucesso!!!", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        };
                        usuarioDAO.buscaTodosOsAmigosDeUmUsuario(idUsuarioLogado).addListenerForSingleValueEvent(valueEventListenerBuscaTodosOsAmigosDeUmUsuario);
                    }
                });

                alertDialog.setNeutralButton("Visualizar estatísticas", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(AmigosActivity.this, EstatisticasActivity.class);
                        intent.putExtra("idDoUsuario", listaDeJogadores.get(position).getId());
                        intent.putExtra("nomeDoUsuario", listaDeJogadores.get(position).getNome());
                        startActivity(intent);
                        finish();
                    }
                });

                alertDialog.setPositiveButton("Avaliar jogador", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(AmigosActivity.this, AvaliacaoResumidaActivity.class);
                        intent.putExtra("estado", listaDeJogadores.get(position).getEstado());
                        intent.putExtra("nome", listaDeJogadores.get(position).getNome());
                        intent.putExtra("id", listaDeJogadores.get(position).getId());
                        startActivity(intent);
                        finish();
                    }
                });

                btnConvite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(AmigosActivity.this, QueroJogarActivity.class);
                        intent.putExtra("idDoUsuario", listaDeJogadores.get(position).getId());
                        intent.putExtra("nomeDoUsuario", listaDeJogadores.get(position).getNome());
                        startActivity(intent);
                        finish();
                    }
                });

                alertDialog.create();
                alertDialog.show();

            }
        });
    }
}
