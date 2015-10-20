package br.com.unigranrio.matafome.aplicacao.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.unigranrio.matafome.R;
import br.com.unigranrio.matafome.dominio.modelo.Barraca;

/**
 * Created by WebFis33 on 15/10/2015.
 */
public class PontoVendaAdapter extends ArrayAdapter<Barraca> {
    public PontoVendaAdapter(Context context, int resource, List<Barraca> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View view = convertView;

        if(view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.item_lista_ponto_venda, null);
        }

        Barraca item = getItem(position);

        if(item != null) {
            TextView txtPontoVenda = (TextView)view.findViewById(R.id.txtNomePontoVenda);

            txtPontoVenda.setText(item.getNome());
        }

        return view;
    }
}
