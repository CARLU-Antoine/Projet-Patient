package com.example.dropdownmenu;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Contact {

    private Context _context;
    private String _contact;

    //creation du contrusteur
    public Contact(String _contact,Context context) {
        this._contact=_contact;
        this._context = context;
    }

    public Context get_context() {
        return _context;
    }

    public String get_contact() {
        return _contact;
    }

    //fonction pour récuperer les contacts enregistré dans le téléphone
    public ArrayList<String> recupContact(ArrayList<String> listeContact){
        Cursor parcourirContact;
        //récupération des informations du téléphone
        ContentResolver contentResolver = get_context().getContentResolver();
        //récupération d'un tableau de string à partir des numéros de téléphone enregistrés dans la référence parcourContact
        parcourirContact = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[] {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME},null,null,   ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY + " ASC");
        //verification du contenue de la référence parcourirContact pour verifier que la valeur soit null
        if (parcourirContact == null){
            //affiche un message d'erreur si aucun numéro de téléphone est enregistré
            Toast.makeText(get_context(), "erreur", Toast.LENGTH_SHORT).show();
        }else{
            //parcour de la référence parcourirContact qui contient un tableau de string contenant des numéros de téléphone
            while (parcourirContact.moveToNext()){
                // à chaque tour de boucle je récuprere la valeur du numéro à l'index de position
                _contact = parcourirContact.getString(parcourirContact.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

                //insertion des numéro de téléphone dans la liste contact transmise dans les paramètres
                listeContact.add(get_contact());
            }
            //je referme le parocour du tableau à partir de la référence comme un endforeach
            parcourirContact.close();
        }
        //retourne la liste contact parcouru avec les numéros de téléphone enregistrés dans le téléphone
        return listeContact;
    }

    public String recupNumero(String nomContact){
        String numero = null;
        //récupération des informations du téléphone
        ContentResolver contentResolver = get_context().getContentResolver();

        //récupération du contact dont le nom est transmis en paramètre
        Cursor parcourirContact = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + "=?", new String[]{nomContact}, null);

        if (parcourirContact == null){
            //affiche un message d'erreur si aucun contact n'est trouvé
            Toast.makeText(get_context(), "Contact non trouvé", Toast.LENGTH_SHORT).show();
        }else{
            //récupération de l'ID du contact trouvé
            String contactId = null;
            while(parcourirContact.moveToNext()){
                contactId = parcourirContact.getString(parcourirContact.getColumnIndex(ContactsContract.Contacts._ID));
            }
            //fermeture du curseur
            parcourirContact.close();

            //récupération du premier numéro de téléphone associé à l'ID du contact
            Cursor parcourirNumero = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER}, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", new String[]{contactId}, null);

            if (parcourirNumero == null){
                //affiche un message d'erreur si aucun numéro de téléphone est enregistré
                Toast.makeText(get_context(), "Aucun numéro de téléphone enregistré", Toast.LENGTH_SHORT).show();
            }else{
                //récupération du premier numéro de téléphone dans le curseur
                if(parcourirNumero.moveToNext()){
                    numero = parcourirNumero.getString(parcourirNumero.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                }
                //fermeture du curseur
                parcourirNumero.close();
            }
        }
        //retourne le premier numéro de téléphone associé au contact dont le nom est transmis en paramètre, ou null si aucun numéro n'est trouvé
        return numero;
    }



    //fonction pour ajouter le contact selectionné dans la liste de contact sur la page Modification
    public void ajouter(AutoCompleteTextView contactValue, ArrayList<String> items, ArrayAdapter<String> adapter,ListView listContactAffichage) {
        //récupération de la valeur actuelle dans le dropdow
        _contact = contactValue.getText().toString();

        //verification de saisie du numéro de téléphone
        if (!get_contact().isEmpty() && get_contact().length() > 0 ) {
            //verification du nombre de numéro de téléphone qui doit être inférieur à 3 d'après le cahier des charges
            if(items.size()<3){
                //ajout du numéro sélectionné dans la liste de numéro
                items.add(get_contact());
                //Permet d'afficher l'ajout des numéro dans la liste de numéro
                listContactAffichage.setAdapter(adapter);
                //effacement du numéro selectionné pour faire plus propre
                contactValue.setText("");
                //affiche une notification de l'ajout du numéro de téléphone
                Toast.makeText(get_context(), "Ajouté " + get_contact(), Toast.LENGTH_SHORT).show();
            }else{
                //affiche une notification de la contrainte de 3 contacts maximum
                Toast.makeText(get_context(), "3 contacts maximum", Toast.LENGTH_SHORT).show();
            }
        } else {
            //affiche une notification qu'aucun numéro n'est sélectionné dans le dropdown
            Toast.makeText(get_context(), "rien a ajouté ", Toast.LENGTH_SHORT).show();
        }
    }

    //fonction pour supprimer toutes les valeurs du dropdown
    public void nettoyerListe(ArrayAdapter<String> adapter){
        //effacer toutes les valeurs présentent dans le dropdown
        adapter.clear();
    }

}
