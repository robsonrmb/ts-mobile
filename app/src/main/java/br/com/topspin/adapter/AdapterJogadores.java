package br.com.topspin.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.topspin.R;
import br.com.topspin.bean.BeanJogador;


public class AdapterJogadores extends BaseAdapter {

    private final List<BeanJogador> listaDeJogadores;
    private Activity act;

    public AdapterJogadores(List<BeanJogador> listaDeJogadores, Activity act) {
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

        View view = act.getLayoutInflater().inflate(R.layout.lista_jogadores, parent, false);
        BeanJogador registro = listaDeJogadores.get(position);

        TextView id = (TextView) view.findViewById(R.id.txtId);
        TextView nome = (TextView) view.findViewById(R.id.txtNome);

        id.setText(registro.getId());
        nome.setText(registro.getNome());

        return view;
    }
}
