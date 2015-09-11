package br.com.unigranrio.matafome.aplicacao;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import br.com.unigranrio.matafome.R;
import br.com.unigranrio.matafome.dominio.modelo.Usuario;
import br.com.unigranrio.matafome.dominio.repositorios.UsuarioRepositorio;
import br.com.unigranrio.matafome.infra.sqLite.config.SqlLiteHelper;
import br.com.unigranrio.matafome.infra.sqLite.repositorios.UsuarioRepositorioSQLite;

public class LoginActivity extends AppCompatActivity {

    private Button btnCadastro;
    private Button btnLogin;

    private EditText txtEmail;
    private EditText txtSenha;

    private UsuarioRepositorio usuarioRepositorio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usuarioRepositorio = new UsuarioRepositorioSQLite(new SqlLiteHelper(this));

        btnCadastro = (Button)findViewById(R.id.btn_cadastro);
        btnLogin = (Button)findViewById(R.id.btn_login);
        txtEmail = (EditText)findViewById(R.id.txt_email);
        txtSenha = (EditText)findViewById(R.id.txt_senha);

        btnCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criarUsuario();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fazerLogin();
            }
        });
    }

    private void criarUsuario(){
        Intent intent = new Intent();
        intent.setClass(this, CriarUsuarioActivity.class);

        startActivity(intent);
    }

    private void fazerLogin(){
        List<String> erros = new ArrayList<>();

        String email = txtEmail.getText().toString();
        String senha = txtSenha.getText().toString();

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
            Usuario busca = usuarioRepositorio.obterPorLogin(email);

            if(busca != null){
                if(busca.getSenha().equals(senha)){

                    SharedPreferences prefs = getSharedPreferences("mata_fome", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();

                    editor.putBoolean("tem_usuario_logado", true);
                    editor.putString("email_usuario_logado", email);
                    editor.putString("senha_usuario_logado", senha);

                    editor.commit();

                    setResult(RESULT_OK);
                    finish();
                } else {
                    new AlertDialog.Builder(this)
                            .setMessage("Email e/ou senha inválidos.")
                            .setTitle("Atenção!")
                            .setPositiveButton("OK", null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            } else {
                new AlertDialog.Builder(this)
                        .setMessage("Email e/ou senha inválidos.")
                        .setTitle("Atenção!")
                        .setPositiveButton("OK", null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }
    }
}
