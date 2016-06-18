package br.edu.fa7.mobilepomodoro.service;

/**
 * Created by erinaldo.souza on 10/06/2016.
 */
public interface Subject  {
    public void register(Observer ob);
    public void notifyObservers(String str);
    public void unRegister(Observer ob);
}
