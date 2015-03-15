package com.appgestor.serviarriendos.models;

/**
 * Created by Poo_Code on 09/11/2014.
 */
public class ModeloListaMunicipios {

    protected int intId;
    protected String strMunicipio;

    public ModeloListaMunicipios(int rId, String rMunicipios) {
        intId = rId;
        strMunicipio = rMunicipios;
    }

    public int getIdMunicipio() {
        return intId;
    }
    public String getMunicipio() {
        return strMunicipio;
    }

}
