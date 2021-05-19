package com.example.appvinculacion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MiEncuesta2 extends AppCompatActivity implements View.OnClickListener{

    private CheckBox check_cisterna,check_tanque,check_pozo,check_rio;
    private CheckBox check_turbia,check_solidos,check_coloracion,check_olor;
    private EditText etHora,texto_diagnostico;


    private Spinner comboPersonas;
    private TextView txtNombre,txtDir,txtCodigo;
    ArrayList<String> listaPersonas;
    ArrayList<Name3> personasList;
    MyDbHelper conn;






    private static final String CERO = "0";
    private static final String DOS_PUNTOS = ":";
    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();
    //Variables para obtener la hora hora
    final int hora = c.get(Calendar.HOUR_OF_DAY);
    final int minuto = c.get(Calendar.MINUTE);
    //Widgets

    Button ibObtenerHora;


    //Mostrar nombre y codigo del encuestador
    private SharedPreferences preferences;

    private static final String CARPETA_PRICIPAL="misImagenesApp/";
    private static final String CARPETA_IMAGEN="imagenes";
    private static final String DIRECTORIO_IMAGEN=CARPETA_PRICIPAL+CARPETA_IMAGEN;
    private String path;//almacena la ruta de la imagen
    File fileImagen;
    Bitmap bitmap;

    private ImageView profileIv;
    private static final int CAMERA_REQUEST_CODE=100;// constantes de permisos
    private static final int STORAGE_REQUEST_CODE=101;
    private static final int IMAGE_PICK_CAMERA_CODE=102;// constantes de selección de imágenes
    private static final int IMAGE_PICK_GALLERY_CODE=103;
    private String[] cameraPermissions;//camara y almacenamiento
    private String[] storagePermissions;//solo almacenamiento
    private Uri imageUri;//variables(contendrá datos para guardar)


    private TextView TituloFecha,HoraInicio,txtLatitud,txtLongitud;
    //private TextView latitud,longitud;
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
    public static final String URL_SAVE_NAME = "http://192.168.1.7/sincronizar/encuesta2.php";
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

        conn=new MyDbHelper(getApplicationContext(),UsuarioAdapter.DB_NAME,null,1);
        comboPersonas=(Spinner) findViewById(R.id.comboPersonas);
        txtNombre=(TextView) findViewById(R.id.txtNombre);
        txtDir=(TextView) findViewById(R.id.txtDir);
        txtCodigo=(TextView) findViewById(R.id.txtCodigo);
        init();
        consultarListaPersonas();
        ArrayAdapter<CharSequence> adaptador=new ArrayAdapter(this,R.layout.simple_spinner_personas,listaPersonas);
        comboPersonas.setAdapter(adaptador);

        comboPersonas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position!=0){
                    txtCodigo.setText(personasList.get(position-1).getCodigo().toString());
                    txtNombre.setText(personasList.get(position-1).getNombre().toString());
                    txtDir.setText(personasList.get(position-1).getDir().toString());
                }else{
                    txtCodigo.setText("");
                    txtNombre.setText("");
                    txtDir.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        profileIv=findViewById(R.id.profileIv);
        initFechahora();
        initLocalizacion();

        //matrices de permisos de inicio
        cameraPermissions=new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions=new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};



        profileIv.setOnClickListener(this);
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

    private void init() {

        check_cisterna=(CheckBox) findViewById(R.id.check_cisterna);
        check_tanque=(CheckBox) findViewById(R.id.check_tanque);
        check_pozo=(CheckBox) findViewById(R.id.check_pozo);
        check_rio=(CheckBox) findViewById(R.id.check_rio);

        check_turbia=(CheckBox) findViewById(R.id.check_cisterna);
        check_solidos=(CheckBox) findViewById(R.id.check_tanque);
        check_coloracion=(CheckBox) findViewById(R.id.check_pozo);
        check_olor=(CheckBox) findViewById(R.id.check_rio);


        texto_diagnostico=(EditText) findViewById(R.id.texto_diagnostico);


    }
    public String pregunta1(){

        Boolean opc1= check_cisterna.isChecked();
        Boolean opc2= check_tanque.isChecked();
        Boolean opc3= check_pozo.isChecked();
        Boolean opc4= check_rio.isChecked();

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
    public String pregunta2(){

        Boolean opc1= check_turbia.isChecked();
        Boolean opc2= check_solidos.isChecked();
        Boolean opc3= check_coloracion.isChecked();
        Boolean opc4= check_olor.isChecked();

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
    public String codigoVinculacion(){
        preferences = getSharedPreferences("Preferences",MODE_PRIVATE);
        String usuario_codigo=preferences.getString("usuario_codigo", null);
        return "Vinc-"+usuario_codigo;
    }
    public String codigoPersona(){
        String codigopersona=txtCodigo.getText().toString().trim();
        return codigopersona;
    }
    public String codigoEncuestador(){
        preferences = getSharedPreferences("Preferences",MODE_PRIVATE);
        String usuario_codigo=preferences.getString("usuario_codigo", null);
        return usuario_codigo;
    }

    public boolean comprobarllenado(){


        if(pregunta1()==""||pregunta2()==""){
            return true;
        }if(codigoVinculacion()!="Vinc-"||codigoEncuestador()==null){
            return true;
        }
        else{return false;}

    }






    private void consultarListaPersonas() {
        SQLiteDatabase db = conn.getReadableDatabase();
        Name3 persona=null;
        personasList = new ArrayList<Name3>();
        Cursor cursor=db.rawQuery("SELECT * FROM "+UsuarioAdapter.TABLE_NAME3,null);
        while (cursor.moveToNext()){
          persona=new Name3();
          persona.setCodigo(cursor.getString(1));
          persona.setNombre(cursor.getString(2));
          persona.setDir(cursor.getString(3));

          personasList.add(persona);
        }
        obtenerLista();
    }

    private void obtenerLista() {
        listaPersonas=new ArrayList<>();
        listaPersonas.add("Seleccione un nombre");

        for(int i=0;i<personasList.size();i++){
            listaPersonas.add(i+" - "+personasList.get(i).getNombre());

        }

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
                    if(!checkStoragePermission()){
                        requestStoragePermission();
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
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
        imageUri = getContentResolver().insert(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
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

        // se recibirá la imagen seleccionada de la cámara o la galería
        if(resultCode==RESULT_OK){
            // se elige la imagen
            if(requestCode==IMAGE_PICK_GALLERY_CODE){
                // elegido de la galería


                //delimitar imagen
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(this);
            }
            else if(requestCode==IMAGE_PICK_CAMERA_CODE){
                // recogido de la cámara

                //delimitar imagen
                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(this);
            }
            else if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
                // imagen recortada recibida
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if(resultCode==RESULT_OK){
                    Uri resultUri=result.getUri();

                    try {
                        //bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), resultUri));
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // establecer imagen
                    profileIv.setImageURI(resultUri);
                }
                else if (resultCode==CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                    //error
                    Exception error = result.getError();
                    Toast.makeText(this,"Error "+error, Toast.LENGTH_SHORT).show();

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
        etHora = (EditText) findViewById(R.id.et_mostrar_hora_picker);
        ibObtenerHora = (Button) findViewById(R.id.ib_obtener_hora);
        ibObtenerHora.setOnClickListener(this);
    }


    private void initLocalizacion(){
        txtLatitud = (TextView) findViewById(R.id.txtLatitud);
        txtLongitud = (TextView) findViewById(R.id.txtLongitud);
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
        txtLatitud.setText("Localización agregada");
        direccion.setText("");
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {


        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                if (grantResults.length>0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted&&storageAccepted){
                        pickFromCamera();
                    }else{
                        Toast.makeText(this,"Se requieren permisos de cámara y almacenamiento",Toast.LENGTH_SHORT);
                    }

                }



            }
            break;
            case STORAGE_REQUEST_CODE:{
                if (grantResults.length>0){
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(storageAccepted){
                        pickFromGallery();
                    }else{
                        Toast.makeText(this,"Se requieren permisos de cámara y almacenamiento",Toast.LENGTH_SHORT);

                    }

                }

            }
            break;
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

    private void obtenerHora(){
        TimePickerDialog recogerHora = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                //Formateo el hora obtenido: antepone el 0 si son menores de 10
                String horaFormateada =  (hourOfDay < 10)? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                String minutoFormateado = (minute < 10)? String.valueOf(CERO + minute):String.valueOf(minute);
                //Obtengo el valor a.m. o p.m., dependiendo de la selección del usuario


                //Muestro la hora con el formato deseado
                etHora.setText(horaFormateada + DOS_PUNTOS + minutoFormateado);
            }
            //Estos valores deben ir en ese orden
            //Al colocar en false se muestra en formato 12 horas y true en formato 24 horas
            //Pero el sistema devuelve la hora en formato 24 horas
        }, hora, minuto, false);

        recogerHora.show();
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
            txtLatitud.setText(sLatitud);
            txtLongitud.setText(sLongitud);
            this.miEncuesta2.setLocation(loc);
        }
        @Override
        public void onProviderDisabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es desactivado
            txtLatitud.setText("GPS Desactivado");
            txtLongitud.setText("GPS Desactivado");
        }
        @Override
        public void onProviderEnabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es activado
            txtLatitud.setText("GPS Activado");
            txtLongitud.setText("GPS Activado");
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
        long id= db.addName2(codigo, fecha, horaInicio, horaFin, foto, longitud, latitud, status);
        Name2 n = new Name2(codigo, fecha, horaInicio, horaFin, foto, longitud, latitud, status);
        names.add(n);

        Toast.makeText(this,"Encuesta "+id+" agregada ",Toast.LENGTH_SHORT).show();
        //refreshList();

    }

    public String getStringImagen(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStreamObject ;
        byteArrayOutputStreamObject = new ByteArrayOutputStream();
        // Converting bitmap image to jpeg format, so by default image will upload in jpeg format.
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject);
        byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();
        final String ConvertImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);
        return ConvertImage;
    }

/*
    private void refreshList() {
        nameAdapter.notifyDataSetChanged();
    }*/

    @Override
    public void onClick(View v) {


        switch (v.getId()){
            case R.id.ib_obtener_hora:
                obtenerHora();
                break;
            case R.id. profileIv:
                imagePickDialog();
                break;

            case R.id. buttonSave:

                if(comprobarllenado()){
                    Toast.makeText(this, "FALTA LLENAR", Toast.LENGTH_SHORT).show();
                }else{
                    saveNameToServer();

                }

                break;

        }


    }


}