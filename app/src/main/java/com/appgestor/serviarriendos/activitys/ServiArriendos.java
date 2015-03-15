package com.appgestor.serviarriendos.activitys;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import static android.view.Gravity.START;
import com.appgestor.serviarriendos.R;
import com.appgestor.serviarriendos.adapters.DrawerArrowDrawable;
import com.appgestor.serviarriendos.adapters.NavDrawerListAdapter;
import com.appgestor.serviarriendos.fragments.FragmentBuscarInmueble;
import com.appgestor.serviarriendos.fragments.FragmentDestacados;
import com.appgestor.serviarriendos.fragments.FragmentListaDeServicios;
import com.appgestor.serviarriendos.fragments.FragmentSinConexionDatos;
import com.appgestor.serviarriendos.models.NavDrawerItem;

import java.util.ArrayList;


public class ServiArriendos extends ActionBarActivity {

    private DrawerArrowDrawable drawerArrowDrawable;
    private float offset;
    private boolean flipped;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private ListView mDrawerList;
    private DrawerLayout drawer;
    private NavDrawerListAdapter adapter;
    private LocationManager locationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servi_arriendos);

        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        // nav drawer icons from resources
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ImageView imageView = (ImageView) findViewById(R.id.drawer_indicator);
        final Resources resources = getResources();

        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
        navDrawerItems = new ArrayList<NavDrawerItem>();
        // agregar un nuevo item al menu deslizante
        // Favoritos
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(3, -1)));
        // Pedidos
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        // Catologo
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(4, -1)));
        // Catologo
        //navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1), true, "Muy Pronto"));
        // Contacto
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(2, -1)));
        // Contacto
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(6, -1)));
        // Contacto
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(0, -1)));
        // Contacto
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons.getResourceId(5, -1)));

        // Recycle the typed array
        navMenuIcons.recycle();

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);

        drawerArrowDrawable = new DrawerArrowDrawable(resources);
        drawerArrowDrawable.setStrokeColor(resources.getColor(R.color.light_gray));
        imageView.setImageDrawable(drawerArrowDrawable);

        drawer.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override public void onDrawerSlide(View drawerView, float slideOffset) {
                offset = slideOffset;

                // Sometimes slideOffset ends up so close to but not quite 1 or 0.
                if (slideOffset >= .995) {
                    flipped = true;
                    drawerArrowDrawable.setFlip(flipped);
                } else if (slideOffset <= .005) {
                    flipped = false;
                    drawerArrowDrawable.setFlip(flipped);
                }

                drawerArrowDrawable.setParameter(offset);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (drawer.isDrawerVisible(START)) {
                    drawer.closeDrawer(START);
                } else {
                    drawer.openDrawer(START);
                }
            }
        });

        final TextView styleButton = (TextView) findViewById(R.id.indicator_style);
        styleButton.setOnClickListener(new View.OnClickListener() {
            boolean rounded = false;

            @Override public void onClick(View v) {
                styleButton.setText(rounded //
                        ? resources.getString(R.string.rounded) //
                        : resources.getString(R.string.squared));

                rounded = !rounded;

                drawerArrowDrawable = new DrawerArrowDrawable(resources, rounded);
                drawerArrowDrawable.setParameter(offset);
                drawerArrowDrawable.setFlip(flipped);
                drawerArrowDrawable.setStrokeColor(resources.getColor(R.color.light_gray));

                imageView.setImageDrawable(drawerArrowDrawable);
            }
        });

        displayView(0);

    }

    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            displayView(position);
        }
    }

    private void displayView(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        switch (position) {
            //Inmuebles destacados
            case 0:
                if(isNetworkAvailable()){
                    Bundle arguments = new Bundle();
                    arguments.putString("operador", "list_completa");
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, FragmentDestacados.newInstance(arguments))
                            .commit();
                    drawer.closeDrawer(START);
                }else {
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, FragmentSinConexionDatos.newInstance(position + 1))
                            .commit();
                    drawer.closeDrawer(START);
                }

                break;

            //Lista de Servicios
            case 1:
                if(isNetworkAvailable()){
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, FragmentListaDeServicios.newInstance(position + 1))
                            .commit();
                    drawer.closeDrawer(START);
                }else {
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, FragmentSinConexionDatos.newInstance(position + 1))
                            .commit();
                    drawer.closeDrawer(START);
                }

                break;

            //Buscar Inmuebles
            case 2:
                if(isNetworkAvailable()){
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, FragmentBuscarInmueble.newInstance(position + 1))
                            .commit();
                    drawer.closeDrawer(START);
                }else {
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, FragmentSinConexionDatos.newInstance(position + 1))
                            .commit();
                    drawer.closeDrawer(START);
                }

                break;

            //Buscar Inmuebles en los mapas
            case 3:
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(callGPSSettingIntent);
                }else{
                    if(isNetworkAvailable()){
                        Intent intent = new Intent(this, ActivitysMapas.class);
                        startActivity(intent);
                    }else {
                        fragmentManager.beginTransaction()
                                .replace(R.id.container, FragmentSinConexionDatos.newInstance(position + 1))
                                .commit();
                        drawer.closeDrawer(START);
                    }

                }

                drawer.closeDrawer(START);
                break;
            //Inmueble en ofertas de ventas
            case 4:

                break;

            //Quiere saber cuanto vale su casa
            case 5:
                break;

            //Salir
            case 6:
                finish();
                break;

            case 7:

                break;

            default:
                break;
        }

    }

    public void onSectionAttached(int anInt) {}

    // Helper method to determine if Internet connection is available.
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
