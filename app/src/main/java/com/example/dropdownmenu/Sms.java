package com.example.dropdownmenu;

import android.content.Context;
import android.telephony.SmsManager;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Sms {
    private Context _context;
    private String _message;

    public Sms(String message,Context context) {
        this._message=message;
        this._context = context;
    }

    public Context get_context() {
        return _context;
    }

    public String get_message() {
        return _message;
    }


    public void envoieSmsPanne(AutoCompleteTextView contactValue){

        String contact = contactValue.getText().toString();

        if(!contact.isEmpty()){

            //création d'un contact avec le constructeur de la classe Contact
            Contact unContact = new Contact(contact,get_context());

            String numeroComparaison = unContact.recupNumero(contact);


            SmsManager mySmsManager = SmsManager.getDefault();
            mySmsManager.sendTextMessage(numeroComparaison,null,get_message(), null, null);

            //affiche une notification de sms envoyé au numéro de téléphone
            Toast.makeText(get_context(), "SMS panne envoyé au contact " + contact, Toast.LENGTH_SHORT).show();
        }else{
            //affiche une notification qu'aucun numéro n'a était sélectionné
            Toast.makeText(get_context(), "Pas de contact sélectionné ", Toast.LENGTH_SHORT).show();
        }
    }

    public void envoieSmsProblemeMedical(AutoCompleteTextView contactValue){

        String contact = contactValue.getText().toString();
        if(!contact.isEmpty()) {
            //création d'un contact avec le constructeur de la classe Contact
            Contact unContact = new Contact(contact,get_context());

            String numeroComparaison = unContact.recupNumero(contact);

            SmsManager mySmsManager = SmsManager.getDefault();
            mySmsManager.sendTextMessage(numeroComparaison,null, get_message(), null, null);

            //affiche une notification de sms envoyé au numéro de téléphone
            Toast.makeText(get_context(), "SMS problème médical envoyé au contact " + contact, Toast.LENGTH_SHORT).show();
        }else{
            //affiche une notification qu'aucun numéro n'a était sélectionné
            Toast.makeText(get_context(), "Pas de contact sélectionné ", Toast.LENGTH_SHORT).show();
        }

    }

    public void envoieSmsBesoinAide(AutoCompleteTextView contactValue){

        String contact = contactValue.getText().toString();

        if(!contact.isEmpty()) {

            //création d'un contact avec le constructeur de la classe Contact
            Contact unContact = new Contact(contact,get_context());

            String numeroComparaison = unContact.recupNumero(contact);

            SmsManager mySmsManager = SmsManager.getDefault();
            mySmsManager.sendTextMessage(numeroComparaison,null,get_message(), null, null);

            //affiche une notification de sms envoyé au numéro de téléphone
            Toast.makeText(get_context(), "SMS besoin d'aide envoyé au contact " + contact, Toast.LENGTH_SHORT).show();
        }else{
            //affiche une notification qu'aucun numéro n'a était sélectionné
            Toast.makeText(get_context(), "Pas de contact sélectionné ", Toast.LENGTH_SHORT).show();
        }
    }

}
