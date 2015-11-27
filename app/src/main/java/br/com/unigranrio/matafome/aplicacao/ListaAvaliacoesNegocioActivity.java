package br.com.unigranrio.matafome.aplicacao;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import br.com.unigranrio.matafome.R;
import br.com.unigranrio.matafome.aplicacao.adapters.AvaliacaoListViewAdapter;
import br.com.unigranrio.matafome.aplicacao.webservices.ObterAvaliacoesAsyncService;
import br.com.unigranrio.matafome.aplicacao.webservices.OnAsyncTaskExecutedListener;
import br.com.unigranrio.matafome.dominio.acoes.Mensagem;
import br.com.unigranrio.matafome.dominio.acoes.ResultadoAcao;
import br.com.unigranrio.matafome.dominio.modelo.Avaliacao;
import br.com.unigranrio.matafome.dominio.modelo.Usuario;

public class ListaAvaliacoesNegocioActivity extends AppCompatActivity implements OnAsyncTaskExecutedListener<ResultadoAcao<List<Avaliacao>>> {

    private AvaliacaoListViewAdapter adapter;
    private List<Avaliacao> avaliacoes;
    private ListView lstAvaliacoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_avaliacoes_negocio);

        lstAvaliacoes = (ListView) findViewById(R.id.lstAvaliacoes);

        avaliacoes = new ArrayList<>();

        adapter = new AvaliacaoListViewAdapter(this, R.layout.item_lista_avaliacoes, avaliacoes);
        lstAvaliacoes.setAdapter(adapter);

        carregarAvaliacoes();
    }

    private void carregarAvaliacoes() {
        try {
            Bundle extras = getIntent().getExtras();
            long idDono = extras.getLong("idDono");

            if(idDono == 0){
                Usuario usuarioLogado = App.obterUsuarioLogado();
                idDono = usuarioLogado.getId();
            }

            ObterAvaliacoesAsyncService service = new ObterAvaliacoesAsyncService();
            service.setOnExecutedListener(this);

            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Carregando avaliações ...");

            service.setProgressDialog(progressDialog);

            service.execute(idDono);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAsyncTaskExecuted(ResultadoAcao<List<Avaliacao>> resultadoAcao) {
        if(resultadoAcao.estaValido()){
            List<Avaliacao> avaliacoes = (List<Avaliacao>)resultadoAcao.getData();

            if(avaliacoes.size() == 0){
                List<Mensagem> mensagems = new ArrayList<>();
                mensagems.add(new Mensagem("Ainda não avaliaram o seu negócio"));

                resultadoAcao.adicionarMensagens(mensagems);

                App.exibirMensagensDeErro(this, resultadoAcao.getMensagens());
            } else {
                this.avaliacoes.clear();
                this.avaliacoes.addAll(avaliacoes);
                this.adapter.notifyDataSetChanged();
            }
        } else {
            App.exibirMensagensDeErro(this, resultadoAcao.getMensagens());
        }
    }
}
