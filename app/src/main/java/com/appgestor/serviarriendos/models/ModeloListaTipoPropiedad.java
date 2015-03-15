package com.appgestor.serviarriendos.models;

/**
 * Created by Poo_Code on 09/11/2014.
 */
public class ModeloListaTipoPropiedad {

    protected int intId;
    protected String strPropiedad;

    public ModeloListaTipoPropiedad(int rId, String rPropiedad) {
        intId = rId;
        strPropiedad = rPropiedad;
    }

    public int getIdTipoPropiedad() {
        return intId;
    }
    public String getTipoPropiedad() {
        return strPropiedad;
    }



}
