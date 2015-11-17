package br.com.unigranrio.matafome.aplicacao;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import br.com.unigranrio.matafome.R;
import br.com.unigranrio.matafome.aplicacao.adapters.AvaliacaoListViewAdapter;
import br.com.unigranrio.matafome.dominio.modelo.Avaliacao;

public class ListaAvaliacoesNegocioActivity extends AppCompatActivity {

    private AvaliacaoListViewAdapter adapter;
    private List<Avaliacao> avaliacoes;
    private ListView lstAvaliacoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_avaliacoes_negocio);

        lstAvaliacoes = (ListView) findViewById(R.id.lstAvaliacoes);

        avaliacoes = new ArrayList<>();

        Avaliacao a = new Avaliacao();
        a.setNota(5);
        a.setComentario("Ótimo atendimento e o lanche super gostoso, recomento a todos que passarem por aqui");
        a.setUsuario(App.obterUsuarioLogado());

        Avaliacao b = new Avaliacao();
        b.setNota(5);
        b.setComentario("Ótimo lanche, super saboroso, recomento a todos que passarem por aqui");
        b.setUsuario(App.obterUsuarioLogado());

        Avaliacao c = new Avaliacao();
        c.setNota(5);
        c.setComentario("Ótimo atendimento, recomento a todos que passarem por aqui");
        c.setUsuario(App.obterUsuarioLogado());

        avaliacoes.add(a);
        avaliacoes.add(b);
        avaliacoes.add(c);

        adapter = new AvaliacaoListViewAdapter(this, R.layout.item_lista_avaliacoes, avaliacoes);

        lstAvaliacoes.setAdapter(adapter);

        //Bundle extras = getIntent().getExtras();
        //double lat = extras.getDouble("lat");
        //double lng = extras.getDouble("lng");

        //carregarAvaliacoes(lat, lng);
    }

    private void carregarAvaliacoes(double lat, double lng) {

    }
}
