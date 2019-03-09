package br.com.topspin.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.topspin.R;
import br.com.topspin.bean.BeanAvaliacaoPendente;


public class AdapterAvaliacoesPendentes extends BaseAdapter {

    private final List<BeanAvaliacaoPendente> avaliacoesPendentes;
    private Activity act;

    public AdapterAvaliacoesPendentes(List<BeanAvaliacaoPendente> avaliacoesPendentes, Activity act) {
        this.avaliacoesPendentes = avaliacoesPendentes;
        this.act = act;
    }

    @Override
    public int getCount() {
        return avaliacoesPendentes.size();
    }

    @Override
    public Object getItem(int position) {
        return avaliacoesPendentes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = act.getLayoutInflater().inflate(R.layout.lista_avaliacoes_pendentes, parent, false);
        BeanAvaliacaoPendente registro = avaliacoesPendentes.get(position);

        TextView idDaAvaliacao = (TextView) view.findViewById(R.id.txtIdAvaliacaoPendente);
        TextView nome = (TextView) view.findViewById(R.id.txtNome);
        TextView apelido = (TextView) view.findViewById(R.id.txtApelido);
        TextView dataAvaliacao = (TextView) view.findViewById(R.id.txtDataAvaliacao);

        idDaAvaliacao.setText(registro.getIdDaAvaliacao());
        nome.setText(registro.getNomeDoAvaliador());
        apelido.setText(registro.getApelidoDoAvaliador());
        dataAvaliacao.setText(registro.getDataDaAvaliacao());

        return view;
    }
}
