package com.appgestor.serviarriendos.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.appgestor.serviarriendos.R;
import com.appgestor.serviarriendos.activitys.DescripcionArriendosActivitys;
import com.appgestor.serviarriendos.activitys.ServiArriendos;
import com.appgestor.serviarriendos.adapters.BucketListAdapterDestacados;
import com.appgestor.serviarriendos.models.ModeloListaDestacados;
import com.appgestor.serviarriendos.views.ProgressBarCircularIndetermininate;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;


import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * Created by Poo_Code on 23/11/2014.
 */
public class FragmentDestacados extends BaseVolleyFragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private ProgressBarCircularIndetermininate progressWheel;
    private View headerView;
    private View footerView;
    private ListView multiColumnList;
    private ArrayList<ModeloListaDestacados> mModeloDestacado;
    private static CustomAdapter adapter;
    private ImageView mDatosConexion;
    Activity problemaActivity;

    public static FragmentDestacados newInstance(Bundle arguments) {
        FragmentDestacados fragment = new FragmentDestacados();
        if(arguments != null){
            fragment.setArguments(arguments);
        }
        return fragment;
    }

    public FragmentDestacados() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lista_de_servicios, container, false);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        problemaActivity = getActivity();
        multiColumnList = (ListView) getActivity().findViewById(R.id.card_list);
        progressWheel = (ProgressBarCircularIndetermininate) getActivity().findViewById(R.id.progressBarCircularIndetermininate);
        headerView = LayoutInflater.from(getActivity()).inflate(R.layout.view_header, null, false);
        footerView = LayoutInflater.from(getActivity()).inflate(R.layout.view_footer, null, false);
        mDatosConexion = (ImageView) getActivity().findViewById(R.id.imgDatos);

        setupGrid();

        super.onActivityCreated(savedInstanceState);



    }


    private void setupGrid() {
        String url = "http://www.appgestor.com/ServicioWebArriendos/service_database.php";
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(final String response) {
                        // response
                        new AsyncTask<String[], Long, Long>(){
                            @Override
                            protected Long doInBackground(String[]... params) {
                                parseJSON(response);
                                return null;
                            }
                            protected void onPreExecute() {
                                multiColumnList.setVisibility(View.GONE);
                            }
                            @Override
                            public void onProgressUpdate(Long... value) {

                            }
                            @Override
                            protected void onPostExecute(Long result){
                              //  Toast.makeText(getActivity(), mModeloDestacado.toString(), Toast.LENGTH_SHORT).show();
                                if(validarDatosServicio() == false){
                                    mDatosConexion.setVisibility(View.VISIBLE);
                                }else {
                                    multiColumnList.addHeaderView(headerView);
                                    multiColumnList.addFooterView(footerView);
                                    adapter = new CustomAdapter(problemaActivity, mModeloDestacado);
                                    adapter.enableAutoMeasure(150);
                                    multiColumnList.setAdapter(adapter);
                                    multiColumnList.setVisibility(View.VISIBLE);
                                    progressWheel.setVisibility(View.GONE);
                                }
                                //onConnectionFinished();
                            }
                        }.execute();
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        onConnectionFailed(error.toString());
                        progressWheel.setVisibility(View.GONE);
                        mDatosConexion.setVisibility(View.VISIBLE);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String>  params = new HashMap<String, String>();

                if(getArguments().getString("operador").toString() == "list_completa"){
                    params.put("operador", "ofertasList");
                }else if(getArguments().getString("operador").toString() == "list_busqueda"){
                    params.put("operador", "list_busqueda");
                    params.put("ciudad", getArguments().getString("ciudad"));
                    params.put("municipio", getArguments().getString("municipio"));
                    params.put("barrio", getArguments().getString("barrio"));
                    params.put("tipo_negocio", getArguments().getString("tipo_negocio"));
                    params.put("tipo_propiedad", getArguments().getString("tipo_propiedad"));
                    params.put("estrato", getArguments().getString("estrato"));
                    params.put("precio_ini", getArguments().getString("precio_ini"));
                    params.put("precio_fin", getArguments().getString("precio_fin"));
                }else{
                    params.put("operador", "list_busqueda_mapa");
                    params.put("id_publicacion", getArguments().getString("id_publicacion"));
                }
                return params;
            }
        };
        addToQueue(jsonRequest);
    }

    private void parseJSON(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray mDestacadp = jsonObject.getJSONArray("publication");

            mModeloDestacado = new ArrayList<ModeloListaDestacados>();
            for (int i = 0; i < mDestacadp.length(); i++) {
                JSONObject pue = mDestacadp.getJSONObject(i);
                // Creamos el objeto publication
                ModeloListaDestacados p = new ModeloListaDestacados(
                        pue.getInt("id"), pue.getDouble("admin"),
                        pue.getInt("estrato"), pue.getInt("alcobas"),
                        pue.getInt("banos"), pue.getString("tipologia"),
                        pue.getString("inmobiliaria"), pue.getString("ciudad"),
                        pue.getString("barrio"), pue.getString("area"),
                        pue.getDouble("precio"), pue.getString("usuario"),
                        pue.getString("email"), pue.getString("telefono"),
                        pue.getString("tiponegocio"), pue.getDouble("cordenada1"),
                        pue.getDouble("cordenada2"), pue.getString("url"));
                mModeloDestacado.add(p);
            }
            Toast.makeText(getActivity(), mModeloDestacado.toString(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class CustomAdapter extends BucketListAdapterDestacados<String> {

        private Activity mActivity;
        private List<ModeloListaDestacados> items;
        private ImageLoader imageLoader1;
        private DisplayImageOptions options1;
        public CustomAdapter(Activity ctx, List<ModeloListaDestacados> elements) {
            super(ctx, elements);
            this.mActivity=ctx;
            this.items=elements;

            //Setup the ImageLoader, we'll use this to display our images
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mActivity).build();
            imageLoader1 = ImageLoader.getInstance();
            imageLoader1.init(config);

            //Setup options for ImageLoader so it will handle caching for us.
            options1 = new DisplayImageOptions.Builder()
                    .cacheInMemory()
                    .cacheOnDisc()
                    .build();

        }

        @Override
        protected View getBucketElement(final int position, final ModeloListaDestacados currentElement) {
            // TODO Auto-generated method stub
            final ViewHolder2 holder ;
            View bucketElement=null;

            LayoutInflater inflater = mActivity.getLayoutInflater();
            bucketElement = inflater.inflate(R.layout.play_giditem, null);
            holder = new ViewHolder2(bucketElement);
            bucketElement.setTag(holder);

            holder.indicator1.setVisibility(View.VISIBLE);
            holder.image.setVisibility(View.INVISIBLE);
            ImageLoadingListener listener = new ImageLoadingListener(){
                @Override
                public void onLoadingStarted(String arg0, View arg1) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onLoadingCancelled(String arg0, View arg1) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
                    holder.indicator1.setVisibility(View.INVISIBLE);
                    holder.image.setVisibility(View.VISIBLE);
                }
                @Override
                public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
                    // TODO Auto-generated method stub
                }
            };

            imageLoader1.displayImage(currentElement.getImgUrl(),holder.image,options1, listener);

            holder.name.setText(currentElement.getCiudad());
            holder.info.setText(currentElement.getBarrio());
            //Formato del precio
            DecimalFormat format = new DecimalFormat("$#,###.##");
            holder.modified.setText(format.format(currentElement.getPrecio()));

            bucketElement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //showMessage("position :" + position);
                    Intent intent = new Intent(getActivity(), DescripcionArriendosActivitys.class);
                    intent.putExtra("id_arriendo", currentElement.getId());
                    intent.putExtra("ciudad", currentElement.getCiudad());
                    intent.putExtra("barrio", currentElement.getBarrio());
                    intent.putExtra("tipologia", currentElement.getTipoNegocio());
                    intent.putExtra("propiedad", currentElement.getTipologia());
                    intent.putExtra("precio", currentElement.getPrecio());
                    intent.putExtra("habitaciones", currentElement.getAlcoba());
                    intent.putExtra("banos", currentElement.getBanos());
                    intent.putExtra("area", currentElement.getArea());
                    intent.putExtra("estrato", currentElement.getEstrato());
                    intent.putExtra("admin", currentElement.getAdmin());
                    intent.putExtra("contacto", currentElement.getUsuario());
                    intent.putExtra("email", currentElement.getEmail());
                    intent.putExtra("imonbiliaria", currentElement.getInmobiliaria());
                    intent.putExtra("telefono", currentElement.getTelefono());
                    intent.putExtra("latitud", currentElement.getLatitud());
                    intent.putExtra("longitud", currentElement.getLongitud());
                    startActivity(intent);
                }
            });

            return bucketElement;
        }

    }

    class ViewHolder2 {
        public TextView name = null;
        public TextView info = null;
        public TextView modified = null;
        //public CheckBox select = null;
        public ImageView image = null, app_icon = null;
        private ProgressBarCircularIndetermininate indicator1;
        //public LinearLayout rowlayout;

        ViewHolder2(View row) {
            name = (TextView) row.findViewById(R.id.projectName);
            info = (TextView) row.findViewById(R.id.companyName);
            modified = (TextView) row.findViewById(R.id.Name);
            image = (ImageView) row.findViewById(R.id.listicon);
            indicator1 = (ProgressBarCircularIndetermininate)row.findViewById(R.id.progressBarCircularImagen);
        }

        void populateFrom(String s) {
            name.setText(s);
        }
    }

    private boolean validarDatosServicio(){
        boolean resultadoDatos;

        if(mModeloDestacado.size() > 0){
            resultadoDatos = true;
        }else{
            resultadoDatos = false;
        }
        return resultadoDatos;
    }

}
