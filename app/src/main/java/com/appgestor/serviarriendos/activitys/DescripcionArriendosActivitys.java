package com.appgestor.serviarriendos.activitys;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.appgestor.serviarriendos.R;
import com.appgestor.serviarriendos.fragments.FragmentContenidoDescripcion;
import com.appgestor.serviarriendos.models.GalleryImagenes;
import com.appgestor.serviarriendos.models.ModeloListaDestacados;

import java.util.ArrayList;

/**
 * Created by Poo_Code on 04/12/2014.
 */
public class DescripcionArriendosActivitys extends Activity {

    Bundle arguments;
    private ArrayList<GalleryImagenes> publicAvaiable = new ArrayList<GalleryImagenes>();
    protected GalleryImagenes mGallery;
    private ImageView drawer_indicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.descripcion_arriendos_activity);

        drawer_indicator = (ImageView) findViewById(R.id.drawer_indicator);
        drawer_indicator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (savedInstanceState == null) {
            arguments = getIntent().getExtras();
            arguments.putInt("id_arriendo", arguments.getInt("id_arriendo"));
            arguments.putString("ciudad", arguments.getString("ciudad"));
            arguments.putString("barrio", arguments.getString("barrio"));
            arguments.putString("tipologia", arguments.getString("tipologia"));
            arguments.putString("propiedad", arguments.getString("propiedad"));
            arguments.putDouble("precio", arguments.getDouble("precio"));
            arguments.putInt("habitaciones", arguments.getInt("habitaciones"));
            arguments.putInt("banos", arguments.getInt("banos"));
            arguments.putString("area", arguments.getString("area"));
            arguments.putInt("estrato", arguments.getInt("estrato"));
            arguments.putDouble("admin", arguments.getDouble("admin"));
            arguments.putString("contacto", arguments.getString("contacto"));
            arguments.putString("email", arguments.getString("email"));
            arguments.putString("imonbiliaria", arguments.getString("imonbiliaria"));
            arguments.putString("telefono", arguments.getString("telefono"));
            arguments.putDouble("latitud", arguments.getDouble("latitud"));
            arguments.putDouble("longitud", arguments.getDouble("longitud"));
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.containerdes, FragmentContenidoDescripcion.newInstance(arguments))
                    .commit();
        }
    }

}
