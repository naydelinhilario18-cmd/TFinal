package com.example.tfinal;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText etCodigo, etPeso, etFecha;
    private Spinner spTipoResiduo;
    private TextView tvReporte;
    private Button btnRegistrar, btnBuscar, btnModificar, btnEliminar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etCodigo = findViewById(R.id.etCodigo);
        etPeso = findViewById(R.id.etPeso);
        etFecha = findViewById(R.id.etFecha);
        spTipoResiduo = findViewById(R.id.spTipoResiduo);
        tvReporte = findViewById(R.id.tvReporte);

        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnBuscar = findViewById(R.id.btnBuscar);
        btnModificar = findViewById(R.id.btnModificar);
        btnEliminar = findViewById(R.id.btnEliminar);

        btnRegistrar.setOnClickListener(v -> registrarResiduo());
        btnBuscar.setOnClickListener(v -> buscarResiduo());
        btnModificar.setOnClickListener(v -> modificarResiduo());
        btnEliminar.setOnClickListener(v -> eliminarResiduo());

        actualizarReporteSQLite();
    }

    private void registrarResiduo() {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "EcolimBD", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        String tipo = spTipoResiduo.getSelectedItem().toString();
        String peso = etPeso.getText().toString().trim();
        String fecha = etFecha.getText().toString().trim();

        if (!peso.isEmpty() && !fecha.isEmpty()) {
            ContentValues registro = new ContentValues();
            registro.put("tipo", tipo);
            registro.put("peso", Double.parseDouble(peso));
            registro.put("fecha", fecha);

            long id = bd.insert("residuos", null, registro);
            bd.close();

            if (id != -1) {
                Toast.makeText(this, "Registrado con Cód: " + id, Toast.LENGTH_SHORT).show();
                limpiarCampos();
                actualizarReporteSQLite();
            }
        } else {
            Toast.makeText(this, "Ingrese peso y fecha", Toast.LENGTH_SHORT).show();
        }
    }

    private void buscarResiduo() {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "EcolimBD", null, 1);
        SQLiteDatabase bd = admin.getReadableDatabase();
        String codigo = etCodigo.getText().toString().trim();

        if (!codigo.isEmpty()) {
            Cursor fila = bd.rawQuery("select tipo, peso, fecha from residuos where codigo=" + codigo, null);
            if (fila.moveToFirst()) {
                etPeso.setText(fila.getString(1));
                etFecha.setText(fila.getString(2));
                String tipo = fila.getString(0);
                ArrayAdapter adapter = (ArrayAdapter) spTipoResiduo.getAdapter();
                for (int i = 0; i < adapter.getCount(); i++) {
                    if (adapter.getItem(i).toString().equals(tipo)) {
                        spTipoResiduo.setSelection(i);
                        break;
                    }
                }
                Toast.makeText(this, "Encontrado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No existe el código", Toast.LENGTH_SHORT).show();
            }
            bd.close();
        } else {
            Toast.makeText(this, "Ingrese código a buscar", Toast.LENGTH_SHORT).show();
        }
    }

    private void modificarResiduo() {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "EcolimBD", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        String codigo = etCodigo.getText().toString().trim();
        String peso = etPeso.getText().toString().trim();
        String fecha = etFecha.getText().toString().trim();

        if (!codigo.isEmpty() && !peso.isEmpty() && !fecha.isEmpty()) {
            ContentValues registro = new ContentValues();
            registro.put("tipo", spTipoResiduo.getSelectedItem().toString());
            registro.put("peso", Double.parseDouble(peso));
            registro.put("fecha", fecha);

            int cantidad = bd.update("residuos", registro, "codigo=" + codigo, null);
            bd.close();

            if (cantidad == 1) {
                Toast.makeText(this, "Modificado", Toast.LENGTH_SHORT).show();
                limpiarCampos();
                actualizarReporteSQLite();
            } else {
                Toast.makeText(this, "Código no existe", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Complete los campos", Toast.LENGTH_SHORT).show();
        }
    }

    private void eliminarResiduo() {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "EcolimBD", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        String codigo = etCodigo.getText().toString().trim();

        if (!codigo.isEmpty()) {
            int cantidad = bd.delete("residuos", "codigo=" + codigo, null);
            bd.close();

            if (cantidad == 1) {
                Toast.makeText(this, "Eliminado", Toast.LENGTH_SHORT).show();
                limpiarCampos();
                actualizarReporteSQLite();
            } else {
                Toast.makeText(this, "Código no existe", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Ingrese código", Toast.LENGTH_SHORT).show();
        }
    }

    private void actualizarReporteSQLite() {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "EcolimBD", null, 1);
        SQLiteDatabase bd = admin.getReadableDatabase();
        Cursor cursor = bd.rawQuery("select codigo, tipo, peso, fecha from residuos", null);
        StringBuilder reporte = new StringBuilder();

        if (cursor.moveToFirst()) {
            do {
                reporte.append("Cód: ").append(cursor.getInt(0))
                        .append(" | ").append(cursor.getString(1))
                        .append(" | ").append(cursor.getDouble(2)).append(" Kg")
                        .append(" | ").append(cursor.getString(3))
                        .append("\n-----------------------------------\n");
            } while (cursor.moveToNext());
            tvReporte.setText(reporte.toString());
        } else {
            tvReporte.setText("No hay registros guardados aún.");
        }
        cursor.close();
        bd.close();
    }

    private void limpiarCampos() {
        etCodigo.setText("");
        etPeso.setText("");
        etFecha.setText("");
        spTipoResiduo.setSelection(0);
    }
}