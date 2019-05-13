package com.example.quanlyxe;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ChiTietDDHAdapter extends ArrayAdapter<ChiTietDonDatHang> {
    Context context;
    int layoutResourceId;
    ArrayList<ChiTietDonDatHang> data = null;
    public ChiTietDDHAdapter(Context context, int resource, ArrayList<ChiTietDonDatHang> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layoutResourceId = resource;
        this.data = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ChiTietDDHAdapter.ChiTietDDHHolder holder;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ChiTietDDHAdapter.ChiTietDDHHolder();

            holder.maXect = row.findViewById(R.id.mxct);
            holder.soLuongct = row.findViewById(R.id.sl);
            holder.maDDHct = row.findViewById(R.id.mddh);

            row.setTag(holder);
        } else {
            holder = (ChiTietDDHAdapter.ChiTietDDHHolder) row.getTag();
        }

        ChiTietDonDatHang item = data.get(position);
String a = String.valueOf(item.getSoLuong());
        holder.maDDHct.setText(item.getMaDDH());
        holder.maXect.setText(item.getMaXe());
        holder.soLuongct.setText(a);



        return  row;
    }

    @Override
    public int getPosition(ChiTietDonDatHang item) {
        return super.getPosition(item);
    }

    static class ChiTietDDHHolder{
        TextView maXect;
        TextView soLuongct;
        TextView maDDHct;
    }
}
