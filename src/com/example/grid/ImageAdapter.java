package com.example.grid;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.GridLayout.LayoutParams;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
	private Context mContext;
	private HashMap<Integer, Bitmap> imagenes = new HashMap<Integer, Bitmap>();
	private int[] mapeador;

	public ImageAdapter(Context c, Bitmap cover, int filas, int columnas,
			int[] mapeadorTablero) {
		mContext = c;
		this.mapeador = mapeadorTablero;
		int ancho = cover.getWidth();
		int alto = cover.getHeight();
		int desplazamientoAncho = ancho / columnas;
		int desplazamientoAlto = alto / filas;
		for (int i = 0; i < filas * columnas; i++) {
			imagenes.put(i, Bitmap.createBitmap(cover, desplazamientoAncho
					* (i % columnas), desplazamientoAlto * (i / filas),
					desplazamientoAncho, desplazamientoAlto));
			if (i == filas * columnas - 1)
				imagenes.get(i).eraseColor(Color.RED);
		}
	}

	public int getCount() {
		return mapeador.length;
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	// create a new ImageView for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;
		if (convertView == null) { // if it's not recycled, initialize some
									// attributes
			imageView = new ImageView(mContext);
			imageView.setLayoutParams(new GridView.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
			imageView.setAdjustViewBounds(true);
			imageView.setPadding(0, 0, 0, 0);
		} else {
			imageView = (ImageView) convertView;
		}
		imageView.setImageBitmap(imagenes.get(mapeador[position]));
		return imageView;
	}

	// references to our images

}