package com.example.appvinculacion;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MenuEncuesta extends AppCompatActivity implements View.OnClickListener{

    private Button encuesta1,encuesta2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_encuesta);
        init();
    }
    private void init(){
        encuesta1=(Button) findViewById(R.id.encuesta1);
        encuesta2=(Button) findViewById(R.id.encuesta2);

        encuesta1.setOnClickListener(this);
        encuesta2.setOnClickListener(this);
    }


    public void onClick(View v) {
        Intent i ;

        switch (v.getId()){
            case R.id.encuesta1: i=new Intent(this,MiEncuesta.class);startActivity(i);break;
            case R.id.encuesta2: Toast.makeText(MenuEncuesta.this, "No está disponible", Toast.LENGTH_LONG).show();;break;

            default:break;
        }

    }
}