package com.appgestor.serviarriendos.models;

/**
 * Created by Poo_Code on 09/11/2014.
 */
public class ModeloListaTipoNegocio {

    protected int intId;
    protected String strTipoNegocio;

    public ModeloListaTipoNegocio(int rId, String rTipoNegocio) {
        intId = rId;
        strTipoNegocio = rTipoNegocio;
    }

    public int getIdTipoNegocio() {
        return intId;
    }
    public String getTipoNegocio() {
        return strTipoNegocio;
    }


}
