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

public class DonDatHangAdapter extends ArrayAdapter<DonDatHang> {
    Context context;
    int layoutResourceId;
    ArrayList<DonDatHang> data = null;
    public DonDatHangAdapter(Context context, int resource, ArrayList<DonDatHang> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layoutResourceId = resource;
        this.data = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        DonDatHangAdapter.DonDatHangHolder holder;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new DonDatHangAdapter.DonDatHangHolder();

            holder.dongdathang = row.findViewById(R.id.maddh);
            holder.ngaydat = row.findViewById(R.id.ngaydat);

            row.setTag(holder);
        } else {
            holder = (DonDatHangAdapter.DonDatHangHolder) row.getTag();
        }

        DonDatHang item = data.get(position);

        holder.dongdathang.setText(item.getMaDDH());
        holder.ngaydat.setText(item.getNgayDat().toString());

        return  row;
    }
    static class DonDatHangHolder{

        TextView dongdathang;
        TextView ngaydat;
    }
}
