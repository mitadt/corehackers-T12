package com.example.chintan.smartwardrobe;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class AnalysisActivity extends AppCompatActivity
{
    Spinner spinnerFilter;
    Button btnSubmit;
    String[] filter = {"Type", "Weather", "Occasion"};
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);

        spinnerFilter = findViewById(R.id.spFilter);
        btnSubmit = findViewById(R.id.btnSubmit);

        ArrayAdapter aa_filter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,filter);
        aa_filter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerFilter.setAdapter(aa_filter);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitdata();
            }
        });
    }

    public void submitdata()
    {

    }
}

