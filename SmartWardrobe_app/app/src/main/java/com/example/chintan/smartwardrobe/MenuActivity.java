package com.example.chintan.smartwardrobe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class MenuActivity extends AppCompatActivity
{
    ImageButton imgbtnAdd,imgbtnUsed,imgbtnUnused,imgbtnAnalyze,imgbtnLogout;

    /*Bundle bundle = getIntent().getExtras();
    String mail = bundle.getString("email");*/

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        imgbtnAdd = findViewById(R.id.imgbtnAdd);
        imgbtnUsed = findViewById(R.id.imgbtnUsed);
        imgbtnUnused = findViewById(R.id.imgbtnUnused);
        imgbtnAnalyze = findViewById(R.id.imgbtnAnalyze);
        imgbtnLogout = findViewById(R.id.imgbtnLogout);

        imgbtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent inlogin = new Intent(MenuActivity.this,LoginActivity.class);
                startActivity(inlogin);
            }
        });

        imgbtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent addclothes =new Intent(MenuActivity.this,AddClothesActivity.class);
                startActivity(addclothes);
            }
        });

        imgbtnUsed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent usedclothes =new Intent(MenuActivity.this,UsedClothActivity.class);
                startActivity(usedclothes);
            }
        });

        imgbtnUnused.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent unusedclothes =new Intent(MenuActivity.this,UnusedClothActivity.class);
                startActivity(unusedclothes);
            }
        });

        imgbtnAnalyze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent unusedclothes =new Intent(MenuActivity.this,AnalysisActivity.class);
                startActivity(unusedclothes);
            }
        });
    }
}
