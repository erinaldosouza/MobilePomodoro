package br.edu.fa7.mobilepomodoro.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import br.edu.fa7.mobilepomodoro.MainActivity;
import br.edu.fa7.mobilepomodoro.R;
import br.edu.fa7.mobilepomodoro.business.PomoTaskBusiness;
import br.edu.fa7.mobilepomodoro.model.PomoTask;
import br.edu.fa7.mobilepomodoro.repository.PomoTaskDao;

/**
 * Created by erinaldo.souza on 04/06/2016.
 */
public class NewTaskActivity extends AppCompatActivity implements View.OnClickListener {

    private int defaultColor;
    private PomoTaskBusiness business;
    private PomoTask task = null;
    private TextView lblTaskName, lblTaskDetail, lblTaskQuntity;
    private EditText txtTaskName, txtTaskDetail, txtTaskQuantity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        findViewById(R.id.btnSaveTask).setOnClickListener(this);

        business = new PomoTaskBusiness(new PomoTaskDao(this));
        lblTaskName = (TextView) findViewById(R.id.lblTaskName);
        lblTaskDetail = (TextView)findViewById(R.id.lblTaskDetail);
        lblTaskQuntity = (TextView)findViewById(R.id.lblPomoQuantity);

        defaultColor = lblTaskName.getCurrentTextColor();

        txtTaskName = (EditText) findViewById(R.id.txtTaskName);
        txtTaskDetail = (EditText) findViewById(R.id.txtTaskDetail);
        txtTaskQuantity = (EditText) findViewById(R.id.txtPomoQuantity);

        Intent in = getIntent();
        if(in.getExtras() != null) {
            task = (PomoTask) in.getExtras().get("task");
            txtTaskName.setText(task.getName());
            txtTaskDetail.setText(task.getDetail());
            txtTaskQuantity.setText(task.getQuantity().toString());
        }
    }

    @Override
    public void onClick(View v) {
        lblTaskName.setTextColor(defaultColor);
        lblTaskDetail.setTextColor(defaultColor);
        lblTaskQuntity.setTextColor(defaultColor);

        String value = txtTaskName.getText().toString();
        boolean hasErros = false;

        if(task == null) {
            task = new PomoTask();
        }

        if(value == null || value.isEmpty()){
            lblTaskName.setTextColor(Color.RED);
            hasErros = true;
        }
        task.setName(value);

        value = txtTaskDetail.getText().toString();
        if(value == null || value.isEmpty()) {
            lblTaskDetail.setTextColor(Color.RED);
            hasErros = true;
        }
        task.setDetail(value);

        value = txtTaskQuantity.getText().toString();
        if(value == null || value.isEmpty()) {
            lblTaskQuntity.setTextColor(Color.RED);
            hasErros = true;
        } else {
            task.setQuantity(Integer.valueOf(value));
        }

        if(hasErros) {
            Toast.makeText(this, "Todos os campos são obrigatórios", Toast.LENGTH_SHORT).show();
        } else {
            txtTaskName.setText("");
            txtTaskDetail.setText("");
            txtTaskQuantity.setText("");

            int newTaskId;
            if(task.getId() != null) {
                newTaskId = business.update(task);

            } else {
                newTaskId = business.create(task);
            }

            if(newTaskId >= 0) {
                Toast.makeText(this, "Tarefa: " + task.getName().toUpperCase() + (task.getId() != null ? " atualizada com sucesso!" :" incluída com sucesso!"), Toast.LENGTH_LONG).show();
                task = null;
                startActivity(new Intent(this, MainActivity.class));
            }
        }
    }
}