package br.com.unigranrio.matafome.aplicacao;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.wallet.wobs.UriData;

import br.com.unigranrio.matafome.R;
import br.com.unigranrio.matafome.aplicacao.webservices.ObterNegocioUsuarioAsyncService;
import br.com.unigranrio.matafome.aplicacao.webservices.OnAsyncTaskExecutedListener;
import br.com.unigranrio.matafome.dominio.acoes.ResultadoAcao;
import br.com.unigranrio.matafome.dominio.modelo.Negocio;
import br.com.unigranrio.matafome.dominio.modelo.Usuario;

public class GerenciarNegocioActivity extends AppCompatActivity implements OnAsyncTaskExecutedListener<ResultadoAcao<Negocio>>, OnMapReadyCallback {

    private GoogleMap map;
    private TextView txtNome;
    private TextView txtDescricao;

    private Button btnVerAvaliacoes;
    private Button btnEditarNegocio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerenciar_negocio);

        txtNome = (TextView) findViewById(R.id.txtNome);
        txtDescricao = (TextView) findViewById(R.id.txtDescricao);
        btnEditarNegocio = (Button) findViewById(R.id.btnEditar);
        btnVerAvaliacoes = (Button) findViewById(R.id.btnAvaliacoes);

        btnVerAvaliacoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarActivityListaAvaliacoesNegocio();
            }
        });

        btnEditarNegocio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarActivityEdicaoNegocio();
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_gerenciar_negocio, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.logout:
                fazerLogout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onAsyncTaskExecuted(ResultadoAcao<Negocio> resultado) {
        if(resultado.estaValido()){
            Negocio negocio = (Negocio)resultado.getData();

            if(negocio != null){
                preencherDadosNegocio(negocio);
            } else {
                abrirTelaCadastroNegocio();
            }
        } else {
            App.exibirMensagensDeErro(this, resultado.getMensagens());
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        map.setMyLocationEnabled(false);

        carregarDadosNegocio();
    }

    private void fazerLogout() {
        App.deslogarUsuario();

        Intent intent = new Intent();
        intent.setClass(this, InicioActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
        finish();
    }

    private void carregarDadosNegocio() {
        try {
            Usuario usuarioLogado = App.obterUsuarioLogado();

            ObterNegocioUsuarioAsyncService service = new ObterNegocioUsuarioAsyncService();
            service.setOnExecutedListener(this);

            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Carregando informações ...");

            service.setProgressDialog(progressDialog);

            service.execute(usuarioLogado.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void preencherDadosNegocio(Negocio negocio) {
        txtNome.setText(negocio.getNome());

        String descricao = negocio.getDescricao();

        if(null == descricao || "".equals(descricao.trim())){
            descricao = "Sem descrição.";
        } else if (descricao.length() > 100){
            descricao = descricao.substring(0, 100);
        }

        txtDescricao.setText(descricao);

        LatLng latLng = new LatLng(negocio.getLatitude(), negocio.getLongitude());

        map.addMarker(new MarkerOptions()
                .position(latLng));

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18.2f));
    }

    private void abrirTelaCadastroNegocio() {
        Intent intent = new Intent();
        intent.setClass(this, CadastrarNegocioActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
        finish();
    }

    private void iniciarActivityListaAvaliacoesNegocio() {
        Intent intent = new Intent();
        intent.setClass(this, ListaAvaliacoesNegocioActivity.class);

        intent.putExtra("idNegocio", 1l); //Passar o id correto ou a LATLNG

        startActivity(intent);
    }

    private void iniciarActivityEdicaoNegocio() {

    }
}
