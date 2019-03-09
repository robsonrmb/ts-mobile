package br.com.topspin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.topspin.R;
import br.com.topspin.model.Usuario;

public class AdapterUsuarios extends ArrayAdapter<Usuario>{

    private ArrayList<Usuario> usuarios;
    private Context context;

    public AdapterUsuarios(Context c, ArrayList<Usuario> objects) {
        super(c, 0, objects);
        this.usuarios = objects;
        this.context = c;
    }

    //@NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if (usuarios != null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.lista_jogadores, parent, false);

            TextView id = (TextView) view.findViewById(R.id.txtId);
            TextView nome = (TextView) view.findViewById(R.id.txtNome);

            Usuario usuario = usuarios.get(position);
            id.setText(usuario.getId());
            nome.setText(usuario.getNome());
        }

        return view;
    }
}
