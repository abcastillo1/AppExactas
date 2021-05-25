package com.example.appvinculacion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity3 extends AppCompatActivity {

    private UsuarioAdapter db;
    //View objects
    private ListView listViewNames;
    private List<Name3> names;
    private BroadcastReceiver broadcastReceiver;
    public static final int NAME_SYNCED_WITH_SERVER = 1;
    public static final int NAME_NOT_SYNCED_WITH_SERVER = 0;
    private NameAdapter3 nameAdapter;
    public static final String URL_SAVE_NAME = "http://192.168.1.7/sincronizar/persona.php";
    public static final String DATA_SAVED_BROADCAST = "net.simplifiedcoding.datasaved";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        registerReceiver(new NetworkStateChecker3(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

            db = new UsuarioAdapter(this);
            names = new ArrayList<>();


            listViewNames = (ListView) findViewById(R.id.listViewNames);

            //adding click listener to button


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
        private void loadNames() {
            names.clear();
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
                    names.add(name);
                } while (cursor.moveToNext());
            }

            nameAdapter = new NameAdapter3(this, R.layout.names, names);
            listViewNames.setAdapter(nameAdapter);
            refreshList();
        }

        private void refreshList() {
            nameAdapter.notifyDataSetChanged();
        }

}