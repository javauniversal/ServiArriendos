package com.appgestor.serviarriendos.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.appgestor.serviarriendos.R;
import com.appgestor.serviarriendos.activitys.ActivityServicioDescripcion;
import com.appgestor.serviarriendos.activitys.DescripcionArriendosActivitys;
import com.appgestor.serviarriendos.activitys.ServiArriendos;
import com.appgestor.serviarriendos.adapters.BucketListAdapter;
import com.appgestor.serviarriendos.models.ModeloListaServios;
import com.appgestor.serviarriendos.views.ProgressBarCircularIndetermininate;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




/**
 * Created by Poo_Code on 22/11/2014.
 */
public class FragmentListaDeServicios extends BaseVolleyFragment {


    private static final String ARG_SECTION_NUMBER = "section_number";
    private ListView multiColumnList;
    private ArrayList<ModeloListaServios> mModeloServicios;
    private static CustomAdapter adapter;
    private ProgressBarCircularIndetermininate progressWheel;
    private View headerView;
    private View footerView;
    private ImageView mDatosConexion;
    Activity problemaActivity;


    public static FragmentListaDeServicios newInstance(int sectionNumber) {
        FragmentListaDeServicios fragment = new FragmentListaDeServicios();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentListaDeServicios() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lista_de_servicios, container, false);
        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        problemaActivity = getActivity();
        setupGrid();
        multiColumnList = (ListView) getActivity().findViewById(R.id.card_list);
        progressWheel = (ProgressBarCircularIndetermininate) getActivity().findViewById(R.id.progressBarCircularIndetermininate);
        headerView = LayoutInflater.from(getActivity()).inflate(R.layout.view_header, null, false);
        footerView = LayoutInflater.from(getActivity()).inflate(R.layout.view_footer, null, false);
        //multiColumnList.setOnItemClickListener(this);
        mDatosConexion = (ImageView) getActivity().findViewById(R.id.imgDatos);
    }

    private void setupGrid() {

        String url = "http://www.appgestor.com/ServicioWebArriendos/service_database.php";
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(final String response) {
                        // response
                        //showMessage("" + response);
                        parseJSON(response);

                        if(validarDatosServicio() == false) {
                            mDatosConexion.setVisibility(View.VISIBLE);
                        }else{
                            multiColumnList.addHeaderView(headerView);
                            multiColumnList.addFooterView(footerView);
                            adapter = new CustomAdapter(problemaActivity, mModeloServicios);
                            adapter.enableAutoMeasure(230);
                            multiColumnList.setAdapter(adapter);
                            multiColumnList.setVisibility(View.VISIBLE);
                            progressWheel.setVisibility(View.GONE);
                        }
                        //onConnectionFinished();
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
                params.put("operador", "servicios");
                return params;
            }
        };
        addToQueue(jsonRequest);
    }

    private void parseJSON(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray mServicio = jsonObject.getJSONArray("servicios");
            mModeloServicios = new ArrayList<ModeloListaServios>();
            for (int i = 0; i < mServicio.length(); i++) {
                JSONObject convServico = mServicio.getJSONObject(i);
                ModeloListaServios mServiMape = new ModeloListaServios(convServico.getInt("id"),
                        convServico.getString("nombre"), convServico.getString("foro"));
                mModeloServicios.add(mServiMape);
            }
        } catch (Exception e) {e.printStackTrace();}
    }

    public class CustomAdapter extends BucketListAdapter<String> {

        private Activity mActivity;
        private List<ModeloListaServios> items;
        private ImageLoader imageLoader;
        private DisplayImageOptions options;

        public CustomAdapter(Activity ctx, List<ModeloListaServios> elements) {
            super(ctx, elements);
            this.mActivity=ctx;
            this.items=elements;

            //Setup the ImageLoader, we'll use this to display our images
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mActivity).build();
            imageLoader = ImageLoader.getInstance();
            imageLoader.init(config);

            //Setup options for ImageLoader so it will handle caching for us.
            options = new DisplayImageOptions.Builder()
                    .cacheInMemory()
                    .cacheOnDisc()
                    .build();
        }



        @Override
        protected View getBucketElement(final int position, final ModeloListaServios currentElement) {
            // TODO Auto-generated method stub
            final ViewHolder holder ;
            View bucketElement=null;

            LayoutInflater inflater = mActivity.getLayoutInflater();
            bucketElement = inflater.inflate(R.layout.grid_item, null);
            holder = new ViewHolder(bucketElement);
            bucketElement.setTag(holder);

            holder.indicator.setVisibility(View.VISIBLE);
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
                    holder.indicator.setVisibility(View.INVISIBLE);
                    holder.image.setVisibility(View.VISIBLE);
                }
                @Override
                public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
                    // TODO Auto-generated method stub
                }
            };

            imageLoader.displayImage(currentElement.getStrUrl(),holder.image,options, listener);

            holder.name.setText(currentElement.getNombreServicio());

            bucketElement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   //showMessage("position : " + currentElement.getNombreServicio());
                   Intent intent = new Intent(getActivity(), ActivityServicioDescripcion.class);
                   intent.putExtra("id_servicio", currentElement.getIdServicio());
                   startActivity(intent);

                }
            });

            return bucketElement;
        }

    }

    class ViewHolder {
        public TextView name = null;
        public TextView info = null;
        public TextView modified = null;
        public ImageView image = null, app_icon = null;
        public LinearLayout rowlayout;
        private ProgressBarCircularIndetermininate indicator;

        ViewHolder(View row) {
            name = (TextView) row.findViewById(R.id.projectName);
            //info = (TextView) row.findViewById(R.id.companyName);
            //modified = (TextView) row.findViewById(R.id.Name);
            image = (ImageView) row.findViewById(R.id.listicon);
            indicator = (ProgressBarCircularIndetermininate)row.findViewById(R.id.progressBarCircularImageninterno);
        }

        void populateFrom(String s) {
            name.setText(s);
        }
    }

    private boolean validarDatosServicio(){
        boolean resultadoDatos;

        if(mModeloServicios.size() > 0){
            resultadoDatos = true;
        }else{
            resultadoDatos = false;
        }
        return resultadoDatos;
    }
}
