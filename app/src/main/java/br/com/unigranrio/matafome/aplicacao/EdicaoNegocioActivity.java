package br.com.unigranrio.matafome.aplicacao;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import br.com.unigranrio.matafome.R;
import br.com.unigranrio.matafome.aplicacao.webservices.ObterNegocioUsuarioAsyncService;
import br.com.unigranrio.matafome.aplicacao.webservices.OnAsyncTaskExecutedListener;
import br.com.unigranrio.matafome.dominio.acoes.ResultadoAcao;
import br.com.unigranrio.matafome.dominio.modelo.Negocio;
import br.com.unigranrio.matafome.dominio.modelo.Usuario;

public class EdicaoNegocioActivity
        extends AppCompatActivity
        implements OnAsyncTaskExecutedListener<ResultadoAcao<Negocio>>, OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private GoogleMap map;
    private TextView txtNome;
    private EditText txtDescricao;

    private Negocio negocio;

    private EditarNegocioSalvarMediator mediator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edicao_negocio);

        txtNome = (TextView) findViewById(R.id.txtNome);
        txtDescricao = (EditText) findViewById(R.id.txtDescricao);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mediator = new EditarNegocioSalvarMediator(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_cadastrar_negocio, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.salvar:
                salvarEdicao();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onAsyncTaskExecuted(ResultadoAcao<Negocio> resultado) {
        if(resultado.estaValido()){
            Negocio negocio = (Negocio)resultado.getData();

            preencherDadosNegocio(negocio);
        } else {
            App.exibirMensagensDeErro(this, resultado.getMensagens());
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        map.setMyLocationEnabled(true);
        map.setOnMapClickListener(this);

        carregarDadosNegocio();
    }

    private void carregarDadosNegocio() {
        try {
            Usuario usuarioLogado = App.obterUsuarioLogado();

            ObterNegocioUsuarioAsyncService service = new ObterNegocioUsuarioAsyncService();
            service.setOnExecutedListener(this);

            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Carregando dados para edição ...");

            service.setProgressDialog(progressDialog);

            service.execute(usuarioLogado.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void preencherDadosNegocio(Negocio negocio) {
        this.negocio = negocio;

        txtNome.setText(negocio.getNome());

        txtDescricao.setText(negocio.getDescricao());

        LatLng latLng = new LatLng(negocio.getLatitude(), negocio.getLongitude());

        map.addMarker(new MarkerOptions().position(latLng));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18.2f));
    }

    private void salvarEdicao() {
        negocio.setDescricao(txtDescricao.getText().toString());

        mediator.salvar(negocio);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        map.clear();

        negocio.setLatitude(latLng.latitude);
        negocio.setLongitude(latLng.longitude);

        map.addMarker(new MarkerOptions()
                .position(latLng));
    }

    public void finalizar(){
        finish();
    }
}
