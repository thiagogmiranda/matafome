package br.com.unigranrio.matafome.aplicacao;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import br.com.unigranrio.matafome.R;
import br.com.unigranrio.matafome.aplicacao.webservices.ObterUsuarioAsyncTask;
import br.com.unigranrio.matafome.aplicacao.webservices.OnAsyncTaskExecutedListener;
import br.com.unigranrio.matafome.dominio.modelo.Usuario;

public class LoginActivity extends AppCompatActivity implements OnAsyncTaskExecutedListener<Usuario> {
    private Button btnLogin;

    private EditText txtEmail;
    private EditText txtSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = (Button)findViewById(R.id.btn_login);
        txtEmail = (EditText)findViewById(R.id.txt_email);
        txtSenha = (EditText)findViewById(R.id.txt_senha);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fazerLogin();
            }
        });
    }

    private void fazerLogin(){
        List<String> erros = new ArrayList<>();

        String email = txtEmail.getText().toString();
        String senha = getSenha();

        if("".equals(email)){
            erros.add("Informe um email");
        }
        if("".equals(senha)){
            erros.add("Informe uma senha");
        }

        if(erros.size() > 0){
            String mensagem = "";

            for (String erro : erros) {
                mensagem = mensagem.concat("- " + erro + "\n");
            }

            new AlertDialog.Builder(this)
                    .setMessage(mensagem)
                    .setTitle("Atenção!")
                    .setPositiveButton("OK", null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {
            try {
                ObterUsuarioAsyncTask task = new ObterUsuarioAsyncTask();

                ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Obtendo dados do usuário..");

                task.setProgressDialog(progressDialog);
                task.setOnExecutedListener(this);

                task.execute(email);
            } catch (Exception e) {
               Log.e("ERRO: ", e.getMessage(), e);
            }
        }
    }

    private String getSenha(){
        return txtSenha.getText().toString();
    }

    @Override
    public void onAsyncTaskExecuted(Usuario usuario) {
        if(credenciaisEstaoValidas(usuario)){
            App.efetuarLogin(usuario);

            setResult(RESULT_OK);
            finish();
        } else {
            showLoginErrorDialog();
        }
    }

    private boolean credenciaisEstaoValidas(Usuario usuario){
        return usuario != null && usuario.getSenha().equals(getSenha());
    }

    private void showLoginErrorDialog(){
        new AlertDialog.Builder(this)
                .setMessage("Email e/ou senha inválidos.")
                .setTitle("Atenção!")
                .setPositiveButton("OK", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
