package com.example.chintan.smartwardrobe;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class UsedClothActivity extends AppCompatActivity
{
    ListView listView;
    ProgressDialog progressDialog;
    int resp;
    public ArrayList<String> clothid,clothtype;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_used_clothes);

        listView = findViewById(R.id.usedlist);

        String email = GlobalVar.email_id;
        /*Bundle bundle = getIntent().getExtras();
        String email = bundle.getString("emailid");*/

        filladapter(email);
    }

    public void filladapter(String email)
    {
        final String emailid = email;
        final Connection conn = new Connection();
        if (conn.checkNetworkAvailable(UsedClothActivity.this)) {

            progressDialog = new ProgressDialog(this);
            progressDialog.show();
            Thread tthread = new Thread() {
                @Override
                public void run() {
                    try
                    {
                        if (conn.getusedlist(emailid))
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
                    clothid = UsedList.getClothid();
                    clothtype = UsedList.getClothtype();
                    if(clothid.isEmpty())
                    {
                        Toast.makeText(UsedClothActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        DataAdapterUsed da = new DataAdapterUsed(UsedClothActivity.this, clothid, clothtype);
                        listView.setAdapter(da);
                    }

                    break;
                case 1:
                    Toast.makeText(UsedClothActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}
