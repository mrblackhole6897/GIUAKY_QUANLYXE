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

public class XeAdapter extends ArrayAdapter<Xe> {
    Context context;
    int layoutResourceId;
    ArrayList<Xe> data = null;
    public XeAdapter(Context context, int resource, ArrayList<Xe> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layoutResourceId = resource;
        this.data = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        XeAdapter.XeHolder holder;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new XeAdapter.XeHolder();
            holder.maxe = row.findViewById(R.id.maxe);
            holder.maloai = row.findViewById(R.id.maloai_xe);
            holder.tenxe = row.findViewById(R.id.tenxe);
            holder.dungtich = row.findViewById(R.id.dungtich);
            holder.dongia = row.findViewById(R.id.dongia);

            row.setTag(holder);
        } else {
            holder = (XeAdapter.XeHolder) row.getTag();
        }

        Xe item = data.get(position);
        holder.maxe.setText(item.getMaXe());
        holder.maloai.setText(item.getMaLoai());
        holder.tenxe.setText(item.getTenXe());
        String a =String.valueOf( item.getDungTich());
        holder.dungtich.setText(a);
        String b =String.valueOf( item.getDonGia());
        holder.dongia.setText(b);

        return  row;
    }
    static class XeHolder{
        TextView maxe;
        TextView maloai;
        TextView tenxe;
        TextView dungtich;
        TextView dongia;
    }
}
