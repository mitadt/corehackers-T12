package com.example.chintan.smartwardrobe;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

public class AddClothesActivity extends AppCompatActivity
{
    Button btnSubmit;
    Spinner spinnerType, spinnerWeather, spinnerOccasion, spinnerRfid, spinnerColor;
    public static ImageButton imgBtn;
    String[] clothtype = {"Shirt","Pant","T-shirt","Top","Skirt"};
    String[] weather = {"Summer", "Winter", "Rainy"};
    String[] occasion = {"Festive","Party","Formal","Casual"};
    String[] rfid = {"1234567890123456","5894562359741365","1695746236984652","658954125896745","3654189657413654","9517538526547453","5467891239547859"};
    String[] color = {"Black", "White", "Red", "Green", "Blue"};

    ProgressDialog dg;
    int resp;
    static byte[] readbytes;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_clothes);

        initScreenComponents();

        spinnerType = findViewById(R.id.spType);
        spinnerWeather = findViewById(R.id.spWeather);
        spinnerOccasion = findViewById(R.id.spOccasion);
        spinnerRfid = findViewById(R.id.spRfid);
        spinnerColor = findViewById((R.id.spColor));

        ArrayAdapter aa_type = new ArrayAdapter(this,android.R.layout.simple_spinner_item,clothtype);
        aa_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter aa_weather = new ArrayAdapter(this,android.R.layout.simple_spinner_item,weather);
        aa_weather.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter aa_occasion = new ArrayAdapter(this,android.R.layout.simple_spinner_item,occasion);
        aa_occasion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter aa_rfid = new ArrayAdapter(this,android.R.layout.simple_spinner_item,rfid);
        aa_occasion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter aa_color = new ArrayAdapter(this,android.R.layout.simple_spinner_item,color);
        aa_occasion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerType.setAdapter(aa_type);
        spinnerWeather.setAdapter(aa_weather);
        spinnerOccasion.setAdapter(aa_occasion);
        spinnerRfid.setAdapter(aa_rfid);
        spinnerColor.setAdapter(aa_color);
    }

    private void initScreenComponents()
    {
        try
        {
            imgBtn = findViewById(R.id.imgbtnCamera);
            imgBtn.setBackgroundColor(Color.TRANSPARENT);
            imgBtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v)
                {
                    Intent i = new Intent(AddClothesActivity.this, CameraPreview.class);
                    startActivity(i);
                }
            });

            btnSubmit = findViewById(R.id.btnAdd);
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    submitdata();
                }
            });
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.d("ERROR:", e.getLocalizedMessage());
        }
    }

    public void submitdata()
    {
        Clothesdata.set_clothtype(spinnerType.getSelectedItem().toString().trim());
        Clothesdata.set_weather(spinnerWeather.getSelectedItem().toString().trim());
        Clothesdata.set_occasion(spinnerOccasion.getSelectedItem().toString().trim());
        Clothesdata.set_rfid(spinnerRfid.getSelectedItem().toString().trim());
        Clothesdata.setColor(spinnerColor.getSelectedItem().toString().trim());

        try
        {
            Clothesdata updata = new Clothesdata();

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.login);

            File fin = new File(CameraPreview._path);
            FileInputStream is = new FileInputStream(fin);
            BufferedInputStream bis = new BufferedInputStream(is);
            readbytes = new byte[bis.available()];
            bis.read(readbytes);

            updata.setImage(readbytes);
            finalsubmit(updata);
        }
        catch(Exception e)
        {
            e.getStackTrace();
        }
    }

    public void finalsubmit(final Clothesdata imgdata)
    {
        final Connection conn = new Connection();
        if (Connection.checkNetworkAvailable(AddClothesActivity.this)) {
            dg = new ProgressDialog(AddClothesActivity.this);
            dg.setMessage("Processing ....");
            dg.show();

            Thread tthread = new Thread() {
                @Override
                public void run() {
                    try {
                        resp = conn.addclothesdata(imgdata);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    hd.sendEmptyMessage(0);
                }
            };
            tthread.start();
        }
        else {
            Toast.makeText(AddClothesActivity.this,"Sorry no network access.", Toast.LENGTH_LONG).show();
        }
    }

    public Handler hd = new Handler() {
        public void handleMessage(Message msg) {

            if (dg.isShowing())
                dg.dismiss();

            switch (resp) {
                case 1:
                    Toast.makeText(getApplicationContext(), "Data Saved Successfully", Toast.LENGTH_LONG).show();
                    finish();
                    Intent intent=new Intent(AddClothesActivity.this,MenuActivity.class);
                    startActivity(intent);
                    break;

                case 2:
                    Toast.makeText(getApplicationContext(), "Data already exists", Toast.LENGTH_LONG).show();
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

}

