package com.appgestor.serviarriendos.models;

/**
 * Created by Poo_Code on 23/11/2014.
 */
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class ModeloListaDestacados {

    protected int id, estrato, alcobas, banos;

    protected String tipologia, inmobiliaria, ciudad, barrio, area, usuario,
            email, telefono, tipoNegocio;
    protected double precio, latitud, longitud, admin;
    private String imgUrl;
    protected String data;
    protected Bitmap photo;

    public ModeloListaDestacados(int id, Double admin, int estrato,
                                    int alcobas, int banos, String tipologia, String inmobiliaria,
                                    String ciudad, String barrio, String area, double precio,
                                    String usuario, String email, String telefono, String tipoNegocio,
                                    double latitud, double longitud, String imgUrl ) {
        this.id = id;
        this.admin = admin;
        this.estrato = estrato;
        this.alcobas = alcobas;
        this.banos = banos;
        this.tipologia = tipologia;
        this.inmobiliaria = inmobiliaria;
        this.ciudad = ciudad;
        this.barrio = barrio;
        this.area = area;
        this.precio = precio;
        this.usuario = usuario;
        this.email = email;
        this.telefono = telefono;
        this.tipoNegocio = tipoNegocio;
        this.latitud = latitud;
        this.longitud = longitud;
        this.imgUrl = imgUrl;
    }

    public int getId() {
        return id;
    }

    public String getCiudad() {
        return ciudad;
    }

    public String getData() {
        return data;
    }

    public double getLatitud() {
        return latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public Double getAdmin() {
        return admin;
    }

    public int getEstrato() {
        return estrato;
    }

    public int getAlcoba() {
        return alcobas;
    }

    public int getBanos() {
        return banos;
    }

    public String getTipologia() {
        return tipologia;
    }

    public String getInmobiliaria() {
        return inmobiliaria;
    }

    public String getBarrio() {
        return barrio;
    }

    public String getArea() {
        return area;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefono() {
        return telefono;
    }

    public double getPrecio(){
        return precio;
    }

    public String getNombre(){
        return usuario;
    }

    public String getTipoNegocio(){
        return tipoNegocio;
    }

    public String getImgUrl() {
        return imgUrl;
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
