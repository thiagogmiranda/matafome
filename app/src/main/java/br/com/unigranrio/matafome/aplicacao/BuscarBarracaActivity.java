package br.com.unigranrio.matafome.aplicacao;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
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
import br.com.unigranrio.matafome.aplicacao.client.ObterBarracasDentroDoRaioAsyncTask;
import br.com.unigranrio.matafome.dominio.modelo.Negocio;

public class BuscarBarracaActivity extends AppCompatActivity
        implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMyLocationChangeListener, GoogleMap.OnMapClickListener {
    private GoogleMap googleMap;
    private Location lastLocation;

    private Marker markerSelecao;

    private LinearLayout dadosBarracaSelecionada;
    private TextView nomeBarracaSelecionada;
    private TextView descBarracaSelecionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_barraca);

        dadosBarracaSelecionada = (LinearLayout)findViewById(R.id.dadosBarraca);
        nomeBarracaSelecionada = (TextView)findViewById(R.id.nome);
        descBarracaSelecionada = (TextView)findViewById(R.id.descricao);

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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
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

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);

        // Get Current Location
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        lastLocation = locationManager.getLastKnownLocation(provider);

        desenharLocalizador(lastLocation);
    }

    private void desenharLocalizador(Location location) {
        final double radius = 150.00;

        LatLng deviceLocation = new LatLng(location.getLatitude(), location.getLongitude());

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(deviceLocation, 17.1f));

        CircleOptions circleOptions = new CircleOptions()
                .center(deviceLocation)
                .radius(radius)
                .fillColor(R.color.wallet_holo_blue_light)
                .strokeWidth(0f);

        googleMap.addCircle(circleOptions);

        try {
            List<Negocio> negocios = new ObterBarracasDentroDoRaioAsyncTask().execute(radius, deviceLocation.latitude, deviceLocation.longitude).get();

            for(Negocio n : negocios){
                googleMap.addMarker(new MarkerOptions()
                        .title(n.getNome())
                        .snippet(n.getDescricao())
                        .position(new LatLng(n.getLatitude(), n.getLongitude())));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        this.markerSelecao = marker;

        nomeBarracaSelecionada.setText(marker.getTitle());
        descBarracaSelecionada.setText(marker.getSnippet());

        dadosBarracaSelecionada.setVisibility(View.VISIBLE);

        return true;
    }

    @Override
    public void onMyLocationChange(Location location) {
        double distance = location.distanceTo(lastLocation);

        if(distance >= 10){
            googleMap.clear();

            desenharLocalizador(location);
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        dadosBarracaSelecionada.setVisibility(View.INVISIBLE);
    }
}
