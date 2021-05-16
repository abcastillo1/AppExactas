package com.example.appvinculacion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuRegistros extends AppCompatActivity implements View.OnClickListener{

    private Button button1,button2,button3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_registros);
        init();
    }

    private void init(){

        button1=(Button) findViewById(R.id.button1);
        button2=(Button) findViewById(R.id.button2);
        button3=(Button) findViewById(R.id.button3);



        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        Intent i ;

        switch (v.getId()){
            case R.id.button1: i=new Intent(this,MainActivity.class);startActivity(i);break;
            case R.id.button2: i=new Intent(this,MainActivity1.class);startActivity(i);break;
            case R.id.button3: i=new Intent(this,MainActivity2.class);startActivity(i);break;

            default:break;
        }
    }
}