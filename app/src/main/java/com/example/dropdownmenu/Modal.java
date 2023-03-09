package com.example.dropdownmenu;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Modal {

    private Context _context;
    private AlertDialog _modal;

    public Modal(AlertDialog modal,Context context) {
        this._modal=modal;
        this._context = context;
    }

    public Context get_context() {
        return _context;
    }

    public AlertDialog get_modal() {
        return _modal;
    }

    // fonction pour modifier le nom et afficher la fenêtre modal
    public void modifierNom(Button buttonModifierNom, EditText editTextNomModifier, TextView textViewNomModif) {
        //création du pop-up
        AlertDialog.Builder builder = new AlertDialog.Builder(get_context());

        //design de la modale avec le nom prenom et l'input
        builder.setTitle("Nom");
        builder.setMessage("Entrer votre nom");
        editTextNomModifier = new EditText(get_context());
        builder.setView(editTextNomModifier);
        EditText finalNomModifier;
        finalNomModifier = editTextNomModifier;

        builder.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String  nomModif;
                //récupération du contenu du nom modif
                nomModif = finalNomModifier.getText().toString();

                //verification de la saisie du nom modifié pour affiché un message d'erreur s'il est vide
                if(!nomModif.isEmpty()) {
                    // preparation de la sauvegarde des données à partir de SharedPreferences avec une référence pour chaque données sauvegardés
                    SharedPreferences sharedPreferencesNomModif;
                    sharedPreferencesNomModif =get_context().getSharedPreferences("nomModification", Context.MODE_PRIVATE);
                    Sauvegarde sauvegardeNomModif= new Sauvegarde(sharedPreferencesNomModif,get_context());
                    sauvegardeNomModif.saveNomModif(nomModif);
                    textViewNomModif.setText(nomModif);
                }else{
                    //affiche une notification que le nom modifié est vide
                    Toast.makeText(get_context(), "nom modifié vide", Toast.LENGTH_SHORT).show();
                }

            }
        });
        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        _modal = builder.create();

        buttonModifierNom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                get_modal().show();
            }
        });
    }

    // fonction pour modifier le prenom et afficher la fenêtre modal
    public void modifierPrenom(Button buttonModifierPrenom, EditText editTextPrenomModifier, TextView textViewPrenomModif) {
        AlertDialog.Builder builder = new AlertDialog.Builder(get_context());
        builder.setTitle("Prénom");
        builder.setMessage("Entrer votre prénom");

        editTextPrenomModifier = new EditText(get_context());
        builder.setView(editTextPrenomModifier);

        EditText finalPrenomModifier;
        finalPrenomModifier = editTextPrenomModifier;
        builder.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String prenomModif;
                prenomModif = finalPrenomModifier.getText().toString();

                //verification de la saisie du prénom modifié pour affiché un message d'erreur s'il est vide
                if(!prenomModif.isEmpty()) {
                    // preparation de la sauvegarde des données à partir de SharedPreferences avec une référence pour chaque données sauvegardés
                    SharedPreferences sharedPreferencesPrenomModif;
                    sharedPreferencesPrenomModif = get_context().getSharedPreferences("prenomModification", Context.MODE_PRIVATE);
                    Sauvegarde sauvegardePrenomModif = new Sauvegarde(sharedPreferencesPrenomModif,get_context());
                    sauvegardePrenomModif.savePrenomModif(prenomModif);
                    textViewPrenomModif.setText(prenomModif);
                }else{
                    //affiche une notification que le prénom modifié est vide
                    Toast.makeText(get_context(), "prénom modifié vide", Toast.LENGTH_SHORT).show();
                }

            }
        });
        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        _modal = builder.create();

        buttonModifierPrenom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {get_modal().show();
            }
        });
    }

    // fonction pour modifier le prenom et afficher la fenêtre modal
    public void supprimerContact(ArrayList<String> tableauContactModification, int i, Sauvegarde sauvegardeContact, SharedPreferences sharedPreferencesContact
    , ListView listContactAffichage, ArrayAdapter<String> adapter) {
        AlertDialog.Builder builder = new AlertDialog.Builder(get_context());
        builder.setTitle("Supprimer ce contact ?");
        builder.setMessage("Voulez-vous vraiment supprimer ce contact ?");
        builder.setPositiveButton("Supprimer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //suppresion du numéro de téléphone en fonction de la position i
                tableauContactModification.remove(i);
                sauvegardeContact.saveContact(sharedPreferencesContact, tableauContactModification);
                //sauvegarde de la modification
                listContactAffichage.setAdapter(adapter);
            }
        });
        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        _modal = builder.create();
        get_modal().show();
    }
}
