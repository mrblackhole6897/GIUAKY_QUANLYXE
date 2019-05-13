package com.example.quanlyxe;

import android.app.Activity;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;


public class LoaiXeAdapter extends ArrayAdapter<LoaiXe> {
   Context context;
   int layoutResourceId;
    ArrayList<LoaiXe> data = null;
    public LoaiXeAdapter(Context context, int resource, ArrayList<LoaiXe> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layoutResourceId = resource;
        this.data = objects;
    }


    @Override
    public View getView(int position, View convertView,ViewGroup parent) {
        View row = convertView;
        LoaiXeHolder holder;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new LoaiXeHolder();

            holder.maloai = row.findViewById(R.id.maloai);
            holder.tenloai = row.findViewById(R.id.tenloai);
            holder.xuatxu = row.findViewById(R.id.xuatxu);

            row.setTag(holder);
        } else {
            holder = (LoaiXeHolder) row.getTag();
        }

        LoaiXe item = data.get(position);

        holder.maloai.setText(item.maLoai);
        holder.tenloai.setText(item.tenLoai);
        holder.xuatxu.setText(item.xuatXu);

        return  row;
    }
    static class LoaiXeHolder{

        TextView maloai;
        TextView tenloai;
        TextView xuatxu;
    }
}
