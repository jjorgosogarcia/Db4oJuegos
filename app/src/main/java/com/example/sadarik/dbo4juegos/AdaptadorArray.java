package com.example.sadarik.dbo4juegos;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdaptadorArray extends ArrayAdapter<Juego> {

    private Context contexto;
    private ArrayList<Juego> lista;
    private int recurso;
    private LayoutInflater i;

    public AdaptadorArray(Context context, int resource, ArrayList<Juego> objects) {
        super(context, resource, objects);
        this.contexto = context;
        this.lista = objects;
        this.recurso = resource;
        this.i = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



public class ViewHolder {
        public TextView tv1, tv2, tv3, tvPre;
        public ImageView iv;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.v("LOG", "" + lista.size());
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = i.inflate(recurso, null);
            vh = new ViewHolder();
            vh.tv1 = (TextView) convertView.findViewById(R.id.tvTitulo);
            vh.tv2 = (TextView) convertView.findViewById(R.id.tvGenero);
            vh.tv3 = (TextView) convertView.findViewById(R.id.tvPlataforma);
            vh.tvPre = (TextView)convertView.findViewById(R.id.tvPrestado);
            vh.iv = (ImageView) convertView.findViewById(R.id.ivImagen);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        Juego game = lista.get(position);

        if(game.getPlataforma().equals("Ps3")){
            vh.iv.setImageResource(R.drawable.ic_ps3);
        }else if(game.getPlataforma().equals("Pc")){
            vh.iv.setImageResource(R.drawable.ic_pc);
        }else if(game.getPlataforma().equals("Xbox")){
            vh.iv.setImageResource(R.drawable.ic_xbox);
        }else{
            vh.iv.setImageResource(R.drawable.ic_launcher);
        }
        vh.tv1.setText(lista.get(position).getTitulo());
        vh.tv2.setText(lista.get(position).getGenero());
        vh.tv3.setText(lista.get(position).getPlataforma());
        vh.tvPre.setText(lista.get(position).getPrestado());
        vh.iv.setTag(position);
        return convertView;
    }
}

