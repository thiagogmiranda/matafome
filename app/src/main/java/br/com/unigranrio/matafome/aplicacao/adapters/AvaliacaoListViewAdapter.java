package br.com.unigranrio.matafome.aplicacao.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import br.com.unigranrio.matafome.R;
import br.com.unigranrio.matafome.dominio.modelo.Avaliacao;

/**
 * Created by Thiago on 17/11/2015.
 */
public class AvaliacaoListViewAdapter extends ArrayAdapter<Avaliacao> {
    public AvaliacaoListViewAdapter(Context context, int resource, List<Avaliacao> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View view = convertView;

        if(view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.item_lista_avaliacoes, null);
        }

        Avaliacao item = getItem(position);

        if(item != null) {
            TextView txtNomeUsuario = (TextView) view.findViewById(R.id.txtNomeUsuario);
            TextView txtNota = (TextView) view.findViewById(R.id.txtNota);
            TextView txtComentario = (TextView) view.findViewById(R.id.txtComentario);

            txtNomeUsuario.setText(item.getUsuario().getNome());
            txtNota.setText(item.getNota() + "");
            txtComentario.setText(item.getComentario());
        }

        return view;
    }
}
