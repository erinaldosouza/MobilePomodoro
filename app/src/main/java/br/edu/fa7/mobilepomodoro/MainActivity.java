package br.edu.fa7.mobilepomodoro;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.List;

import br.edu.fa7.mobilepomodoro.activity.NewTaskActivity;
import br.edu.fa7.mobilepomodoro.adapters.RecyclerViewerAdapter;
import br.edu.fa7.mobilepomodoro.business.PomoTaskBusiness;
import br.edu.fa7.mobilepomodoro.model.PomoTask;
import br.edu.fa7.mobilepomodoro.repository.PomoTaskDao;
import br.edu.fa7.mobilepomodoro.service.BoundCountDownService;
import br.edu.fa7.mobilepomodoro.service.Observer;

public class MainActivity extends AppCompatActivity implements Observer {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private PomoTaskBusiness business;
    private RecyclerView mRecyclerView;
    private TextView noTasks;
    private static PomoTask task;
    private static BoundCountDownService boundCountDownService;
    private static TextView taskDetail;
    private static TextView txtCounter;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerViewerAdapter recyclerViewerAdapter;

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if(boundCountDownService == null) {
                BoundCountDownService.LocalBinder binder = (BoundCountDownService.LocalBinder) service;
                boundCountDownService = binder.getService();
                boundCountDownService.register(MainActivity.this);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Mobile Pomodoro");
        toolbar.setSubtitle("Seja Mais Produtivo!");
        toolbar.setLogo(R.mipmap.ic_pomodoro);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addTask);
        txtCounter = (TextView) findViewById(R.id.txtCounter);
        taskDetail = (TextView) findViewById(R.id.taskDetail);

        if(savedInstanceState != null) {
            boundCountDownService = (BoundCountDownService) savedInstanceState.get("boundCountDownService");
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Boolean.TRUE.equals(RecyclerViewerAdapter.isPlaying())) {
                    Toast.makeText(MainActivity.this, "Conclua a tarefa corrente antes de cadastrar uma nova tarefa!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent in = new Intent(MainActivity.this, NewTaskActivity.class);
                    startActivity(in);
                }
            }
        });

        noTasks = (TextView) findViewById(R.id.txtNoTasks);

        business = new PomoTaskBusiness(new PomoTaskDao(this));
        List<PomoTask> tasks = business.readAll();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        RecyclerViewerAdapter recyclerViewerAdapter = new RecyclerViewerAdapter(tasks, this);
        mRecyclerView = (RecyclerView) findViewById(R.id.taskRecyclerView);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(recyclerViewerAdapter);

        if(!tasks.isEmpty()) {
            noTasks.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.INVISIBLE);
            noTasks.setVisibility(View.VISIBLE);
        }

        if(getIntent().getExtras() != null){
            task = (PomoTask)getIntent().getExtras().get("task");
            this.startCounter();
        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public static void startTask(PomoTask task) {
        boundCountDownService.startTask(task.getQuantity());
        txtCounter.setText("25:00");
        MainActivity.task = task;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        Intent in = new Intent(this, BoundCountDownService.class);
        bindService(in, mConnection, Context.BIND_AUTO_CREATE);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://br.edu.fa7.mobilepomodoro/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();
        unbindService(mConnection);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://br.edu.fa7.mobilepomodoro/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void notification(String str) {
        String title = null;
        if(boundCountDownService.getQtde() == 0) {

            taskDetail.setText("Sucesso!");
            txtCounter.setText("0:0");

            title = "Tarefa realizada com sucesso!";
            RecyclerViewerAdapter.setPlaying(false);
            ((CardView)task.getHolder().itemView).setCardBackgroundColor(Color.GREEN);

        } else {
            title = "De olho no cronometro!";
            String det = task.getName() + " - " + (1 + task.getQuantity() - boundCountDownService.getQtde()) + " de " + task.getQuantity();

            if(str.contains("DESCANSE")) {
                det += " - " + str.replaceAll("[0-9]", "").replaceAll("-", "");
            }

            this.taskDetail.setText(det);

            txtCounter.setText(str.replaceAll("[a-zA-Z]", "").replaceAll("-", ""));
        }

        if(str.equals("0:0")) {
            Intent in = new Intent(this, MainActivity.class);
            TaskStackBuilder tb = TaskStackBuilder.create(this);
            tb.addNextIntent(in);
            tb.addParentStack(MainActivity.class);

            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            Notification.Builder builder = new Notification.Builder(this)
                    .setSmallIcon(R.mipmap.ic_pomodoro)
                    .setContentTitle(title)
                    .setContentText(task.getName())
                    .setAutoCancel(true);
            //Não é preciso que a notificaçao abra nenhuma activity, service ou receive
            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            builder.setSound(soundUri);

            nm.notify(1, builder.build());
        }
    }

    public void startCounter() {
        txtCounter.setVisibility(View.VISIBLE);
        boundCountDownService.setQtde(task.getQuantity());
        boundCountDownService.startTask(task.getQuantity());
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopService(new Intent(this, BoundCountDownService.class));
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putSerializable("boundCountDownService", boundCountDownService);
    }
}