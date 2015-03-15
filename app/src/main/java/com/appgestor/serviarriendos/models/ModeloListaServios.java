package com.appgestor.serviarriendos.models;

/**
 * Created by Poo_Code on 17/11/2014.
 */
public class ModeloListaServios {

    protected int intIde;
    protected String strNombre;
    protected String strUrl;

    public ModeloListaServios(int rInIde, String rStrNombre, String rstrUrl){
        intIde = rInIde;
        strNombre = rStrNombre;
        strUrl = rstrUrl;
    }
    public int getIdServicio() {
        return intIde;
    }

    public String getNombreServicio(){
        return strNombre;
    }

    public String getStrUrl() {
        return strUrl;
    }
}