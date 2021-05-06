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

public class NetworkStateChecker extends BroadcastReceiver {

    private Context context;
    private UsuarioAdapter db;
    private MainActivity main;

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;

        db = new UsuarioAdapter(context);

        /*ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (connectivityManager!=null) {
                NetworkCapabilities networkCapabilities=connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                if (networkCapabilities!=null) {
                    if(networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)||networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)||networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)){
                        //getting all the unsynced names
                        Cursor cursor = db.getUnsyncedNames();
                        if (cursor.moveToFirst()) {
                            do {
                                //calling the method to save the unsynced name to MySQL
                                saveName(
                                        cursor.getString(cursor.getColumnIndex(UsuarioAdapter.c_CODIGO)),
                                        cursor.getString(cursor.getColumnIndex(UsuarioAdapter.c_NOMBRE))
                                );
                            } while (cursor.moveToNext());
                        }
                    }
                }
            }
        }*/


        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        // si hay una red
        if (activeNetwork != null) {
            /// Si está conectado a wifi o plan de datos móviles
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {

                //getting all the unsynced names
                Cursor cursor = db.getUnsyncedNames();
                if (cursor.moveToFirst()) {
                    do {
                        //calling the method to save the unsynced name to MySQL
                        saveName(
                                cursor.getInt(cursor.getColumnIndex(db.COLUMN_ID)),
                                cursor.getString(cursor.getColumnIndex(db.c_CODIGO)),
                                cursor.getString(cursor.getColumnIndex(db.c_NOMBRE))
                        );
                    } while (cursor.moveToNext());
                }
            }
        }

    }

    private void saveName(final int id, final String codigo, final String nombre) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.URL_SAVE_NAME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                // actualizando el estado en sqlite
                                db.updateNameStatus(id,main.NAME_SYNCED_WITH_SERVER);

                                // enviando la transmisión para actualizar la lista
                                context.sendBroadcast(new Intent(MainActivity.DATA_SAVED_BROADCAST));
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
                params.put("nombre", nombre);

                return params;
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }




}
