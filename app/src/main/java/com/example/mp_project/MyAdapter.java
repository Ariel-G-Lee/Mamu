package com.example.mp_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
/*
 * MyAdapter
 * @author 허윤서,강주혜
 */
public class MyAdapter extends BaseAdapter {

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<SampleData> sample;

    public MyAdapter(Context context, ArrayList<SampleData> data) {
        mContext = context;
        sample = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return sample.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public SampleData getItem(int position) {
        return sample.get(position);
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.listview, null);

        ImageView imageView = (ImageView)view.findViewById(R.id.icon);
        TextView name = (TextView)view.findViewById(R.id.name);
        TextView content = (TextView)view.findViewById(R.id.content);

        imageView.setImageResource(sample.get(position).geticon());
        name.setText(sample.get(position).getname());
        content.setText(sample.get(position).getcontent());

        return view;
    }
}