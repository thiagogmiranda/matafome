package br.com.unigranrio.matafome.aplicacao;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import br.com.unigranrio.matafome.R;
import br.com.unigranrio.matafome.aplicacao.webservices.ObterNegocioUsuarioAsyncService;
import br.com.unigranrio.matafome.aplicacao.webservices.OnAsyncTaskExecutedListener;
import br.com.unigranrio.matafome.dominio.acoes.ResultadoAcao;
import br.com.unigranrio.matafome.dominio.modelo.Negocio;
import br.com.unigranrio.matafome.dominio.modelo.Usuario;

public class GerenciarNegocioActivity extends Activity implements OnAsyncTaskExecutedListener<ResultadoAcao<Negocio>>, OnMapReadyCallback {

    private static final int CADASTRO_NEGOCIO = 1;

    private GoogleMap map;
    private TextView txtNome;
    private TextView txtDescricao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerenciar_negocio);

        txtNome = (TextView) findViewById(R.id.txtNome);
        txtDescricao = (TextView) findViewById(R.id.txtDescricao);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CADASTRO_NEGOCIO:
                tratarResultadoCadastro(resultCode);
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
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
        txtDescricao.setText(negocio.getDescricao());

        LatLng latLng = new LatLng(negocio.getLatitude(), negocio.getLongitude());

        map.addMarker(new MarkerOptions()
                .position(latLng));
    }

    private void abrirTelaCadastroNegocio() {
        Intent intent = new Intent();
        intent.setClass(this, CadastrarNegocioActivity.class);

        startActivityForResult(intent, CADASTRO_NEGOCIO);
    }

    private void tratarResultadoCadastro(int resultCode) {
        if(resultCode == RESULT_OK){
            carregarDadosNegocio();
        }
    }
}
