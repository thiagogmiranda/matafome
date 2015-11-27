package br.com.unigranrio.matafome.aplicacao;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import br.com.unigranrio.matafome.R;
import br.com.unigranrio.matafome.aplicacao.webservices.ObterLanchesDentroDoRaioAsyncService;
import br.com.unigranrio.matafome.aplicacao.webservices.OnAsyncTaskExecutedListener;
import br.com.unigranrio.matafome.dominio.modelo.Negocio;

public class PesquisaLanchesRapidosActivity extends AppCompatActivity
        implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMyLocationChangeListener, GoogleMap.OnMapClickListener,
        OnAsyncTaskExecutedListener<List<Negocio>> {
    private GoogleMap googleMap;
    private Location lastLocation;

    private Marker markerSelecao;
    private LinearLayout acoesContainer;
    private Button btnVerMaisSobre;

    private TextView lblRaioBusca;
    private SeekBar raioBuscaSeekBar;

    private Dictionary<String, Negocio> cache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_barraca);

        cache = new Hashtable<>();

        lblRaioBusca = (TextView) findViewById(R.id.lblRaioBusca);
        raioBuscaSeekBar = (SeekBar) findViewById(R.id.raioBuscaSeekBar);
        acoesContainer = (LinearLayout) findViewById(R.id.acoesContainer);
        btnVerMaisSobre = (Button) findViewById(R.id.btnDetalhesNegocio);

        acoesContainer.setVisibility(View.INVISIBLE);

        raioBuscaSeekBar.setMax(500);
        raioBuscaSeekBar.setProgress(100);
        raioBuscaSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                lblRaioBusca.setText(String.format("%d m", seekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int raio = seekBar.getProgress();

                if (raio < 50) {
                    seekBar.setProgress(50);
                }

                desenharLocalizador();
            }
        });

        btnVerMaisSobre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirTelaDetalhesNegocio();
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void abrirTelaDetalhesNegocio() {
        Intent intent = new Intent();
        intent.setClass(this, DetalhesNegocioActivity.class);

        String nome = markerSelecao.getTitle();

        Negocio negocio = cache.get(nome);

        intent.putExtra("idNegocio", negocio.getId());

        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_pesquisa_lanches, menu);
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

    private void fazerLogout(){
        App.deslogarUsuario();

        Intent intent = new Intent();
        intent.setClass(this, InicioActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
        finish();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnMyLocationChangeListener(this);
        googleMap.setOnMapClickListener(this);

        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location locationNet = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if (locationGPS != null) {
            lastLocation = locationGPS;
        } else {
            lastLocation = locationNet;
        }

        desenharLocalizador();
    }

    private void desenharLocalizador() {
        acoesContainer.setVisibility(View.INVISIBLE);

        final int radius = raioBuscaSeekBar.getProgress();

        googleMap.clear();
        if (lastLocation != null) {
            LatLng deviceLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(deviceLocation, calcularZoom()));

            CircleOptions circleOptions = new CircleOptions()
                    .center(deviceLocation)
                    .radius(radius)
                    .fillColor(R.color.wallet_holo_blue_light)
                    .strokeWidth(0f);

            googleMap.addCircle(circleOptions);

            try {
                ObterLanchesDentroDoRaioAsyncService task = new ObterLanchesDentroDoRaioAsyncService();

                String mensagem = getResources().getString(R.string.pesquisando_lanches);

                ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage(String.format(mensagem, radius));

                task.setProgressDialog(progressDialog);
                task.setOnExecutedListener(this);

                task.execute((double) radius, deviceLocation.latitude, deviceLocation.longitude);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        this.markerSelecao = marker;

        btnVerMaisSobre.setText("Ver mais sobre " + marker.getTitle());
        acoesContainer.setVisibility(View.VISIBLE);

        return false;
    }

    @Override
    public void onMyLocationChange(Location location) {
        double distance = 0;

        if (lastLocation != null) {
            distance = location.distanceTo(lastLocation);
        }

        if (distance >= (raioBuscaSeekBar.getProgress() / 2)) {
            lastLocation = location;

            desenharLocalizador();
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        acoesContainer.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onAsyncTaskExecuted(List<Negocio> negocios) {
        for (Negocio negocio : negocios) {
            double distancia = distancia(lastLocation.getLatitude(), lastLocation.getLongitude(), negocio.getLatitude(), negocio.getLongitude());

            googleMap.addMarker(new MarkerOptions()
                    .title(negocio.getNome())
                    .snippet(String.format("%.2f metros", distancia))
                    .position(new LatLng(negocio.getLatitude(), negocio.getLongitude())));

            cache.put(negocio.getNome(), negocio);
        }
    }

    private float calcularZoom() {
        return 18.00f - ((float) raioBuscaSeekBar.getProgress() / 175.00f);
    }

    private double distancia(double latA, double lngA, double latB, double lngB) {
        double distanciaKM = (Math.asin(
                Math.sqrt(
                        Math.pow(Math.sin(Math.toRadians(latB - latA) / 2), 2) +
                                Math.pow(Math.sin(Math.toRadians(lngB - lngA) / 2), 2) *
                                        Math.cos(Math.toRadians(latA)) *
                                        Math.cos(Math.toRadians(latB))
                )
        ) * 6371) * 2;

        // Converte para metros
        return distanciaKM * 1000;
    }
}
