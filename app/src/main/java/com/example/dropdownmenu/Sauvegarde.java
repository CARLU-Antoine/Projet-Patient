package com.example.dropdownmenu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class Sauvegarde {

    private Context _context;
    private SharedPreferences _sauvegarde;

    public Sauvegarde(SharedPreferences sauvegarde,Context context) {
        this._sauvegarde=sauvegarde;
        this._context = context;
    }

    public Context get_context() {
        return _context;
    }

    public SharedPreferences get_sauvegarde() {
        return _sauvegarde;
    }

    // fonction pour vérifier la saisie du nom et du prenom et sauvegarder que le nom
    public void saveNom(String nom,String prenom) {
        if (!nom.isEmpty() && !prenom.isEmpty()) {
            SharedPreferences.Editor editor = get_sauvegarde().edit();
            //insérer le nom pour pour l'enregistrer
            editor.putString("nom", nom);
            //appliquer la sauvegarde
            editor.apply();
        }
    }


    // fonction pour récupérer le nom pour la vérification de la sauvegarde
    public String loadNom() {
        String nom;
        nom = get_sauvegarde().getString("nom", "");
        //retourne le nom pour l'afficher
        return nom;
    }

    // fonction pour vérifier la saisie du nom et du prenom et sauvegarder que le prenom
    public void savePrenom(String prenom,String nom) {
        if (!prenom.isEmpty() && !nom.isEmpty()) {
            SharedPreferences.Editor editor = get_sauvegarde().edit();
            //insérer le prenom pour pour l'enregistrer
            editor.putString("prenom", prenom);
            //appliquer la sauvegarde
            editor.apply();
        }
    }

    // fonction pour récupérer le prénom pour la vérification de la sauvegarde
    public String loadPrenom() {
        String prenom;
        prenom = get_sauvegarde().getString("prenom", "");
        //retourne le prenom pour l'afficher
        return prenom;
    }


    // fonction pour vérifier la saisie du nom et du prenom et sauvegarder que le nom
    public void saveNomModif(String nomModif) {
        if (!nomModif.isEmpty()) {
            SharedPreferences.Editor editor = get_sauvegarde().edit();
            //insérer le nom pour pour l'enregistrer
            editor.putString("nomModification", nomModif);
            //appliquer la sauvegarde
            editor.apply();
        }
    }

    // fonction pour récupérer le nom pour la vérification de la sauvegarde
    public String loadNomModif() {
        String nomModif;
        nomModif = get_sauvegarde().getString("nomModification", "");
        //retourne le nom pour l'afficher
        return nomModif;
    }

    // fonction pour vérifier la saisie du nom et du prenom et sauvegarder que le prenom
    public void savePrenomModif(String prenomModif) {
        if (!prenomModif.isEmpty()) {
            SharedPreferences.Editor editor = get_sauvegarde().edit();
            //insérer le prenom pour pour l'enregistrer
            editor.putString("prenomModification", prenomModif);
            //appliquer la sauvegarde
            editor.apply();
        }
    }

    // fonction pour récupérer le prénom pour la vérification de la sauvegarde
    public String loadPrenomModif() {
        String prenomModif;
        prenomModif = get_sauvegarde().getString("prenomModification", "");
        //retourne le prenom pour l'afficher
        return prenomModif;
    }

    // fonction pour la vérification de la saisie
    public void verificationSaisie(String nom, String prenom) {
        if(!nom.isEmpty() || !prenom.isEmpty()) {
            if (!nom.isEmpty()) {
                if (!prenom.isEmpty()) {
                        Intent intentSms;
                        intentSms = new Intent(get_context(), PageSms.class);
                        get_context().startActivity(intentSms);
                }else {
                    Toast.makeText(get_context(), "Champ prenom vide !", Toast.LENGTH_SHORT).show();
                    }
            }else{
                Toast.makeText(get_context(), "Champ nom vide !", Toast.LENGTH_SHORT).show();
                }
        }else {
            Toast.makeText(get_context(), "Champ prenom ou nom vide !", Toast.LENGTH_SHORT).show();
            }
    }

    // fonction pour vérifier l'existence du nom et prenom sauvegardé
    public void verificationSaisieSauvegarde(String nomSauvegarde, String prenomSauvegarde) {
        //verification que le nom et prenom sont différents de vide
        if(!nomSauvegarde.isEmpty() && !prenomSauvegarde.isEmpty()) {
            Intent intentSms;
            //création d'une redirection vers une autre page
            intentSms = new Intent(get_context(), PageSms.class);
            get_context().startActivity(intentSms);
        }/*else {
            //afficher un message d'erreur qui stipule que le nom et prenom n'ont pas était sauvegardé et saisi sur la page d'accueil
            Toast.makeText(context, "informations non sauvegardé ! ", Toast.LENGTH_SHORT).show();
        }
         */
    }

    //fonction pour sauvegarder et coder les informations des contacts
    public void saveContact(SharedPreferences sharedPreferencesContact, ArrayList<String> tableauContact){
            try {
                sharedPreferencesContact.edit().putString("contacts", ObjectSerializer.serialize(tableauContact)).apply();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    //fonction pour sauvegarder et décoder les informations des contacts afin de les charger sur la page
    public ArrayList<String> loadContact(ArrayList<String> tableauContact){
        try {

            tableauContact =(ArrayList<String>) ObjectSerializer.deserialize(get_sauvegarde().getString("contacts",ObjectSerializer.serialize(new ArrayList<String>())));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return tableauContact;
    }

}
