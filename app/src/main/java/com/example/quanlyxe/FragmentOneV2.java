package com.example.quanlyxe;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class FragmentOneV2 extends ListFragment {
    ArrayList<LoaiXe> loaiXes = new ArrayList<>();
    Button insbton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        ListLoaiXeTask listLoaiXeTask = new ListLoaiXeTask();
        listLoaiXeTask.execute();
        return inflater.inflate(R.layout.fragment_danh_sach,container,false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        insbton = getView().findViewById(R.id.insbton_lx);
        insbton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                LayoutInflater inflater=getLayoutInflater();
                v= inflater.inflate(R.layout.them_loaixe,null);
                final EditText maloai = v.findViewById(R.id.ins_lx_maloai);
                final EditText tenloai = v.findViewById(R.id.ins_lx_tenloai);
                final EditText xuatxu = v.findViewById(R.id.ins_lx_xuatxu);

                final Button thoat=(Button) v.findViewById(R.id.ins_lx_cancle);
                final Button ok=(Button) v.findViewById(R.id.ins_lx_ok);



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
                        String mloai = maloai.getText().toString();
                        String tloai = tenloai.getText().toString();
                        String xxu = xuatxu.getText().toString();

                        LoaiXeTaskInsert loaiXeTaskInsert = new LoaiXeTaskInsert();
                        loaiXeTaskInsert.execute("maloai="+mloai+"&tenloai="+tloai+"&xuatsu="+xxu);

                        dialog.dismiss();
                    }
                });
            }
        });

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
//        Toast.makeText(getActivity(),loaiXes.get(position).maLoai,Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(),XeActivity.class);
        intent.putExtra("maLoai",loaiXes.get(position).maLoai);
        startActivity(intent);
        super.onListItemClick(l, v, position, id);
    }
    class ListLoaiXeTask  extends AsyncTask<Void, Void, ArrayList<LoaiXe>>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<LoaiXe> loaiXes) {
            super.onPostExecute(loaiXes);
            LoaiXeAdapter adapter = new LoaiXeAdapter(getActivity(),R.layout.list_item_loaixe,loaiXes);
            setListAdapter(adapter);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected ArrayList<LoaiXe> doInBackground(Void... voids) {

            try {
                URL url = new URL(IPSV.ip+"listLoaiXe");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

                DocumentBuilderFactory BuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder =BuilderFactory.newDocumentBuilder();
                Document document = documentBuilder.parse(connection.getInputStream());
                NodeList dsNode = document.getElementsByTagName("LOAIXE");
                loaiXes =getdsLoaiXe(dsNode);

            }catch (Exception ex)
            {
                Log.e("Loi", ex.toString());
            }
            return loaiXes;
        }
    }
    private ArrayList<LoaiXe> getdsLoaiXe (NodeList dsNode)
    {
        ArrayList<LoaiXe> dsLoaiXe = new ArrayList<>();
        for (int i=0;i< dsNode.getLength(); i++)
        {
            Element nodeLoaiXe = (Element) dsNode.item(i);
            String maLoai = nodeLoaiXe.getElementsByTagName("MALOAI").item(0).getTextContent().trim();
            String tenLoai = nodeLoaiXe.getElementsByTagName("TENLOAI").item(0).getTextContent().trim();
            String xuatXu = nodeLoaiXe.getElementsByTagName("XUATXU").item(0).getTextContent().trim();

            LoaiXe loaiXe = new LoaiXe(maLoai,tenLoai,xuatXu);
            dsLoaiXe.add(loaiXe);
        }
        return dsLoaiXe;

    }
    class LoaiXeTaskInsert extends AsyncTask<String,Void,String>
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
                URL url = new URL("http://192.168.1.12/WebService/WebService.asmx/insertLoaiXe");
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
