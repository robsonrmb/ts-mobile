package br.com.topspin.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
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
import br.com.topspin.util.UtilACT;

public class PesquisaActivity extends AppCompatActivity {

    private int contador;
    private ProgressDialog progressDialog;
    private Toolbar toolbar;
    private Spinner spinnerEstados;
    private ArrayAdapter<String> adapterEstados;

    private ListView lv_jogadores;
    private List<Usuario> listaDeJogadores = new ArrayList<Usuario>();
    private AdapterJogadoresListView adapterJogadoresLV;

    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private ValueEventListener valueEventListenerBuscaTodosOsUsuarios;
    private ValueEventListener valueEventListenerBuscaAmigosDeUmUsuario;

    private Preferencias preferencias;
    private String idUsuarioLogado;

    @Override
    protected void onStop() {
        super.onStop();
        usuarioDAO.buscaTodosOsUsuarios().removeEventListener(valueEventListenerBuscaTodosOsUsuarios);
        /*if (usuarioDAO.buscaAmigosDeUmUsuario(idUsuarioLogado) != null) {
            usuarioDAO.buscaAmigosDeUmUsuario(idUsuarioLogado).removeEventListener(valueEventListenerBuscaAmigosDeUmUsuario);
        }*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisa);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("PESQUISAR ADVERSÁRIOS");
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        preferencias = new Preferencias(PesquisaActivity.this);
        idUsuarioLogado = preferencias.getIdUsuarioLogado();
        if ("0".equals(preferencias.getTema())) {
            toolbar.setBackgroundColor(Color.parseColor(Constantes.QUADRA_RAPIDA));
        }else if ("1".equals(preferencias.getTema())) {
            toolbar.setBackgroundColor(Color.parseColor(Constantes.SAIBRO));
        }else{
            toolbar.setBackgroundColor(Color.parseColor(Constantes.GRAMA));
        }

        spinnerEstados = (Spinner) findViewById(R.id.spinnerEstados);
        adapterEstados = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Constantes.ESTADOS);
        adapterEstados.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEstados.setAdapter(adapterEstados);

        lv_jogadores = (ListView) findViewById(R.id.idListaJogadores);
        adapterJogadoresLV = new AdapterJogadoresListView(listaDeJogadores, this);
        lv_jogadores.setAdapter(adapterJogadoresLV);

        //QUANDO SELECIONAR UM VALOR DO SPINNER ESTADOS
        spinnerEstados.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

                if (position != 0) {
                    progressDialog = ProgressDialog.show(PesquisaActivity.this, "Aguarde!!!", "Processo em andamento.", true, false);
                }
                listaDeJogadores = new ArrayList<Usuario>();
                adapterJogadoresLV = new AdapterJogadoresListView(listaDeJogadores, PesquisaActivity.this);

                //LISTENERS NA PROPRIA ACTIVITY
                valueEventListenerBuscaTodosOsUsuarios = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        contador = 0;
                        for (DataSnapshot dados: dataSnapshot.getChildren()) {
                            contador++;
                            final Usuario usuario = dados.getValue(Usuario.class);
                            String idEstadoSelecionado = UtilACT.buscaEstadoDoIndice(spinnerEstados.getSelectedItemId());
                            String idEstadoDoUsuario = UtilACT.retornaDadoFormatadoParaEstado(usuario.getEstado(), true);
                            if (idEstadoSelecionado.equals(idEstadoDoUsuario) && usuario.getEstado() != null && !"0".equals(usuario.getEstado())) {
                                usuario.setId(dados.getKey());
                                if (!usuario.getId().equals(idUsuarioLogado)) {

                                    valueEventListenerBuscaAmigosDeUmUsuario = new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            String idAmigo = null;
                                            for (DataSnapshot dados: dataSnapshot.getChildren()) {
                                                String idAmigoDaLista = dados.getValue(String.class);
                                                if (usuario.getId().equals(idAmigoDaLista)) {
                                                    idAmigo = idAmigoDaLista;
                                                }
                                            }
                                            if (idAmigo == null) {
                                                usuario.setAmigo("N");
                                            }else{
                                                usuario.setAmigo("S");
                                            }
                                            listaDeJogadores.add(usuario);

                                            adapterJogadoresLV.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    };
                                    usuarioDAO.buscaTodosOsAmigosDeUmUsuario(idUsuarioLogado).addListenerForSingleValueEvent(valueEventListenerBuscaAmigosDeUmUsuario);
                                    //listaDeJogadores.add(usuario);
                                }
                            }
                            if (progressDialog != null && contador >= dataSnapshot.getChildrenCount()) {
                                progressDialog.dismiss();
                            }
                        }
                        //adapterNomes.notifyDataSetChanged();
                        adapterJogadoresLV = new AdapterJogadoresListView(listaDeJogadores, PesquisaActivity.this);
                        lv_jogadores.setAdapter(adapterJogadoresLV);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
                usuarioDAO.buscaTodosOsUsuarios().addListenerForSingleValueEvent(valueEventListenerBuscaTodosOsUsuarios);

                Log.w("", "OnItemSelected.");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //CLIQUE DO REGISTRO NO LISTVIEW
        lv_jogadores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(PesquisaActivity.this);
                alertDialog.setTitle("Jogador: " + listaDeJogadores.get(position).getNome());
                alertDialog.setMessage("Escolha um dos botões abaixo:");
                alertDialog.setCancelable(true);

                alertDialog.setNegativeButton("Adicionar como amigo", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        valueEventListenerBuscaAmigosDeUmUsuario = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String idAmigo = null;
                                for (DataSnapshot dados: dataSnapshot.getChildren()) {
                                    String idAmigoDaLista = dados.getValue(String.class);
                                    if (listaDeJogadores.get(position).getId().equals(idAmigoDaLista)) {
                                        idAmigo = idAmigoDaLista;
                                    }
                                }
                                if (idAmigo != null) {
                                    Toast.makeText(PesquisaActivity.this, "Este atleta já estava adicionado como seu amigo!!!", Toast.LENGTH_SHORT).show();
                                } else {
                                    idAmigo = listaDeJogadores.get(position).getId();
                                    Amigo amigo = new Amigo(idUsuarioLogado, idAmigo);
                                    amigo.salvar();
                                    listaDeJogadores.get(position).setAmigo("S");
                                    Toast.makeText(PesquisaActivity.this, "Atleta adicionado como seu amigo!!!", Toast.LENGTH_SHORT).show();

                                    adapterJogadoresLV.notifyDataSetChanged();
                                }
                                Log.w("", "getUser:onChildAdded");
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        };
                        usuarioDAO.buscaTodosOsAmigosDeUmUsuario(idUsuarioLogado).addListenerForSingleValueEvent(valueEventListenerBuscaAmigosDeUmUsuario);

                    }
                });

                alertDialog.setNeutralButton("Visualizar estatísticas", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(PesquisaActivity.this, EstatisticasActivity.class);
                        intent.putExtra("idDoUsuario", listaDeJogadores.get(position).getId());
                        intent.putExtra("nomeDoUsuario", listaDeJogadores.get(position).getNome());
                        startActivity(intent);
                        finish();
                    }
                });

                alertDialog.setPositiveButton("Avaliar jogador", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(PesquisaActivity.this, AvaliacaoResumidaActivity.class);
                        intent.putExtra("estado", spinnerEstados.getSelectedItem().toString());
                        intent.putExtra("nome", listaDeJogadores.get(position).getNome());
                        intent.putExtra("id", listaDeJogadores.get(position).getId());
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
