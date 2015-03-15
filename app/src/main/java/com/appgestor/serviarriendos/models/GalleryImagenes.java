package com.appgestor.serviarriendos.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class GalleryImagenes {
	
	protected int id_imagen, id_arriendos;
	protected String data;
	protected Bitmap photo;
	
	public GalleryImagenes(int id_imagen, int id_arriendos) {
		this.id_imagen = id_imagen;
		this.id_arriendos = id_arriendos;
	}
	
	public int getId(){return id_imagen;}
	public int getIdArriendo(){return id_arriendos;}
	public String getData() {return data;}
	
	public void setData(String data) {
	    this.data = data;
	    try {   
	      byte[] byteData = Base64.decode(data, Base64.DEFAULT);
	      this.photo = BitmapFactory.decodeByteArray( byteData, 0, byteData.length);
	    }
	    catch(Exception e) {
	      e.printStackTrace();
	    }
	  }
	     
	  public Bitmap getPhoto() {
	    return photo;
	  }
}
