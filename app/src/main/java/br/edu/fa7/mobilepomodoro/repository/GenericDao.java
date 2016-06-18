package br.edu.fa7.mobilepomodoro.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.edu.fa7.mobilepomodoro.model.IModel;
import br.edu.fa7.mobilepomodoro.model.PomoTask;

/**
 * Created by erinaldo.souza on 02/06/2016.
 */
public abstract class GenericDao<T extends IModel> implements IDao<T> {
    private SQLiteDatabase db;
    private String table;

    public GenericDao(Context context, String table) {
        this.db = new DBHelper(context).getWritableDatabase();
        this.table = table;
    }


    @Override
    public int create(T t) {
        ContentValues values = getContenValues(t);
        int inserted = (int)db.insert(table, "_id = ?", values);
        Log.i("DAO", "Task id: " + inserted + " inserted!");
        return inserted;
    }

    @Override
    public List<T> readAll() {
        List<T> objs = new ArrayList<>();
        Cursor cursor = db.query(table, null, null, null, null, null, " _id DESC");

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                objs.add(getObjectFromCursor(cursor));
            }
        }

        return objs;
    }


    @Override
    public T read(Number id) {
        T obj  = null;
        Cursor cursor = db.query(table, null, "_id = ?", new String[]{id.toString()}, null, null, null, null);
        if(cursor.moveToFirst()) {
            obj = getObjectFromCursor(cursor);
        }
        return obj;
    }

    @Override
    public int update(T objeto) {
        ContentValues values = getContenValues(objeto);
        return db.update(table, values, " _id = ?", new String[]{objeto.getId().toString()});
    }

    @Override
    public int delete(Number id) {
        return db.delete(table, " _id = ?", new String[]{id.toString()});
    }

    /*método dependente da implementação*/
    public abstract T getObjectFromCursor(Cursor cursor);

    /*método dependente da implementação*/
    public abstract ContentValues getContenValues(T obj);
}