package com.appgestor.serviarriendos.models;

/**
 * Created by Poo_Code on 09/11/2014.
 */
public class ModeloListaBarrio {

    protected int intId;
    protected String strBarrio;

    public ModeloListaBarrio(int rId, String rBarrio) {
        intId = rId;
        strBarrio = rBarrio;
    }

    public int getIdBarrio() {
        return intId;
    }
    public String getBarrio() {
        return strBarrio;
    }


}
