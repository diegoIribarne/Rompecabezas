package com.example.grid;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

public class PuzzleFragment extends Fragment {
	private int columnas = 4;
	private int filas = 4;
	private int pedazos = columnas * filas;
	private GridView gridview;
	private ImageAdapter adapter;
	int[] mapeadorTablero = new int[pedazos];
	int espacioVacio;
	boolean trabarBotones = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.cover_puzzle, container, false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Mezclar();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		gridview = (GridView) getActivity().findViewById(R.id.gridView1);
		Bitmap cover = BitmapFactory.decodeResource(getResources(),
				R.drawable.cover);
		adapter = new ImageAdapter(getActivity(), cover, filas, columnas,
				mapeadorTablero);
		gridview.setVerticalScrollBarEnabled(false);
		gridview.setNumColumns(columnas);
		gridview.setAdapter(adapter);
		gridview.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == event.ACTION_MOVE && !trabarBotones) {
					trabarBotones = true;
					int action = event.getActionMasked();
					float currentXPosition = event.getX();
					float currentYPosition = event.getY();
					int position = gridview.pointToPosition(
							(int) currentXPosition, (int) currentYPosition);
					if (position != espacioVacio
							&& detectarEspacioVacio(position)) {
						intercambiar(position);
						gridview.invalidateViews();
					}
					if (ganasteElJuego())
						new FireMissilesDialogFragment().show(
								getFragmentManager(), getTag());
				}
				if (event.getAction() == event.ACTION_UP)
					trabarBotones = false;
				return false;
			}

			private boolean ganasteElJuego() {
				for (int i = 0; i < mapeadorTablero.length; i++) {
					if (mapeadorTablero[i] != i)
						return false;
				}
				return true;
			}

			private void intercambiar(int pos) {
				int vacio = mapeadorTablero[espacioVacio];
				mapeadorTablero[espacioVacio] = mapeadorTablero[pos];
				espacioVacio = pos;
				mapeadorTablero[pos] = vacio;
			}

			private boolean detectarEspacioVacio(int position) {
				//detectar Izquierda
				if(position%filas!=0&&position-1==espacioVacio)
					return true;
			
				//detectar Derecha
				if(position%filas!=filas-1&&position+1==espacioVacio)
					return true;
				//detectar Arriba
				if(position+filas==espacioVacio||position-filas==espacioVacio)
					return true;
				return false;
			}
		});
	}

	private void Mezclar() {
		for (int i = 0; i < mapeadorTablero.length; i++) {
			boolean existe = false;
			int random;
			do {
				random = (int) (Math.random() * pedazos);
				existe = false;
				for (int j = 0; j < i; j++) {
					if (mapeadorTablero[j] == random)
						existe = true;
				}
			} while (existe);
			mapeadorTablero[i] = random;
			if (random == pedazos - 1)
				espacioVacio = i;
		}
	}

	@SuppressLint("ValidFragment")
	public class FireMissilesDialogFragment extends DialogFragment {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the Builder class for convenient dialog construction
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage("FELICIDADES")
					.setPositiveButton("JUGAR",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									Mezclar();
									gridview.invalidateViews();
								}
							})
					.setNegativeButton("CERRAR",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

								}
							});
			// Create the AlertDialog object and return it
			return builder.create();
		}
	}
}
