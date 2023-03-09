package com.example.dropdownmenu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PageSaisie extends AppCompatActivity {
    private String nom,prenom;

    // creation de la layout à partir de la classe
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pagesaisie);

        Button button_action_saisie;
        TextView saisieNom,saisiePrenom;
        String nomSauvegarde,prenomSauvegarde;
        SharedPreferences sharedPreferencesNom,sharedPreferencesPrenom;


        button_action_saisie = findViewById(R.id.button_action_saisie);
        saisieNom = findViewById(R.id.saisieNom);
        saisiePrenom = findViewById(R.id.saisiePrenom);

        // preparation de la sauvegarde des données à partir de SharedPreferences avec une référence pour chaque données sauvegardés
        sharedPreferencesNom =this.getSharedPreferences("nom", Context.MODE_PRIVATE);
        sharedPreferencesPrenom =this.getSharedPreferences("prenom", Context.MODE_PRIVATE);

        //création d'un objet sauvegarde avec le constructeur de la classe Sauvegarde
        Sauvegarde sauvegardeNom = new Sauvegarde(sharedPreferencesNom,this);

        //création d'un objet sauvegarde avec le constructeur de la classe Sauvegarde
        Sauvegarde sauvegardePrenom = new Sauvegarde(sharedPreferencesPrenom,this);



        // création de la redicrection à partir de la layout contact à la layout main
        button_action_saisie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //récupération des valeurs du nom et prenom dans les variables à partir du label
                 nom = saisieNom.getText().toString();
                 prenom = saisiePrenom.getText().toString();

                 //manipulation de l'objet sauvegarde avec les méthodes afin de sauvegarde le nom, le prénom et verifier la saisie
                sauvegardeNom.saveNom(nom,prenom);
                sauvegardePrenom.savePrenom(prenom,nom);
                sauvegardeNom.verificationSaisie(nom,prenom);
           }
        });


        //chargement du nom et prenom sur la page directement afin de verifier que les informations ont étaient déja saisies ou non
        nomSauvegarde=sauvegardeNom.loadNom();
        prenomSauvegarde=sauvegardePrenom.loadPrenom();

        //appel à la méthode verificationSaisieSauvegarde pour verifier que le nom et le prénom ont étaient sauvegardé
        sauvegardeNom.verificationSaisieSauvegarde(nomSauvegarde,prenomSauvegarde);

    }
}