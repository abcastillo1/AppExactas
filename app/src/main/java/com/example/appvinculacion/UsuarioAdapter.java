package com.example.appvinculacion;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class UsuarioAdapter {

    //nombre de la base de datos
    public static final String DB_NAME = "PROYECTO_EXACTAS1";
    //version de la base de datos
    private final int DB_VERSION = 1;
    //nombre de la tabla
    public static final String TABLE_NAME = "usuario";

    public static final String TABLE_NAME1 = "encuesta1";
    public static final String TABLE_NAME2 = "encuesta2";
    public static final String TABLE_NAME3 = "persona";


    //nombre de la tabla
    public static final String COLUMN_ID="id";
    public static final String c_NOMBRE="nombre";
    public static final String c_CODIGO="codigo";
    public static final String c_ESTADO="estado";
    public static final String c_DIRECCION="direccion";
    public static final String c_EDAD="edad";
    public static final String c_SEXO="sexo";



    public static final String c_FECHA="fecha";
    public static final String c_HORAINICIO="horaInicio";
    public static final String c_HORAFIN="horaFin";
    public static final String c_FOTO="foto";
    public static final String c_UTM_LO="Longitud";
    public static final String c_UTM_LA="Latitud";

    public static final String c_CODIGO_PERSONA="codigo_persona";
    public static final String c_NUM="numero";
    public static final String c_TIPOVIVIENDA="tipoVivienda";
    public static final String c_OTROTIPOVIVIENDA="otroTipoVivienda";
    public static final String c_NUMEROPISOS="numeroPisos";
    public static final String c_TECHO="techo";
    public static final String c_PAREDES="paredes";
    public static final String c_PISO="piso";
    public static final String c_VIVIENDA="vivienda";
    public static final String c_NUMEROPERSONAS="numeroPersonas";
    public static final String c_PROBLEMASESTOMACALES="problemasEstomacales";
    public static final String c_TIPOPROBLEMASESTOMACALES="tipoProblemasEstomacales";
    public static final String c_OTROTIPOPROBLEMASESTOMACALES="otroProblemasEstomacales";
    public static final String c_ENFERMEDADPIEL="enfermedadPiel";
    public static final String c_TIPOENFERMEDADPIEL="tipoEnfermedadPiel";
    public static final String c_OTRAENFERMEDADPIEL="otraEnfermedadPiel";
    public static final String c_ABASTECIMIENTOAGUA="abastecimientoAgua";
    public static final String c_NOMBRERIO="nombreRio";
    public static final String c_OTROABASTECIMIENTOAGUA="otroAbastecimientoAgua";
    public static final String c_SISTERNATANQUE="sisternaTanque";
    public static final String c_ORIGENAGUA="origenAgua";
    public static final String c_TRATAMIENTOORIGENAGUA="tratamientoOrigenAgua";
    public static final String c_USOAGUA="usoAgua";
    //TABLA capacidadsisternatanque
    public static final String c_CAPACIDADTANQUE="capacidadTanque";
    public static final String c_CAPACIDADSISTERNA="capacidadSisterna";
    //TABLA cloracionlimpieza
    public static final String c_FRECUENCIALIMPIEZA="frecuenciaLimpieza";
    public static final String c_FRECUENCIACLORACION="frecuenciaCloracion";
    public static final String c_OTROFRECUENCIACLORACION="otroFrecuenciaCloracion";
    public static final String c_DOSISCLORACION="dosisCloracion";
    public static final String c_OTRODOSISCLORACION="otroDosisCloracion";
    //TABLA aguaanimalesriego
    public static final String c_MASCOTASANIMAL="mascotas_animal";
    public static final String c_CONSUMOSANIMAL="consumo_animal";
    public static final String c_VENTAANIMAL="venta_animal";
    public static final String c_ORNAMENTALESRIEGO="ornamentales_riego";
    public static final String c_CONSUMORIEGO="consumo_riego";
    public static final String c_VENTARIEGO="venta_riego";
  //nombre_pers,direccion_pers,edad_pers,sexo_pers,latitud_pers,longitud_pers









    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + c_CODIGO + " VARCHAR, "
            + c_NOMBRE + " VARCHAR, "
            + c_ESTADO + " TINYINT);";


    public static final String CREATE_TABLE2 = "CREATE TABLE " + TABLE_NAME2 + " ("
            + COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
            + c_CODIGO + " VARCHAR, "
            + c_FECHA + " VARCHAR, "
            + c_HORAINICIO + " VARCHAR, "
            + c_HORAFIN + " VARCHAR, "
            + c_FOTO + " TEXT, "
            + c_UTM_LO + " VARCHAR, "
            + c_UTM_LA + " VARCHAR, "
            + c_ESTADO + " TINYINT);";

    public static final String CREATE_TABLE1 = "CREATE TABLE " + TABLE_NAME1 + " ("
            + COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
            + c_CODIGO + " VARCHAR, "
            + c_CODIGO_PERSONA +" VARCHAR, "
            + c_FECHA + " VARCHAR, "
            + c_NUM +" VARCHAR, "//int
            + c_HORAINICIO + " VARCHAR, "
            + c_HORAFIN + " VARCHAR, "
            + c_FOTO + " VARCHAR, "
            + c_TIPOVIVIENDA + " VARCHAR, "
            + c_OTROTIPOVIVIENDA + " VARCHAR, "
            + c_NUMEROPISOS + " VARCHAR, "//int
            + c_TECHO + " VARCHAR, "
            + c_PAREDES + " VARCHAR, "
            + c_PISO + " VARCHAR, "
            + c_VIVIENDA + " VARCHAR, "
            + c_NUMEROPERSONAS + " VARCHAR, "
            + c_PROBLEMASESTOMACALES + " VARCHAR, "
            + c_TIPOPROBLEMASESTOMACALES + " VARCHAR, "
            + c_OTROTIPOPROBLEMASESTOMACALES + " VARCHAR, "
            + c_ENFERMEDADPIEL + " VARCHAR, "
            + c_TIPOENFERMEDADPIEL + " VARCHAR, "
            + c_OTRAENFERMEDADPIEL + " VARCHAR, "
            + c_ABASTECIMIENTOAGUA + " VARCHAR, "
            + c_NOMBRERIO + " VARCHAR, "
            + c_OTROABASTECIMIENTOAGUA + " VARCHAR, "
            + c_SISTERNATANQUE + " VARCHAR, "
            + c_ORIGENAGUA + " VARCHAR, "
            + c_TRATAMIENTOORIGENAGUA + " VARCHAR, "
            + c_USOAGUA + " VARCHAR, "
            + c_CAPACIDADTANQUE + " VARCHAR, "//int
            + c_CAPACIDADSISTERNA + " VARCHAR, "//int
            + c_FRECUENCIALIMPIEZA + " VARCHAR, "
            + c_FRECUENCIACLORACION + " VARCHAR, "
            + c_OTROFRECUENCIACLORACION + " VARCHAR, "
            + c_DOSISCLORACION + " VARCHAR, "
            + c_OTRODOSISCLORACION + " VARCHAR, "
            + c_MASCOTASANIMAL + " VARCHAR, "//int
            + c_CONSUMOSANIMAL + " VARCHAR, "
            + c_VENTAANIMAL + " VARCHAR, "
            + c_ORNAMENTALESRIEGO + " VARCHAR, "//int
            + c_CONSUMORIEGO + " VARCHAR, "
            + c_VENTARIEGO + " VARCHAR, "
            + c_ESTADO + " TINYINT);";

    public static final String CREATE_TABLE3 = "CREATE TABLE " + TABLE_NAME3 + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + c_CODIGO + " VARCHAR, "
            + c_NOMBRE + " VARCHAR, "
            + c_DIRECCION + " VARCHAR, "
            + c_EDAD + " VARCHAR, "
            + c_SEXO + " VARCHAR, "
            + c_UTM_LO + " VARCHAR, "
            + c_UTM_LA + " VARCHAR, "
            + c_ESTADO + " TINYINT);";



    private static SQLiteDatabase database;
    private static MyDbHelper helper;
    private final Context context;
    private UsuarioModel model;


    public UsuarioAdapter(Context context) {
        this.context = context;
        helper=new MyDbHelper(context, DB_NAME, null, DB_VERSION);
    }

    public void openRead(){
        database=helper.getReadableDatabase();
    }
    public void openWrite(){
        database=helper.getWritableDatabase();
    }
    public void close(){
        database.close();
    }


    public UsuarioModel login(String usuario,String codigo){

        String where= "nombre = ? AND codigo = ?";
        Cursor cursor = database.query(TABLE_NAME,null, where, new String[]{usuario, codigo},null, null, null);
        if(cursor.getCount() < 1){
            return null;
        }else{
            cursor.moveToFirst();
            model=new UsuarioModel();
            model.set_contrasena(cursor.getString(cursor.getColumnIndex("codigo")));
            model.set_nombre(cursor.getString(cursor.getColumnIndex("nombre")));
            return model;

        }
    }

    public long insert(UsuarioModel model,int status){
        ContentValues values = new ContentValues();
        values.put("nombre", model.get_nombre());
        values.put("codigo", model.get_contrasena());
        values.put("estado", status);
        return database.insert(TABLE_NAME,null,values);
    }

    //////////////////////////////// METODOS AGREGADOS /////////////////////////

    public boolean addName(String codigo, String name, int status) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(c_CODIGO, codigo);
        contentValues.put(c_NOMBRE, name);
        contentValues.put(c_ESTADO, status);


        db.insert(TABLE_NAME, null, contentValues);
        db.close();
        return true;
    }

    public boolean updateNameStatus(int id, int status) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(c_ESTADO, status);
        db.update(TABLE_NAME, contentValues, COLUMN_ID + "=" + id, null);
        db.close();
        return true;
    }

    public Cursor getNames() {
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + c_CODIGO + " ASC;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public Cursor getUnsyncedNames() {
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + c_ESTADO + " = 0;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    //METODOS AGREGADOS DE LA ENCUESTA2

    public long addName2(String codigo, String fecha, String horaInicio, String horaFin, String Foto,  String Longitud, String Latitud, int status) {



        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(c_CODIGO, codigo);
        contentValues.put(c_FECHA, fecha);
        contentValues.put(c_HORAINICIO, horaInicio);
        contentValues.put(c_HORAFIN, horaFin);
        contentValues.put(c_FOTO, Foto);
        contentValues.put(c_UTM_LO, Longitud);
        contentValues.put(c_UTM_LA, Latitud);
        contentValues.put(c_ESTADO, status);

        long id=db.insert(TABLE_NAME2, null, contentValues);
        db.close();
        return id;
    }
    public boolean updateNameStatus2(int id, int status) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(c_ESTADO, status);
        db.update(TABLE_NAME2, contentValues, COLUMN_ID + "=" + id, null);
        db.close();
        return true;
    }
    public Cursor getNames2() {
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME2 + " ORDER BY " + COLUMN_ID + " ASC;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }
    public Cursor getUnsyncedNames2() {
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME2 + " WHERE " + c_ESTADO + " = 0;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    //METODOS AGREGADOS DE LA ENCUESTA1

    public void addName1(String codigo,String codigo_per, String fecha, String horaInicio, String horaFin, String Foto,
                         String tipoVivienda, String otroTipoVivienda,  String numeroPisos, String techo , String paredes, String piso, String vivienda, String numeroPersonas,
                         String problemasEstomacales,  String tipoProblemasEstomacales, String otroProblemasEstomacales, String enfermedadPiel,  String tipoEnfermedadPiel,
                         String otraEnfermedadPiel, String abastecimientoAgua, String nombreRio, String otroAbastecimientoAgua, String sisternaTanque, String origenAgua,
                         String tratamientoOrigenAgua, String usoAgua, String capacidadTanque, String capacidadSisterna, String frecuenciaLimpieza, String frecuenciaCloracion,
                         String otroFrecuenciaCloracion, String dosisCloracion, String otroDosisCloracion, String mascotas_animal, String consumo_animal, String venta_animal,
                         String ornamentales_riego, String consumo_riego, String venta_riego, int status ) {


        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();



        contentValues.put(c_CODIGO, codigo);
        contentValues.put(c_CODIGO_PERSONA, codigo_per);
        contentValues.put(c_FECHA, fecha);
        contentValues.put(c_HORAINICIO, horaInicio);
        contentValues.put(c_HORAFIN, horaFin);
        contentValues.put(c_FOTO, Foto);
        contentValues.put(c_TIPOVIVIENDA, tipoVivienda);
        contentValues.put(c_OTROTIPOVIVIENDA, otroTipoVivienda);
        contentValues.put(c_NUMEROPISOS, numeroPisos);
        contentValues.put(c_TECHO, techo);
        contentValues.put(c_PAREDES, paredes);
        contentValues.put(c_PISO, piso);
        contentValues.put(c_VIVIENDA, vivienda);
        contentValues.put(c_NUMEROPERSONAS, numeroPersonas);
        contentValues.put(c_PROBLEMASESTOMACALES, problemasEstomacales);
        contentValues.put(c_TIPOPROBLEMASESTOMACALES, tipoProblemasEstomacales);
        contentValues.put(c_OTROTIPOPROBLEMASESTOMACALES, otroProblemasEstomacales);
        contentValues.put(c_ENFERMEDADPIEL, enfermedadPiel);
        contentValues.put(c_TIPOENFERMEDADPIEL, tipoEnfermedadPiel);
        contentValues.put(c_OTRAENFERMEDADPIEL, otraEnfermedadPiel);
        contentValues.put(c_ABASTECIMIENTOAGUA, abastecimientoAgua);
        contentValues.put(c_NOMBRERIO, nombreRio);
        contentValues.put(c_OTROABASTECIMIENTOAGUA, otroAbastecimientoAgua);
        contentValues.put(c_SISTERNATANQUE, sisternaTanque);
        contentValues.put(c_ORIGENAGUA, origenAgua);
        contentValues.put(c_TRATAMIENTOORIGENAGUA, tratamientoOrigenAgua);
        contentValues.put(c_USOAGUA, usoAgua);
        contentValues.put(c_CAPACIDADTANQUE, capacidadTanque);
        contentValues.put(c_CAPACIDADSISTERNA, capacidadSisterna);
        contentValues.put(c_FRECUENCIALIMPIEZA, frecuenciaLimpieza);
        contentValues.put(c_FRECUENCIACLORACION, frecuenciaCloracion);
        contentValues.put(c_OTROFRECUENCIACLORACION, otroFrecuenciaCloracion);
        contentValues.put(c_DOSISCLORACION, dosisCloracion);
        contentValues.put(c_OTRODOSISCLORACION, otroDosisCloracion);
        contentValues.put(c_MASCOTASANIMAL, mascotas_animal);
        contentValues.put(c_CONSUMOSANIMAL, consumo_animal);
        contentValues.put(c_VENTAANIMAL, venta_animal);
        contentValues.put(c_ORNAMENTALESRIEGO, ornamentales_riego);
        contentValues.put(c_CONSUMORIEGO, consumo_riego);
        contentValues.put(c_VENTARIEGO, venta_riego);
        contentValues.put(c_ESTADO, status);





        db.insert(TABLE_NAME1, null, contentValues);

        db.close();


    }
    public boolean updateNameStatus1(int id, int status) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(c_ESTADO, status);

        db.update(TABLE_NAME1, contentValues, COLUMN_ID + "=" + id, null);

        db.close();
        return true;
    }

    public Cursor getNames1() {
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME1 + " ORDER BY " + COLUMN_ID + " ASC;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }
    public Cursor getUnsyncedNames1() {
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME1 + " WHERE " + c_ESTADO + " = 0;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }



    //persona

    public void addName3(String codigo_persona,String nombre_persona,String dir_persona,
                         String edad_persona,String sexo_persona,String longitud_persona,String latitud_persona,int status_persona
    ) {

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(c_CODIGO, codigo_persona);
        contentValues.put(c_NOMBRE, nombre_persona);
        contentValues.put(c_DIRECCION, dir_persona);
        contentValues.put(c_EDAD, edad_persona);
        contentValues.put(c_SEXO, sexo_persona);
        contentValues.put(c_UTM_LO, longitud_persona);
        contentValues.put(c_UTM_LA,latitud_persona);
        contentValues.put(c_ESTADO, status_persona);

        db.insert(TABLE_NAME3, null, contentValues);
        db.close();
    }

    public boolean updateNameStatus3(int id, int status) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(c_ESTADO, status);
        db.update(TABLE_NAME3, contentValues, COLUMN_ID + "=" + id, null);
        db.close();
        return true;
    }

    public Cursor getUnsyncedNames3() {
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME3 + " WHERE " + c_ESTADO + " = 0;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public Cursor getNames3() {
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME3 + " ORDER BY " + COLUMN_ID + " ASC;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }



}
