package br.com.unigranrio.matafome.aplicacao;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import br.com.unigranrio.matafome.R;
import br.com.unigranrio.matafome.aplicacao.webservices.CadastrarNegocioAsyncService;
import br.com.unigranrio.matafome.aplicacao.webservices.OnAsyncTaskExecutedListener;
import br.com.unigranrio.matafome.dominio.acoes.ResultadoAcao;
import br.com.unigranrio.matafome.dominio.modelo.Negocio;

public class CadastrarNegocioActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, OnAsyncTaskExecutedListener<ResultadoAcao<Negocio>> {

    private GoogleMap googleMap;
    private LatLng latLng;
    private EditText txtNome;
    private EditText txtDescricao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_ponto_venda);

        txtNome = (EditText)findViewById(R.id.txtNome);
        txtDescricao = (EditText)findViewById(R.id.txtDescricao);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_cadastrar_negocio, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.salvar) {
            if (latLng != null) {
                salvar();
            }else {
                Toast toast = Toast.makeText(this, "Selecione uma localização", Toast.LENGTH_LONG);
                toast.show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        googleMap.clear();

        this.latLng = latLng;

        googleMap.addMarker(new MarkerOptions()
                .position(latLng));
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        map.setOnMapClickListener(this);
        map.setMyLocationEnabled(true);

        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (location == null) {
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }

        if (location != null){
            LatLng deviceLocation = new LatLng(location.getLatitude(), location.getLongitude());
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(deviceLocation, 18));
        }
    }

    @Override
    public void onAsyncTaskExecuted(ResultadoAcao<Negocio> resultadoAcao) {
        if(resultadoAcao.estaValido()){
            Intent intent = new Intent();
            intent.setClass(this, GerenciarNegocioActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);
            finish();
        } else {
            App.exibirMensagensDeErro(this, resultadoAcao.getMensagens());
        }
    }

    private void salvar() {
        try {
            Negocio negocio = new Negocio();
            negocio.setDescricao(txtDescricao.getText().toString());
            negocio.setNome(txtNome.getText().toString());
            negocio.setIdDono(App.obterUsuarioLogado().getId());
            negocio.setLatitude(latLng.latitude);
            negocio.setLongitude(latLng.longitude);

            CadastrarNegocioAsyncService service = new CadastrarNegocioAsyncService();
            service.setOnExecutedListener(this);

            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Salvando informações ...");

            service.setProgressDialog(progressDialog);

            service.execute(negocio);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
