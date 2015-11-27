package br.com.unigranrio.matafome.aplicacao;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import br.com.unigranrio.matafome.R;
import br.com.unigranrio.matafome.aplicacao.webservices.EditarUsuarioAsyncTask;
import br.com.unigranrio.matafome.aplicacao.webservices.OnAsyncTaskExecutedListener;
import br.com.unigranrio.matafome.dominio.acoes.ResultadoAcao;
import br.com.unigranrio.matafome.dominio.modelo.Usuario;

public class CadastroTipoUsuarioActivity extends Activity implements OnAsyncTaskExecutedListener<ResultadoAcao> {

    private Button estouComFome;
    private Button queroVender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_tipo_usuario);

        estouComFome = (Button) findViewById(R.id.btnQueroComer);
        queroVender = (Button) findViewById(R.id.btnQueroVender);

        estouComFome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atualizarTipoUsuario("UC");
            }
        });
        queroVender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atualizarTipoUsuario("UV");
            }
        });
    }

    private void atualizarTipoUsuario(String tipo){
        Usuario usuario = App.obterUsuarioLogado();
        usuario.setTipo(tipo);

        try {
            EditarUsuarioAsyncTask task = new EditarUsuarioAsyncTask();

            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Preparando primeira incialização ...");

            task.setProgressDialog(progressDialog);
            task.setOnExecutedListener(this);

            task.execute(usuario);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void abrirTelaPesquisaLanches(){
        Intent intent = new Intent();
        intent.setClass(this, PesquisaLanchesRapidosActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
        finish();
    }

    @Override
    public void onAsyncTaskExecuted(ResultadoAcao resultadoAcao) {
        if(resultadoAcao.estaValido()) {
            Usuario usuario = (Usuario) resultadoAcao.getData();
            App.efetuarLogin(usuario);

            switch (usuario.getTipo()) {
                case "UC":
                    abrirTelaPesquisaLanches();
                    break;
                case "UV":
                    abrirTelaCadastroNegocio();
                    break;
            }
        } else {
            App.exibirMensagensDeErro(this, resultadoAcao.getMensagens());
        }
    }

    private void abrirTelaCadastroNegocio() {
        Intent intent = new Intent();
        intent.setClass(this, CadastrarNegocioActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
        finish();
    }
}
