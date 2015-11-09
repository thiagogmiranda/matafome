package br.com.unigranrio.matafome.aplicacao;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import br.com.unigranrio.matafome.R;
import br.com.unigranrio.matafome.dominio.modelo.Usuario;

public class InicioActivity extends Activity {
    private Button btnEntrar;
    private Button btnCadastrarUsuario;

    private static final int LOGIN = 1;
    private static final int CADASTRO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_inicio);

        btnEntrar = (Button) findViewById(R.id.btnEntrar);
        btnCadastrarUsuario = (Button) findViewById(R.id.btnCadastrarUsuario);

        btnEntrar.setVisibility(View.INVISIBLE);
        btnCadastrarUsuario.setVisibility(View.INVISIBLE);

        Usuario usuario = App.obterUsuarioLogado();

        if (usuario != null) {
            btnEntrar.setVisibility(View.INVISIBLE);
            btnCadastrarUsuario.setVisibility(View.INVISIBLE);

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

            btnEntrar.setVisibility(View.VISIBLE);
            btnCadastrarUsuario.setVisibility(View.VISIBLE);
        }
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
            abrirActivityLogin();
        }
    }

    private void tratarResultadoLogin(int resultCode) {
        if (resultCode == RESULT_OK) {
            iniciarActivityPrincipal();
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
        //intent.setClass(this, PesquisaLanchesRapidosActivity.class);
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

        startActivityForResult(intent, 1);
    }

    private void abrirActivityCadastroUsuario() {
        Intent intent = new Intent();
        intent.setClass(this, CriarUsuarioActivity.class);

        startActivity(intent);
    }
}
