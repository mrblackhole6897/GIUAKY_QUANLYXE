package com.example.quanlyxe;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static java.lang.Integer.parseInt;

public class XeActivity extends AppCompatActivity {
    ArrayList<Xe> xes = new ArrayList<>();
    ListView listXe;
    String data ="";
    BottomNavigationView bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xe);
        addControl();

        Bundle extras = getIntent().getExtras();
         data = extras.getString("maLoai");

        ListXeTask listXeTask = new ListXeTask();
        listXeTask.execute(data);
        event();
    }
    class ListXeTask extends AsyncTask<String, Void, ArrayList<Xe>>
    {
        @Override
        protected ArrayList<Xe> doInBackground(String... strings) {
            try {
                URL url = new URL("http://192.168.1.12/WebService/WebService.asmx/listXe");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

                OutputStream os = connection.getOutputStream();
                OutputStreamWriter writer = new OutputStreamWriter(os,"UTF-8");
                        writer.write("maloai="+strings[0]);
                        writer.flush();
                        writer.close();
                        os.close();


                DocumentBuilderFactory BuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder =BuilderFactory.newDocumentBuilder();
                Document document = documentBuilder.parse(connection.getInputStream());
                NodeList dsNode = document.getElementsByTagName("XE");
                xes =getdsXe(dsNode);

            }catch (Exception ex)
            {
                Log.e("Loi", ex.toString());
            }
            return xes;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<Xe> xes) {
            super.onPostExecute(xes);
            XeAdapter xeAdapter = new XeAdapter(XeActivity.this,R.layout.list_item_xe,xes);
            listXe.setAdapter(xeAdapter);

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }


    }
    private ArrayList<Xe> getdsXe (NodeList dsNode)
    {
        ArrayList<Xe> dsXe = new ArrayList<>();
        for (int i=0;i< dsNode.getLength(); i++)
        {
            Element nodeLoaiXe = (Element) dsNode.item(i);
            String maXe = nodeLoaiXe.getElementsByTagName("MAXE").item(0).getTextContent();
            String tenXe = nodeLoaiXe.getElementsByTagName("TENXE").item(0).getTextContent();
            String maLoai = nodeLoaiXe.getElementsByTagName("MALOAI").item(0).getTextContent();
            String dungTich = nodeLoaiXe.getElementsByTagName("DUNGTICH").item(0).getTextContent();
            String donGia = nodeLoaiXe.getElementsByTagName("DONGIA").item(0).getTextContent();

            Xe xe = new Xe(maXe,tenXe,maLoai,parseInt(dungTich),parseInt(donGia));
            dsXe.add(xe);
        }
        return dsXe;

    }


    private void addControl()
    {

        listXe = findViewById(R.id.listxe);
        bar =  findViewById(R.id.xe_bottombar);
        bar.setOnNavigationItemSelectedListener(listener);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener listener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch(menuItem.getItemId()) {
                        case R.id.ic_them:
                        {
                            final AlertDialog.Builder builder=new AlertDialog.Builder(XeActivity.this);
                            LayoutInflater inflater=getLayoutInflater();
                            View v= inflater.inflate(R.layout.them_xe,null);
                            final TextView maloai =(TextView) v.findViewById(R.id.ins_xe_maloai);
                            final EditText maxe = v.findViewById(R.id.ins_xe_maxe);
                            final EditText tenxe = v.findViewById(R.id.ins_xe_tenxe);
                            final EditText dungtich = v.findViewById(R.id.ins_xe_dungtich);
                            final EditText dongia = v.findViewById(R.id.ins_xe_dongia);
                            final Button thoat=(Button) v.findViewById(R.id.ins_xe_cancle);
                            final Button ok=(Button) v.findViewById(R.id.ins_xe_ok);
                            maloai.setText(data);




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
                                    String mxe = maxe.getText().toString();
                                    String txe = tenxe.getText().toString();
                                    String dtich = dungtich.getText().toString();
                                    String dgia = dongia.getText().toString();
                                    XeTaskInsert xeTaskInsert = new XeTaskInsert();
                                    xeTaskInsert.execute("maxe="+mxe+"&tenxe="+txe+"&dungtich="+dtich+"&dongia="+dgia+"&maloai="+data);


                                    dialog.dismiss();
                                }
                            });
                        }
                        break;
                        case R.id.ic_xoa:
                        {
                            AlertDialog.Builder b = new AlertDialog.Builder(XeActivity.this);
                            b.setTitle("Confirm");
                            b.setMessage("Bạn có chắc muốn xóa Đơn đặt hàng này?");

                            b.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    LoaiXeTaskDelete loaiXeTaskDelete = new LoaiXeTaskDelete();
                                   loaiXeTaskDelete.execute("maloai="+data);
                                    dialog.dismiss();
                                }
                            });
                            b.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                            AlertDialog alertDialog = b.create();
                            alertDialog.show();
                        }
                        break;

                    }
                    return false;
                }

            };
    class XeTaskInsert extends AsyncTask<String,Void,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.equalsIgnoreCase("false"))
            {
                Toast.makeText(XeActivity.this, "Không thành công", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(XeActivity.this, "Thành công", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                URL url = new URL("http://192.168.1.12/WebService/WebService.asmx/insertXe");
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
    class LoaiXeTaskDelete extends AsyncTask<String, Void, String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.equalsIgnoreCase("false"))
            {
                Toast.makeText(XeActivity.this, "Không thành công", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(XeActivity.this, "Thành công", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("http://192.168.1.12//WebService/WebService.asmx/deleteLoaiXe");
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
    class XeTaskDelete extends AsyncTask<String, Void, String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.equalsIgnoreCase("false"))
            {
                Toast.makeText(XeActivity.this, "Không thành công", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(XeActivity.this, "Thành công", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("http://192.168.1.12/WebService/WebService.asmx/deleteXe");
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
    private void event(){

        listXe.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String   mxe = xes.get(position).getMaXe().trim();
                String   txe = xes.get(position).getTenXe().trim();
                String  dtich = String.valueOf(xes.get(position).getDungTich());
                String  dgia = String.valueOf(xes.get(position).getDonGia());

                final AlertDialog.Builder builder=new AlertDialog.Builder(XeActivity.this);
                LayoutInflater inflater=getLayoutInflater();
                View v= inflater.inflate(R.layout.them_xe,null);
                final TextView maloai =(TextView) v.findViewById(R.id.ins_xe_maloai);
                final EditText maxe = v.findViewById(R.id.ins_xe_maxe);
                final EditText tenxe = v.findViewById(R.id.ins_xe_tenxe);
                final EditText dungtich = v.findViewById(R.id.ins_xe_dungtich);
                final EditText dongia = v.findViewById(R.id.ins_xe_dongia);


                final Button thoat=(Button) v.findViewById(R.id.ins_xe_cancle);
                final Button ok=(Button) v.findViewById(R.id.ins_xe_ok);
                final Button xoa = (Button) v.findViewById(R.id.ins_xe_xoa);
                xoa.setVisibility(View.VISIBLE);

                maloai.setText(data);
                maxe.setText(mxe);
                tenxe.setText(txe);
                dungtich.setText(dtich);
                dongia.setText(dgia);

//                            int sl = parseInt(soluong_them.getText().toString());
//                            ChiTietDonDatHang add = new ChiTietDonDatHang(mx_them.getText().toString(),sl,mddh_them.getText().toString());


                builder.setCancelable(true);
                builder.setView(v);
                final AlertDialog dialog=builder.create();
                dialog.show();
                thoat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        xoa.setVisibility(View.INVISIBLE);
                        dialog.dismiss();
                    }
                });
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String mxe = maxe.getText().toString();
                        String txe = tenxe.getText().toString();
                        String dtich = dungtich.getText().toString();
                        String dgia = dongia.getText().toString();

                        XeTaskUpdate xeTaskUpdate = new XeTaskUpdate();
                        xeTaskUpdate.execute("maxe="+mxe+"&tenxe="+txe+"&dungtich="+dtich+"&dongia="+dgia);

                        xoa.setVisibility(View.INVISIBLE);
                        dialog.dismiss();
                    }
                });
                xoa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String mxe = maxe.getText().toString();

                        XeTaskDelete xeTaskDelete = new XeTaskDelete();
                        xeTaskDelete.execute("maxe="+mxe);
                        xoa.setVisibility(View.INVISIBLE);
                        dialog.dismiss();
                    }
                });

            }


        });
    }
    class XeTaskUpdate extends AsyncTask<String,Void,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.equalsIgnoreCase("false"))
            {
                Toast.makeText(XeActivity.this, "Không thành công", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(XeActivity.this, "Thành công", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("http://192.168.1.12/WebService/WebService.asmx/updateXe");
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
