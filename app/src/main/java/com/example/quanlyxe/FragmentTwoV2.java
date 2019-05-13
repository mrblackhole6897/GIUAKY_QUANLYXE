package com.example.quanlyxe;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class FragmentTwoV2 extends ListFragment {
    ArrayList<DonDatHang> donDatHangs = new ArrayList<>();
    Button insbton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        donDatHangs.add(new DonDatHang("129872",new Date()));
//        donDatHangs.add(new DonDatHang("129872",new Date()));
//        donDatHangs.add(new DonDatHang("129872",new Date()));
//        donDatHangs.add(new DonDatHang("129872",new Date()));
//        donDatHangs.add(new DonDatHang("129872",new Date()));
        ListDonDatHangTask listDonDatHangTask = new ListDonDatHangTask();
        listDonDatHangTask.execute();



        event();

        return inflater.inflate(R.layout.fragment_ds_ddh,container,false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        insbton = getView().findViewById(R.id.insbton);
        insbton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                LayoutInflater inflater=getLayoutInflater();
                v= inflater.inflate(R.layout.them_ddh,null);
                final EditText mddh_them = v.findViewById(R.id.ins_mddh);
                final TextView ngaylap_them = v.findViewById(R.id.ins_ngaylap);

                final Button thoat=(Button) v.findViewById(R.id.ins_cancle);
                final Button ok=(Button) v.findViewById(R.id.ins_ok);

                Date date = new Date();
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String ngay = df.format(date.getTime());
                ngaylap_them.setText(ngay);
//                            int sl = parseInt(soluong_them.getText().toString());
//                            ChiTietDonDatHang add = new ChiTietDonDatHang(mx_them.getText().toString(),sl,mddh_them.getText().toString());


                builder.setCancelable(true);
                builder.setView(v);
                final AlertDialog dialog=builder.create();
                dialog.show();
                thoat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String mddh = mddh_them.getText().toString();
                        String ngayl = ngaylap_them.getText().toString();
                        DDHTaskInsert ddhTaskInsert = new DDHTaskInsert();
                        ddhTaskInsert.execute("maddh="+mddh+"&ngaylap="+ngayl);

                        dialog.dismiss();
                    }
                });
            }
        });

    }
    private void event(){

    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(getActivity(),ChiTietDDHActivity.class);
        intent.putExtra("maDDH",donDatHangs.get(position).getMaDDH());
        startActivity(intent);
        super.onListItemClick(l, v, position, id);
    }
    class ListDonDatHangTask extends AsyncTask<Void, Void, ArrayList<DonDatHang>>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<DonDatHang> donDatHangs) {
            super.onPostExecute(donDatHangs);
            DonDatHangAdapter adapter = new DonDatHangAdapter(getActivity(),R.layout.list_ddh,donDatHangs);
            setListAdapter(adapter);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected ArrayList<DonDatHang> doInBackground(Void... voids) {


            try {


                URL url = new URL(IPSV.ip+"listDonDatHang");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

                DocumentBuilderFactory BuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder =BuilderFactory.newDocumentBuilder();
                Document document = documentBuilder.parse(connection.getInputStream());
                NodeList dsNode = document.getElementsByTagName("DONDATHANG");
                donDatHangs =getdsDonDatHang(dsNode);

            }catch (Exception ex)
            {
                Log.e("Loi", ex.toString());
            }
            return donDatHangs;

        }
    }
    private ArrayList<DonDatHang> getdsDonDatHang (NodeList dsNode)
    {
        ArrayList<DonDatHang> dsDonDatHang = new ArrayList<>();
        for (int i=0;i< dsNode.getLength(); i++)
        {
            Element nodeDonDatHang = (Element) dsNode.item(i);
            String maDonDatHang = nodeDonDatHang.getElementsByTagName("MADDH").item(0).getTextContent().trim();
            String ngayDat = nodeDonDatHang.getElementsByTagName("NGAYLAP").item(0).getTextContent().trim();

            DonDatHang donDatHang = new DonDatHang(maDonDatHang, ngayDat);
            dsDonDatHang.add(donDatHang);
        }
        return dsDonDatHang;

    }



    class DDHTaskInsert extends AsyncTask<String, Void, String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s =="false")
            {
                Toast.makeText(getActivity(), "Không thành công", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getActivity(), "Thành công", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("http://192.168.1.12/WebService/WebService.asmx/insertDonHang");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

                OutputStream os = connection.getOutputStream();
                OutputStreamWriter writer = new OutputStreamWriter(os,"UTF-8");
                writer.write(strings[0]);
                writer.flush();
                writer.close();
                os.close();


                DocumentBuilderFactory BuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder =BuilderFactory.newDocumentBuilder();
                Document document = documentBuilder.parse(connection.getInputStream());
                NodeList dsNode = document.getElementsByTagName("boolean");
                String kq = dsNode.item(0).getTextContent().trim();
                return kq;
            }catch (Exception ex)
            {
                Log.e("Loi", ex.toString());
            }
            return null;
        }
    }
}

