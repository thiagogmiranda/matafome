package br.com.unigranrio.matafome.aplicacao;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import br.com.unigranrio.matafome.R;
import br.com.unigranrio.matafome.aplicacao.webservices.CriarUsuarioAsyncTask;
import br.com.unigranrio.matafome.aplicacao.webservices.OnAsyncTaskExecutedListener;
import br.com.unigranrio.matafome.dominio.acoes.Mensagem;
import br.com.unigranrio.matafome.dominio.acoes.ResultadoAcao;
import br.com.unigranrio.matafome.dominio.modelo.Usuario;

public class CriarUsuarioActivity extends AppCompatActivity implements OnAsyncTaskExecutedListener<ResultadoAcao> {

    private EditText txtNome;
    private EditText txtEmail;
    private EditText txtSenha;
    private EditText txtConfirmacaoSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_usuario);
        txtNome = (EditText)findViewById(R.id.txt_nome_cadastro);
        txtEmail = (EditText)findViewById(R.id.txt_email_cadastro);
        txtSenha = (EditText)findViewById(R.id.txt_senha_cadastro);
        txtConfirmacaoSenha = (EditText)findViewById(R.id.txt_confirma_senha);

        Button btnCriar = (Button)findViewById(R.id.btn_form_criar);

        btnCriar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criarUsuario();
            }
        });
    }

    private void criarUsuario(){
        String nome = txtNome.getText().toString();
        String email = txtEmail.getText().toString();
        String senha = txtSenha.getText().toString();
        String confirmaSenha = txtConfirmacaoSenha.getText().toString();

        List<Mensagem> erros = new ArrayList<>();

        if("".equals(nome)){
            erros.add(new Mensagem("Informe um nome"));
        }
        if("".equals(email)){
            erros.add(new Mensagem("Informe um email"));
        }
        if("".equals(senha)){
            erros.add(new Mensagem("Informe uma senha"));
        } else if(!confirmaSenha.equals(senha)){
            erros.add(new Mensagem("Senhas não conferem"));
        }

        if(erros.size() > 0){
            App.exibirMensagensDeErro(this, erros);
        } else {
            Usuario usuario = new Usuario();
            usuario.setNome(txtNome.getText().toString());
            usuario.setEmail(txtEmail.getText().toString());
            usuario.setSenha(txtSenha.getText().toString());

            try {
                CriarUsuarioAsyncTask task = new CriarUsuarioAsyncTask();

                ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Um minuto..");
                progressDialog.setMessage("Criando Usuário...");

                task.setProgressDialog(progressDialog);
                task.setOnExecutedListener(this);

                task.execute(usuario);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onAsyncTaskExecuted(final ResultadoAcao resultado) {
        if(resultado.estaValido()){
            new AlertDialog.Builder(this)
                    .setMessage("Bem vindo ao mata fome.")
                    .setTitle("Cadastro realizado!")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finalizarComSucesso(resultado);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .show();
        } else {
            App.exibirMensagensDeErro(this, resultado.getMensagens());
        }
    }

    private void finalizarComSucesso(ResultadoAcao resultadoAcao){
        Usuario usuario = null; // (Usuario)resultadoAcao.getData();

        App.efetuarLogin(usuario);

        setResult(RESULT_OK);
        finish();
    }
}
