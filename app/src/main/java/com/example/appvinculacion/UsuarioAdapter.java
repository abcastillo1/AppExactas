package com.example.appvinculacion;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class UsuarioAdapter {

    //nombre de la base de datos
    private final String DB_NAME = "PROYECTO_EXACTAS1";
    //version de la base de datos
    private final int DB_VERSION = 1;
    //nombre de la tabla
    public static final String TABLE_NAME = "usuario";

    public static final String TABLE_NAME2 = "encuesta2";

    //nombre de la tabla
    public static final String COLUMN_ID="id";
    public static final String c_NOMBRE="nombre";
    public static final String c_CODIGO="codigo";
    public static final String c_ESTADO="estado";

    public static final String c_FECHA="fecha";
    public static final String c_HORAINICIO="horaInicio";
    public static final String c_HORAFIN="horaFin";
    public static final String c_FOTO="foto";
    public static final String c_UTM_LO="Longitud";
    public static final String c_UTM_LA="Latitud";











    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
            + c_CODIGO + " VARCHAR, "
            + c_NOMBRE + " TEXT, "
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

    //METODOS AGREGADOS DE LA ENCUESTA

    public boolean addName2(String codigo, String fecha, String horaInicio, String horaFin, String Foto,  String Longitud, String Latitud, int status) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(c_CODIGO, codigo);
        contentValues.put(c_NOMBRE, fecha);
        contentValues.put(c_HORAINICIO, horaInicio);
        contentValues.put(c_HORAFIN, horaFin);
        contentValues.put(c_FOTO, Foto);
        contentValues.put(c_UTM_LO, Longitud);
        contentValues.put(c_UTM_LA, Latitud);
        contentValues.put(c_ESTADO, status);

        db.insert(TABLE_NAME2, null, contentValues);
        db.close();
        return true;
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


}
