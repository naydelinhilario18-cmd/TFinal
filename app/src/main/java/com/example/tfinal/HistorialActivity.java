package com.example.tfinal;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class HistorialActivity extends AppCompatActivity {

    private ListView listViewRegistros;
    private Button btnVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        listViewRegistros = findViewById(R.id.listViewRegistros);
        btnVolver = findViewById(R.id.btnVolver);

        cargarRegistros();

        btnVolver.setOnClickListener(v -> finish());
    }

    private void cargarRegistros() {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "EcolimBD", null, 1);
        SQLiteDatabase bd = admin.getReadableDatabase();

        ArrayList<String> listaTransacciones = new ArrayList<>();
        Cursor cursor = bd.rawQuery("select codigo, area, tipo, peso, observaciones, fecha from residuos", null);

        if (cursor.moveToFirst()) {
            do {
                String item = "ID Registro: " + cursor.getInt(0) +
                        "\nÁrea: " + cursor.getString(1) +
                        "\nResiduo: " + cursor.getString(2) +
                        "\nPeso: " + cursor.getDouble(3) + " Kg" +
                        "\nObservación: " + cursor.getString(4) +
                        "\nFecha/Hora: " + cursor.getString(5) +
                        "\n---------------------------------------------";
                listaTransacciones.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        bd.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaTransacciones);
        listViewRegistros.setAdapter(adapter);
    }
}