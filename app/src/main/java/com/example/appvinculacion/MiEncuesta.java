package com.example.appvinculacion;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.provider.MediaStore;


import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MiEncuesta extends AppCompatActivity {

    private CheckBox check_otros,check_otros1,check_si,check_si1,check_si2,check_si3,check_rio,check_otros2,check_cisterna,check_tanque,check_si4,check_si5,check_otras,
            check_otras1,check_otras2,check_animales,check_animal_consumo,check_animal_venta, check_riego,check_sembrio_consumo,check_sembrio_venta;

    private View campo_otros,campo_otros1,texto_info19,texto_otros,texto_otros1,texto_otros2;

    private LinearLayout textPregunta11,textPregunta13,textPregunta15,textPregunta17,textPregunta19,textPregunta20,textPregunta22,textPregunta23,textPregunta24,
            textPregunta25,textPregunta26,textPregunta27,textPregunta30,textPregunta32,textPregunta33,textPregunta34,textPregunta35,textPregunta36,textPregunta37;


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


    TextView latitud,longitud;
    TextView direccion;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_encuesta);
        init();
        profileIv=findViewById(R.id.profileIv);

        latitud = (TextView) findViewById(R.id.txtLatitud);
        longitud = (TextView) findViewById(R.id.txtLongitud);
        direccion = (TextView) findViewById(R.id.txtDireccion);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        } else {
            locationStart();
        }
        //init db Helper
        //dbHelper=new MyDbHelper(this);


        //matrices de permisos de inicio
        cameraPermissions=new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions=new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        //Botón para agregar la imagen del encuestador


        profileIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePickDialog();
            }
        });

        //Botón para agregar el registro

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
        check_otros = (CheckBox) findViewById(R.id.check_otros);
        campo_otros = findViewById(R.id.campo_otros);

        check_otros1 = (CheckBox) findViewById(R.id.check_otros1);
        campo_otros1 = findViewById(R.id.campo_otros1);

        check_si = (CheckBox) findViewById(R.id.check_si);
        textPregunta11 = findViewById(R.id.textPregunta11);

        check_si1 = (CheckBox) findViewById(R.id.check_si1);
        textPregunta13 = findViewById(R.id.textPregunta13);

        check_si2 = (CheckBox) findViewById(R.id.check_si2);
        textPregunta15 = findViewById(R.id.textPregunta15);

        check_si3 = (CheckBox) findViewById(R.id.check_si3);
        textPregunta17 = findViewById(R.id.textPregunta17);

        check_rio = (CheckBox) findViewById(R.id.check_rio);
        textPregunta19 = findViewById(R.id.textPregunta19);

        check_otros2 = (CheckBox) findViewById(R.id.check_otros2);
        textPregunta20 = findViewById(R.id.textPregunta20);

        check_cisterna = (CheckBox) findViewById(R.id.check_cisterna);
        textPregunta22 = findViewById(R.id.textPregunta22);

        check_tanque = (CheckBox) findViewById(R.id.check_tanque);
        textPregunta23 = findViewById(R.id.textPregunta23);
        textPregunta24 = findViewById(R.id.textPregunta24);
        texto_info19= findViewById(R.id.texto_info19);
        textPregunta25= findViewById(R.id.textPregunta25);

        check_si4 = (CheckBox) findViewById(R.id.check_si4);
        textPregunta26= findViewById(R.id.textPregunta26);
        textPregunta27= findViewById(R.id.textPregunta27);

        check_si5 = (CheckBox) findViewById(R.id.check_si5);
        textPregunta30= findViewById(R.id.textPregunta30);

        check_animales = (CheckBox) findViewById(R.id.check_animales);
        textPregunta32= findViewById(R.id.textPregunta32);

        check_animal_consumo = (CheckBox) findViewById(R.id.check_animal_consumo);
        textPregunta33= findViewById(R.id.textPregunta33);

        check_animal_venta = (CheckBox) findViewById(R.id.check_animal_venta);
        textPregunta34 = findViewById(R.id.textPregunta34);

        check_riego = (CheckBox) findViewById(R.id.check_riego);
        textPregunta35 = findViewById(R.id.textPregunta35);

        check_sembrio_consumo= (CheckBox) findViewById(R.id.check_sembrio_consumo);
        textPregunta36 = findViewById(R.id.textPregunta36);

        check_sembrio_venta= (CheckBox) findViewById(R.id.check_sembrio_venta);
        textPregunta37 = findViewById(R.id.textPregunta37);

        check_otras= (CheckBox) findViewById(R.id.check_otras);
        texto_otros= findViewById(R.id.texto_otros);


        check_otras1= (CheckBox) findViewById(R.id.check_otras1);
        texto_otros1= findViewById(R.id.texto_otros1);

        check_otras2= (CheckBox) findViewById(R.id.check_otras2);
        texto_otros2= findViewById(R.id.texto_otros2);



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




}