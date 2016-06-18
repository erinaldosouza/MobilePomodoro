package br.edu.fa7.mobilepomodoro.model;

import android.os.Parcelable;

import java.io.Serializable;

import br.edu.fa7.mobilepomodoro.adapters.RecyclerViewerAdapter;

/**
 * Created by erinaldo.souza on 02/06/2016.
 */
public class PomoTask implements IModel, Serializable{

    private Integer id;

    private String name;

    private Integer quantity;

    private String detail;

    private transient RecyclerViewerAdapter.RecyclerViewHolder holder;

    public PomoTask(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public PomoTask(int id, String name, int quantity, String detail) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.detail = detail;
    }

    public PomoTask() {
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String toString() {
        return " id: " + id + ", name: " + name + ", detail" + detail + ", quantity: " + quantity;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public RecyclerViewerAdapter.RecyclerViewHolder getHolder() {
        return holder;
    }

    public void setHolder(RecyclerViewerAdapter.RecyclerViewHolder holder) {
        this.holder = holder;
    }
}