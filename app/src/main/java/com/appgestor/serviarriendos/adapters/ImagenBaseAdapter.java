package com.appgestor.serviarriendos.adapters;

import android.app.Activity;
import android.content.res.TypedArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.appgestor.serviarriendos.R;
import com.appgestor.serviarriendos.models.GalleryImagenes;

import java.util.ArrayList;

@SuppressWarnings("deprecation")
public class ImagenBaseAdapter extends BaseAdapter {

	protected Activity activity;
	protected ArrayList<GalleryImagenes> items;
	int mGalleryItemBackground;
	protected GalleryImagenes mGallery;
	
	public ImagenBaseAdapter(Activity activity , ArrayList<GalleryImagenes> items) {
		this.activity = activity;
		this.items = items;
		TypedArray attr = activity.obtainStyledAttributes(R.styleable.HelloGallery);
		mGalleryItemBackground = attr.getResourceId(
                R.styleable.HelloGallery_android_galleryItemBackground, 0);
        attr.recycle();
	}

	@Override
	public int getCount() {
		return items.size();
	}
	
	@Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
    	return items.get(position).getId();
    }

	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	
    	ImageView imageView = new ImageView(activity);
    	
    	if(convertView == null){
    		
    	}
    	
    	mGallery = items.get(position);
    	imageView.setImageBitmap(mGallery.getPhoto());
    	imageView.setLayoutParams(new Gallery.LayoutParams(150, 100));
    	imageView.setScaleType(ImageView.ScaleType.FIT_XY);
    	imageView.setBackgroundResource(mGalleryItemBackground);
    	
    	return imageView;
    }
    
    

}
