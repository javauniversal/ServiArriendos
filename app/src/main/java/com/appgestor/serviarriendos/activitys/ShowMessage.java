package com.appgestor.serviarriendos.activitys;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by Poo_Code on 18/12/2014.
 */
public class ShowMessage {

    private String mensaje;

    public ShowMessage() {}

    public void mostrarMensaje(String _mensaje, Activity _actividad){
        mensaje = _mensaje;
        Toast.makeText(_actividad, mensaje,
                Toast.LENGTH_SHORT).show();
    }
}
