package com.appgestor.serviarriendos.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appgestor.serviarriendos.R;
import com.appgestor.serviarriendos.activitys.ServiArriendos;
import com.appgestor.serviarriendos.activitys.ShowMessage;

/**
 * Created by Poo_Code on 18/12/2014.
 */
public class FragmentSinConexionDatos extends BaseVolleyFragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    public static FragmentSinConexionDatos newInstance(int sectionNumber) {
        FragmentSinConexionDatos fragment = new FragmentSinConexionDatos();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentSinConexionDatos() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sin_conexion_datos, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        String mensajeDatos = getString(R.string.conexion_datos);
        ShowMessage ms = new ShowMessage();
        ms.mostrarMensaje(mensajeDatos,getActivity());
        super.onActivityCreated(savedInstanceState);

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
}
