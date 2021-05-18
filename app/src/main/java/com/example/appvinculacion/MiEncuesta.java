package com.example.appvinculacion;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MiEncuesta extends AppCompatActivity implements View.OnClickListener{
    private Button seccion2;

    private View texto_info19;

    private LinearLayout textPregunta11,textPregunta13,textPregunta15,textPregunta17,textPregunta19,textPregunta20,textPregunta22,textPregunta23,textPregunta24,
            textPregunta25,textPregunta26,textPregunta27,textPregunta30,textPregunta32,textPregunta33,textPregunta34,textPregunta35,textPregunta36,textPregunta37;


    private CheckBox check_casa,check_departamento,check_cuarto,check_otros;//p1
    private EditText campo_otros;

    private CheckBox check_uno,check_dos,check_tres,check_cuatro;//p2

    private CheckBox check_cinc,check_teja,check_loseta,check_loza;//p3

    private CheckBox check_madera,check_bloque,check_hormigon,check_loza1;//p4

    private CheckBox check_madera1,check_hormigon1,check_porcelanato,check_tierra;//p5

    //DATOS DEL ENCUESTADO
    private EditText encuestado_nombre,encuestado_direccion,encuestado_edad;
    private CheckBox check_hombre,check_mujer;

    //LA VIVIENDA QUE HABITA ES
    private CheckBox check_propia,check_rentada,check_prestada,check_otros1;
    private TextView campo_otros1;

    //¿CUÁNTAS PERSONAS HABITAN LA VIVIENDA?
    private EditText encuestado_vivienda;

    //¿ALGÚN INTEGRANTE DE LA FAMILIA HA TENIDO PROBLEMAS ESTOMACALES O INTESTINALES EN ESTE AÑO?
    private CheckBox check_si,check_no;

    //¿TUVO TRATAMIENTO MÉDICO?
    private CheckBox check_si1,check_no1;

    //¿CUÁL FUE EL DIAGNÓSTICO?
    private CheckBox check_diarrea,check_gastroenteritis,check_amebiasis,check_echericha,check_colera,check_otras;
    private EditText texto_otros;

    //¿ALGÚN INTEGRANTE DE LA FAMILIA TENIDO ENFERMEDADES DE LA PIEL?
    private CheckBox check_si2,check_no2;

    //¿TUVO TRATAMIENTO MÉDICO?
    private CheckBox check_si3,check_no3;

    //¿CUÁL FUE EL DIAGNÓSTICO?
    private CheckBox  check_hongos,check_escabiosis,check_alergias,check_otra;
    private EditText  texto_diagnostico1;

    //¿DE DONDE SE ABASTECE PARA EL AGUA DE CONSUMO?
    private CheckBox check_potable,check_pozo,check_rio,check_lluvia,check_tanquero,check_embotellada,check_otros2;
    private EditText texto_nombreRio,campo_otros2;

    //¿TIENE CISTERNA O TANQUE ELEVADO?
    private CheckBox check_cisterna,check_tanque,check_ninguno;

    //CAPACIDAD DE LA CISTERNA (metros^3)
    private CheckBox check_1,check_2,check_3,check_4,check_5,check_6,check_7;

    //CAPACIDAD DEL TANQUE ELEVADO (Litros)
    private CheckBox check_250,check_500,check_600,check_1000,check_1100,check_2000,check_2500;

    //¿CON QUÉ FRECUENCIA REALIZA LA LIMPIEZA O LAVADO?
    private CheckBox check_semanal,check_quincenal,check_mensual,check_trimestral,check_semestral,check_anual,check_bienal,check_nunca;

    //INDICAR SI REALIZA CLORACIÓN AL AGUA DE ESTOS ALMACENAMIENTOS
    private CheckBox check_si4,check_no4;

    //¿CON QUÉ FRECUENCIA CLORA EL TANQUE?
    private CheckBox check_semanal1,check_quincenal1,check_mensual1,check_bimensual1,check_trimestral1,check_cuatrimestral1,check_semestral1,check_anual1,check_otras1;
    private EditText texto_otros1;

    //¿CUAL ES LA DOSIFICACIÓN UTILIZADA?
    private CheckBox check_op1,check_op2,check_op3,check_op4,check_op5,check_op6,check_op7,check_op8,check_otras2;
    private EditText texto_otros2;

    //EL AGUA QUE USTED UTILIZA PARA BEBER ES:
    private CheckBox check_llave,check_hervida,check_filtrada,check_embotellada1;

    //REALIZA ALGÚN TIPO DE TRATAMIENTO AL AGUA QUE UTLIZA PARA BEBER
    private CheckBox check_si5,check_no5;

    //INDICAR QUE TRATAMIENTO
    private EditText tratamiento;

    //¿QUÉ USO LE DA AL AGUA?
    private CheckBox check_cocina,check_aseo,check_lavado,check_animales,check_riego;

    //EN EL CASO DE TENER ANIMALES QUE CONSUMEN AGUA INDICAR:
    private CheckBox check_animal_mascota,check_animal_consumo,check_animal_venta;

    //INDICAR ANIMALES PARA CONSUMO
    private EditText text_animal_consumo;

     //INDICAR ANIMALES PARA VENTA
    private EditText text_animal_venta;

    //EN EL CASO DE TENER SEMBRÍOS QUE CONSUMEN AGUA INDICAR
    private CheckBox check_ornamentales,check_sembrio_consumo,check_sembrio_venta;

    //INDICAR SEMBRÍOS PARA CONSUMO
    private EditText text_sembrios_consumo;

    //INDICAR SEMBRÍOS PARA VENTA
    private EditText text_sembrios_venta;




    private ImageView profileIv;
    // constantes de permisos
    private static final int CAMERA_REQUEST_CODE=100;
    private static final int STORAGE_REQUEST_CODE=101;
    // constantes de selección de imágenes
    private static final int IMAGE_PICK_CAMERA_CODE=102;
    private static final int IMAGE_PICK_GALLERY_CODE=103;
    // matrices de permisos
    private String[] cameraPermissions;//camara y almacenamiento
    private String[] storagePermissions;//solo almacenamiento
    //variables(contendrá datos para guardar)
    private Uri imageUri;
    private String nombre,codigo;
    //db helper
    //private MyDbHelper dbHelper;


    private TextView TituloFecha,HoraInicio, latitud,longitud;
    private TextView direccion;

    private SharedPreferences preferences;

    private UsuarioAdapter db;
    private Button buttonSave;
    private ListView listViewNames;
    private List<Name1> names;
    private ListView listViewNames3;
    private List<Name3> names3;


    private BroadcastReceiver broadcastReceiver;
    public static final int NAME_SYNCED_WITH_SERVER = 1;
    public static final int NAME_NOT_SYNCED_WITH_SERVER = 0;
    private NameAdapter1 nameAdapter;
    private NameAdapter3 nameAdapter3;

    public static final String URL_SAVE_NAME = "http://192.168.1.7/sincronizar/encuesta1.php";
    public static final String URL_SAVE_NAME3 = "http://192.168.1.7/sincronizar/persona.php";

    public static final String DATA_SAVED_BROADCAST = "net.simplifiedcoding.datasaved";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_encuesta);
        //matrices de permisos de inicio
        cameraPermissions=new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions=new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        registerReceiver(new NetworkStateChecker1(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        registerReceiver(new NetworkStateChecker3(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        db = new UsuarioAdapter(this);
        names = new ArrayList<>();
        names3 = new ArrayList<>();


        init();



        fechayhora();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        } else {
            locationStart();
        }
        seccion2.setOnClickListener(this);
        profileIv.setOnClickListener(this);
        //init db Helper
        //dbHelper=new MyDbHelper(this);
        loadNames();
        loadNames3();
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //loading the names again
                loadNames();
                loadNames3();
            }
        };
        //registering the broadcast receiver to update sync status
        registerReceiver(broadcastReceiver, new IntentFilter(DATA_SAVED_BROADCAST));










    }

    private void imagePickDialog(){
        // opción para mostrar en el diálogo
        String[] options = {"Camara","Galería"};
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Elegir imagen de");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which==0){
                    if(!checkCameraPermissions()){
                        requestCameraPermission();
                    }else{
                        // permiso ya concedido
                        pickFromCamera();
                    }
                }
                else if(which==1){
                    if(!checkCameraPermissions()){
                        requestCameraPermission();
                    }else{
                        // permiso ya concedido
                        pickFromGallery();
                    }
                }
            }
        });
        //create/show dialog
        builder.create().show();
    }

    private void pickFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);

    }

    private void pickFromCamera() {
        ContentValues values=new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Título de la imagen");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Descripción de la imagen");

        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        // intento de abrir la cámara para la imagen
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);
    }

    private boolean checkStoragePermission() {
        //comprobar si el permiso de almacenamiento está habilitado o no
        boolean result = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission(){
        //solicitar el permiso de almacenamiento
        ActivityCompat.requestPermissions(this,storagePermissions,STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermissions(){
        //comprobar si el permiso de cámara está habilitado o no
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void requestCameraPermission(){
        //solicitar el permiso de cámara
        ActivityCompat.requestPermissions(this,cameraPermissions,CAMERA_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //image picked from camera or gallery will be recieved
        if(resultCode==RESULT_OK){
            //image is picked
            if(requestCode==IMAGE_PICK_GALLERY_CODE){
                //picked from gallery

                //crop image
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(this);
            }
            else if(requestCode==IMAGE_PICK_CAMERA_CODE){
                //picked from camera

                //crop image
                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(this);
            }
            else if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
                //croped image received
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if(resultCode==RESULT_OK){
                    Uri resultUri=result.getUri();
                    imageUri = resultUri;
                    //set Image
                    profileIv.setImageURI(resultUri);
                }
                else if (resultCode==CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                    //error
                    Exception error = result.getError();
                    Toast.makeText(this,""+error, Toast.LENGTH_SHORT).show();

                }

            }


        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void init(){

        check_casa = (CheckBox) findViewById(R.id.check_casa);
        check_departamento = (CheckBox) findViewById(R.id.check_departamento);
        check_cuarto = (CheckBox) findViewById(R.id.check_cuarto);
        check_otros = (CheckBox) findViewById(R.id.check_otros);
        campo_otros = findViewById(R.id.campo_otros);

        check_uno=(CheckBox) findViewById(R.id.check_uno);
        check_dos=(CheckBox) findViewById(R.id.check_dos);
        check_tres=(CheckBox) findViewById(R.id.check_tres);
        check_cuatro=(CheckBox) findViewById(R.id.check_cuatro);

        check_cinc=(CheckBox) findViewById(R.id.check_cinc);
        check_teja=(CheckBox) findViewById(R.id.check_teja);
        check_loseta=(CheckBox) findViewById(R.id.check_loseta);
        check_loza=(CheckBox) findViewById(R.id.check_loza);

        check_madera=(CheckBox) findViewById(R.id.check_madera);
        check_bloque=(CheckBox) findViewById(R.id.check_bloque);
        check_hormigon=(CheckBox) findViewById(R.id.check_hormigon);
        check_loza1=(CheckBox) findViewById(R.id.check_loza1);

        check_madera1=(CheckBox) findViewById(R.id.check_madera1);
        check_hormigon1=(CheckBox) findViewById(R.id.check_hormigon1);
        check_porcelanato=(CheckBox) findViewById(R.id.check_porcelanato);
        check_tierra=(CheckBox) findViewById(R.id.check_tierra);

        encuestado_nombre=(EditText) findViewById(R.id.encuestado_nombre);
        encuestado_direccion=(EditText) findViewById(R.id.encuestado_direccion);
        encuestado_edad=(EditText) findViewById(R.id.encuestado_edad);
        check_hombre=(CheckBox) findViewById(R.id.check_hombre);
        check_mujer=(CheckBox) findViewById(R.id.check_mujer);

        check_propia=(CheckBox) findViewById(R.id.check_propia);
        check_rentada=(CheckBox) findViewById(R.id.check_rentada);
        check_prestada=(CheckBox) findViewById(R.id.check_prestada);
        check_otros1=(CheckBox) findViewById(R.id.check_otros1);
        campo_otros1=(EditText) findViewById(R.id.campo_otros1);

        encuestado_vivienda=(EditText) findViewById(R.id.encuestado_vivienda);

        check_si=(CheckBox) findViewById(R.id.check_si);
        check_no=(CheckBox) findViewById(R.id.check_no);
        textPregunta11 = findViewById(R.id.textPregunta11);

        check_si1=(CheckBox) findViewById(R.id.check_si1);
        check_no1=(CheckBox) findViewById(R.id.check_no1);
        textPregunta13 = findViewById(R.id.textPregunta13);

        check_diarrea=(CheckBox) findViewById(R.id.check_diarrea);
        check_gastroenteritis=(CheckBox) findViewById(R.id.check_gastroenteritis);
        check_amebiasis=(CheckBox) findViewById(R.id.check_amebiasis);
        check_echericha=(CheckBox) findViewById(R.id.check_echericha);
        check_colera=(CheckBox) findViewById(R.id.check_colera);
        check_otras= (CheckBox) findViewById(R.id.check_otras);
        texto_otros= (EditText)findViewById(R.id.texto_otros);


        check_si2 = (CheckBox) findViewById(R.id.check_si2);
        check_no2= (CheckBox) findViewById(R.id.check_no2);
        textPregunta15 = findViewById(R.id.textPregunta15);

        check_si3 = (CheckBox) findViewById(R.id.check_si3);
        check_no3 = (CheckBox) findViewById(R.id.check_no3);
        textPregunta17 = findViewById(R.id.textPregunta17);

        check_hongos= (CheckBox) findViewById(R.id.check_hongos);
        check_escabiosis= (CheckBox) findViewById(R.id.check_escabiosis);
        check_alergias= (CheckBox) findViewById(R.id.check_alergias);
        check_otra= (CheckBox) findViewById(R.id.check_otra);

        check_potable=(CheckBox) findViewById(R.id.check_potable);
        check_pozo=(CheckBox) findViewById(R.id.check_pozo);
        check_rio=(CheckBox) findViewById(R.id.check_rio);
        check_lluvia=(CheckBox) findViewById(R.id.check_lluvia);
        check_tanquero=(CheckBox) findViewById(R.id.check_tanquero);
        check_embotellada=(CheckBox) findViewById(R.id.check_embotellada);
        check_otros2=(CheckBox) findViewById(R.id.check_otros2);
        textPregunta19 = findViewById(R.id.textPregunta19);
        textPregunta20 = findViewById(R.id.textPregunta20);
        texto_nombreRio=(EditText)findViewById(R.id.texto_nombreRio);
        campo_otros2=(EditText)findViewById(R.id.campo_otros2);

        check_cisterna = (CheckBox) findViewById(R.id.check_cisterna);
        textPregunta22 = findViewById(R.id.textPregunta22);
        check_tanque = (CheckBox) findViewById(R.id.check_tanque);
        textPregunta23 = findViewById(R.id.textPregunta23);
        check_ninguno= (CheckBox) findViewById(R.id.check_ninguno);

        check_1=(CheckBox) findViewById(R.id.check_1);
        check_2=(CheckBox) findViewById(R.id.check_2);
        check_3=(CheckBox) findViewById(R.id.check_3);
        check_4=(CheckBox) findViewById(R.id.check_4);
        check_5=(CheckBox) findViewById(R.id.check_5);
        check_6=(CheckBox) findViewById(R.id.check_6);
        check_7=(CheckBox) findViewById(R.id.check_7);

        check_250=(CheckBox) findViewById(R.id.check_250);
        check_500=(CheckBox) findViewById(R.id.check_500);
        check_600=(CheckBox) findViewById(R.id.check_600);
        check_1000=(CheckBox) findViewById(R.id.check_1000);
        check_1100=(CheckBox) findViewById(R.id.check_1100);
        check_2000=(CheckBox) findViewById(R.id.check_2000);
        check_2500=(CheckBox) findViewById(R.id.check_2500);

        check_semanal=(CheckBox) findViewById(R.id.check_semanal);
        check_quincenal=(CheckBox) findViewById(R.id.check_quincenal);
        check_mensual=(CheckBox) findViewById(R.id.check_mensual);
        check_trimestral=(CheckBox) findViewById(R.id.check_trimestral);
        check_semestral=(CheckBox) findViewById(R.id.check_semestral);
        check_anual=(CheckBox) findViewById(R.id.check_anual);
        check_bienal=(CheckBox) findViewById(R.id.check_bienal);
        check_nunca=(CheckBox) findViewById(R.id.check_nunca);

        check_si4=(CheckBox) findViewById(R.id.check_si4);
        textPregunta26=findViewById(R.id.textPregunta26);
        textPregunta27=findViewById(R.id.textPregunta27);
        check_no4=(CheckBox) findViewById(R.id.check_no4);


        check_semanal1=(CheckBox) findViewById(R.id.check_semanal1);
        check_quincenal1=(CheckBox) findViewById(R.id.check_quincenal1);
        check_mensual1=(CheckBox) findViewById(R.id.check_mensual1);
        check_bimensual1=(CheckBox) findViewById(R.id.check_bimensual1);
        check_trimestral1=(CheckBox) findViewById(R.id.check_trimestral1);
        check_cuatrimestral1=(CheckBox) findViewById(R.id.check_cuatrimestral1);
        check_semestral1=(CheckBox) findViewById(R.id.check_semestral1);
        check_anual1=(CheckBox) findViewById(R.id.check_anual1);
        check_otras1=(CheckBox) findViewById(R.id.check_otras1);
        texto_otros1= findViewById(R.id.texto_otros1);

        check_op1=(CheckBox) findViewById(R.id.check_op1);
        check_op2=(CheckBox) findViewById(R.id.check_op2);
        check_op3=(CheckBox) findViewById(R.id.check_op3);
        check_op4=(CheckBox) findViewById(R.id.check_op4);
        check_op5=(CheckBox) findViewById(R.id.check_op5);
        check_op6=(CheckBox) findViewById(R.id.check_op6);
        check_op7=(CheckBox) findViewById(R.id.check_op7);
        check_op8=(CheckBox) findViewById(R.id.check_op8);
        check_otras2=(CheckBox) findViewById(R.id.check_otras2);
        texto_otros2= findViewById(R.id.texto_otros2);

        check_llave=(CheckBox) findViewById(R.id.check_llave);
        check_hervida=(CheckBox) findViewById(R.id.check_hervida);
        check_filtrada=(CheckBox) findViewById(R.id.check_filtrada);
        check_embotellada1=(CheckBox) findViewById(R.id.check_embotellada1);


        check_si5=(CheckBox) findViewById(R.id.check_si5);
        textPregunta30= findViewById(R.id.textPregunta30);
        check_no5=(CheckBox) findViewById(R.id.check_no5);


        tratamiento=(EditText)findViewById(R.id.tratamiento);

        //¿QUÉ USO LE DA AL AGUA?
        check_cocina=(CheckBox) findViewById(R.id.check_cocina);
        check_aseo=(CheckBox) findViewById(R.id.check_aseo);
        check_lavado=(CheckBox) findViewById(R.id.check_lavado);
        check_animales=(CheckBox) findViewById(R.id.check_animales);
        check_riego=(CheckBox) findViewById(R.id.check_riego);
        textPregunta32= findViewById(R.id.textPregunta32);
        textPregunta35 = findViewById(R.id.textPregunta35);


        check_animal_mascota=(CheckBox) findViewById(R.id.check_animal_mascota);
        check_animal_consumo=(CheckBox) findViewById(R.id.check_animal_consumo);
        textPregunta33= findViewById(R.id.textPregunta33);
        check_animal_venta=(CheckBox) findViewById(R.id.check_animal_venta);
        textPregunta34 = findViewById(R.id.textPregunta34);
        text_animal_consumo=(EditText) findViewById(R.id.text_animal_consumo);
        text_animal_venta=(EditText) findViewById(R.id.text_animal_venta);



        check_ornamentales=(CheckBox) findViewById(R.id.check_ornamentales);
        check_sembrio_consumo=(CheckBox) findViewById(R.id.check_sembrio_consumo);
        check_sembrio_venta=(CheckBox) findViewById(R.id.check_sembrio_venta);
        textPregunta36 = findViewById(R.id.textPregunta36);
        textPregunta37 = findViewById(R.id.textPregunta37);

        text_sembrios_consumo=(EditText) findViewById(R.id.text_sembrios_consumo);
        text_sembrios_venta=(EditText) findViewById(R.id.text_sembrios_venta);


        texto_diagnostico1=(EditText)findViewById(R.id.texto_diagnostico1);


        textPregunta24 = findViewById(R.id.textPregunta24);
        texto_info19= findViewById(R.id.texto_info19);
        textPregunta25= findViewById(R.id.textPregunta25);

        seccion2= (Button) findViewById(R.id.seccion2);


        profileIv=findViewById(R.id.profileIv);
        TituloFecha = (TextView) findViewById(R.id.TituloFecha);
        HoraInicio= (TextView) findViewById(R.id.HoraInicio);
        latitud = (TextView) findViewById(R.id.txtLatitud);
        longitud = (TextView) findViewById(R.id.txtLongitud);
        direccion = (TextView) findViewById(R.id.txtDireccion);


        //Si da en otros se mostrará una caja de texto
        check_otros.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                campo_otros.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });

        //Si da en otros se mostrará una caja de texto
        check_otros1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                campo_otros1.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });

        //Si da en "si" se mostrará una pregunta más
        check_si.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                textPregunta11.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });

        //Si da en "si" se mostrará una pregunta más
        check_si1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                textPregunta13.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });


        check_si2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                textPregunta15.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });

        check_si3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                textPregunta17.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });

        check_rio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                textPregunta19.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });

        check_otros2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                textPregunta20.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });

        check_cisterna.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                textPregunta22.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                textPregunta24.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                texto_info19.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                textPregunta25.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });

        check_tanque.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                textPregunta23.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                textPregunta24.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                texto_info19.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                textPregunta25.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });

        check_si4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                textPregunta26.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                textPregunta27.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });

        check_si5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                textPregunta30.setVisibility(isChecked ? View.VISIBLE : View.GONE);

            }
        });

        //En el caso de tener animales que consumen agua indicar:
        check_animales.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                textPregunta32.setVisibility(isChecked ? View.VISIBLE : View.GONE);

            }
        });

        //animales para consumo
        check_animal_consumo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                textPregunta33.setVisibility(isChecked ? View.VISIBLE : View.GONE);

            }
        });

        //animales para venta
        check_animal_venta.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                textPregunta34.setVisibility(isChecked ? View.VISIBLE : View.GONE);

            }
        });
        //En el caso de tener sembrios que consumen agua indicar:
        check_riego.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                textPregunta35.setVisibility(isChecked ? View.VISIBLE : View.GONE);

            }
        });
        //sembrios para consumo
        check_sembrio_consumo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                textPregunta36.setVisibility(isChecked ? View.VISIBLE : View.GONE);

            }
        });
        //sembrios para venta
        check_sembrio_venta.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                textPregunta37.setVisibility(isChecked ? View.VISIBLE : View.GONE);

            }
        });


        check_otras.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                texto_otros.setVisibility(isChecked ? View.VISIBLE : View.GONE);

            }
        });


        check_otras1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                texto_otros1.setVisibility(isChecked ? View.VISIBLE : View.GONE);

            }
        });

        check_otras2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                texto_otros2.setVisibility(isChecked ? View.VISIBLE : View.GONE);

            }
        });


    }



    public String pregunta1(){

        Boolean opc1= check_casa.isChecked();
        Boolean opc2= check_departamento.isChecked();
        Boolean opc3= check_cuarto.isChecked();
        Boolean opc4= check_otros.isChecked();

        int val1 = (opc1) ? 1 : 0;
        int val2 = (opc2) ? 1 : 0;
        int val3 = (opc3) ? 1 : 0;
        int val4 = (opc4) ? 1 : 0;

        if(val1==0&&val2==0&&val3==0&&val4==0){
            return "";
        }else{
            return val1+""+val2+""+val3+""+val4;
        }

    }
    public int pregunta2(){
        //check_uno,check_dos,check_tres,check_cuatro
        Boolean opc1= check_uno.isChecked();
        Boolean opc2= check_dos.isChecked();
        Boolean opc3= check_tres.isChecked();
        Boolean opc4= check_cuatro.isChecked();
        int val1=0;

        if(opc1){
            val1=1;
        }if(opc2){
            val1=2;
        }if(opc3){
            val1=3;
        }if(opc4){
            val1=4;
        }
        return val1;
    }
    public String pregunta3(){
        Boolean opc1= check_cinc.isChecked();
        Boolean opc2= check_teja.isChecked();
        Boolean opc3= check_loseta.isChecked();
        Boolean opc4= check_loza.isChecked();

        int val1 = (opc1) ? 1 : 0;
        int val2 = (opc2) ? 1 : 0;
        int val3 = (opc3) ? 1 : 0;
        int val4 = (opc4) ? 1 : 0;

        if(val1==0&&val2==0&&val3==0&&val4==0){
            return "";
        }else{
            return val1+""+val2+""+val3+""+val4;
        }


    }
    public String pregunta4(){
        Boolean opc1= check_madera.isChecked();
        Boolean opc2= check_bloque.isChecked();
        Boolean opc3= check_hormigon.isChecked();
        Boolean opc4= check_loza1.isChecked();

        int val1 = (opc1) ? 1 : 0;
        int val2 = (opc2) ? 1 : 0;
        int val3 = (opc3) ? 1 : 0;
        int val4 = (opc4) ? 1 : 0;

        if(val1==0&&val2==0&&val3==0&&val4==0){
            return "";
        }else{
            return val1+""+val2+""+val3+""+val4;
        }


    }
    public String pregunta5(){
        Boolean opc1= check_madera1.isChecked();
        Boolean opc2= check_hormigon1.isChecked();
        Boolean opc3= check_porcelanato.isChecked();
        Boolean opc4= check_tierra.isChecked();

        int val1 = (opc1) ? 1 : 0;
        int val2 = (opc2) ? 1 : 0;
        int val3 = (opc3) ? 1 : 0;
        int val4 = (opc4) ? 1 : 0;

        if(val1==0&&val2==0&&val3==0&&val4==0){
            return "";
        }else{
            return val1+""+val2+""+val3+""+val4;
        }


    }
    public String pregunta_nombre(){
        String nombreencuestado=encuestado_nombre.getText().toString().trim();
        return nombreencuestado;
    }
    public String pregunta_direccion(){
        String direncuestado=encuestado_direccion.getText().toString().trim();
        return direncuestado;
    }
    public String pregunta_edad(){
        String edadencuestado=encuestado_edad.getText().toString().trim();
        return edadencuestado;
    }
    public String pregunta_sexo(){
        Boolean opc1= check_hombre.isChecked();
        Boolean opc2= check_mujer.isChecked();

        String val1=null;

        if(opc1){
            val1="Hombre";
        }if(opc2){
            val1="Mujer";
        }
        return val1;
    }
    public String pregunta6(){

        Boolean opc1= check_propia.isChecked();
        Boolean opc2= check_rentada.isChecked();
        Boolean opc3= check_prestada.isChecked();
        Boolean opc4= check_otros1.isChecked();

        String val1=null;

        if(opc1){
            val1="Propia";
        }if(opc2){
            val1="Rentada";
        }if(opc3){
            val1="Prestada";
        }if(opc4){
            val1=campo_otros1.getText().toString().trim();
        }
        return val1;
    }
    public String pregunta7(){
        String numhabitantes=encuestado_vivienda.getText().toString().trim();
        return numhabitantes;
    }
    public int pregunta8(){

        Boolean opc1= check_si.isChecked();
        Boolean opc2= check_no.isChecked();

        int val1=0;

        if(opc1){
            val1=1;
        }if(opc2){
            val1=0;
        }
        return val1;
    }
    public String pregunta9(){
        //check_diarrea,check_gastroenteritis,check_amebiasis,check_echericha,check_colera,check_otras
        Boolean opc1= check_diarrea.isChecked();
        Boolean opc2= check_gastroenteritis.isChecked();
        Boolean opc3= check_amebiasis.isChecked();
        Boolean opc4= check_echericha.isChecked();
        Boolean opc5= check_colera.isChecked();
        Boolean opc6= check_otras.isChecked();

        int val1 = (opc1) ? 1 : 0;
        int val2 = (opc2) ? 1 : 0;
        int val3 = (opc3) ? 1 : 0;
        int val4 = (opc4) ? 1 : 0;
        int val5 = (opc5) ? 1 : 0;
        int val6 = (opc6) ? 1 : 0;

        return val1+""+val2+""+val3+""+val4+""+val5+""+val6;
    }
    public int pregunta10(){
        Boolean opc1= check_si2.isChecked();
        Boolean opc2= check_no2.isChecked();
        int val1=0;

        if(opc1){
            val1=1;
        }if(opc2){
            val1=0;
        }
        return val1;
    }
    public String pregunta11(){
        //check_hongos,check_escabiosis,check_alergias,check_otra
        Boolean opc1= check_hongos.isChecked();
        Boolean opc2= check_escabiosis.isChecked();
        Boolean opc3= check_alergias.isChecked();
        Boolean opc4= check_otra.isChecked();

        int val1 = (opc1) ? 1 : 0;
        int val2 = (opc2) ? 1 : 0;
        int val3 = (opc3) ? 1 : 0;
        int val4 = (opc4) ? 1 : 0;

        return val1+""+val2+""+val3+""+val4;
    }//¿Cuál fue el diagnóstico?
    public String pregunta12(){
        //check_potable,check_pozo,check_rio,check_lluvia,check_tanquero,check_embotellada,check_otros2
        Boolean opc1= check_potable.isChecked();
        Boolean opc2= check_pozo.isChecked();
        Boolean opc3= check_rio.isChecked();
        Boolean opc4= check_lluvia.isChecked();
        Boolean opc5= check_tanquero.isChecked();
        Boolean opc6= check_embotellada.isChecked();
        Boolean opc7= check_otros2.isChecked();

        int val1 = (opc1) ? 1 : 0;
        int val2 = (opc2) ? 1 : 0;
        int val3 = (opc3) ? 1 : 0;
        int val4 = (opc4) ? 1 : 0;
        int val5 = (opc5) ? 1 : 0;
        int val6 = (opc6) ? 1 : 0;
        int val7 = (opc7) ? 1 : 0;

        if(val1==0&&val2==0&&val3==0&&val4==0&&val5==0&&val6==0&&val7==0){
            return "";
        }else{
            return val1+""+val2+""+val3+""+val4+""+val5+""+val6+""+val7;
        }

    }//¿DE DONDE SE ABASTECE PARA EL AGUA DE CONSUMO?
    public String pregunta13(){
        //check_cisterna,check_tanque,check_ninguno
        Boolean opc1= check_cisterna.isChecked();
        Boolean opc2= check_tanque.isChecked();
        Boolean opc3= check_ninguno.isChecked();

        int val1 = (opc1) ? 1 : 0;
        int val2 = (opc2) ? 1 : 0;
        int val3 = (opc3) ? 1 : 0;

        if(val1==0&&val2==0&&val3==0){
            return "";
        }else{
            return val1+""+val2+""+val3;
        }

    }//¿TIENE CISTERNA O TANQUE ELEVADO?
    public int pregunta14(){

        Boolean opc1= check_1.isChecked();
        Boolean opc2= check_2.isChecked();
        Boolean opc3= check_3.isChecked();
        Boolean opc4= check_4.isChecked();
        Boolean opc5= check_5.isChecked();
        Boolean opc6= check_6.isChecked();
        Boolean opc7= check_7.isChecked();

        int val1=0;

        if(opc1){
            val1=1;
        }if(opc2){
            val1=2;
        }if(opc3){
            val1=3;
        }if(opc4){
            val1=4;
        }if(opc5){
            val1=5;
        }if(opc6){
            val1=6;
        }if(opc7){
            val1=7;
        }
        return val1;
    }//capacidad cirterna
    public int pregunta15(){

        Boolean opc1= check_250.isChecked();
        Boolean opc2= check_500.isChecked();
        Boolean opc3= check_600.isChecked();
        Boolean opc4= check_1000.isChecked();
        Boolean opc5= check_1100.isChecked();
        Boolean opc6= check_2000.isChecked();
        Boolean opc7= check_2500.isChecked();

        int val1=0;

        if(opc1){
            val1=250;
        }if(opc2){
            val1=500;
        }if(opc3){
            val1=600;
        }if(opc4){
            val1=1000;
        }if(opc5){
            val1=1100;
        }if(opc6){
            val1=2000;
        }if(opc7){
            val1=2500;
        }
        return val1;
    }//capacidad tanque
    public String pregunta16(){
        Boolean opc1= check_semanal.isChecked();
        Boolean opc2= check_quincenal.isChecked();
        Boolean opc3= check_mensual.isChecked();
        Boolean opc4= check_trimestral.isChecked();
        Boolean opc5= check_semestral.isChecked();
        Boolean opc6= check_anual.isChecked();
        Boolean opc7= check_bienal.isChecked();
        Boolean opc8= check_nunca.isChecked();

        int val1 = (opc1) ? 1 : 0;
        int val2 = (opc2) ? 1 : 0;
        int val3 = (opc3) ? 1 : 0;
        int val4 = (opc4) ? 1 : 0;
        int val5 = (opc5) ? 1 : 0;
        int val6 = (opc6) ? 1 : 0;
        int val7 = (opc7) ? 1 : 0;
        int val8 = (opc8) ? 1 : 0;

        if(val1==0 && val2==0 && val3==0 && val4==0 && val5==0 && val6==0 && val7==0 && val8==0){
            return "";
        }else{
            return val1+""+val2+""+val3+""+val4+""+val5+""+val6+""+val7+""+val8;
        }

    }//con qué frecuencia realiza la limpieza o lavado
    public String pregunta17(){
        Boolean opc1= check_semanal1.isChecked();
        Boolean opc2= check_quincenal1.isChecked();
        Boolean opc3= check_mensual1.isChecked();
        Boolean opc4= check_bimensual1.isChecked();
        Boolean opc5= check_trimestral1.isChecked();
        Boolean opc6= check_cuatrimestral1.isChecked();
        Boolean opc7= check_semestral1.isChecked();
        Boolean opc8= check_anual1.isChecked();
        Boolean opc9= check_otras1.isChecked();

        int val1 = (opc1) ? 1 : 0;
        int val2 = (opc2) ? 1 : 0;
        int val3 = (opc3) ? 1 : 0;
        int val4 = (opc4) ? 1 : 0;
        int val5 = (opc5) ? 1 : 0;
        int val6 = (opc6) ? 1 : 0;
        int val7 = (opc7) ? 1 : 0;
        int val8 = (opc8) ? 1 : 0;
        int val9 = (opc9) ? 1 : 0;

        if(val1==0 && val2==0 && val3==0 && val4==0 && val5==0 && val6==0 && val7==0 && val8==0 && val9==0){
            return "";
        }else{
            return val1+""+val2+""+val3+""+val4+""+val5+""+val6+""+val7+""+val8+""+val9;
        }


    }//¿CON QUÉ FRECUENCIA CLORA EL TANQUE?
    public String pregunta18(){
        Boolean opc1= check_op1.isChecked();
        Boolean opc2= check_op2.isChecked();
        Boolean opc3= check_op3.isChecked();
        Boolean opc4= check_op4.isChecked();
        Boolean opc5= check_op5.isChecked();
        Boolean opc6= check_op6.isChecked();
        Boolean opc7= check_op7.isChecked();
        Boolean opc8= check_op8.isChecked();
        Boolean opc9= check_otras2.isChecked();

        int val1 = (opc1) ? 1 : 0;
        int val2 = (opc2) ? 1 : 0;
        int val3 = (opc3) ? 1 : 0;
        int val4 = (opc4) ? 1 : 0;
        int val5 = (opc5) ? 1 : 0;
        int val6 = (opc6) ? 1 : 0;
        int val7 = (opc7) ? 1 : 0;
        int val8 = (opc8) ? 1 : 0;
        int val9 = (opc9) ? 1 : 0;


        if(val1==0 && val2==0 && val3==0 && val4==0 && val5==0 && val6==0 && val7==0 && val8==0 && val9==0){
            return "";
        }else{
            return val1+""+val2+""+val3+""+val4+""+val5+""+val6+""+val7+""+val8+""+val9;
        }

    }//dosificacion utilizada
    public String pregunta19(){
        Boolean opc1= check_llave.isChecked();
        Boolean opc2= check_hervida.isChecked();
        Boolean opc3= check_filtrada.isChecked();
        Boolean opc4= check_embotellada1.isChecked();

        int val1 = (opc1) ? 1 : 0;
        int val2 = (opc2) ? 1 : 0;
        int val3 = (opc3) ? 1 : 0;
        int val4 = (opc4) ? 1 : 0;
        if(val1==0&&val2==0&&val3==0&&val4==0){
            return "";
        }else{
            return val1+""+val2+""+val3+""+val4;
        }


    }//origen del agua
    public String pregunta20(){
        Boolean opc1= check_cocina.isChecked();
        Boolean opc2= check_aseo.isChecked();
        Boolean opc3= check_lavado.isChecked();
        Boolean opc4= check_animales.isChecked();
        Boolean opc5= check_riego.isChecked();

        int val1 = (opc1) ? 1 : 0;
        int val2 = (opc2) ? 1 : 0;
        int val3 = (opc3) ? 1 : 0;
        int val4 = (opc4) ? 1 : 0;
        int val5 = (opc5) ? 1 : 0;

        if(val1==0&&val2==0&&val3==0&&val4==0&&val5==0){
            return "";
        }else{
            return val1+""+val2+""+val3+""+val4+""+val5;
        }

    }//uso del agua
    public int pregunta21(){

        Boolean opc1= check_animal_mascota.isChecked();

        int val1;

        if(opc1){
            val1=1;
        }else{
            val1=0;
        }
        return val1;
    }//mascotas
    public int pregunta22(){

        Boolean opc1= check_ornamentales.isChecked();

        int val1;

        if(opc1){
            val1=1;
        }else{
            val1=0;
        }
        return val1;
    }//ornamentales


    public boolean comprobarllenado(){

        
        if(pregunta1()==""||pregunta2()==0||pregunta3()==""||pregunta4()==""||pregunta5()==""){
            return true;
        }if(pregunta_nombre().isEmpty()||pregunta_direccion().isEmpty()||pregunta_edad().isEmpty()|| pregunta_sexo()==null){
            return true;
        }if(pregunta6()==null||pregunta7().isEmpty()||pregunta12()==""||pregunta13()==""||pregunta19()==""||pregunta20()==""){
            return true;
        }
        else{return false;}

    }


    //Para obtener ubicacion

    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        MiEncuesta.Localizacion Local = new MiEncuesta.Localizacion();
        Local.setUbicacionActivity(this);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) Local);
        latitud.setText("Localización agregada");
        direccion.setText("");
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationStart();
                return;
            }
        }
    }
    public void setLocation(Location loc) {
        //Obtener la direccion de la calle a partir de la latitud y la longitud
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);
                    direccion.setText(DirCalle.getAddressLine(0));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /* Aqui empieza la Clase Localizacion */
    public class Localizacion implements LocationListener {
        MiEncuesta miEncuesta;
        public  MiEncuesta getUbicacionActivity() {
            return miEncuesta;
        }
        public void setUbicacionActivity(MiEncuesta miEncuesta) {
            this.miEncuesta = miEncuesta;
        }
        @Override
        public void onLocationChanged(Location loc) {
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
            // debido a la deteccion de un cambio de ubicacion
            loc.getLatitude();
            loc.getLongitude();
            String sLatitud = String.valueOf(loc.getLatitude());
            String sLongitud = String.valueOf(loc.getLongitude());
            latitud.setText(sLatitud);
            longitud.setText(sLongitud);
            this.miEncuesta.setLocation(loc);
        }
        @Override
        public void onProviderDisabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es desactivado
            latitud.setText("GPS Desactivado");
        }
        @Override
        public void onProviderEnabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es activado
            latitud.setText("GPS Activado");
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d("debug", "LocationProvider.AVAILABLE");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                    break;
            }
        }
    }

    public void fechayhora() {

        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        Date date = new Date();

        String fecha = formatoFecha.format(date);
        String Hora = formatoHora.format(date);
        TituloFecha.setText(fecha);
        HoraInicio.setText(Hora);
    }

    public String horaFinal() {

        SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        Date date = new Date();

        String Hora = formatoHora.format(date);
        return Hora;
    }

    ///CONEXION CON LA DBB Y ENVÍO DE DATOS

    private void loadNames() {
        names.clear();
        Cursor cursor = db.getNames1();
        if (cursor.moveToFirst()) {
            do {
                Name1 name = new Name1(
                        cursor.getString(cursor.getColumnIndex(db.c_CODIGO)),
                        cursor.getString(cursor.getColumnIndex(db.c_HORAFIN)),
                        cursor.getInt(cursor.getColumnIndex(db.c_ESTADO))
                );
                names.add(name);
            } while (cursor.moveToNext());
        }

        nameAdapter = new NameAdapter1(this, R.layout.names, names);

    }

    private void loadNames3() {
        names3.clear();
        Cursor cursor = db.getNames3();
        if (cursor.moveToFirst()) {
            do {
                Name3 name = new Name3(
                        cursor.getString(cursor.getColumnIndex(db.c_CODIGO)),
                        cursor.getString(cursor.getColumnIndex(db.c_NOMBRE)),
                        cursor.getString(cursor.getColumnIndex(db.c_DIRECCION)),
                        cursor.getString(cursor.getColumnIndex(db.c_SEXO)),
                        cursor.getString(cursor.getColumnIndex(db.c_EDAD)),
                        cursor.getString(cursor.getColumnIndex(db.c_UTM_LO)),
                        cursor.getString(cursor.getColumnIndex(db.c_UTM_LA)),
                        cursor.getInt(cursor.getColumnIndex(db.c_ESTADO))
                );
                names3.add(name);
            } while (cursor.moveToNext());
        }

        nameAdapter3 = new NameAdapter3(this, R.layout.names3, names3);

    }



    private void saveNameToLocalStorage(String codigo,

                                         String fecha,
                                         String horaInicio,
                                         String horaFin,
                                         String foto,
                                         String tipoVivienda,
                                         String otroTipoVivienda,
                                         String numeroPisos,
                                         String techo,
                                         String paredes,
                                         String piso,
                                         String vivienda,
                                         String numeroPersonas,
                                         String problemasEstomacales,
                                         String tipoProblemasEstomacales,
                                         String otroProblemasEstomacales,
                                         String enfermedadPiel,
                                         String tipoEnfermedadPiel,
                                         String otraEnfermedadPiel,
                                         String abastecimientoAgua,
                                         String nombreRio,
                                         String otroAbastecimientoAgua,
                                         String sisternaTanque,
                                         String origenAgua,
                                         String tratamientoOrigenAgua,
                                         String usoAgua,
                                         String capacidadTanque,
                                         String capacidadSisterna,
                                         String frecuenciaLimpieza,
                                         String frecuenciaCloracion,
                                         String otroFrecuenciaCloracion,
                                         String dosisCloracion,
                                         String otroDosisCloracion,
                                         String mascotas_animal,
                                         String consumo_animal,
                                         String venta_animal,
                                         String ornamentales_riego,
                                         String consumo_riego,
                                         String venta_riego,
                                         int status
                                         ) {

        db.addName1(codigo, fecha, horaInicio, horaFin, foto, tipoVivienda, otroTipoVivienda, numeroPisos, techo, paredes, piso, vivienda,
                numeroPersonas, problemasEstomacales, tipoProblemasEstomacales, otroProblemasEstomacales, enfermedadPiel, tipoEnfermedadPiel,
                otraEnfermedadPiel, abastecimientoAgua, nombreRio, otroAbastecimientoAgua, sisternaTanque, origenAgua, tratamientoOrigenAgua,
                usoAgua, capacidadTanque, capacidadSisterna, frecuenciaLimpieza, frecuenciaCloracion, otroFrecuenciaCloracion, dosisCloracion,
                otroDosisCloracion, mascotas_animal, consumo_animal, venta_animal, ornamentales_riego, consumo_riego, venta_riego, status);


        Name1 n = new Name1(codigo, horaFin, status);
        names.add(n);

        Toast.makeText(this,"Encuesta  agregada ",Toast.LENGTH_SHORT).show();
        //refreshList();

    }


    private void saveNameToLocalStorage3(String codigo_persona,
                                        String nombre_persona,
                                        String dir_persona,
                                        String edad_persona,
                                        String sexo_persona,
                                        String longitud_persona,
                                        String latitud_persona,
                                        int status_persona) {
        db.addName3(codigo_persona, nombre_persona, dir_persona,  edad_persona, sexo_persona, longitud_persona, latitud_persona, status_persona);
        Name3 n = new Name3(codigo_persona, nombre_persona,dir_persona,  edad_persona, sexo_persona, longitud_persona, latitud_persona, status_persona);
        names3.add(n);

        Toast.makeText(this,"Persona  agregada ",Toast.LENGTH_SHORT).show();
        //refreshList();
    }

    private void saveNameToServer() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Guardando en el servidor...");
        progressDialog.show();

        preferences = getSharedPreferences("Preferences",MODE_PRIVATE);
        //String usuario_nombre=preferences.getString("usuario_nombre", null);
        String usuario_codigo=preferences.getString("usuario_codigo", null);

        final String codigo = usuario_codigo;
        final String fecha = TituloFecha.getText().toString().trim();
        final String horaInicio = HoraInicio.getText().toString().trim();
        final String horaFin = this.horaFinal();
        final String foto ="imageUri";


        final String tipoVivienda=this.pregunta1();
        final String otroTipoVivienda=campo_otros.getText().toString().trim();
        final String numeroPisos=String.valueOf(pregunta2());
        final String techo=this.pregunta3();
        final String paredes=this.pregunta4();
        final String piso=this.pregunta5();
        final String vivienda=this.pregunta6();
        final String numeroPersonas=this.pregunta7();
        final String problemasEstomacales=String.valueOf(pregunta8());
        final String tipoProblemasEstomacales=this.pregunta9();
        final String otroProblemasEstomacales=texto_otros.getText().toString().trim();
        final String enfermedadPiel=String.valueOf(pregunta10());
        final String tipoEnfermedadPiel=this.pregunta11();
        final String otraEnfermedadPiel=texto_diagnostico1.getText().toString().trim();
        final String abastecimientoAgua=this.pregunta12();
        final String nombreRio=texto_nombreRio.getText().toString().trim();
        final String otroAbastecimientoAgua=campo_otros2.getText().toString().trim();
        final String sisternaTanque=this.pregunta13();
        final String origenAgua=this.pregunta19();
        final String tratamientoOrigenAgua=tratamiento.getText().toString().trim();
        final String usoAgua=this.pregunta20();
        final String capacidadTanque=String.valueOf(pregunta15());
        final String capacidadSisterna=String.valueOf(pregunta14());
        final String frecuenciaLimpieza=this.pregunta16();
        final String frecuenciaCloracion=this.pregunta17();
        final String otroFrecuenciaCloracion=texto_otros1.getText().toString().trim();
        final String dosisCloracion=this.pregunta18();
        final String otroDosisCloracion=texto_otros2.getText().toString();
        final String mascotas_animal=String.valueOf(pregunta21());
        final String consumo_animal=text_animal_consumo.getText().toString().trim();
        final String venta_animal=text_animal_venta.getText().toString().trim();
        final String ornamentales_riego=String.valueOf(pregunta22());
        final String consumo_riego=text_sembrios_consumo.getText().toString().trim();
        final String venta_riego=text_sembrios_venta.getText().toString().trim();


        final String codigo_persona=9+""+8;








        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SAVE_NAME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj1 = new JSONObject(response);
                            if (!obj1.getBoolean("error")) {

                                saveNameToLocalStorage(
                                        codigo,
                                        fecha,
                                        horaInicio,
                                        horaFin,
                                        foto,
                                        tipoVivienda,
                                        otroTipoVivienda,
                                        numeroPisos,
                                        techo,
                                        paredes,
                                        piso,
                                        vivienda,
                                        numeroPersonas,
                                        problemasEstomacales,
                                        tipoProblemasEstomacales,
                                        otroProblemasEstomacales,
                                        enfermedadPiel,
                                        tipoEnfermedadPiel,
                                        otraEnfermedadPiel,
                                        abastecimientoAgua,
                                        nombreRio,
                                        otroAbastecimientoAgua,
                                        sisternaTanque,
                                        origenAgua,
                                        tratamientoOrigenAgua,
                                        usoAgua,
                                        capacidadTanque,
                                        capacidadSisterna,
                                        frecuenciaLimpieza,
                                        frecuenciaCloracion,
                                        otroFrecuenciaCloracion,
                                        dosisCloracion,
                                        otroDosisCloracion,
                                        mascotas_animal,
                                        consumo_animal,
                                        venta_animal,
                                        ornamentales_riego,
                                        consumo_riego,
                                        venta_riego,
                                        NAME_SYNCED_WITH_SERVER

                                        );
                            } else {
                                // si hay algun error
                                // guardando el nombre en sqlite con estado no sincronizado
                                saveNameToLocalStorage(codigo, fecha, horaInicio, horaFin, foto, tipoVivienda, otroTipoVivienda, numeroPisos, techo, paredes, piso, vivienda,
                                        numeroPersonas, problemasEstomacales, tipoProblemasEstomacales, otroProblemasEstomacales, enfermedadPiel, tipoEnfermedadPiel,
                                        otraEnfermedadPiel, abastecimientoAgua, nombreRio, otroAbastecimientoAgua, sisternaTanque, origenAgua, tratamientoOrigenAgua,
                                        usoAgua, capacidadTanque, capacidadSisterna, frecuenciaLimpieza, frecuenciaCloracion, otroFrecuenciaCloracion, dosisCloracion,
                                        otroDosisCloracion, mascotas_animal, consumo_animal, venta_animal, ornamentales_riego, consumo_riego, venta_riego, NAME_NOT_SYNCED_WITH_SERVER
                                        );
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        // en caso de error al almacenar el nombre en sqlite con estado no sincronizado
                        saveNameToLocalStorage(codigo, fecha, horaInicio, horaFin, foto, tipoVivienda, otroTipoVivienda, numeroPisos, techo, paredes, piso, vivienda,
                                numeroPersonas, problemasEstomacales, tipoProblemasEstomacales, otroProblemasEstomacales, enfermedadPiel, tipoEnfermedadPiel,
                                otraEnfermedadPiel, abastecimientoAgua, nombreRio, otroAbastecimientoAgua, sisternaTanque, origenAgua, tratamientoOrigenAgua,
                                usoAgua, capacidadTanque, capacidadSisterna, frecuenciaLimpieza, frecuenciaCloracion, otroFrecuenciaCloracion, dosisCloracion,
                                otroDosisCloracion, mascotas_animal, consumo_animal, venta_animal, ornamentales_riego, consumo_riego, venta_riego, NAME_NOT_SYNCED_WITH_SERVER
                                );
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();


                params.put("codigo", codigo);
                params.put("fecha", fecha);
                params.put("horaInicio", horaInicio);
                params.put("horaFin", horaFin);
                params.put("foto", foto);
                params.put("tipoVivienda",tipoVivienda );
                params.put("otroTipoVivienda", otroTipoVivienda);
                params.put("numeroPisos",numeroPisos);
                params.put("techo",techo);
                params.put("paredes",paredes );
                params.put("piso",piso );
                params.put("vivienda",vivienda);
                params.put("numeroPersonas",numeroPersonas);
                params.put("problemasEstomacales",problemasEstomacales);
                params.put("tipoProblemasEstomacales",tipoProblemasEstomacales);
                params.put("otroProblemasEstomacales",otroProblemasEstomacales);
                params.put("enfermedadPiel",enfermedadPiel);
                params.put("tipoEnfermedadPiel",tipoEnfermedadPiel );
                params.put("otraEnfermedadPiel",otraEnfermedadPiel );
                params.put("abastecimientoAgua",abastecimientoAgua );
                params.put("nombreRio",nombreRio );
                params.put("otroAbastecimientoAgua",otroAbastecimientoAgua);
                params.put("sisternaTanque",sisternaTanque );
                params.put("origenAgua",origenAgua );
                params.put("tratamientoOrigenAgua",tratamientoOrigenAgua);
                params.put("usoAgua",usoAgua );
                params.put("capacidadTanque",capacidadTanque);
                params.put("capacidadSisterna",capacidadSisterna);
                params.put("frecuenciaLimpieza",frecuenciaLimpieza);
                params.put("frecuenciaCloracion",frecuenciaCloracion);
                params.put("otroFrecuenciaCloracion",otroFrecuenciaCloracion);
                params.put("dosisCloracion",dosisCloracion);
                params.put("otroDosisCloracion",otroDosisCloracion);
                params.put("mascotas_animal",mascotas_animal);
                params.put("consumo_animal",consumo_animal );
                params.put("venta_animal",venta_animal );
                params.put("ornamentales_riego",ornamentales_riego);
                params.put("consumo_riego",consumo_riego );
                params.put("venta_riego",venta_riego );

                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }







    private void saveNameToServer3() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Guardando persona...");
        progressDialog.show();



        final String codigo_persona=9+""+8;
        final String nombre_persona=pregunta_nombre();
        final String dir_persona=pregunta_direccion();
        final String edad_persona=pregunta_edad();
        final String sexo_persona=pregunta_sexo();

        final String Longitud = longitud.getText().toString().trim();
        final String Latitud = latitud.getText().toString().trim();







        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL_SAVE_NAME3,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj1 = new JSONObject(response);
                            if (!obj1.getBoolean("error")) {

                                saveNameToLocalStorage3(
                                        codigo_persona,
                                        nombre_persona,
                                        dir_persona,
                                        edad_persona,
                                        sexo_persona,
                                        Longitud,
                                        Latitud,
                                        NAME_SYNCED_WITH_SERVER
                                );
                            } else {
                                // si hay algun error
                                // guardando el nombre en sqlite con estado no sincronizado
                                saveNameToLocalStorage3(codigo_persona,
                                        nombre_persona, dir_persona, edad_persona, sexo_persona, Longitud, Latitud, NAME_NOT_SYNCED_WITH_SERVER);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        // en caso de error al almacenar el nombre en sqlite con estado no sincronizado
                        saveNameToLocalStorage3(codigo_persona,
                                nombre_persona, dir_persona, edad_persona, sexo_persona, Longitud, Latitud, NAME_NOT_SYNCED_WITH_SERVER);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("codigo_persona",codigo_persona);
                params.put("nombre_persona",nombre_persona);
                params.put("dir_persona",dir_persona);
                params.put("edad_persona",edad_persona);
                params.put("sexo_persona",sexo_persona);
                params.put("longitud_persona",Longitud);
                params.put("latitud_persona",Latitud);


                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }









    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id. profileIv:
                imagePickDialog();
                break;

            case R.id. seccion2:

                if(comprobarllenado()){
                    Toast.makeText(this, "FALTA LLENAR", Toast.LENGTH_SHORT).show();
                }else{
                    saveNameToServer();
                    saveNameToServer3();
                }


                break;

        }

    }



}