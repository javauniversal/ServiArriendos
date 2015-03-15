package com.appgestor.serviarriendos.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.appgestor.serviarriendos.R;
import com.appgestor.serviarriendos.models.ModeloListaServios;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;



/**
 * Created by Poo_Code on 17/11/2014.
 */
public class AdapterServicios extends BucketListAdapter<ModeloListaServios> {

    // Variables.
    private Activity mContexto;
    private ArrayList<ModeloListaServios> mDatos;
    private LayoutInflater mInflador;
    ImageLoader imageLoader;
    DisplayImageOptions options;

    // Constructor.
    public AdapterServicios(Activity contexto, ArrayList<ModeloListaServios> datos) {
        super(contexto,datos);
        mContexto = contexto;
        mDatos = datos;
    }

    @Override
    protected View getBucketElement(int position, ModeloListaServios currentElement) {
        ViewHolder holder;
        View bucketElement = null;

        LayoutInflater inflater = mContexto.getLayoutInflater();
        bucketElement = inflater.inflate(R.layout.grid_item, null);
        holder = new ViewHolder(bucketElement);
        bucketElement.setTag(holder);
        bindView(holder, position);
        return bucketElement;
    }


    private void bindView(ViewHolder contenedor, int position) {
        ModeloListaServios m = mDatos.get(position);
        contenedor.name.setText(m.getNombreServicio());
    }

    class ViewHolder {
        public TextView lblId;
        public TextView name;
        public ImageView image;

        ViewHolder(View row){
            //name = (TextView) row.findViewById(R.id.name);
        }

        void populateFrom(String s){
            name.setText(s);
        }
    }

}
