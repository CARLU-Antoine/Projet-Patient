package com.example.dropdownmenu;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

public class Permission {
    private Context context;


    public Permission(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    //fonction pour demander les permissions
    public void demandePermission(String[] tableauPermission){
        if (!hasPermissions(getContext(),tableauPermission)) {
            ActivityCompat.requestPermissions((Activity) getContext(), tableauPermission, 1);
        }
    }

    //fonction pour verifier que les permissions n'ont pas étaient déjà acceptées
    public boolean hasPermissions(Context context, String... PERMISSIONS) {

        if (context != null && PERMISSIONS != null) {

            for (String permission: PERMISSIONS){

                if (ActivityCompat.checkSelfPermission(context,permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }

        return true;
    }
}
