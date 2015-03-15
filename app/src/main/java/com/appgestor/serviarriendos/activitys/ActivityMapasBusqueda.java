package com.appgestor.serviarriendos.activitys;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.appgestor.serviarriendos.R;
import com.appgestor.serviarriendos.fragments.FragmentDestacados;

/**
 * Created by Poo_Code on 26/01/2015.
 */
public class ActivityMapasBusqueda extends Activity {


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
            arguments.putString("operador", arguments.getString("operador"));
            arguments.putString("id_publicacion", arguments.getString("id_publicacion"));
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.containerdesser, FragmentDestacados.newInstance(arguments))
                    .commit();
        }
    }
}
