package com.example.appvinculacion;

import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText et_main_usuario,et_main_contrasena;
    private Button btn_main_registrarse,btn_main_nuevo;

    private UsuarioAdapter adapter;
    private UsuarioModel model;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        init();
        validarSesion();

        btn_main_registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usuario = et_main_usuario.getText().toString();
                String contrasena=et_main_contrasena.getText().toString();
                boolean validarInterfaz = validarCampos(usuario,contrasena);

                if(validarInterfaz){
                    adapter.openRead();
                    model=adapter.login(usuario, contrasena);
                    adapter.close();
                    //Cuando es diferente de nullo se accede pero ya no se crea otro usuario
                    if(model != null){
                        Toast.makeText(LoginActivity.this, "Iniciando sesión", Toast.LENGTH_LONG).show();
                        SharedPreferences.Editor editor= preferences.edit();
                        editor.putString("usuario_nombre",model.get_nombre());
                        editor.putString("usuario_codigo",model.get_contrasena());
                        editor.commit();

                        irMenu();
                    }else {
                        Toast.makeText(LoginActivity.this, "usuario no encontrado", Toast.LENGTH_LONG).show();

                    }

                }
            }
        });
        btn_main_nuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent abrirRegistro = new Intent(LoginActivity.this,RegistroUsuario.class);
                startActivity(abrirRegistro);
            }
        });

    }

    public void init(){
        et_main_usuario=findViewById(R.id.et_main_usuario);
        et_main_contrasena=findViewById(R.id.et_main_contrasena);
        btn_main_registrarse=findViewById(R.id.btn_main_registrarse);
        btn_main_nuevo=findViewById(R.id.btn_main_nuevo);
        adapter=new UsuarioAdapter(getApplicationContext());
        model=new UsuarioModel();
        preferences=getSharedPreferences("Preferences",MODE_PRIVATE);
    }

    private void validarSesion(){
        String usuario_nombre=preferences.getString("usuario_nombre", null);
        String usuario_codigo=preferences.getString("usuario_codigo", null);

        if(usuario_codigo!=null && usuario_nombre!=null){
            irMenu();
        }

    }
    private void irMenu(){
        Intent principal = new Intent(this, Menu.class);
        principal.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(principal);
    }


    public boolean validarCampos(String usuario,String contrasena){
        if (usuario.isEmpty()||contrasena.isEmpty()){
            Toast.makeText(this,"Por favor ingrese su nombre y código",Toast.LENGTH_LONG).show();
            return false;
        }else if (usuario.length()<6||contrasena.length()<2){
            Toast.makeText(this,"Por favor ingrese su nombre y código válidos",Toast.LENGTH_LONG).show();
            return false;
        }else{
            return true;
        }
    }


}