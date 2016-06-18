package br.edu.fa7.mobilepomodoro.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Observable;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by erinaldo.souza on 09/06/2016.
 */
public class BoundCountDownService extends Service implements Subject, Serializable {

    private final IBinder binder;
    private  Thread runner;
    private static boolean start = false;
    private static CountDownTimer counter;
    private CountDownTimer restCounter;

    private String timeStr;
    private int qtde;

    public void setQtde(int qtde) {
        this.qtde = qtde;
    }

    public int getQtde() {
        return qtde;
    }

    private Set<Observer> observers = new HashSet<>();

    public BoundCountDownService() {

        this.binder = new LocalBinder();
    }

    public void startTask(final int qtdeTask) {
        qtde = qtdeTask;
        counter = new CountDownTimer(25000, 1000) {
            int counterQtde = qtdeTask;

            @Override
            public void onTick(long millisUntilFinished) {
                timeStr = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) + ":" + TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);
                notifyObservers(timeStr);
            }

            @Override
            public void onFinish() {
                if((--counterQtde) == 0) {
                    start = false;
                } else {
                    restCounter.start();
                }
                qtde = counterQtde;
                notifyObservers("0:0");
            }
        };

        restCounter = new CountDownTimer(5000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                timeStr = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) + ":" + TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);
                notifyObservers("DESCANSE - " + timeStr);
            }

            @Override
            public void onFinish() {
                notifyObservers("0:0");
                counter.start();
            }
        };

            runner = new Thread(new Runnable() {
                @Override
                public void run() {
                    if (!start) {
                       counter.start();
                        start = true;
                    };
                };
            });

        runner.start();
    };

    public static void stopTask() {
        if(start) {
            counter.cancel();
            start = false;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return binder;
    }

    @Override
    public void register(Observer ob) {
        observers.add(ob);
    }

    @Override
    public void notifyObservers(String timeStr) {
        for (Observer ob:observers) {
            ob.notification(timeStr);
        }
    }

    @Override
    public void unRegister(Observer ob) {
        observers.remove(ob);
    }

    public class LocalBinder extends Binder {
        public BoundCountDownService getService() {
            return BoundCountDownService.this;
        }
    }
}