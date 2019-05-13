package com.example.quanlyxe;


import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
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

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static java.lang.Integer.parseInt;
import static java.lang.Integer.parseUnsignedInt;

public class ChiTietDDHActivity extends AppCompatActivity {
    ArrayList<ChiTietDonDatHang> chiTietDonDatHangs = new ArrayList<>();
    ListView list_ctddh;

    BottomNavigationView bottomNavigationView;
    String data="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_ddh);
        control();
        Bundle extras = getIntent().getExtras();
        data = extras.getString("maDDH");

        ChiTietDDHTask task = new ChiTietDDHTask();
        task.execute("maloai="+data);
        event();
    }
    class ChiTietDDHTask extends AsyncTask<String,Void, ArrayList<ChiTietDonDatHang>>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<ChiTietDonDatHang> chiTietDonDatHangs) {
            super.onPostExecute(chiTietDonDatHangs);
            ChiTietDDHAdapter adapter = new ChiTietDDHAdapter(ChiTietDDHActivity.this,R.layout.list_item_ctddh,chiTietDonDatHangs);
            list_ctddh.setAdapter(adapter);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected ArrayList<ChiTietDonDatHang> doInBackground(String... strings) {
            try {
                URL url = new URL("http://192.168.1.12/WebService/WebService.asmx/listChiTietDonDatHang");
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
                NodeList dsNode = document.getElementsByTagName("CHITIETDONDATHANG");
                chiTietDonDatHangs =getdsCTDDH(dsNode);

            }catch (Exception ex)
            {
                Log.e("Loi", ex.toString());
            }
            return chiTietDonDatHangs;

        }
    }
    private ArrayList<ChiTietDonDatHang> getdsCTDDH (NodeList dsNode)
    {
        ArrayList<ChiTietDonDatHang> dsXe = new ArrayList<>();
        for (int i=0;i< dsNode.getLength(); i++)
        {
            Element nodeDDH = (Element) dsNode.item(i);
            String maDDH = nodeDDH.getElementsByTagName("MADDH").item(0).getTextContent().trim();
            String maXe = nodeDDH.getElementsByTagName("MAXE").item(0).getTextContent().trim();
            String soLuong = nodeDDH.getElementsByTagName("SOLUONG").item(0).getTextContent().trim();


            ChiTietDonDatHang chiTietDonDatHang = new ChiTietDonDatHang(maXe,parseInt(soLuong),maDDH);
            dsXe.add(chiTietDonDatHang);
        }
        return dsXe;

    }
    private void control(){
        list_ctddh = findViewById(R.id.list_ctddh);
//        add = findViewById(R.id.ic_them);

        bottomNavigationView = findViewById(R.id.bottomNavView_Bar);
        bottomNavigationView.setOnNavigationItemSelectedListener(listener);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener listener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch(menuItem.getItemId()) {
                        case R.id.ic_them:
                        {
                            final AlertDialog.Builder builder=new AlertDialog.Builder(ChiTietDDHActivity.this);
                            LayoutInflater inflater=getLayoutInflater();
                            View v= inflater.inflate(R.layout.them_ctdh,null);
                            final TextView mddh_them =(TextView) v.findViewById(R.id.mddh_them);
                            final EditText mx_them = v.findViewById(R.id.mx_them);
                            final EditText soluong_them = v.findViewById(R.id.soluong_them);
                            final Button thoat=(Button) v.findViewById(R.id.cancle);
                            final Button ok=(Button) v.findViewById(R.id.ok);
                            mddh_them.setText(data);

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
                                    String mxe = mx_them.getText().toString();
                                    String sluong = soluong_them.getText().toString();
                                    ChiTietTaskInsert chiTietTaskInsert = new ChiTietTaskInsert();
                                    chiTietTaskInsert.execute("maddh="+data+"&maxe="+mxe+"&soluong="+sluong);
                                    dialog.dismiss();
                                }
                            });
                        }
                        break;
                        case R.id.ic_xoa:
                        {
                            AlertDialog.Builder b = new AlertDialog.Builder(ChiTietDDHActivity.this);
                            b.setTitle("Confirm");
                            b.setMessage("Bạn có chắc muốn xóa Đơn đặt hàng này?");

                            b.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    DonDatHangTaskDelete donDatHangTaskDelete = new DonDatHangTaskDelete();
                                    donDatHangTaskDelete.execute("maddh="+data);
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


    private void event(){

        list_ctddh.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String   mddh = chiTietDonDatHangs.get(position).getMaDDH().trim();
                String   mx = chiTietDonDatHangs.get(position).getMaXe().trim();
                String  sluong = String.valueOf(chiTietDonDatHangs.get(position).getSoLuong());

                final AlertDialog.Builder builder=new AlertDialog.Builder(ChiTietDDHActivity.this);
                LayoutInflater inflater=getLayoutInflater();
                View v= inflater.inflate(R.layout.them_ctdh,null);
                final TextView mddh_them =(TextView) v.findViewById(R.id.mddh_them);
                final EditText mx_them = v.findViewById(R.id.mx_them);
                final EditText soluong_them = v.findViewById(R.id.soluong_them);
                final Button thoat=(Button) v.findViewById(R.id.cancle);
                final Button ok=(Button) v.findViewById(R.id.ok);
                final Button xoa = (Button) v.findViewById(R.id.xoa);
                xoa.setVisibility(View.VISIBLE);
                mddh_them.setText(mddh);
                mx_them.setText(mx);
                soluong_them.setText(sluong);

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
                        String mxe = mx_them.getText().toString();
                        String sluong = soluong_them.getText().toString();
                        ChiTietTaskUpdate chiTietTaskUpdate = new ChiTietTaskUpdate();
                        chiTietTaskUpdate.execute("maddh="+data+"&maxe="+mxe+"&soluong="+sluong);
                        xoa.setVisibility(View.INVISIBLE);
                        dialog.dismiss();
                    }
                });
                xoa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String mxe = mx_them.getText().toString();
                        String sluong = soluong_them.getText().toString();
                        ChiTietTaskDelete chiTietTaskDelete = new ChiTietTaskDelete();
                        chiTietTaskDelete.execute("maddh="+data+"&maxe="+mxe);
                        xoa.setVisibility(View.INVISIBLE);
                        dialog.dismiss();
                    }
                });

            }


        });
    }
    class ChiTietTaskDelete extends AsyncTask<String,Void,String>
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
                Toast.makeText(ChiTietDDHActivity.this, "Không thành công", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(ChiTietDDHActivity.this, "Thành công", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("http://192.168.1.12/WebService/WebService.asmx/deleteCtDonDatHang");
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
    class ChiTietTaskInsert extends AsyncTask<String,Void,String>
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
                Toast.makeText(ChiTietDDHActivity.this, "Không thành công", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(ChiTietDDHActivity.this, "Thành công", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                URL url = new URL("http://192.168.1.12/WebService/WebService.asmx/insertCTDonDatHang");
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
    class ChiTietTaskUpdate extends AsyncTask<String,Void,String>
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
                Toast.makeText(ChiTietDDHActivity.this, "Không thành công", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(ChiTietDDHActivity.this, "Thành công", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("http://192.168.1.12/WebService/WebService.asmx/updateCTDonDatHang");
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
    class DonDatHangTaskDelete extends AsyncTask<String, Void, String>
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
                Toast.makeText(ChiTietDDHActivity.this, "Không thành công", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(ChiTietDDHActivity.this, "Thành công", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("http://192.168.1.12/WebService/WebService.asmx/deleteDonDatHang");
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
