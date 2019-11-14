package com.example.mp_project;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
/*
 * MyAdapter
 * @author 허윤서,강주혜, 김희주
 */
public class MyAdapter extends BaseAdapter {
    Utils utils;
    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<ContentValues> values;

    public MyAdapter(Context context, ArrayList<ContentValues> data) {
        mContext = context;
        values = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public ContentValues getItem(int position) {
        return values.get(position);
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.listview, null);

        ImageView imageView = (ImageView)view.findViewById(R.id.icon);
        TextView name = (TextView)view.findViewById(R.id.name);
        TextView content = (TextView)view.findViewById(R.id.content);

        byte[] tmp = values.get(position).getAsByteArray("Image");

        if(tmp.length<=0){
            Bitmap bitmap = utils.ByteArraytoBitmap(tmp);
            imageView.setImageBitmap(bitmap);
        }

        //Bitmap bitmap = utils.ByteArraytoBitmap(sample.get(position).getAsByteArray("Image"));

        name.setText(values.get(position).getAsString("MemoTitle"));
        content.setText(values.get(position).getAsString("MemoContents"));

        return view;
    }
}