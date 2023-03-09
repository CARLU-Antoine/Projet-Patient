package com.example.dropdownmenu;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Button;

import java.util.ArrayList;



public class PageModification extends AppCompatActivity {

    private static ArrayList<String> tableauContactModification = new ArrayList<String>();
    private static ListView listContactAffichage;
    private static Sauvegarde sauvegardeContact;
    private static ArrayAdapter<String> adapter, adapterContact;
    private static SharedPreferences sharedPreferencesContact;
    private AlertDialog modalInterfaceNom;
    private AlertDialog modalInterfacePrenom;
    private static AlertDialog modalInterfaceSupprimer;
    private EditText editTextNomModifier, editTextPrenomModifier;
    private String contact;


    // creation de la layout à partir de la classe
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pagemodification);

        //initialisation des différents types de variable
        SharedPreferences sharedPreferencesNom, sharedPreferencesPrenom, sharedPreferencesNomModif, sharedPreferencesPrenomModif;
        ArrayList<String> listeContact = new ArrayList<String>();
        AutoCompleteTextView ContactModifieDropdown;
        Button ajouterNumero, supprimerNumeroAll, buttonRetourMain, buttonModifierNom, buttonModifierPrenom;
        String nom, prenom, nomSauvegarde, prenomSauvegarde, nomModifSauvegarde, prenomModifSauvegarde;
        TextView textViewNomModif, textViewPrenomModif;


        // récupération des elements graphique dans les variables afin de les manipuler dans la class
        ContactModifieDropdown = findViewById(R.id.values);
        listContactAffichage = findViewById(R.id.listContact);
        ajouterNumero = findViewById(R.id.ajouterNumero);
        supprimerNumeroAll = findViewById(R.id.supprimerNumeroAll);
        buttonRetourMain = findViewById(R.id.buttonRetourMain);
        buttonModifierNom = findViewById(R.id.buttonModifierNom);
        textViewNomModif = findViewById(R.id.textViewNomModif);
        buttonModifierPrenom = findViewById(R.id.buttonModifierPrenom);
        textViewPrenomModif = findViewById(R.id.textViewPrenomModif);

        // preparation de la sauvegarde des données à partir de SharedPreferences avec une référence pour chaque données sauvegardés
        sharedPreferencesNom = this.getSharedPreferences("nom", Context.MODE_PRIVATE);
        sharedPreferencesPrenom = this.getSharedPreferences("prenom", Context.MODE_PRIVATE);
        sharedPreferencesNomModif = this.getSharedPreferences("nomModification", Context.MODE_PRIVATE);
        sharedPreferencesPrenomModif = this.getSharedPreferences("prenomModification", Context.MODE_PRIVATE);
        sharedPreferencesContact = this.getSharedPreferences("contacts", Context.MODE_PRIVATE);


        //création d'un objet modal avec le constructeur de la classe Modal
        Modal modalNom = new Modal(modalInterfaceNom, this);

        //création d'un objet modal avec le constructeur de la classe Modal
        Modal modalPrenom = new Modal(modalInterfacePrenom, this);


        //Manipulation de l'objet avec les méthodes
        modalNom.modifierNom(buttonModifierNom, editTextNomModifier, textViewNomModif);
        modalPrenom.modifierPrenom(buttonModifierPrenom, editTextPrenomModifier, textViewPrenomModif);


        // création de la redicrection à partir de la layout contact à la layout main
        buttonRetourMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentSms;
                //création de la redirection de la PageModification à la PageSms
                intentSms = new Intent(PageModification.this, PageSms.class);
                //lancement de l'activité de la PageSms
                startActivity(intentSms);
            }
        });

        // insertion des la valeur actuelle dans le auto complete txt à partir de la selection du dropdown
        ContactModifieDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String positiontableauContactModifie;
                //récupération du contenue du contact sélectionné grâce à la position
                positiontableauContactModifie = parent.getItemAtPosition(position).toString();
            }
        });

        //création d'un contact avec le constructeur de la classe Contact
        Contact unContact = new Contact(contact, this);

        //appel à la methode recupContact de la classe Contact à partir de l'objet contact qu'on a créé précédemment
        listeContact = unContact.recupContact(listeContact);
        ;
        //création d'un objet sauvegarde avec le constructeur de la classe Sauvegarde
        Sauvegarde sauvegardeNom = new Sauvegarde(sharedPreferencesNom, this);

        //création d'un objet sauvegarde avec le constructeur de la classe Sauvegarde
        Sauvegarde sauvegardePrenom = new Sauvegarde(sharedPreferencesPrenom, this);

        //création d'un objet sauvegarde avec le constructeur de la classe Sauvegarde
        Sauvegarde sauvegardeNomModif = new Sauvegarde(sharedPreferencesNomModif, this);

        //création d'un objet sauvegarde avec le constructeur de la classe Sauvegarde
        Sauvegarde sauvegardePrenomModif = new Sauvegarde(sharedPreferencesPrenomModif, this);

        //création d'un objet sauvegarde afin de l'utiliser
        sauvegardeContact = new Sauvegarde(sharedPreferencesContact, this);


        nomSauvegarde = sauvegardeNom.loadNom();
        prenomSauvegarde = sauvegardePrenom.loadPrenom();

        nomModifSauvegarde = sauvegardeNomModif.loadNomModif();
        prenomModifSauvegarde = sauvegardePrenomModif.loadPrenomModif();

        if (nomModifSauvegarde.isEmpty()) {
            textViewNomModif.setText(nomSauvegarde);
        } else {
            textViewNomModif.setText(nomModifSauvegarde);
        }
        if (prenomModifSauvegarde.isEmpty()) {
            textViewPrenomModif.setText(prenomSauvegarde);
        } else {
            textViewPrenomModif.setText(prenomModifSauvegarde);
        }


        //décodage et récupration des informations du tableau contact
        tableauContactModification = sauvegardeContact.loadContact(tableauContactModification);

        //sauvegarde de la liste
        sauvegardeContact.saveContact(sharedPreferencesContact, tableauContactModification);

        // lier la fonction au bouton pour ajouter un numéro de téléphone
        ajouterNumero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unContact.ajouter(ContactModifieDropdown, tableauContactModification, adapter, listContactAffichage);
                sauvegardeContact.saveContact(sharedPreferencesContact, tableauContactModification);
            }
        });


        // lier la fonction au bouton pour supprimer tous les numéro de téléphone de la liste
        supprimerNumeroAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unContact.nettoyerListe(adapter);
                //sauvegarde de la modification
                sauvegardeContact.saveContact(sharedPreferencesContact, tableauContactModification);
            }
        });

        //insertion des informations du tableau item dans le dropdown
        adapterContact = new ArrayAdapter<String>(this, R.layout.dropdown, listeContact);
        ContactModifieDropdown.setAdapter(adapterContact);

        //création concrète de la liste des conctats qui sera présente sur la page Modification
        listContactAffichage.setLongClickable(true);
        adapter = new ListeContact(this, tableauContactModification);
        listContactAffichage.setAdapter(adapter);
    }

    //fonction pour supprimer le numéro courant qui a était sélectionné
    public static void supprimer(int i, ImageView bouttonSupprime) {
        Modal modalSupprimerContact = new Modal(modalInterfaceSupprimer, bouttonSupprime.getContext());
        modalSupprimerContact.supprimerContact(tableauContactModification, i, sauvegardeContact, sharedPreferencesContact,
                listContactAffichage, adapter);
    }

}