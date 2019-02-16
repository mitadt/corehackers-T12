package com.example.chintan.smartwardrobe;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity
{
    Button btnLogin, btnRegister;
    EditText editText1,editText2;
    ProgressDialog progressDialog;
    int resp;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editText1=findViewById(R.id.editemail);
        editText2=findViewById(R.id.editpassword);

        btnLogin = findViewById(R.id.btnlogin);
        btnRegister = findViewById(R.id.btnregistration);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inregis=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(inregis);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    public void login()
    {
        if (editText1.getText().toString().equals("") || editText2.getText().toString().equals(""))
        {
            android.app.AlertDialog alert = new android.app.AlertDialog.Builder(LoginActivity.this).create();
            alert.setTitle("Enter All Details");
            alert.setMessage("All Fields Are Mandatory");
            alert.show();
        }
        else
        {
            final String email = editText1.getText().toString();
            GlobalVar.email_id = email;
            /*Bundle bundle = new Bundle();
            bundle.putString("emailid", email);*/

            final String pass = editText2.getText().toString();
            final Connection conn = new Connection();
            if (conn.checkNetworkAvailable(LoginActivity.this)) {
                progressDialog = new ProgressDialog(LoginActivity.this);

                progressDialog.show();
                Thread th1 = new Thread() {
                    @Override
                    public void run() {
                        try
                        {
                            int i = conn.authUser(email,pass);
                            resp = i;
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        hd.sendEmptyMessage(0);

                    }
                };
                th1.start();
            } else {
                Toast.makeText(LoginActivity.this, "Sorry no network access.", Toast.LENGTH_LONG).show();
            }
        }
    }

    public Handler hd = new Handler() {
        public void handleMessage(Message msg) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            switch (resp) {
                case 1:
                    editText1.setText("");
                    editText2.setText("");

                    Toast.makeText(getApplicationContext(), "Login Successfully", Toast.LENGTH_SHORT).show();
                    Intent menu=new Intent(LoginActivity.this,MenuActivity.class);

                    /*Bundle bundle = new Bundle();
                    bundle.putString("email", editText1.getText().toString());
                    menu.putExtras(bundle);*/

                    startActivity(menu);
                    break;

                case 2:
                    editText1.setText("");
                    editText2.setText("");
                    Toast.makeText(getApplicationContext(), "Invalid Mail or Password", Toast.LENGTH_LONG).show();
                    break;
                case 0:
                    Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };
}