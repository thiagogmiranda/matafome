package br.com.unigranrio.matafome.aplicacao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import br.com.unigranrio.matafome.R;
import br.com.unigranrio.matafome.dominio.modelo.Usuario;

public class InicioActivity extends Activity {
    private Button btnEntrar;
    private Button btnCadastrarUsuario;

    private LinearLayout acoes;

    private static final int LOGIN = 1;
    private static final int CADASTRO = 2;

    private static final int LOAD_TIMEOUT = 2000; // 2 segundos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_inicio);

        btnEntrar = (Button) findViewById(R.id.btnEntrar);
        btnCadastrarUsuario = (Button) findViewById(R.id.btnCadastrarUsuario);

        acoes = (LinearLayout) findViewById(R.id.acoes);

        iniciarApp();
    }

    private void iniciarApp() {
        acoes.setVisibility(View.INVISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Usuario usuario = App.obterUsuarioLogado();

                if (usuario != null) {
                    acoes.setVisibility(View.INVISIBLE);

                    iniciarActivityPrincipal();
                } else {
                    btnEntrar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            abrirActivityLogin();
                        }
                    });
                    btnCadastrarUsuario.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            abrirActivityCadastroUsuario();
                        }
                    });

                    acoes.setVisibility(View.VISIBLE);
                }
            }
        }, LOAD_TIMEOUT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case LOGIN:
                tratarResultadoLogin(resultCode);
                break;
            case CADASTRO:
                tratarResultadoCadastro(resultCode);
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void tratarResultadoCadastro(int resultCode) {
        if (resultCode == RESULT_OK) {
            iniciarApp();
        }
    }

    private void tratarResultadoLogin(int resultCode) {
        if (resultCode == RESULT_OK) {
            iniciarApp();
        }
    }

    private void iniciarActivityPrincipal() {
        Usuario usuario = App.obterUsuarioLogado();
        String tipo = usuario.getTipo();

        switch (tipo){
            case "UC": abrirTelaPesquisaLanches();
                break;
            case "UV": abrirTelaGerenciamentoNegocio();
                break;
            case "UX": abrirTelaEscolhaAcao();
                break;
            case "UN": abrirTelaCadastroTipoUsuario();
                break;
        }
    }

    private void abrirTelaCadastroTipoUsuario(){
        Intent intent = new Intent();
        intent.setClass(this, CadastroTipoUsuarioActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
        finish();
    }

    private void abrirTelaEscolhaAcao(){
        Intent intent = new Intent();
        //intent.setClass(this, PesquisaLanchesRapidosActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
        finish();
    }

    private void abrirTelaGerenciamentoNegocio(){
        Intent intent = new Intent();
        intent.setClass(this, GerenciarNegocioActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
        finish();
    }

    private void abrirTelaPesquisaLanches(){
        Intent intent = new Intent();
        intent.setClass(this, PesquisaLanchesRapidosActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
        finish();
    }

    private void abrirActivityLogin() {
        Intent intent = new Intent();
        intent.setClass(this, LoginActivity.class);

        startActivityForResult(intent, LOGIN);
    }

    private void abrirActivityCadastroUsuario() {
        Intent intent = new Intent();
        intent.setClass(this, CriarUsuarioActivity.class);

        startActivityForResult(intent, CADASTRO);
    }
}
