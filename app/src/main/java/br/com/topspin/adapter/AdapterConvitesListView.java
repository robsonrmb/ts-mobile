package br.com.topspin.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.topspin.R;
import br.com.topspin.model.ConviteJogo;
import br.com.topspin.model.ConviteJogoAndamento;
import br.com.topspin.model.Usuario;


public class AdapterConvitesListView extends BaseAdapter {

    private final List<ConviteJogoAndamento> listaDeConvites;
    private Activity act;

    public AdapterConvitesListView(List<ConviteJogoAndamento> listaDeConvites, Activity act) {
        this.listaDeConvites = listaDeConvites;
        this.act = act;
    }

    @Override
    public int getCount() {
        return listaDeConvites.size();
    }

    @Override
    public Object getItem(int position) {
        return listaDeConvites.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = act.getLayoutInflater().inflate(R.layout.lista_convites_listview, parent, false);
        ConviteJogoAndamento conviteJogoAndamento = listaDeConvites.get(position);

        TextView id = (TextView) view.findViewById(R.id.txtId);
        TextView nome = (TextView) view.findViewById(R.id.txtNome);
        TextView apelido = (TextView) view.findViewById(R.id.txtApelido);

        TextView data = (TextView) view.findViewById(R.id.txtDataDoJogo);
        TextView local = (TextView) view.findViewById(R.id.txtLocalDoJogo);
        TextView observacao = (TextView) view.findViewById(R.id.txtObservacao);
        TextView periodo = (TextView) view.findViewById(R.id.txtPeriodoDoJogo);

        ImageView img_status = (ImageView) view.findViewById(R.id.imgStatus);

        id.setText(conviteJogoAndamento.getId());
        apelido.setText(conviteJogoAndamento.getApelido());
        nome.setText(conviteJogoAndamento.getFraseDoConvite());

        data.setText(conviteJogoAndamento.getDataDoJogo());
        local.setText(conviteJogoAndamento.getLocalDoJogo());
        observacao.setText(conviteJogoAndamento.getObservacao());
        periodo.setText(conviteJogoAndamento.getPeriodoDoJogo());
        img_status.setImageResource(conviteJogoAndamento.getIdDaImagemDoStatus());

        return view;
    }

}
