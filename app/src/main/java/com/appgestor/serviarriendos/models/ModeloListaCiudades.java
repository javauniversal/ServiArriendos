package com.appgestor.serviarriendos.models;

/**
 * Created by Poo_Code on 08/11/2014.
 */
public class ModeloListaCiudades {

    protected int intId;
    protected String strCiudad;

    public ModeloListaCiudades(int rId, String rCiudad) {
        intId = rId;
        strCiudad = rCiudad;
    }

    public int getIdCiuda() {
        return intId;
    }
    public String getCiudad() {
        return strCiudad;
    }


}
