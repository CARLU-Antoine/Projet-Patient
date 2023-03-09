package com.example.dropdownmenu;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

class ListeContact extends ArrayAdapter<String> {
    //création de variable listeContactAffichage qui est un tableau
    private ArrayList<String> listContactAffichage;
    //création de la variable qui va permettre de récuperer le context des éléments graphique de la page précédente
    private Context _context;

    //creation du constructeur
    public ListeContact(Context context, ArrayList<String> tableauContactModification) {
        super(context, R.layout.list_contact, tableauContactModification);
        this._context = context;
        listContactAffichage = tableauContactModification;
    }

    public Context get_context() {
        return _context;
    }

    public ArrayList<String> getListContactAffichage() {
        return listContactAffichage;
    }



    // methode pour l'affichage de la liste de contact lorsque qu'on les modifie
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutAffichageContact;
            TextView contactAffichage, numeroPlusUn;
            ImageView bouttonSupprime;

            //utilisation du layout list_contact afin d'afficher la liste de contact dans la page Modification
            layoutAffichageContact = (LayoutInflater) _context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            convertView = layoutAffichageContact.inflate(R.layout.list_contact, null);


            //récupération des éléments graphique afin de les implementer dans la page Modification
            contactAffichage = convertView.findViewById(R.id.name);
            bouttonSupprime = convertView.findViewById(R.id.remove);
            numeroPlusUn = convertView.findViewById(R.id.number);

            // rajout de chiffre devant les numéro de téléphone
            numeroPlusUn.setText(position + 1 + ".");
            contactAffichage.setText(listContactAffichage.get(position));

            // lier la fonction au bouton pour supprimer le numero en fonction de la position du numéro selectionné
            bouttonSupprime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //appel à la fonction supprimer pour supprimer le numero en fonction de la position du numéro cliqué
                    PageModification.supprimer(position,bouttonSupprime);
                }
            });
        }
        return convertView;
    }

}

