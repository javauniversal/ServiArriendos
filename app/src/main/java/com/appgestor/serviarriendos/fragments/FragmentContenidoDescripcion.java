package com.appgestor.serviarriendos.fragments;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.appgestor.serviarriendos.R;
import com.appgestor.serviarriendos.activitys.ShowMessage;
import com.appgestor.serviarriendos.adapters.ImagenBaseAdapter;
import com.appgestor.serviarriendos.models.GalleryImagenes;
import com.appgestor.serviarriendos.views.Button;
import com.appgestor.serviarriendos.views.ButtonFlat;
import com.appgestor.serviarriendos.views.ProgressBarCircularIndetermininate;
import com.appgestor.serviarriendos.widgets.DialogParameter;
import com.appgestor.serviarriendos.widgets.Dialog;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Poo_Code on 05/12/2014.
 */
public class FragmentContenidoDescripcion extends BaseVolleyFragment implements
        OnItemClickListener, View.OnClickListener {

    protected TextView mTciudad, mTbarrio,mTtiponegocio,mTtipopropiedad, mTprecio;
    protected TextView mThabitaciones, mTbanos, mTarea, mTestrato, mTadmin, mContacto;
    protected TextView mEmail, mInmobiliariaTextViewi, mTelefonoTextViewt2;
    protected Button streeView, consulta;
    protected DialogParameter dialog;
    protected Dialog dialogoAlert;
    protected JSONArray mImagenesbd = null;
    private ArrayList<GalleryImagenes> publicAvaiable = new ArrayList<GalleryImagenes>();
    protected GalleryImagenes mGallery;
    protected Gallery gallery;
    private ProgressBarCircularIndetermininate progressWheel, progreemail;
    protected ShowMessage ms;
    protected AlertDialog d;

    public static FragmentContenidoDescripcion newInstance(Bundle arguments) {
        FragmentContenidoDescripcion fragment = new FragmentContenidoDescripcion();
        if(arguments != null){
            fragment.setArguments(arguments);
        }
        return fragment;
    }

    public FragmentContenidoDescripcion() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frament_contenido_descripcion, container, false);

        mTciudad = (TextView) rootView.findViewById(R.id.ciudadTextViewt);
        mTbarrio = (TextView) rootView.findViewById(R.id.barrio);
        mTtiponegocio = (TextView) rootView.findViewById(R.id.tipoInmueble);
        mTtipopropiedad = (TextView) rootView.findViewById(R.id.tipoNegocio);
        mTprecio = (TextView) rootView.findViewById(R.id.precioInmueble);
        mThabitaciones = (TextView) rootView.findViewById(R.id.alcobasTextViewt);
        mTbanos = (TextView) rootView.findViewById(R.id.banosTextViewt);
        mTarea = (TextView) rootView.findViewById(R.id.areaTextViewt);
        mTestrato = (TextView) rootView.findViewById(R.id.estrato);
        mTadmin = (TextView) rootView.findViewById(R.id.cuotaAdmin);
        mContacto = (TextView) rootView.findViewById(R.id.nombreTextView);
        mEmail = (TextView) rootView.findViewById(R.id.emailTextView);
        mInmobiliariaTextViewi = (TextView) rootView.findViewById(R.id.inmobiliariaTextViewi);
        mTelefonoTextViewt2 = (TextView) rootView.findViewById(R.id.telefonoTextViewt2);
        progressWheel = (ProgressBarCircularIndetermininate) rootView.findViewById(R.id.progregaleria);
        progreemail = (ProgressBarCircularIndetermininate) rootView.findViewById(R.id.progreemail);

        streeView = (Button) rootView.findViewById(R.id.streeView);
        streeView.setOnClickListener(this);

        consulta = (Button) rootView.findViewById(R.id.consulta);
        consulta.setOnClickListener(this);

        gallery = (Gallery) rootView.findViewById(R.id.gallery);

        final ImageView imageView = (ImageView) rootView.findViewById(R.id.drawer_indicator);

        ms = new ShowMessage();

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getParametrosDetalles();
        setupGrid();
        gallery.setOnItemClickListener(this);

    }

    private void setupGrid() {
        String url = "http://www.appgestor.com/ServicioWebArriendos/service_database.php";
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        // response
                        parseJSON(response);

                        gallery.setAdapter(new ImagenBaseAdapter(getActivity(),publicAvaiable));

                        gallery.setVisibility(View.VISIBLE);
                        progressWheel.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        onConnectionFailed(error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("operador", "gallerya");
                params.put("id_imagen", String.valueOf(getArguments().getInt("id_arriendo")));
                return params;
            }
        };
        addToQueue(jsonRequest);
    }

    private void parseJSON(String json){

        try {
            JSONObject jsonObject = new JSONObject(json);
            mImagenesbd = jsonObject.getJSONArray("imagenes");

            for (int i = 0; i < mImagenesbd.length(); i++) {
                JSONObject mImagenesObjetos = mImagenesbd.getJSONObject(i);

                GalleryImagenes mGalleryImagenes = new GalleryImagenes(
                        mImagenesObjetos.getInt("id_imagen"),
                        mImagenesObjetos.getInt("id_arriendo"));
                mGalleryImagenes.setData(mImagenesObjetos
                        .getString("photo"));
                publicAvaiable.add(mGalleryImagenes);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getParametrosDetalles() {
        mTciudad.setText(getArguments().getString("ciudad"));
        mTbarrio.setText(getArguments().getString("barrio"));
        mTtiponegocio.setText(getArguments().getString("tipologia"));
        mTtipopropiedad.setText(getArguments().getString("propiedad"));

        DecimalFormat format = new DecimalFormat("$#,###.##");
        mTprecio.setText(format.format(getArguments().getDouble("precio")));
        mThabitaciones.setText(String.valueOf(getArguments().getInt("habitaciones")));
        mTbanos.setText(String.valueOf(getArguments().getInt("banos")));
        mTarea.setText(getArguments().getString("area"));
        mTestrato.setText(String.valueOf(getArguments().getInt("estrato")));
        mTadmin.setText(format.format(getArguments().getDouble("admin")));
        mContacto.setText(getArguments().getString("contacto"));
        mEmail.setText(getArguments().getString("email"));
        mInmobiliariaTextViewi.setText(getArguments().getString("imonbiliaria"));
        mTelefonoTextViewt2.setText(getArguments().getString("telefono"));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.streeView:
                //Accion que genera la vista a StreeView con la longitud y la latitud
                String uri = "google.streetview:cbll=" + getArguments().getDouble("latitud") + "," + getArguments().getDouble("longitud")
                        + "&cbp=1,99.56,,1,-5.27&mz=21";
                Intent streetView = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse(uri));
                try {
                    startActivity(streetView);
                } catch (Exception e) {
                    ms.mostrarMensaje("Error Loading StreetView..", getActivity());
                }
                break;

            case R.id.consulta:
                dialog = new DialogParameter(getActivity(), "Información.", "m");
                dialog.show();
                ButtonFlat acceptButton = dialog.getButtonAccept();
                ButtonFlat cancelButton = dialog.getButtonCancel();
                acceptButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dialog.getNombre().getText().toString().equals("")) {
                            ms.mostrarMensaje("El nombre es obligatorio.", getActivity());
                            dialog.getNombre().requestFocus();
                        }else if(!dialog.getEmail().getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+\\.[a-z]+")){
                            ms.mostrarMensaje("E-mail incorrecto digite nuevamente.", getActivity());
                            dialog.getEmail().requestFocus();
                        }else if(dialog.getTelefono().getText().toString().equals("")){
                            ms.mostrarMensaje("El teléfono es obligatorio.", getActivity());
                            dialog.getTelefono().requestFocus();
                        }else{
                            dialog.dismiss();
                            progreemail.setVisibility(View.VISIBLE);
                            setEmailReporte();

                        }
                    }
                });
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mGallery = publicAvaiable.get(position);

        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View textEntryView = factory.inflate(R.layout.imagen_ampliada,null);

        ImageView ivAnimacion = (ImageView) textEntryView.findViewById(R.id.imageS);
        ivAnimacion.setImageBitmap(mGallery.getPhoto());

        d = new AlertDialog.Builder(getActivity()).setView(textEntryView).setCancelable(true).show();
    }

    public void setEmailReporte(){

        String url = "http://www.appgestor.com/ServicioWebArriendos/enviocorreo.php";
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        // response
                        if(response.toString().equals("1")){
                            dialogoAlert = new Dialog(getActivity(),"Respuesta", "Correo enviado. En breve se comunicaran con usted para más información.");
                        }else{
                            dialogoAlert = new Dialog(getActivity(),"Respuesta", "Problemas al enviar el correo vuelva a intentarlo más tarde.");
                        }
                        progreemail.setVisibility(View.GONE);
                        dialogoAlert.show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        onConnectionFailed(error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                DecimalFormat format = new DecimalFormat("$#,###.##");
                params.put("nombre", dialog.getNombre().getText().toString());
                params.put("correo", dialog.getEmail().getText().toString());
                params.put("telefono", getArguments().getString("telefono"));
                params.put("id", String.valueOf(getArguments().getInt("id_arriendo")));
                params.put("inmobiliaria", getArguments().getString("imonbiliaria"));
                params.put("precio", format.format(getArguments().getDouble("precio")));
                params.put("ciudad", getArguments().getString("ciudad"));
                params.put("correoq", getArguments().getString("email"));
                return params;
            }
        };
        addToQueue(jsonRequest);
    }


}
