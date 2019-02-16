package com.example.chintan.smartwardrobe;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    Button btnRegister;
    EditText editName, editAge, editEmail, editPass;
    RadioButton rbMale, rbFemale;

    //public static String selgender = "";
    ProgressDialog dg;
    int resp;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnRegister = findViewById(R.id.btnregister);
        editName = findViewById(R.id.editname);
        editAge = findViewById(R.id.editage);
        editEmail = findViewById(R.id.editemail);
        editPass = findViewById(R.id.editpassword);
        rbMale = findViewById(R.id.rbmale);
        rbFemale = findViewById(R.id.rbfemale);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                validatesubmit();
            }
        });
    }

    public void validatesubmit()
    {
        final String name = editName.getText().toString().trim(),
                age = editAge.getText().toString().trim(),
                email = editEmail.getText().toString().trim(),
                password = editPass.getText().toString().trim();

        final EditText[] Alledit = {editName, editAge, editEmail, editPass};
        for (EditText edit : Alledit)
        {
            if (edit.getText().toString().trim().length() == 0)
            {
                edit.setError("Empty Field");
                edit.requestFocus();
            }
        }

        if (!isValidEmail(email))
        {
            editEmail.setError("Invalid Mail Id");
        }
        else if (!isValidUname(name))
        {
            editName.setError("Invalid Name");
        }
        else if (password.length() < 4)
        {
            editPass.setError("Password Length must be atleast 4");
        }
        else
            { if (rbMale.isChecked())
            {
                Userdata.setName(name);
                Userdata.setEmail(email);
                Userdata.setAge(age);
                Userdata.setPassword(password);
                Userdata.setGender("Male");
                register();
            }
            else
                {
                Userdata.setName(name);
                Userdata.setEmail(email);
                Userdata.setAge(age);
                Userdata.setPassword(password);
                Userdata.setGender("Female");
                register();
            }
        }
    }

    public void register()
    {
        final Connection conn = new Connection();
        if (Connection.checkNetworkAvailable(RegisterActivity.this))
        {
            dg = new ProgressDialog(RegisterActivity.this);
            dg.setMessage("Processing ....");
            dg.show();

            Thread tthread = new Thread() {
                @Override
                public void run() {
                    try
                    {
                        resp = conn.register();
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
            Toast.makeText(RegisterActivity.this,"Sorry no network access.", Toast.LENGTH_LONG).show();
        }
    }

    public Handler hd = new Handler() {
        public void handleMessage(Message msg)
        {
            if (dg.isShowing())
                dg.dismiss();

            switch (resp)
            {
                case 1:
                    Toast.makeText(getApplicationContext(), "Register Successfully", Toast.LENGTH_LONG).show();
                    finish();
                    break;

                case 2:
                    Toast.makeText(getApplicationContext(), "Email Id already exists", Toast.LENGTH_LONG).show();
                    break;

                case 3:
                    Toast.makeText(getApplicationContext(), "Try Later", Toast.LENGTH_LONG).show();
                    break;

                case 0:
                    Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    private boolean isValidEmail(String email)
    {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isValidUname(String name)
    {
        String N_Pattern = "^([A-Za-z\\+]+[A-Za-z0-9]{1,10})$";
        Pattern pattern = Pattern.compile(N_Pattern);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

    /*private boolean isValidAge(String age)
    {
        String EMAIL_PATTERN = "[0-9]";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(age);
        return matcher.matches();
    } */
}