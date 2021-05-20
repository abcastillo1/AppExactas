package com.example.appvinculacion;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NetworkStateChecker2 extends BroadcastReceiver {

    private Context context;
    private UsuarioAdapter db;
    private MainActivity2 main;

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;

        db = new UsuarioAdapter(context);

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        // si hay una red
        if (activeNetwork != null) {
            /// Si está conectado a wifi o plan de datos móviles
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {

                //getting all the unsynced names
                Cursor cursor = db.getUnsyncedNames2();


                if (cursor.moveToFirst()) {
                    do {
                        //calling the method to save the unsynced name to MySQL
                        saveName(

                                cursor.getInt(cursor.getColumnIndex(db.COLUMN_ID)),
                                cursor.getString(cursor.getColumnIndex(db.c_CODIGO)),
                                cursor.getString(cursor.getColumnIndex(db.c_CODIGO_PERSONA)),
                                cursor.getString(cursor.getColumnIndex(db.c_CODIGOENCUESTA)),
                                cursor.getString(cursor.getColumnIndex(db.c_FECHA)),
                                cursor.getString(cursor.getColumnIndex(db.c_HORAINICIO)),
                                cursor.getString(cursor.getColumnIndex(db.c_HORAFIN)),
                                cursor.getString(cursor.getColumnIndex(db.c_HORAMUESTRA)),
                                cursor.getString(cursor.getColumnIndex(db.c_FOTO)),
                                cursor.getString(cursor.getColumnIndex(db.c_LUGAR)),
                                cursor.getString(cursor.getColumnIndex(db.c_ASPECTO)),
                                cursor.getString(cursor.getColumnIndex(db.c_OBSERVACIONES))

                        );
                    } while (cursor.moveToNext());
                }
            }
        }

    }

    private void saveName(final int id,
                          final String codigo,
                          final String codigoPersona,
                          final String codigoEncuesta,
                          final String fecha,
                          final String horaInicio,
                          final String horaFin,
                          final String horaMuestra,
                          final String foto,
                          final String lugar,
                          final String aspecto,
                          final String observaciones) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity2.URL_SAVE_NAME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj2 = new JSONObject(response);
                            if (!obj2.getBoolean("error")) {
                                // actualizando el estado en sqlite
                                db.updateNameStatus2(id,main.NAME_SYNCED_WITH_SERVER);

                                // enviando la transmisión para actualizar la lista
                                context.sendBroadcast(new Intent(MainActivity2.DATA_SAVED_BROADCAST));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_encuestador",codigo);
                params.put("id_pers",codigoPersona);
                params.put("lugar",lugar);
                params.put("codigo",codigoEncuesta);
                params.put("aspecto",aspecto);
                params.put("observaciones",observaciones);
                params.put("foto",foto);
                params.put("fecha",fecha);
                params.put("horaInicio",horaInicio);
                params.put("horaFin",horaFin);
                params.put("horaMuestra",horaMuestra);

                return params;
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

}
