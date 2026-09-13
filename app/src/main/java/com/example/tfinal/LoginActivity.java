package com.example.tfinal;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsuario, etPassword;
    private Button btnIngresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsuario = findViewById(R.id.etUsuario);
        etPassword = findViewById(R.id.etPassword);
        btnIngresar = findViewById(R.id.btnIngresar);

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarLogin();
            }
        });
    }

    private void validarLogin() {
        String usuario = etUsuario.getText().toString().trim();
        String pass = etPassword.getText().toString().trim();

        if (!usuario.isEmpty() && !pass.isEmpty()) {
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "EcolimBD", null, 1);
            SQLiteDatabase bd = admin.getWritableDatabase();

            Cursor fila = bd.rawQuery("select nombre from usuarios where dni='" + usuario + "' and password='" + pass + "'", null);

            if (fila.moveToFirst()) {
                String nombreOperario = fila.getString(0);
                Toast.makeText(this, "¡Bienvenido " + nombreOperario + "!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "DNI o contraseña incorrectos", Toast.LENGTH_SHORT).show();
            }
            bd.close();
        } else {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
        }
    }
}