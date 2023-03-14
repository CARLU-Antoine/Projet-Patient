package com.example.dropdownmenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Button;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import java.util.ArrayList;
import java.util.List;


public class PageSms extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private BluetoothLeScanner mBluetoothLeScanner;
    private boolean mScanning;
    private Button premierButtonSms, secondButtonSms, troisiemeButtonSms;
    private TextView informationsUtilisateur;
    private Sms smsPanne, smsProblemeMedical, smsBesoinAide;
    private String nomSauvegarde, prenomSauvegarde, nomModif, prenomModif;
    private AutoCompleteTextView contactDropdownAffichage;
    private Beacon unbeacon;
    private FusedLocationProviderClient fusedLocationClient;

    // creation de la layout à partir de la classe
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pagesms);

        String[] tableauPermission;
        //tableau de string contenant les permissions
        tableauPermission = new String[]{

                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.SEND_SMS,
                Manifest.permission.READ_SMS,
        };

        //création d'un objet permission afin de l'utiliser
        Permission permission = new Permission(this);

        // Appel à la méthode demande permission de la classe Permission
        permission.demandePermission(tableauPermission);
        Button buttonEditContact;
        //initialisation des différents types de variable
        BluetoothAdapter mBluetoothAdapter;

        SharedPreferences sharedPreferencesContact, sharedPreferencesNom, sharedPreferencesPrenom, sharedPreferencesNomModif, sharedPreferencesPrenomModif;

        ArrayList<String> tableauContactDropdown = new ArrayList<>();
        ArrayAdapter<String> adapter;

        informationsUtilisateur = findViewById(R.id.informationsUtilisateur);
        contactDropdownAffichage = findViewById(R.id.auto_complete_txt);
        premierButtonSms = findViewById(R.id.premierButtonSms);
        secondButtonSms = findViewById(R.id.secondButtonSms);
        troisiemeButtonSms = findViewById(R.id.troisiemeButtonSms);

        sharedPreferencesNom = PageSms.this.getSharedPreferences("nom", Context.MODE_PRIVATE);
        sharedPreferencesPrenom = PageSms.this.getSharedPreferences("prenom", Context.MODE_PRIVATE);
        sharedPreferencesNomModif = PageSms.this.getSharedPreferences("nomModification", Context.MODE_PRIVATE);
        sharedPreferencesPrenomModif = PageSms.this.getSharedPreferences("prenomModification", Context.MODE_PRIVATE);
        // preparation de la sauvegarde des données à partir de SharedPreferences avec une référence pour chaque données sauvegardés
        sharedPreferencesContact = PageSms.this.getSharedPreferences("contacts", Context.MODE_PRIVATE);

        //création d'un objet sauvegarde afin de l'utiliser
        Sauvegarde sauvegardeNom = new Sauvegarde(sharedPreferencesNom, PageSms.this);

        //création d'un objet sauvegarde afin de l'utiliser
        Sauvegarde sauvegardePrenom = new Sauvegarde(sharedPreferencesPrenom, PageSms.this);

        //création d'un objet sauvegarde afin de l'utiliser
        Sauvegarde sauvegardeNomModif = new Sauvegarde(sharedPreferencesNomModif, PageSms.this);

        //création d'un objet sauvegarde afin de l'utiliser
        Sauvegarde sauvegardePrenomModif = new Sauvegarde(sharedPreferencesPrenomModif, PageSms.this);

        //création d'un objet sauvegarde afin de l'utiliser
        Sauvegarde sauvegardeContact = new Sauvegarde(sharedPreferencesContact, PageSms.this);


        //décodage et récupration des informations du tableau contact
        tableauContactDropdown = sauvegardeContact.loadContact(tableauContactDropdown);

        //insertion des informations du tableau item dans le dropdown
        adapter = new ArrayAdapter<String>(PageSms.this, R.layout.dropdown, tableauContactDropdown);
        contactDropdownAffichage.setAdapter(adapter);


        nomSauvegarde = sauvegardeNom.loadNom();
        prenomSauvegarde = sauvegardePrenom.loadPrenom();

        nomModif = sauvegardeNomModif.loadNomModif();
        prenomModif = sauvegardePrenomModif.loadPrenomModif();


        // Demander l'autorisation de localisation
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        } else {
            // L'autorisation a déjà été accordée, donc vous pouvez récupérer la localisation actuelle
            recupererLocalisation();
        }


        // insertion des la valeur actuelle dans le auto complete txt à partir de la selection du dropdown
        contactDropdownAffichage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            String positiontableauContact;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                positiontableauContact = parent.getItemAtPosition(position).toString();
            }
        });

        // récupération des elements graphique dans les variables afin de les manipuler dans la class
        buttonEditContact = findViewById(R.id.buttonEditContact);


        // Check if BLE is supported on the device
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "Votre téléphone ne peut pas détecter des beacons", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Get the BluetoothAdapter and BluetoothLeScanner
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();


        // création de la redicrection à partir de la layout main à la layout Contact
        buttonEditContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentContact;
                intentContact = new Intent(PageSms.this, PageModification.class);
                startActivity(intentContact);
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();

        // Check if the necessary permissions are granted
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                return;
            }
        }
        unbeacon = new Beacon(this, mScanning, mBluetoothLeScanner, mScanCallback, PERMISSION_REQUEST_COARSE_LOCATION);
        unbeacon.scanBeacon(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbeacon = new Beacon(this, mScanning, mBluetoothLeScanner, mScanCallback, PERMISSION_REQUEST_COARSE_LOCATION);
        unbeacon.scanBeacon(false);
    }


    private void recupererLocalisation() {
        // Créez un objet FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Récupérez la dernière localisation connue
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            // La localisation a été récupérée avec succès, vous pouvez la traiter ici
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();

                                    String messsageUtilisateurPanne, messsageUtilisateurProblemeMedical, messsageUtilisateurBesoinAide;
                                    if(nomModif.isEmpty() && prenomModif.isEmpty()) {
                                        //aficher le nom et le prénom modifiés du patient s'ils ont étaient modifié dans la page Sms
                                        informationsUtilisateur.setText("Patient "+ nomSauvegarde + " "+ prenomSauvegarde);
                                        messsageUtilisateurPanne="Le patient "+prenomSauvegarde+" "+nomSauvegarde+" a une panne ! Il est à la latitude : "+latitude + " et à la longitude : " + longitude+ " .";
                                        messsageUtilisateurProblemeMedical="Le patient "+prenomSauvegarde+" "+nomSauvegarde+" a un problème medical! Il est à la latitude : "+latitude + " et à la longitude : " + longitude+ " .";
                                        messsageUtilisateurBesoinAide="Le patient "+prenomSauvegarde+" "+nomSauvegarde+" a besoin d'aide ! Il est à la latitude : "+latitude + " et à la longitude : " + longitude+ " .";
                                        //création d'un objet permission afin de l'utiliser
                                        smsPanne = new Sms(messsageUtilisateurPanne,PageSms.this);
                                        smsProblemeMedical = new Sms(messsageUtilisateurProblemeMedical,PageSms.this);
                                        smsBesoinAide = new Sms(messsageUtilisateurBesoinAide,PageSms.this);
                                    }else if (!nomModif.isEmpty() && !prenomModif.isEmpty()){
                                        informationsUtilisateur.setText("Patient "+ nomModif + " "+ prenomModif);
                                        messsageUtilisateurPanne="Le patient "+prenomModif+" "+nomModif+" a une panne ! Il est à la latitude : "+latitude + " et à la longitude : " + longitude+ " .";
                                        messsageUtilisateurProblemeMedical="Le patient "+prenomModif+" "+nomModif+" a un problème medical! Il est à la latitude : "+latitude + " et à la longitude : " + longitude+ " .";
                                        messsageUtilisateurBesoinAide="Le patient "+prenomModif+" "+nomModif+" a besoin d'aide ! Il est à la latitude : "+latitude + " et la à longitude : " + longitude+ " .";

                                        //création d'un objet permission afin de l'utiliser
                                        smsPanne = new Sms(messsageUtilisateurPanne,PageSms.this);
                                        smsProblemeMedical = new Sms(messsageUtilisateurProblemeMedical,PageSms.this);
                                        smsBesoinAide = new Sms(messsageUtilisateurBesoinAide,PageSms.this);
                                    }else if(!nomModif.isEmpty() && prenomModif.isEmpty()){
                                        informationsUtilisateur.setText("Patient "+ nomModif + " "+ prenomSauvegarde);
                                        messsageUtilisateurPanne="Le patient "+prenomSauvegarde+" "+nomModif+" a une panne ! Il est à la latitude : "+latitude + " et à la longitude : " + longitude+ " .";
                                        messsageUtilisateurProblemeMedical="Le patient "+prenomSauvegarde+" "+nomModif+" a un problème medical! Il est à la latitude : "+latitude + " et à la longitude : " + longitude+ " .";
                                        messsageUtilisateurBesoinAide="Le patient "+prenomSauvegarde+" "+nomModif+" a besoin d'aide ! Il est à la latitude : "+latitude + " et à la longitude : " + longitude+ " .";

                                        //création d'un objet permission afin de l'utiliser
                                        smsPanne = new Sms(messsageUtilisateurPanne,PageSms.this);
                                        smsProblemeMedical = new Sms(messsageUtilisateurProblemeMedical,PageSms.this);
                                        smsBesoinAide = new Sms(messsageUtilisateurBesoinAide,PageSms.this);
                                    }else if(!prenomModif.isEmpty() && nomModif.isEmpty()){
                                        informationsUtilisateur.setText("Patient "+ nomSauvegarde + " "+ prenomModif);
                                        messsageUtilisateurPanne="Le patient "+prenomModif+" "+nomSauvegarde+" a une panne ! Il est à la latitude : "+latitude + " et à la longitude : " + longitude+ " .";
                                        messsageUtilisateurProblemeMedical="Le patient "+prenomModif+" "+nomSauvegarde+" a un problème medical! Il est à la latitude : "+latitude + " et à la longitude : " + longitude+ " .";
                                        messsageUtilisateurBesoinAide="Le patient "+prenomModif+" "+nomSauvegarde+" a besoin d'aide !  Il est à la latitude : "+latitude + " et à la longitude : " + longitude+ " .";
                                        //création d'un objet permission afin de l'utiliser
                                        smsPanne = new Sms(messsageUtilisateurPanne,PageSms.this);
                                        smsProblemeMedical = new Sms(messsageUtilisateurProblemeMedical,PageSms.this);
                                        smsBesoinAide = new Sms(messsageUtilisateurBesoinAide,PageSms.this);
                                    }



                                    // lier la méthode envoieSms au bouton
                                    premierButtonSms.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            smsBesoinAide.envoieSmsBesoinAide(contactDropdownAffichage);
                                        }
                                    });

                                    // lier la méthode envoieSms au bouton
                                    secondButtonSms.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            smsProblemeMedical.envoieSmsProblemeMedical(contactDropdownAffichage);
                                        }
                                    });

                                    // lier la méthode envoieSms au bouton
                                    troisiemeButtonSms.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            smsPanne.envoieSmsPanne(contactDropdownAffichage);
                                        }
                                    });

                                }
                        }
                });
    }

    private ScanCallback mScanCallback = new ScanCallback() {
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);



            // The Eddystone UUID for detecting beacons
            String EDDYSTONE_UUID = "0000FEAA-0000-1000-8000-00805F9B34FB";

            // The MAC address of the beacon to detect
            String BEACON_MAC_ADDRESS = "CA:C3:45:D7:92:9D";

            // Check if the scan result contains Eddystone UUID
            List<ParcelUuid> uuids = result.getScanRecord().getServiceUuids();

            String macAddress = result.getDevice().getAddress();

            if (uuids != null && uuids.contains(ParcelUuid.fromString(EDDYSTONE_UUID))
                    && macAddress.equals(BEACON_MAC_ADDRESS)) {
                int rssi = result.getRssi();
                KalmanFilter kalmanFilter = new KalmanFilter(0.008, 0.05);
                TextView labelDistanceTexte, labelDistanceValeur, labelDistanceMetre;
                // Use the Kalman filter to update the RSSI value
                double filteredRssi = kalmanFilter.update(rssi);

                Beacon beaconDistance = new Beacon(PageSms.this, filteredRssi);

                // Calculate the distance
                Double distance = beaconDistance.calculerDistance();
                // Round the distance to two decimal places and store it in an integer
                int distanceInt = (int) (distance * 100);
                String distanceValeur = String.valueOf(distanceInt / 100.0);

                labelDistanceTexte = findViewById(R.id.distanceTexte);
                labelDistanceValeur = findViewById(R.id.distanceValeur);
                labelDistanceMetre = findViewById(R.id.distanceMetre);

                labelDistanceTexte.setText("Distance du beacon : ");
                labelDistanceValeur.setText(distanceValeur);

                if (distance >= 2) {
                    labelDistanceMetre.setText(" mètres");
                } else {
                    labelDistanceMetre.setText(" mètre");
                }

                String metreMessage = (String) labelDistanceMetre.getText();
                String messsageUtilisateurPanne, messsageUtilisateurProblemeMedical, messsageUtilisateurBesoinAide;


                if(nomModif.isEmpty() && prenomModif.isEmpty()) {
                    messsageUtilisateurPanne="Le patient "+prenomSauvegarde+" "+nomSauvegarde+" a une panne ! Il est à une distance de "+ distanceValeur+ metreMessage+" .";
                    messsageUtilisateurProblemeMedical="Le patient "+prenomSauvegarde+" "+nomSauvegarde+" a un problème medical! Il est à une distance de "+ distanceValeur+ metreMessage+" .";
                    messsageUtilisateurBesoinAide="Le patient "+prenomSauvegarde+" "+nomSauvegarde+" a besoin d'aide ! Il est à une distance de "+ distanceValeur+ metreMessage+" .";
                    //création d'un objet permission afin de l'utiliser
                    smsPanne = new Sms(messsageUtilisateurPanne,PageSms.this);
                    smsProblemeMedical = new Sms(messsageUtilisateurProblemeMedical,PageSms.this);
                    smsBesoinAide = new Sms(messsageUtilisateurBesoinAide,PageSms.this);
                }else if (!nomModif.isEmpty() && !prenomModif.isEmpty()){
                    messsageUtilisateurPanne="Le patient "+prenomModif+" "+nomModif+" a une panne ! Il est à une distance de "+ distanceValeur+ metreMessage+" .";
                    messsageUtilisateurProblemeMedical="Le patient "+prenomModif+" "+nomModif+" a un problème medical! Il est à une distance de "+ distanceValeur+ metreMessage+" .";
                    messsageUtilisateurBesoinAide="Le patient "+prenomModif+" "+nomModif+" a besoin d'aide ! Il est à une distance de "+ distanceValeur+ metreMessage+" .";

                    //création d'un objet permission afin de l'utiliser
                    smsPanne = new Sms(messsageUtilisateurPanne,PageSms.this);
                    smsProblemeMedical = new Sms(messsageUtilisateurProblemeMedical,PageSms.this);
                    smsBesoinAide = new Sms(messsageUtilisateurBesoinAide,PageSms.this);
                }else if(!nomModif.isEmpty() && prenomModif.isEmpty()){
                    messsageUtilisateurPanne="Le patient "+prenomSauvegarde+" "+nomModif+" a une panne ! Il est à une distance de "+ distanceValeur+ metreMessage+" .";
                    messsageUtilisateurProblemeMedical="Le patient "+prenomSauvegarde+" "+nomModif+" a un problème medical! Il est à une distance de "+ distanceValeur+ metreMessage+" .";
                    messsageUtilisateurBesoinAide="Le patient "+prenomSauvegarde+" "+nomModif+" a besoin d'aide ! Il est à une distance de "+ distanceValeur+ metreMessage+" .";

                    //création d'un objet permission afin de l'utiliser
                    smsPanne = new Sms(messsageUtilisateurPanne,PageSms.this);
                    smsProblemeMedical = new Sms(messsageUtilisateurProblemeMedical,PageSms.this);
                    smsBesoinAide = new Sms(messsageUtilisateurBesoinAide,PageSms.this);
                }else if(!prenomModif.isEmpty() && nomModif.isEmpty()){
                    messsageUtilisateurPanne="Le patient "+prenomModif+" "+nomSauvegarde+" a une panne ! Il est à une distance de "+ distanceValeur+ metreMessage+" .";
                    messsageUtilisateurProblemeMedical="Le patient "+prenomModif+" "+nomSauvegarde+" a un problème medical! Il est à une distance de "+ distanceValeur+ metreMessage+" .";
                    messsageUtilisateurBesoinAide="Le patient "+prenomModif+" "+nomSauvegarde+" a besoin d'aide !  Il est à une distance de "+ distanceValeur+ metreMessage+" .";
                    //création d'un objet permission afin de l'utiliser
                    smsPanne = new Sms(messsageUtilisateurPanne,PageSms.this);
                    smsProblemeMedical = new Sms(messsageUtilisateurProblemeMedical,PageSms.this);
                    smsBesoinAide = new Sms(messsageUtilisateurBesoinAide,PageSms.this);
                }

                // lier la méthode envoieSms au bouton
                premierButtonSms.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        smsBesoinAide.envoieSmsBesoinAide(contactDropdownAffichage);
                    }
                });

                // lier la méthode envoieSms au bouton
                secondButtonSms.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        smsProblemeMedical.envoieSmsProblemeMedical(contactDropdownAffichage);
                    }
                });

                // lier la méthode envoieSms au bouton
                troisiemeButtonSms.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        smsPanne.envoieSmsPanne(contactDropdownAffichage);
                    }
                });
            }
        }
    };
}
