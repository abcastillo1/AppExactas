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

public class NetworkStateChecker1 extends BroadcastReceiver {
    private Context context;
    private UsuarioAdapter db;
    private MainActivity1 main;

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
                Cursor cursor = db.getUnsyncedNames1();
                if (cursor.moveToFirst()) {
                    do {
                        //calling the method to save the unsynced name to MySQL
                        saveName(
                                cursor.getInt(cursor.getColumnIndex(db.COLUMN_ID)),
                                cursor.getString(cursor.getColumnIndex(db.c_CODIGO)),
                                cursor.getString(cursor.getColumnIndex(db.c_FECHA)),
                                cursor.getString(cursor.getColumnIndex(db.c_HORAINICIO)),
                                cursor.getString(cursor.getColumnIndex(db.c_HORAFIN)),
                                cursor.getString(cursor.getColumnIndex(db.c_FOTO)),
                                cursor.getString(cursor.getColumnIndex(db.c_TIPOVIVIENDA )),
                                cursor.getString(cursor.getColumnIndex(db.c_OTROTIPOVIVIENDA)),
                                cursor.getInt(cursor.getColumnIndex(db.c_NUMEROPISOS)),
                                cursor.getString(cursor.getColumnIndex(db.c_TECHO)),
                                cursor.getString(cursor.getColumnIndex(db.c_PAREDES)),
                                cursor.getString(cursor.getColumnIndex(db.c_PISO)),
                                cursor.getString(cursor.getColumnIndex(db.c_VIVIENDA)),
                                cursor.getString(cursor.getColumnIndex(db.c_NUMEROPERSONAS)),
                                cursor.getInt(cursor.getColumnIndex(db.c_PROBLEMASESTOMACALES)),
                                cursor.getString(cursor.getColumnIndex(db.c_TIPOPROBLEMASESTOMACALES)),
                                cursor.getString(cursor.getColumnIndex(db.c_OTROTIPOPROBLEMASESTOMACALES)),
                                cursor.getInt(cursor.getColumnIndex(db.c_ENFERMEDADPIEL)),
                                cursor.getString(cursor.getColumnIndex(db.c_TIPOENFERMEDADPIEL)),
                                cursor.getString(cursor.getColumnIndex(db.c_OTRAENFERMEDADPIEL)),
                                cursor.getString(cursor.getColumnIndex(db.c_ABASTECIMIENTOAGUA)),
                                cursor.getString(cursor.getColumnIndex(db.c_NOMBRERIO)),
                                cursor.getString(cursor.getColumnIndex(db.c_OTROABASTECIMIENTOAGUA)),
                                cursor.getString(cursor.getColumnIndex(db.c_SISTERNATANQUE)),
                                cursor.getString(cursor.getColumnIndex(db.c_ORIGENAGUA)),
                                cursor.getString(cursor.getColumnIndex(db.c_TRATAMIENTOORIGENAGUA)),
                                cursor.getString(cursor.getColumnIndex(db.c_USOAGUA)),
                                cursor.getInt(cursor.getColumnIndex(db.c_CAPACIDADTANQUE)),
                                cursor.getInt(cursor.getColumnIndex(db.c_CAPACIDADSISTERNA)),
                                cursor.getString(cursor.getColumnIndex(db.c_FRECUENCIALIMPIEZA)),
                                cursor.getString(cursor.getColumnIndex(db.c_FRECUENCIACLORACION)),
                                cursor.getString(cursor.getColumnIndex(db.c_OTROFRECUENCIACLORACION)),
                                cursor.getString(cursor.getColumnIndex(db.c_DOSISCLORACION)),
                                cursor.getString(cursor.getColumnIndex(db.c_OTRODOSISCLORACION)),
                                cursor.getInt(cursor.getColumnIndex(db.c_MASCOTASANIMAL)),
                                cursor.getString(cursor.getColumnIndex(db.c_CONSUMOSANIMAL)),
                                cursor.getString(cursor.getColumnIndex(db.c_VENTAANIMAL)),
                                cursor.getInt(cursor.getColumnIndex(db.c_ORNAMENTALESRIEGO)),
                                cursor.getString(cursor.getColumnIndex(db.c_CONSUMORIEGO)),
                                cursor.getString(cursor.getColumnIndex(db.c_VENTARIEGO)),
                                cursor.getInt(cursor.getColumnIndex(db.c_ESTADO))

                        );
                    } while (cursor.moveToNext());
                }
            }
        }

    }
    private void saveName(final int id,final String codigo,final  String fecha,final String horaInicio,final String horaFin,final String foto,
                          final String tipoVivienda, final String otroTipoVivienda, final int numeroPisos, final String techo ,
                          final String paredes, final String piso, final String vivienda, final String numeroPersonas,
                          final int problemasEstomacales,  final String tipoProblemasEstomacales, final String otroProblemasEstomacales, final int enfermedadPiel,  final String tipoEnfermedadPiel,
                          final String otraEnfermedadPiel, final String abastecimientoAgua, final String nombreRio, final String otroAbastecimientoAgua, final String sisternaTanque, final String origenAgua,
                          final String tratamientoOrigenAgua, final String usoAgua, final int capacidadTanque, final int capacidadSisterna, final String frecuenciaLimpieza, final String frecuenciaCloracion,
                          final String otroFrecuenciaCloracion, final String dosisCloracion, final String otroDosisCloracion, final int mascotas_animal, final String consumo_animal, final String venta_animal,
                          final int ornamentales_riego, final String consumo_riego, final String venta_riego, final int status





    ) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity1.URL_SAVE_NAME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj1 = new JSONObject(response);
                            if (!obj1.getBoolean("error")) {
                                // actualizando el estado en sqlite
                                db.updateNameStatus1(id,main.NAME_SYNCED_WITH_SERVER);

                                // enviando la transmisión para actualizar la lista
                                context.sendBroadcast(new Intent(MainActivity1.DATA_SAVED_BROADCAST));
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
                params.put("codigo", codigo);
                params.put("fecha", fecha);
                params.put("horaInicio", horaInicio);
                params.put("horaFin", horaFin);
                params.put("foto", foto);
                params.put("tipoVivienda",tipoVivienda );
                params.put("otroTipoVivienda", otroTipoVivienda);
                params.put("numeroPisos",String.valueOf(numeroPisos));
                params.put("techo",techo);
                params.put("paredes",paredes );
                params.put("piso",piso );
                params.put("vivienda",vivienda);
                params.put("numeroPersonas",numeroPersonas);
                params.put("problemasEstomacales",String.valueOf(problemasEstomacales));
                params.put("tipoProblemasEstomacales",tipoProblemasEstomacales);
                params.put("otroProblemasEstomacales",otroProblemasEstomacales);
                params.put("enfermedadPiel",String.valueOf(enfermedadPiel));
                params.put("tipoEnfermedadPiel",tipoEnfermedadPiel );
                params.put("otraEnfermedadPiel",otraEnfermedadPiel );
                params.put("abastecimientoAgua",abastecimientoAgua );
                params.put("nombreRio",nombreRio );
                params.put("otroAbastecimientoAgua",otroAbastecimientoAgua);
                params.put("sisternaTanque",sisternaTanque );
                params.put("origenAgua",origenAgua );
                params.put("tratamientoOrigenAgua",tratamientoOrigenAgua);
                params.put("usoAgua",usoAgua );
                params.put("capacidadTanque",String.valueOf(capacidadTanque));
                params.put("capacidadSisterna",String.valueOf(capacidadSisterna));
                params.put("frecuenciaLimpieza",frecuenciaLimpieza);
                params.put("frecuenciaCloracion",frecuenciaCloracion);
                params.put("otroFrecuenciaCloracion",otroFrecuenciaCloracion);
                params.put("dosisCloracion",dosisCloracion);
                params.put("otroDosisCloracion",otroDosisCloracion);
                params.put("mascotas_animal",String.valueOf(mascotas_animal));
                params.put("consumo_animal",consumo_animal );
                params.put("venta_animal",venta_animal );
                params.put("ornamentales_riego",String.valueOf(ornamentales_riego));
                params.put("consumo_riego",consumo_riego );
                params.put("venta_riego",venta_riego );

                return params;
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

}
