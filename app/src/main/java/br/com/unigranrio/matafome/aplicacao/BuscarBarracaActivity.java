package br.com.unigranrio.matafome.aplicacao;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import java.util.List;

import br.com.unigranrio.matafome.R;
import br.com.unigranrio.matafome.aplicacao.webservices.ObterBarracasDentroDoRaioAsyncTask;
import br.com.unigranrio.matafome.aplicacao.webservices.OnAsyncTaskExecutedListener;
import br.com.unigranrio.matafome.dominio.modelo.Barraca;

public class BuscarBarracaActivity extends AppCompatActivity
        implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMyLocationChangeListener, GoogleMap.OnMapClickListener,
        OnAsyncTaskExecutedListener<List<Barraca>> {
    private GoogleMap googleMap;
    private Location lastLocation;

    private Marker markerSelecao;
    private LinearLayout acoesContainer;

    private TextView lblRaioBusca;
    private SeekBar raioBuscaSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_barraca);

        lblRaioBusca = (TextView) findViewById(R.id.lblRaioBusca);
        raioBuscaSeekBar = (SeekBar) findViewById(R.id.raioBuscaSeekBar);
        acoesContainer = (LinearLayout)findViewById(R.id.acoesContainer);

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

                if(raio < 50){
                    seekBar.setProgress(50);
                }

                desenharLocalizador();
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_buscar_barraca, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.listar_barracas) {
            return true;
        } else if (id == R.id.sair) {
            SharedPreferences prefs = getSharedPreferences("mata_fome", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();

            editor.putBoolean(LoginActivity.IS_USUARIO_LOGADO, false);
            editor.remove(LoginActivity.EMAIL_USUARIO_LOGADO);

            editor.commit();

            Intent intent = new Intent();
            intent.setClass(this, InicioActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
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

        if(locationGPS != null){
            lastLocation = locationGPS;
        } else {
            lastLocation = locationNet;
        }

        desenharLocalizador();
    }

    private void desenharLocalizador() {
        acoesContainer.setVisibility(View.INVISIBLE);

        final double radius = raioBuscaSeekBar.getProgress();

        googleMap.clear();
    if(lastLocation != null) {
        LatLng deviceLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(deviceLocation, calcularZoom()));

        CircleOptions circleOptions = new CircleOptions()
                .center(deviceLocation)
                .radius(radius)
                .fillColor(R.color.wallet_holo_blue_light)
                .strokeWidth(0f);

        googleMap.addCircle(circleOptions);

        try {
            ObterBarracasDentroDoRaioAsyncTask task = new ObterBarracasDentroDoRaioAsyncTask();

            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Um minuto..");
            progressDialog.setMessage("Carregando lista de barracas próximas de você..");

            task.setProgressDialog(progressDialog);
            task.setOnExecutedListener(this);

            task.execute(radius, deviceLocation.latitude, deviceLocation.longitude);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        this.markerSelecao = marker;

        acoesContainer.setVisibility(View.VISIBLE);

        return false;
    }

    @Override
    public void onMyLocationChange(Location location) {
        double distance = 0;

        if(lastLocation != null) {
          distance = location.distanceTo(lastLocation);
        }

        if(distance >= 15){
            lastLocation = location;

            desenharLocalizador();
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        acoesContainer.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onAsyncTaskExecuted(List<Barraca> barracas) {
        for(Barraca barraca : barracas){
            double distancia = distancia(lastLocation.getLatitude(), lastLocation.getLongitude(), barraca.getLatitude(), barraca.getLongitude());

            googleMap.addMarker(new MarkerOptions()
                    .title(barraca.getNome())
                    .snippet(String.format("%.2f metros", distancia))
                    .position(new LatLng(barraca.getLatitude(), barraca.getLongitude())));
        }
    }

    private float calcularZoom(){
        return 18.00f - ((float)raioBuscaSeekBar.getProgress() / 175.00f);
    }

    private double distancia(double latA, double lngA, double latB, double lngB)
    {
        double distanciaKM = (Math.asin(
                    Math.sqrt(
                            Math.pow(Math.sin(Math.toRadians(latB - latA)/2), 2) +
                            Math.pow(Math.sin(Math.toRadians(lngB - lngA)/2), 2) *
                            Math.cos(Math.toRadians(latA)) *
                            Math.cos(Math.toRadians(latB))
                    )
        ) * 6371) * 2;

        // Converte para metros
        return distanciaKM * 1000;
    }
}
