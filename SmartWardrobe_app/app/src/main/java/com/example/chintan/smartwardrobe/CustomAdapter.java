package com.example.chintan.smartwardrobe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter
{
    Context context;
    String ClothId[];
    String  ClothType[];
    LayoutInflater inflter;

    public CustomAdapter(Context applicationContext, String[] ClothId, String[] ClothType)
    {
        this.context = applicationContext;
        this.ClothId=ClothId;
        this.ClothType=ClothType;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return ClothId.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent)
    {
        view = inflter.inflate(R.layout.activity_usedlist, null);
        TextView idlist=(TextView)view.findViewById(R.id.txtclothid);
        TextView typelist=(TextView)view.findViewById(R.id.txtclothtype);

        idlist.setText(ClothId[i]);
        typelist.setText(ClothType[i]);
        return view;
    }
}
