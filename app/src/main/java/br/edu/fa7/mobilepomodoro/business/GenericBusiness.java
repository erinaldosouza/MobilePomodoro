package br.edu.fa7.mobilepomodoro.business;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.edu.fa7.mobilepomodoro.model.IModel;
import br.edu.fa7.mobilepomodoro.repository.DBHelper;
import br.edu.fa7.mobilepomodoro.repository.GenericDao;
import br.edu.fa7.mobilepomodoro.repository.IDao;
import br.edu.fa7.mobilepomodoro.repository.PomoTaskDao;

/**
 * Created by erinaldo.souza on 02/06/2016.
 */
public abstract class GenericBusiness<T extends IModel> implements IBusiness<T> {
    protected GenericDao<T> dao;

    public GenericBusiness(GenericDao<T> dao) {
        this.dao = dao;
    }

    @Override
    public int create(T t) {
        return dao.create(t);
    }

    @Override
    public List<T> readAll() {
        return dao.readAll();
    }


    @Override
    public T read(Number id) {
        return dao.read(id);
    }

    @Override
    public int update(T objeto) {
       return dao.update(objeto);
    }

    @Override
    public int delete(Number id) {
        return dao.delete(id);
    }
}