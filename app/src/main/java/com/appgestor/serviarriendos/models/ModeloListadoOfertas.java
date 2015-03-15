package com.appgestor.serviarriendos.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

/**
 * Created by Poo_Code on 07/11/2014.
 */

public class ModeloListadoOfertas {
    protected int intId;
    protected String strCiudad;
    protected Bitmap photo;
    protected String data;

    public ModeloListadoOfertas(int rId, String rCiudad) {

        intId = rId;
        strCiudad = rCiudad;

    }

    public int getId() {
        return intId;
    }

    public String getCiudad() {
        return strCiudad;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
        try {
            byte[] byteData = Base64.decode(data, Base64.DEFAULT);
            this.photo = BitmapFactory.decodeByteArray(byteData, 0,
                    byteData.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Bitmap getPhoto() {
        return photo;
    }
}
