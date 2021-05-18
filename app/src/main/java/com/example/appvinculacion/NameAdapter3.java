package com.example.appvinculacion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class NameAdapter3 extends ArrayAdapter<Name3> {

    private List<Name3> names;
    private Context context;

    public NameAdapter3(Context context, int resource, List<Name3> names) {
        super(context, resource,names);
        this.context = context;
        this.names = names;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //getting the layoutinflater
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //getting listview itmes
        View listViewItem = inflater.inflate(R.layout.names, null, true);
        //TextView textViewCode = (TextView) listViewItem.findViewById(R.id.textViewCodigo);
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        //TextView textViewDir = (TextView) listViewItem.findViewById(R.id.textViewDir);
        //TextView textViewSexo = (TextView) listViewItem.findViewById(R.id.textViewSexo);
       // TextView textViewEdad = (TextView) listViewItem.findViewById(R.id.textViewEdad);
        //TextView textViewLon = (TextView) listViewItem.findViewById(R.id.textViewLon);
        //TextView textViewLat = (TextView) listViewItem.findViewById(R.id.textViewLat);


        ImageView imageViewStatus = (ImageView) listViewItem.findViewById(R.id.imageViewStatus);

        //getting the current name
        Name3 name = names.get(position);

        //setting the name to textview

        //textViewCode.setText(""+name.getCodigo());
        textViewName.setText(""+name.getSexo());
        //textViewDir.setText(""+name.getDir());
       // textViewSexo.setText(""+name.getSexo());
        //textViewEdad.setText(""+name.getEdad());
       // textViewLon.setText(""+name.getLon());
       // textViewLat.setText(""+name.getLat());




        //if the synced status is 0 displaying
        //queued icon
        //else displaying synced icon
        if (name.getEstado() == 0)
            imageViewStatus.setBackgroundResource(R.drawable.stopwatch);
        else
            imageViewStatus.setBackgroundResource(R.drawable.success);

        return listViewItem;
    }
}
