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

import com.bumptech.glide.Glide;

import java.util.ArrayList;
/*
 * MyAdapter
 * @author 허윤서, 강주혜, 김희주, 이가빈
 */
public class MyAdapter extends BaseAdapter {
    Utils utils;
    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<ContentValues> values;

    public MyAdapter(Context context, ArrayList<ContentValues> data) {
        mContext = context;
        values = data;
        utils = new Utils(mContext);
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void setItem(ArrayList<ContentValues> newValues){
        values = newValues;
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

        byte[] bytearrays = values.get(position).getAsByteArray("Image");

        String url = values.get(position).getAsString("YoutubeUrl");

        // Youtube url이 저장되어 있지 않을 경우 첨부한 사진을 listView의 imageView에 띄운다.
        if(url.equals("")){
            if(bytearrays.length>0 && bytearrays != null){
                Bitmap bitmap = utils.ByteArraytoBitmap(bytearrays);
                imageView.setImageBitmap(utils.ByteArraytoBitmap(bytearrays));
            }
        } else {
            // Youtube url이 저장되어있을 경우 url을 통해 가져와
            // 해당 동영상의 썸네일을 ListView의 imageView에 띄운다.
            String id = url.substring(url.lastIndexOf("/")+1);
            String thumnailUrl = "https://img.youtube.com/vi/"+ id+ "/" + "mqdefault.jpg";
            Glide.with(content).load(thumnailUrl).into(imageView);
        }


        //Bitmap bitmap = utils.ByteArraytoBitmap(sample.get(position).getAsByteArray("Image"));

        name.setText(values.get(position).getAsString("MemoTitle"));
        content.setText(values.get(position).getAsString("MemoContents"));

        return view;
    }
}