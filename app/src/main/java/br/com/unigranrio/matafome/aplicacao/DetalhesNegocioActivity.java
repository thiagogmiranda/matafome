package br.com.unigranrio.matafome.aplicacao;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import br.com.unigranrio.matafome.R;
import br.com.unigranrio.matafome.aplicacao.webservices.ObterNegocioPorLocalizacaoAsyncService;
import br.com.unigranrio.matafome.aplicacao.webservices.ObterNegocioUsuarioAsyncService;
import br.com.unigranrio.matafome.aplicacao.webservices.OnAsyncTaskExecutedListener;
import br.com.unigranrio.matafome.dominio.acoes.ResultadoAcao;
import br.com.unigranrio.matafome.dominio.modelo.Avaliacao;
import br.com.unigranrio.matafome.dominio.modelo.DetalheNegocio;
import br.com.unigranrio.matafome.dominio.modelo.Negocio;
import br.com.unigranrio.matafome.dominio.modelo.Usuario;

public class DetalhesNegocioActivity extends AppCompatActivity implements OnAsyncTaskExecutedListener<ResultadoAcao<DetalheNegocio>> {

    private TextView txtNomeNegocio;
    private TextView txtDescricaoNegocio;

    private TextView txtNota;
    private TextView txtComentario;

    private LinearLayout avaliacaoContainer;
    private LinearLayout avalieContainer;

    private Negocio negocio;

    private Button btnAvaliar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_negocio);

        txtNomeNegocio = (TextView) findViewById(R.id.txtNome);
        txtDescricaoNegocio = (TextView) findViewById(R.id.txtDescricao);
        txtNota = (TextView) findViewById(R.id.txtNota);
        txtComentario = (TextView) findViewById(R.id.txtComentario);

        btnAvaliar = (Button) findViewById(R.id.btnAvaliar);

        avaliacaoContainer = (LinearLayout) findViewById(R.id.suaAvaliacaoContainer);
        avalieContainer = (LinearLayout) findViewById(R.id.avalieContainer);

        avalieContainer.setVisibility(View.INVISIBLE);
        avaliacaoContainer.setVisibility(View.INVISIBLE);

        Bundle extras = getIntent().getExtras();
        long idNegocio = extras.getLong("idNegocio");

        btnAvaliar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirTelaEnviarAvaliacao();
            }
        });

        carregarDetalhes(idNegocio);
    }

    private static final int AVALIAR = 10;

    private void abrirTelaEnviarAvaliacao() {
        Intent intent = new Intent();
        intent.setClass(this, EnviarAvaliacaoActivity.class);

        intent.putExtra("idNegocio", negocio.getId());

        startActivityForResult(intent, AVALIAR);
    }

    private void carregarDetalhes(long idNegocio){
        try {
            Usuario usuarioLogado = App.obterUsuarioLogado();

            ObterNegocioPorLocalizacaoAsyncService service = new ObterNegocioPorLocalizacaoAsyncService();
            service.setOnExecutedListener(this);

            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Obtendo informações sobre o vendedor selecionado ...");

            service.setProgressDialog(progressDialog);

            service.execute(idNegocio, usuarioLogado.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAsyncTaskExecuted(ResultadoAcao<DetalheNegocio> resultado) {
        if(resultado.estaValido()){
            DetalheNegocio detalhe = (DetalheNegocio)resultado.getData();
            
            preencherDados(detalhe);
        } else {
            App.exibirMensagensDeErro(this, resultado.getMensagens());

            finish();
        }
    }

    private void preencherDados(DetalheNegocio detalhe) {
        negocio = detalhe.getNegocio();

        Avaliacao avaliacao = detalhe.getAvaliacaoUsuario();

        txtNomeNegocio.setText(negocio.getNome());

        String descricao = negocio.getDescricao();

        if(null == descricao || "".equals(descricao.trim())){
            descricao = "Sem descrição.";
        }

        txtDescricaoNegocio.setText(descricao);

        if(avaliacao != null){
            txtNota.setText(String.format("%d", avaliacao.getNota()));
            txtComentario.setText(avaliacao.getComentario());

            avaliacaoContainer.setVisibility(View.VISIBLE);
            avalieContainer.setVisibility(View.INVISIBLE);
        } else {
            avaliacaoContainer.setVisibility(View.INVISIBLE);
            avalieContainer.setVisibility(View.VISIBLE);
        }
    }
}
