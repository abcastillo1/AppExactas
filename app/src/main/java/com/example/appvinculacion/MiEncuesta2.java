package com.example.appvinculacion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import android.graphics.Bitmap;
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
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MiEncuesta2 extends AppCompatActivity implements View.OnClickListener{
    //Mostrar nombre y codigo del encuestador
    private SharedPreferences preferences;

    private ImageView profileIv;
    private static final int CAMERA_REQUEST_CODE=100;// constantes de permisos
    private static final int STORAGE_REQUEST_CODE=101;
    private static final int IMAGE_PICK_CAMERA_CODE=102;// constantes de selección de imágenes
    private static final int IMAGE_PICK_GALLERY_CODE=103;
    private String[] cameraPermissions;//camara y almacenamiento
    private String[] storagePermissions;//solo almacenamiento
    private Uri imageUri;//variables(contendrá datos para guardar)
    private Bitmap bitmap;

    private TextView TituloFecha,HoraInicio,txtLatitud,txtLongitud;
    private TextView latitud,longitud;
    private TextView direccion;

    private UsuarioAdapter db;
    //View objects
    private Button buttonSave;
    private ListView listViewNames;
    private List<Name2> names;
    private BroadcastReceiver broadcastReceiver;
    public static final int NAME_SYNCED_WITH_SERVER = 1;
    public static final int NAME_NOT_SYNCED_WITH_SERVER = 0;
    private NameAdapter2 nameAdapter;
    public static final String URL_SAVE_NAME = "http://192.168.1.8/sincronizar/encuesta2.php";
    public static final String DATA_SAVED_BROADCAST = "net.simplifiedcoding.datasaved";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_encuesta2);







        registerReceiver(new NetworkStateChecker2(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        db = new UsuarioAdapter(this);
        names = new ArrayList<>();
        buttonSave = (Button) findViewById(R.id.buttonSave);
        TituloFecha = (TextView) findViewById(R.id.TituloFecha);
        HoraInicio = (TextView) findViewById(R.id.HoraInicio);
        txtLatitud = (TextView) findViewById(R.id.txtLatitud);
        txtLongitud = (TextView) findViewById(R.id.txtLongitud);

        profileIv=findViewById(R.id.profileIv);
        initFechahora();
        initLocalizacion();

        //matrices de permisos de inicio
        cameraPermissions=new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions=new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        profileIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePickDialog();
            }
        });

        //adding click listener to button
        buttonSave.setOnClickListener(this);
        loadNames();
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //loading the names again
                loadNames();
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
                Uri filePath = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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


    //Para obtener ubicacion
    private void initFechahora(){
        TituloFecha = (TextView) findViewById(R.id.TituloFecha);
        HoraInicio= (TextView) findViewById(R.id.HoraInicio);
        fechayhora();
    }


    private void initLocalizacion(){
        latitud = (TextView) findViewById(R.id.txtLatitud);
        longitud = (TextView) findViewById(R.id.txtLongitud);
        direccion = (TextView) findViewById(R.id.txtDireccion);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        } else {
            locationStart();
        }

    }

    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        MiEncuesta2.Localizacion Local = new MiEncuesta2.Localizacion();
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
        MiEncuesta2 miEncuesta2;
        public  MiEncuesta2 getUbicacionActivity() {
            return miEncuesta2;
        }
        public void setUbicacionActivity(MiEncuesta2 miEncuesta2) {
            this.miEncuesta2 = miEncuesta2;
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
            this.miEncuesta2.setLocation(loc);
        }
        @Override
        public void onProviderDisabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es desactivado
            latitud.setText("GPS Desactivado");
            longitud.setText("GPS Desactivado");
        }
        @Override
        public void onProviderEnabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es activado
            latitud.setText("GPS Activado");
            longitud.setText("GPS Activado");
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

    //fecha y hora
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






    private void loadNames() {
        names.clear();
        Cursor cursor = db.getNames2();
        if (cursor.moveToFirst()) {
            do {
                Name2 name = new Name2(
                        cursor.getString(cursor.getColumnIndex(db.c_CODIGO)),
                        cursor.getString(cursor.getColumnIndex(db.c_FECHA)),
                        cursor.getString(cursor.getColumnIndex(db.c_HORAINICIO)),
                        cursor.getString(cursor.getColumnIndex(db.c_HORAFIN)),
                        cursor.getString(cursor.getColumnIndex(db.c_FOTO)),
                        cursor.getString(cursor.getColumnIndex(db.c_UTM_LO)),
                        cursor.getString(cursor.getColumnIndex(db.c_UTM_LA)),
                        cursor.getInt(cursor.getColumnIndex(db.c_ESTADO))
                );
                names.add(name);
            } while (cursor.moveToNext());
        }

        nameAdapter = new NameAdapter2(this, R.layout.names, names);

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
        final String horaInicio = HoraInicio.getText().toString().trim();;
        final String horaFin = this.horaFinal();
        final String foto =getStringImagen(bitmap);
        final String longitud = txtLongitud.getText().toString().trim();
        final String latitud = txtLatitud.getText().toString().trim();





        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SAVE_NAME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj2 = new JSONObject(response);
                            if (!obj2.getBoolean("error")) {
                                // si hay un exito
                                // almacenando el nombre en sqlite con estado sincronizado
                                saveNameToLocalStorage(codigo, fecha, horaInicio, horaFin, foto, longitud, latitud, NAME_SYNCED_WITH_SERVER);
                            } else {
                                // si hay algun error
                                // guardando el nombre en sqlite con estado no sincronizado
                                saveNameToLocalStorage(codigo, fecha, horaInicio, horaFin, foto, longitud, latitud, NAME_NOT_SYNCED_WITH_SERVER);
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
                        saveNameToLocalStorage(codigo, fecha, horaInicio, horaFin, foto, longitud, latitud, NAME_NOT_SYNCED_WITH_SERVER);
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
                params.put("longitud", longitud);
                params.put("latitud", latitud);

                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }
    private void saveNameToLocalStorage(String codigo, String fecha, String horaInicio, String horaFin, String foto, String longitud, String latitud, int status) {
        //editTextCode.setText("");
       // editTextName.setText("");
        db.addName2(codigo, fecha, horaInicio, horaFin, foto, longitud, latitud, status);
        Name2 n = new Name2(codigo, fecha, horaInicio, horaFin, foto, longitud, latitud, status);
        names.add(n);
        //refreshList();

    }
    public String getStringImagen(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
/*
    private void refreshList() {
        nameAdapter.notifyDataSetChanged();
    }*/

    @Override
    public void onClick(View v) {
        saveNameToServer();
    }


}