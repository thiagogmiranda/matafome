package br.com.unigranrio.matafome.aplicacao;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import br.com.unigranrio.matafome.R;
import br.com.unigranrio.matafome.aplicacao.webservices.EditarUsuarioAsyncTask;
import br.com.unigranrio.matafome.aplicacao.webservices.EnviarAvaliacaoAsyncService;
import br.com.unigranrio.matafome.aplicacao.webservices.OnAsyncTaskExecutedListener;
import br.com.unigranrio.matafome.dominio.acoes.ResultadoAcao;
import br.com.unigranrio.matafome.dominio.modelo.Avaliacao;
import br.com.unigranrio.matafome.dominio.modelo.Negocio;
import br.com.unigranrio.matafome.dominio.modelo.Usuario;

public class EnviarAvaliacaoActivity extends AppCompatActivity implements OnAsyncTaskExecutedListener<ResultadoAcao<Avaliacao>> {

    private SeekBar notaSeekBar;
    private TextView txtNotaSelecionada;

    private EditText txtComentario;

    private Button btnEnviar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_avaliacao);

        notaSeekBar = (SeekBar) findViewById(R.id.nota);
        txtNotaSelecionada = (TextView) findViewById(R.id.txtNotaSelecionada);
        txtComentario = (EditText) findViewById(R.id.txtComentarioAvaliacao);

        btnEnviar = (Button) findViewById(R.id.btnAvaliar);

        notaSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                txtNotaSelecionada.setText("" + seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarAvaliacao();
            }
        });
    }

    private void enviarAvaliacao() {
        Bundle extras = getIntent().getExtras();

        long idNegocio = extras.getLong("idNegocio");
        Negocio negocio = new Negocio();
        negocio.setId(idNegocio);

        Usuario usuario = App.obterUsuarioLogado();

        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setNota(notaSeekBar.getProgress());
        avaliacao.setComentario(txtComentario.getText().toString());
        avaliacao.setUsuario(usuario);
        avaliacao.setNegocio(negocio);

        try {
            EnviarAvaliacaoAsyncService task = new EnviarAvaliacaoAsyncService();

            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Enviando avaliação ...");

            task.setProgressDialog(progressDialog);
            task.setOnExecutedListener(this);

            task.execute(avaliacao);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onAsyncTaskExecuted(ResultadoAcao<Avaliacao> resultado) {
        if(resultado.estaValido()){
            setResult(RESULT_OK);
            finish();
        } else {
            App.exibirMensagensDeErro(this, resultado.getMensagens());
        }
    }
}
