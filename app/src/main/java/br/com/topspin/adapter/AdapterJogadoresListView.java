package br.com.topspin.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.topspin.R;
import br.com.topspin.model.Usuario;
import br.com.topspin.util.UtilACT;


public class AdapterJogadoresListView extends BaseAdapter {

    private final List<Usuario> listaDeJogadores;
    private Activity act;

    public AdapterJogadoresListView(List<Usuario> listaDeJogadores, Activity act) {
        this.listaDeJogadores = listaDeJogadores;
        this.act = act;
    }

    @Override
    public int getCount() {
        return listaDeJogadores.size();
    }

    @Override
    public Object getItem(int position) {
        return listaDeJogadores.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = act.getLayoutInflater().inflate(R.layout.lista_jogadores_listview, parent, false);
        Usuario usuario = listaDeJogadores.get(position);

        TextView id = (TextView) view.findViewById(R.id.txtId);
        TextView nome = (TextView) view.findViewById(R.id.txtNome);
        TextView apelido = (TextView) view.findViewById(R.id.txtApelido);
        ImageView amigo = (ImageView) view.findViewById(R.id.imgAmigo);
        TextView estado = (TextView) view.findViewById(R.id.txtEstado);
        TextView cidade = (TextView) view.findViewById(R.id.txtCidade);

        id.setText(usuario.getId());
        nome.setText(usuario.getNome());
        apelido.setText(usuario.getApelido());
        cidade.setText(usuario.getCidade());
        estado.setText(usuario.getEstado());
        amigo.setVisibility(View.INVISIBLE);
        if ("S".equals(usuario.getAmigo())) {
            amigo.setVisibility(View.VISIBLE);
        }
        return view;
    }
}
