package com.example.chintan.smartwardrobe;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class DataAdapterUnused extends ArrayAdapter<String>
{
    public Activity context;

    public ArrayList<String> cid, ctype;
    //public ArrayList<String> ctype;

    public DataAdapterUnused(Activity context, ArrayList<String> cid, ArrayList<String> ctype)
    {
        super(context,R.layout.activity_usedlist,cid);
        this.context=context;
        this.cid=cid;
        this.ctype=ctype;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.activity_usedlist, null, true);

        TextView textViewid = rowView.findViewById(R.id.txtclothid);
        TextView textViewtype = rowView.findViewById(R.id.txtclothtype);

        textViewid.setText(cid.get(position));
        textViewtype.setText(ctype.get(position));

        return rowView;
    }
}
