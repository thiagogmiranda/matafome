package br.com.unigranrio.matafome.aplicacao;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

import br.com.unigranrio.matafome.R;

public class DetalhesNegocioActivity extends AppCompatActivity {

    private TextView txtNomeNegocio;
    private TextView txtDescricaoNegocio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_negocio);

        txtNomeNegocio = (TextView) findViewById(R.id.txtNome);
        txtDescricaoNegocio = (TextView) findViewById(R.id.txtDescricao);
    }

    private void carregarDetalhes(){
        
    }
}
