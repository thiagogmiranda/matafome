package br.com.unigranrio.matafome.aplicacao;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
        List<String> erros = new ArrayList<>();

        String nome = txtNome.getText().toString();
        String email = txtEmail.getText().toString();
        String senha = txtSenha.getText().toString();
        String confirmaSenha = txtConfirmacaoSenha.getText().toString();

        if("".equals(nome)){
            erros.add("Informe um nome");
        }
        if("".equals(email)){
            erros.add("Informe um email");
        }
        if("".equals(senha)){
            erros.add("Informe uma senha");
        } else if(!confirmaSenha.equals(senha)){
            erros.add("Senhas não conferem");
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
    public void onAsyncTaskExecuted(ResultadoAcao resultado) {
        if(resultado.deuCerto()){
            new AlertDialog.Builder(this)
                    .setMessage("Bem vindo ao mata fome.")
                    .setTitle("Cadastro realizado!")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .show();
        } else {
            List<Mensagem> msgs = resultado.getMensagens();
            String mensagem = "";

            for (Mensagem m : msgs) {
                mensagem = mensagem.concat("- " + m.getTexto() + "\n");
            }

            new AlertDialog.Builder(this)
                    .setMessage(mensagem)
                    .setTitle("Atenção!")
                    .setPositiveButton("OK", null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }
}
