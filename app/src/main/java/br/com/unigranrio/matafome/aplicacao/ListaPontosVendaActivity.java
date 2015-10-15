package br.com.unigranrio.matafome.aplicacao;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import br.com.unigranrio.matafome.R;
import br.com.unigranrio.matafome.aplicacao.adapters.PontoVendaAdapter;
import br.com.unigranrio.matafome.aplicacao.webservices.ObterBarracasUsuarioAsyncTask;
import br.com.unigranrio.matafome.aplicacao.webservices.OnAsyncTaskExecutedListener;
import br.com.unigranrio.matafome.dominio.modelo.Barraca;

public class ListaPontosVendaActivity extends AppCompatActivity implements OnAsyncTaskExecutedListener<List<Barraca>> {

    private List<Barraca> pontosVendaList;
    private PontoVendaAdapter pontoVendaAdapter;
    private ListView lstPontosVenda;

    private ObterBarracasUsuarioAsyncTask obterBarracasUsuarioAsyncTask;

    public ListaPontosVendaActivity(){
        pontosVendaList = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_pontos_venda);

        lstPontosVenda = (ListView) findViewById(R.id.lstPontosVenda);

        pontoVendaAdapter = new PontoVendaAdapter(this, R.layout.item_lista_ponto_venda, pontosVendaList);

        lstPontosVenda.setAdapter(pontoVendaAdapter);

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Carregando lista de pontos de venda ...");

        obterBarracasUsuarioAsyncTask = new ObterBarracasUsuarioAsyncTask();
        obterBarracasUsuarioAsyncTask.setOnExecutedListener(this);
        obterBarracasUsuarioAsyncTask.setProgressDialog(progressDialog);

        atualizarLista();
    }

    public void atualizarLista(){
        SharedPreferences prefs = getSharedPreferences("mata_fome", MODE_PRIVATE);
        String email = prefs.getString(LoginActivity.EMAIL_USUARIO_LOGADO, "");

        obterBarracasUsuarioAsyncTask.execute(email);
    }

    @Override
    public void onAsyncTaskExecuted(List<Barraca> pontosVenda) {
        pontosVendaList.clear();
        pontosVendaList.addAll(pontosVenda);
        pontoVendaAdapter.notifyDataSetChanged();
    }
}
