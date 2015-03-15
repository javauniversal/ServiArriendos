package com.appgestor.serviarriendos.activitys;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.appgestor.serviarriendos.R;
import com.appgestor.serviarriendos.views.Button;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Poo_Code on 11/12/2014.
 */
public class ActivitysMapas extends FragmentActivity implements GooglePlayServicesClient.ConnectionCallbacks,
                                                                GoogleMap.OnMyLocationChangeListener, GooglePlayServicesClient.OnConnectionFailedListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private LocationClient mLocationClient;
    private LatLng variable;
    private int idPublicacion;
    private String tipilogia;
    private double precio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        setUpMapIfNeeded();
        mMap.setInfoWindowAdapter(new MyInfoWindowAdapter());
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String[] separated = marker.getTitle().split(":");
                separated[1] = separated[1].trim();
                showMessage(separated[1]);

                Intent intent = new Intent(ActivitysMapas.this, ActivityMapasBusqueda.class);
                intent.putExtra("operador", "list_mapa");
                intent.putExtra("id_publicacion", String.valueOf(separated[1]));
                startActivity(intent);
            }
        });

        setupGrid();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Connect the client.
        if (isGooglePlayServicesAvailable()) {
            mLocationClient.connect();
        }
    }

    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        mLocationClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                // Activo mi localizacion en el mapa
                mMap.setMyLocationEnabled(true);
                // Activamos la capa o layer MyLocation
                mMap.setOnMyLocationChangeListener(this);
                mLocationClient = new LocationClient(this, this, this);

            }
        }
    }

    private boolean isGooglePlayServicesAvailable() {

        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("Location Updates", "Google Play services is available.");
            return true;
        } else {
            return false;
        }

    }

    @Override
    public void onConnected(Bundle bundle) {
        // Display the connection status
        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
        Location locationt = mLocationClient.getLastLocation();
        mMap.setTrafficEnabled(true);
        if (locationt == null) {
            Toast.makeText(this, "Su ubicación actual no esta disponible",
                    Toast.LENGTH_SHORT).show();
        } else {
            LatLng latLngt = new LatLng(locationt.getLatitude(),
                    locationt.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                    latLngt, 7);
            mMap.animateCamera(cameraUpdate);
        }
    }

    @Override
    public void onDisconnected() {
        Toast.makeText(this, "Disconnected. Please re-connect.",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);

            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(),
                    "Sorry. Location services not available to you",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void setMarker(LatLng position, String titulo, Double info) {
        DecimalFormat format = new DecimalFormat("$#,###.##");
        mMap.addMarker(new MarkerOptions().position(position)
                .title(titulo)
                .snippet("Precio: " + format.format(info))
                .anchor(0.5f, 0.5f)
                .flat(true)
                .visible(true)
                .draggable(true)
                .flat(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bigcity)));
        // myMaker.showInfoWindow();
    }

    private void setupGrid() {

        RequestQueue queue = Volley.newRequestQueue(ActivitysMapas.this.getApplicationContext());
        String url = "http://www.appgestor.com/ServicioWebArriendos/positionMaps.php";
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(final String response) {
                        // response
                        parseJSON(response);
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        showMessage("Error Volly");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String>  params = new HashMap<String, String>();
                params.put("operador", "posicionesMapas");
                return params;
            }
        };
        queue.add(jsonRequest);
    }

    private void parseJSON(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray mServicio = jsonObject.getJSONArray("publication");
            for (int i = 0; i < mServicio.length(); i++) {
                JSONObject pue = mServicio.getJSONObject(i);
                variable = new LatLng(pue.getDouble("latitud"),pue.getDouble("longitud"));
                idPublicacion = pue.getInt("id");
                tipilogia = pue.getString("tipologia");
                precio = pue.getDouble("precio");
                setMarker(variable,"Código: "+idPublicacion,precio);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onMyLocationChange(Location location) {
        // TODO Auto-generated method stub
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Decide what to do based on the original request code
        switch (requestCode) {

            case CONNECTION_FAILURE_RESOLUTION_REQUEST:

                switch (resultCode) {
                    case Activity.RESULT_OK:
                        mLocationClient.connect();
                        break;
                }

        }
    }



    //Metodo dende implementamos la vista del marke
    public class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;
        private TextView mTitulo;
        private TextView mPrecio;
        private Button informacion;

        MyInfoWindowAdapter() {
            myContentsView = getLayoutInflater().inflate(
                    R.layout.custom_info_contents, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {

            mTitulo = ((TextView) myContentsView.findViewById(R.id.txtCiudad));
            mTitulo.setText(marker.getTitle());
            mPrecio = ((TextView) myContentsView.findViewById(R.id.txtPrecio));
            mPrecio.setText(marker.getSnippet());

            return myContentsView;
        }

    }

    private void showMessage(String message) {
        Toast.makeText(ActivitysMapas.this, message,
                Toast.LENGTH_SHORT).show();
    }

}
