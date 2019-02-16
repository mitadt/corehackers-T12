package com.example.chintan.smartwardrobe;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class UnusedClothActivity extends AppCompatActivity
{
    ListView listView;
    ProgressDialog progressDialog;
    int resp;
    public ArrayList<String> clothid,clothtype;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unused_clothes);

        listView = findViewById(R.id.unusedlist);

        String email = GlobalVar.email_id;
        /*Bundle bundle = getIntent().getExtras();
        String email = bundle.getString("emailid");*/

        filladapter(email);
    }

    public void filladapter(String email)
    {
        final String emailid = email;
        final Connection conn = new Connection();
        if (conn.checkNetworkAvailable(UnusedClothActivity.this)) {

            progressDialog = new ProgressDialog(this);
            progressDialog.show();
            Thread tthread = new Thread() {
                @Override
                public void run() {
                    try
                    {
                        if (conn.getunusedlist(emailid))
                        {
                            resp = 0;
                        }
                        else
                        {
                            resp = 1;
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    hd.sendEmptyMessage(0);
                }
            };
            tthread.start();
        }
        else
        {
            Toast.makeText(this, "Sorry no network access.", Toast.LENGTH_LONG).show();
        }
    }

    public Handler hd = new Handler() {
        public void handleMessage(Message msg) {

            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            switch (resp) {
                case 0:
                    clothid = UnusedList.getClothid();
                    clothtype = UnusedList.getClothtype();
                    if(clothid.isEmpty())
                    {
                        Toast.makeText(UnusedClothActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        DataAdapterUnused da = new DataAdapterUnused(UnusedClothActivity.this, clothid, clothtype);
                        listView.setAdapter(da);
                    }

                    break;
                case 1:
                    Toast.makeText(UnusedClothActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}
