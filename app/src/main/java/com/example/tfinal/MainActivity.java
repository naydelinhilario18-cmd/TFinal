package com.example.tfinal;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private Spinner spArea, spTipoResiduo;
    private EditText etPeso, etObservaciones;
    private Button btnRegistrar, btnVerRegistros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spArea = findViewById(R.id.spArea);
        spTipoResiduo = findViewById(R.id.spTipoResiduo);
        etPeso = findViewById(R.id.etPeso);
        etObservaciones = findViewById(R.id.etObservaciones);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnVerRegistros = findViewById(R.id.btnVerRegistros);

        String[] opcionesAreas = {"Zona Norte", "Zona Sur", "Oficinas Administrativas", "Almacén Principal", "Planta de Producción"};
        ArrayAdapter<String> adapterArea = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opcionesAreas);
        adapterArea.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spArea.setAdapter(adapterArea);

        String[] opcionesResiduos = {"Plástico", "Papel / Cartón", "Vidrio", "Orgánico", "Peligroso / RAEE"};
        ArrayAdapter<String> adapterResiduos = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opcionesResiduos);
        adapterResiduos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTipoResiduo.setAdapter(adapterResiduos);

        btnRegistrar.setOnClickListener(v -> registrarResiduoEcologico());

        btnVerRegistros.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistorialActivity.class);
            startActivity(intent);
        });
    }

    private void registrarResiduoEcologico() {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "EcolimBD", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        String area = spArea.getSelectedItem().toString();
        String tipo = spTipoResiduo.getSelectedItem().toString();
        String peso = etPeso.getText().toString().trim();
        String observaciones = etObservaciones.getText().toString().trim();

        String fechaHoraActual = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        if (!peso.isEmpty()) {
            ContentValues registro = new ContentValues();
            registro.put("area", area);
            registro.put("tipo", tipo);
            registro.put("peso", Double.parseDouble(peso));
            registro.put("observaciones", observaciones);
            registro.put("fecha", fechaHoraActual);

            long id = bd.insert("residuos", null, registro);
            bd.close();

            if (id != -1) {
                Toast.makeText(this, "¡Registrado con éxito!", Toast.LENGTH_LONG).show();
                limpiarCampos();
            } else {
                Toast.makeText(this, "Error al registrar en la base de datos", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Por favor ingrese el peso del residuo", Toast.LENGTH_SHORT).show();
        }
    }

    private void limpiarCampos() {
        spArea.setSelection(0);
        spTipoResiduo.setSelection(0);
        etPeso.setText("");
        etObservaciones.setText("");
    }
}