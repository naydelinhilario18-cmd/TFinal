package com.example.tfinal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {

    public AdminSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table residuos(" +
                "codigo integer primary key autoincrement, " +
                "area text, " +
                "tipo text, " +
                "peso real, " +
                "observaciones text, " +
                "fecha text)");

        db.execSQL("create table usuarios(" +
                "dni text primary key, " +
                "nombre text, " +
                "password text)");

        db.execSQL("insert into usuarios(dni, nombre, password) values('user18', 'Trabajador Ecolim', '1234')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists residuos");
        db.execSQL("drop table if exists usuarios");
        onCreate(db);
    }
}