package com.appgestor.serviarriendos.activitys;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.appgestor.serviarriendos.R;
import com.appgestor.serviarriendos.fragments.FragmentDestacados;

/**
 * Created by Poo_Code on 25/01/2015.
 */

public class ActividadBuscarArriendo extends Activity {

    Bundle arguments;
    private ImageView drawer_indicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.descripcion_servicios_activity);
        drawer_indicator = (ImageView) findViewById(R.id.drawer_indicator);
        drawer_indicator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (savedInstanceState == null) {
            arguments = getIntent().getExtras();
            arguments.putString("operador", "list_busqueda");
            arguments.putString("ciudad", arguments.getString("ciudad"));
            arguments.putString("municipio", arguments.getString("municipio"));
            arguments.putString("barrio", arguments.getString("barrio"));
            arguments.putString("tipo_negocio", arguments.getString("tipo_negocio"));
            arguments.putString("tipo_propiedad", arguments.getString("tipo_propiedad"));
            arguments.putString("estrato", arguments.getString("estrato"));
            arguments.putString("precio_ini", arguments.getString("precio_ini"));
            arguments.putString("precio_fin", arguments.getString("precio_fin"));

            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.containerdesser, FragmentDestacados.newInstance(arguments))
                    .commit();
        }
    }
}
