package com.appgestor.serviarriendos.fragments;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.appgestor.serviarriendos.R;
import com.appgestor.serviarriendos.adapters.BucketListAdapterServicios;
import com.appgestor.serviarriendos.models.ModeloDescripcionServicio;
import com.appgestor.serviarriendos.views.CheckBox;
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
 * Created by Poo_Code on 15/12/2014.
 */
public class FragmentDescripcionServicios extends BaseVolleyFragment {

    private View headerView;
    private View footerView;
    private ListView multiColumnList;
    private ArrayList<ModeloDescripcionServicio> mModeloDescipcionServicio;
    private ProgressBarCircularIndetermininate progressWheel;
    private static CustomAdapterDescripcionServicios adapter;

    public static FragmentDescripcionServicios newInstance(Bundle arguments) {
        FragmentDescripcionServicios fragment = new FragmentDescripcionServicios();
        if(arguments != null){
            fragment.setArguments(arguments);
        }
        return fragment;
    }

    public FragmentDescripcionServicios() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lista_de_servicios, container, false);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupGrid();
        multiColumnList = (ListView) getActivity().findViewById(R.id.card_list);
        progressWheel = (ProgressBarCircularIndetermininate) getActivity().findViewById(R.id.progressBarCircularIndetermininate);
        headerView = LayoutInflater.from(getActivity()).inflate(R.layout.view_header, null, false);
        footerView = LayoutInflater.from(getActivity()).inflate(R.layout.view_footer, null, false);

    }


    private void setupGrid() {
        String url = "http://www.appgestor.com/ServicioWebArriendos/service_database.php";
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(final String response) {
                        //showMessage(response+"");
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
                                onConnectionFinished();
                                multiColumnList.addHeaderView(headerView);
                                multiColumnList.addFooterView(footerView);
                                adapter = new CustomAdapterDescripcionServicios(getActivity(),mModeloDescipcionServicio);
                                adapter.enableAutoMeasure(150);
                                multiColumnList.setAdapter(adapter);
                                multiColumnList.setVisibility(View.VISIBLE);
                                progressWheel.setVisibility(View.GONE);
                            }
                        }.execute();
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
                params.put("operador", "listado_servicios");
                params.put("id_servicio", String.valueOf(getArguments().getInt("id_servicio")));
                return params;
            }
        };
        addToQueue(jsonRequest);
    }

    private void showMessage(String message) {
        Toast.makeText(getActivity(), message,
                Toast.LENGTH_SHORT).show();
    }

    private void parseJSON(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray mDestacadp = jsonObject.getJSONArray("sercios_arriendos");

            mModeloDescipcionServicio = new ArrayList<ModeloDescripcionServicio>();
            for (int i = 0; i < mDestacadp.length(); i++) {
                JSONObject pue = mDestacadp.getJSONObject(i);
                ModeloDescripcionServicio service = new ModeloDescripcionServicio(pue.getInt("id_ser"),
                        pue.getString("nombre"),pue.getString("apellido"), pue.getString("telefono"),
                        pue.getString("celular"), pue.getString("empresa"),pue.getString("direccion"),
                        pue.getInt("puntuacion"),pue.getString("correo"),pue.getString("nit"),
                        pue.getString("foto"),pue.getString("ciudad"),pue.getString("servicio"));
                mModeloDescipcionServicio.add(service);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class CustomAdapterDescripcionServicios extends BucketListAdapterServicios{

        private Activity mActivity;
        private List<ModeloDescripcionServicio> items;
        private ImageLoader imageLoader;
        private DisplayImageOptions options;

        public CustomAdapterDescripcionServicios(Activity ctx, List<ModeloDescripcionServicio> elements) {
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
        protected View getBucketElement(final int position, final ModeloDescripcionServicio currentElement) {
            // TODO Auto-generated method stub
            final ViewHolder3 holder ;
            View bucketElement = null;

            LayoutInflater inflater = mActivity.getLayoutInflater();
            bucketElement = inflater.inflate(R.layout.play_giditem_servicio, null);
            holder = new ViewHolder3(bucketElement);
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

            imageLoader.displayImage(currentElement.getSerfoto(),holder.image,options, listener);

            holder.name.setText(currentElement.getSerempresa());
            holder.servicio.setText(currentElement.getServicio());
            holder.empresa.setText(currentElement.getSercelular());
            holder.puntuacion.setRating(currentElement.getSerpuntuacion());

            return bucketElement;
        }

    }

    class ViewHolder3 {
        public TextView name = null;
        public TextView servicio = null;
        public TextView empresa = null;
        public RatingBar puntuacion = null;
        public ImageView image = null;
        private ProgressBarCircularIndetermininate indicator;
        //public LinearLayout rowlayout;

        ViewHolder3(View row) {
            name = (TextView) row.findViewById(R.id.projectName);
            servicio = (TextView) row.findViewById(R.id.companyName);
            empresa = (TextView) row.findViewById(R.id.Name);
            image = (ImageView) row.findViewById(R.id.listicon);
            puntuacion = (RatingBar) row.findViewById(R.id.ratingBar1);
            indicator = (ProgressBarCircularIndetermininate)row.findViewById(R.id.progressBarCircularImagen);
        }

        void populateFrom(String s) {
            name.setText(s);
        }
    }
}
