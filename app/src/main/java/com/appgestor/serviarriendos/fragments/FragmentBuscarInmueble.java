package com.appgestor.serviarriendos.fragments;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.appgestor.serviarriendos.R;
import com.appgestor.serviarriendos.activitys.ActividadBuscarArriendo;
import com.appgestor.serviarriendos.activitys.ActivityServicioDescripcion;
import com.appgestor.serviarriendos.activitys.ServiArriendos;
import com.appgestor.serviarriendos.activitys.ShowMessage;
import com.appgestor.serviarriendos.models.ModeloListaBarrio;
import com.appgestor.serviarriendos.models.ModeloListaCiudades;
import com.appgestor.serviarriendos.models.ModeloListaDestacados;
import com.appgestor.serviarriendos.models.ModeloListaMunicipios;
import com.appgestor.serviarriendos.models.ModeloListaTipoNegocio;
import com.appgestor.serviarriendos.models.ModeloListaTipoPropiedad;
import com.appgestor.serviarriendos.views.ButtonRectangle;
import com.appgestor.serviarriendos.views.ProgressBarCircularIndetermininate;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Poo_Code on 23/11/2014.
 */
public class FragmentBuscarInmueble extends BaseVolleyFragment implements View.OnClickListener {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private ArrayList<ModeloListaCiudades> ciudadesModelo;
    private ArrayList<ModeloListaMunicipios> municipiosModelo;
    private ArrayList<ModeloListaBarrio> barrriosModelo;
    private ArrayList<ModeloListaTipoNegocio> tipoNegocioModelo;
    private ArrayList<ModeloListaTipoPropiedad> tipoPropiedadModelo;
    private String[] ciudad_busquedad, municipios_busquedad, barrio_busquedad, tipoNegocio_busquedad, tipoPropiedad_busquedad;
    private Spinner mSpinnerCiudades, spinnerMunicipio, spinnerBarrio, spinnerTipoNegocio, spinnerTipoPropiedad, spinnerEstrato, spinnerRangoPrecio;
    private ButtonRectangle btnBuscar;
    private EditText editTextPrecioDesde, editTextPrecioHasta;
    private ModeloListaCiudades iNdicatorM;
    private ModeloListaMunicipios mIndicadorMun;
    private ModeloListaBarrio mIndicadorBarrio;
    private ModeloListaTipoNegocio mIndicadorNegocio;
    private ModeloListaTipoPropiedad mIndicadorPropiedad;
    protected ShowMessage ms;

    public static FragmentBuscarInmueble newInstance(int sectionNumber) {
        FragmentBuscarInmueble fragment = new FragmentBuscarInmueble();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentBuscarInmueble() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_buscar_inmueble, container, false);
        editTextPrecioDesde = (EditText) rootView.findViewById(R.id.editTextPrecioDesde);
        editTextPrecioHasta = (EditText) rootView.findViewById(R.id.editTextPrecioHasta);
        btnBuscar = (ButtonRectangle) rootView.findViewById(R.id.btnBuscar);
        btnBuscar.setOnClickListener(this);
        ms = new ShowMessage();

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        getVistasCiudades();
        getVistasTipoNegocio();
        getVistasTipoPropiedad();
        getVistasEstrato();
        getVistasRangoPrecios();

        super.onActivityCreated(savedInstanceState);

    }

    // Obtiene e inicializa las vistas.
    public void getVistasCiudades() {
        mSpinnerCiudades = (Spinner) getView().findViewById(R.id.spinnerCiudad);
        String url = "http://www.appgestor.com/ServicioWebArriendos/service_database.php";
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        // response
                        parseJSON(response);
                        //onConnectionFinished();
                        ArrayAdapter<String> spiCiudades = new ArrayAdapter<String>(
                                getActivity(),
                                R.layout.textview_spinner,
                                ciudad_busquedad);
                        mSpinnerCiudades.setAdapter(spiCiudades);
                        mSpinnerCiudades
                                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    public void onItemSelected(
                                            AdapterView<?> parent,
                                            View view, final int pos,
                                            long id) {
                                        iNdicatorM = ciudadesModelo.get(pos);

                                        getVistasMunicipio(iNdicatorM);


                                    }public void onNothingSelected(
                                            AdapterView<?> parent) {
                                    }

                                });// Fin del evento del spin
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        onConnectionFailed(error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String>  params = new HashMap<String, String>();
                params.put("operador", "ciudades");
                return params;
            }
        };
        addToQueue(jsonRequest);

    }

    private void parseJSON(String json) {
        try {

            JSONObject jsonObject = new JSONObject(json);
            JSONArray mCiudades = jsonObject.getJSONArray("ciudades");

            ciudadesModelo = new ArrayList<ModeloListaCiudades>();
            ciudad_busquedad = new String[mCiudades.length()];

            for (int i = 0; i < mCiudades.length(); i++) {
                JSONObject ciu = mCiudades.getJSONObject(i);
                ciudad_busquedad[i] = mCiudades.getJSONObject(i).getString("nombre");
                ModeloListaCiudades mModeloCiudades = new ModeloListaCiudades(ciu.getInt("id"), ciu.getString("nombre"));
                ciudadesModelo.add(mModeloCiudades);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getVistasMunicipio(final ModeloListaCiudades indicadorMunicipio){

        spinnerMunicipio = (Spinner) getView().findViewById(R.id.spinnerMunicipio);

        String url = "http://www.appgestor.com/ServicioWebArriendos/service_database.php";

        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        // response
                        parseJSON2(response);
                        //onConnectionFinished();
                        ArrayAdapter<String> spiMunicipio = new ArrayAdapter<String>(
                                getActivity(),
                                R.layout.textview_spinner,
                                municipios_busquedad);
                        spinnerMunicipio.setAdapter(spiMunicipio);
                        spinnerMunicipio
                                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    public void onItemSelected(AdapterView<?> parent, View view, final int pos, long id) {

                                        mIndicadorMun = municipiosModelo.get(pos);

                                        getVistasBarrio(mIndicadorMun);

                                    }
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }

                                });// Fin del evento del spin
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        onConnectionFailed(error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String>  params = new HashMap<String, String>();
                params.put("operador", "municipio");
                params.put("ciudad", indicadorMunicipio.getIdCiuda()+"");
                return params;
            }
        };

        addToQueue(jsonRequest);

    }

    private void parseJSON2(String json) {
        try {

            JSONObject jsonObject = new JSONObject(json);
            JSONArray mMunicipio = jsonObject.getJSONArray("municipio");

            municipiosModelo = new ArrayList<ModeloListaMunicipios>();
            municipios_busquedad = new String[mMunicipio.length()];

            for (int i = 0; i < mMunicipio.length(); i++) {
                JSONObject ciu = mMunicipio.getJSONObject(i);
                municipios_busquedad[i] = mMunicipio.getJSONObject(i).getString("nombre");
                ModeloListaMunicipios mModeloMunicipio = new ModeloListaMunicipios(ciu.getInt("id"), ciu.getString("nombre"));
                municipiosModelo.add(mModeloMunicipio);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getVistasBarrio(final ModeloListaMunicipios rModeloMunicipio){

        spinnerBarrio = (Spinner) getView().findViewById(R.id.spinnerBarrio);

        String url = "http://www.appgestor.com/ServicioWebArriendos/service_database.php";

        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        // response
                        parseJSON3(response);
                        //onConnectionFinished();
                        ArrayAdapter<String> spiBarrio145 = new ArrayAdapter<String>(
                                getActivity(),
                                R.layout.textview_spinner,
                                barrio_busquedad);
                        spinnerBarrio.setAdapter(spiBarrio145);
                        spinnerBarrio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            public void onItemSelected(AdapterView<?> parent, View view, final int pos, long id) {
                                mIndicadorBarrio = barrriosModelo.get(pos);
                             }
                            public void onNothingSelected(AdapterView<?> parent) {

                            }

                        });// Fin del evento del spin
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        onConnectionFailed(error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String>  params = new HashMap<String, String>();
                params.put("operador", "barrio");
                params.put("municipio", rModeloMunicipio.getIdMunicipio()+"");
                return params;
            }
        };
        addToQueue(jsonRequest);
    }

    private void parseJSON3(String json) {
        try {

            JSONObject jsonObject = new JSONObject(json);
            JSONArray mBarrio = jsonObject.getJSONArray("barrio");

            barrriosModelo = new ArrayList<ModeloListaBarrio>();
            barrio_busquedad = new String[mBarrio.length()];

            for (int i = 0; i < mBarrio.length(); i++) {
                JSONObject ciu = mBarrio.getJSONObject(i);
                barrio_busquedad[i] = mBarrio.getJSONObject(i).getString("nombre");
                ModeloListaBarrio mModeloBarrio = new ModeloListaBarrio(ciu.getInt("id"), ciu.getString("nombre"));
                barrriosModelo.add(mModeloBarrio);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getVistasTipoNegocio(){
        spinnerTipoNegocio = (Spinner) getView().findViewById(R.id.spinnerTipoNegocio);
        String url = "http://www.appgestor.com/ServicioWebArriendos/service_database.php";
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        // response
                        parseJSON4(response);
                        //onConnectionFinished();
                        ArrayAdapter<String> spiTipoNegocio12 = new ArrayAdapter<String>(
                                getActivity(),
                                R.layout.textview_spinner,
                                tipoNegocio_busquedad);
                        spinnerTipoNegocio.setAdapter(spiTipoNegocio12);
                        spinnerTipoNegocio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            public void onItemSelected(AdapterView<?> parent, View view, final int pos, long id) {
                                mIndicadorNegocio = tipoNegocioModelo.get(pos);
                            }
                            public void onNothingSelected(AdapterView<?> parent) {}
                        });// Fin del evento del spin
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        onConnectionFailed(error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String>  params = new HashMap<String, String>();
                params.put("operador", "tipoNegocio");
                return params;
            }
        };
        addToQueue(jsonRequest);

    }

    public void parseJSON4(String json){

        try {

            JSONObject jsonObject = new JSONObject(json);
            JSONArray mTipoNego = jsonObject.getJSONArray("tipoNegocio");

            tipoNegocioModelo = new ArrayList<ModeloListaTipoNegocio>();
            tipoNegocio_busquedad = new String[mTipoNego.length()];

            for (int i = 0; i < mTipoNego.length(); i++) {
                JSONObject ciu = mTipoNego.getJSONObject(i);
                tipoNegocio_busquedad[i] = mTipoNego.getJSONObject(i).getString("nombre");
                ModeloListaTipoNegocio mModeloTipoNegocio = new ModeloListaTipoNegocio(ciu.getInt("id"), ciu.getString("nombre"));
                tipoNegocioModelo.add(mModeloTipoNegocio);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getVistasTipoPropiedad(){
        spinnerTipoPropiedad = (Spinner) getView().findViewById(R.id.spinnerTipoPropiedad);
        String url = "http://www.appgestor.com/ServicioWebArriendos/service_database.php";

        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        // response
                        parseJSON5(response);
                        //onConnectionFinished();
                        ArrayAdapter<String> spiTipoPropieda2223 = new ArrayAdapter<String>(
                                getActivity(),
                                R.layout.textview_spinner,
                                tipoPropiedad_busquedad);
                        spinnerTipoPropiedad.setAdapter(spiTipoPropieda2223);
                        spinnerTipoPropiedad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            public void onItemSelected(AdapterView<?> parent, View view, final int pos, long id) {
                                mIndicadorPropiedad = tipoPropiedadModelo.get(pos);
                            }
                            public void onNothingSelected(AdapterView<?> parent) {}
                        });// Fin del evento del spin
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        onConnectionFailed(error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String>  params = new HashMap<String, String>();
                params.put("operador", "tipoPropiedad");
                return params;
            }
        };
        addToQueue(jsonRequest);
    }


    public void parseJSON5(String json){

        try {

            JSONObject jsonObject = new JSONObject(json);
            JSONArray mTipoPropiedad = jsonObject.getJSONArray("tipoPropiedad");

            tipoPropiedadModelo = new ArrayList<ModeloListaTipoPropiedad>();
            tipoPropiedad_busquedad = new String[mTipoPropiedad.length()];

            for (int i = 0; i < mTipoPropiedad.length(); i++) {
                JSONObject ciu = mTipoPropiedad.getJSONObject(i);
                tipoPropiedad_busquedad[i] = mTipoPropiedad.getJSONObject(i).getString("nombre");
                ModeloListaTipoPropiedad mModeloTipoPropiedad = new ModeloListaTipoPropiedad(ciu.getInt("id"), ciu.getString("nombre"));
                tipoPropiedadModelo.add(mModeloTipoPropiedad);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getVistasEstrato(){

        new Thread(new Runnable() {
            String[] precio_rango = { "1 ", "2", "3", "4", "5", "6", "7" };
            public void run() {
                spinnerEstrato = (Spinner) getView().findViewById(R.id.spinnerEstrato);
                ArrayAdapter<String> prec = new ArrayAdapter<String>(getActivity(), R.layout.textview_spinner, precio_rango);
                spinnerEstrato.setAdapter(prec);

            }
        }).start();
    }

    public void getVistasRangoPrecios(){

        new Thread(new Runnable() {
            public void run() {

            }
        }).start();

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            ((ServiArriendos) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
        }catch (ClassCastException e){
            // La actividad no implementa la interfaz necesaria.
            throw new ClassCastException(activity.toString()
                    + " debe implementar OnObraSeleccionadoListener");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnBuscar:
                Intent intent = new Intent(getActivity(), ActividadBuscarArriendo.class);
                intent.putExtra("operador", "list_busqueda");
                intent.putExtra("ciudad", String.valueOf(iNdicatorM.getIdCiuda()));
                intent.putExtra("municipio", String.valueOf(mIndicadorMun.getIdMunicipio()));
                intent.putExtra("barrio", String.valueOf(mIndicadorBarrio.getIdBarrio()));
                intent.putExtra("tipo_negocio", String.valueOf(mIndicadorNegocio.getIdTipoNegocio()));
                intent.putExtra("tipo_propiedad", String.valueOf(mIndicadorPropiedad.getIdTipoPropiedad()));
                intent.putExtra("estrato", spinnerEstrato.getSelectedItem().toString());
                intent.putExtra("precio_ini", editTextPrecioDesde.getText().toString());
                intent.putExtra("precio_fin", editTextPrecioHasta.getText().toString());
                startActivity(intent);
                break;
        }
    }


}
