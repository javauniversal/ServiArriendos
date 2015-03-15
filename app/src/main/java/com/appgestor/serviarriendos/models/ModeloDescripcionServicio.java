package com.appgestor.serviarriendos.models;

/**
 * Created by Poo_Code on 16/12/2014.
 */
public class ModeloDescripcionServicio {

    private int serid;
    private int serpuntuacion;
    private String sernombre, serapellido, sertelefono, sercelular, serempresa, serdireccion, sercorreo, sernit, serfoto, ciudad, servicio;

    public ModeloDescripcionServicio(int _serid, String _sernombre, String _serapellido, String _sertelefono,
                                     String _sercelular, String _serempresa, String _serdireccion, int _serpuntuacion,
                                     String _sercorreo, String _sernit, String _serfoto, String _ciudad, String _servicio){

        serid = _serid;
        sernombre = _sernombre;
        serapellido = _serapellido;
        sertelefono = _sertelefono;
        sercelular = _sercelular;
        serempresa = _serempresa;
        serdireccion = _serdireccion;
        serpuntuacion = _serpuntuacion;
        sercorreo = _sercorreo;
        sernit = _sernit;
        serfoto = _serfoto;
        ciudad = _ciudad;
        servicio = _servicio;
    }


    public int getSerid() {
        return serid;
    }

    public int getSerpuntuacion() {
        return serpuntuacion;
    }

    public String getSernombre() {
        return sernombre;
    }

    public String getSerapellido() {
        return serapellido;
    }

    public String getSertelefono() {
        return sertelefono;
    }

    public String getSercelular() {
        return sercelular;
    }

    public String getSerdireccion() {
        return serdireccion;
    }

    public String getSerempresa() {
        return serempresa;
    }

    public String getSernit() {
        return sernit;
    }

    public String getSercorreo() {
        return sercorreo;
    }

    public String getSerfoto() {
        return serfoto;
    }

    public String getCiudad() {
        return ciudad;
    }

    public String getServicio() {
        return servicio;
    }

}
