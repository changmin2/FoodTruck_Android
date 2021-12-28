package org.techtown.navagation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Dataplace extends AppCompatActivity {
    Data data = (Data) getApplication();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dataplace);

    }
    public void setData(Double longitude){

        data.setLongitude(123.22222222);
    }
    public Double getData(){
        return data.getLongitude();
    }
}