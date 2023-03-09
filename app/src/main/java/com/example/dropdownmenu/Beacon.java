package com.example.dropdownmenu;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.ParcelUuid;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import java.util.List;

public class Beacon {

    private Context _context;
    private boolean _mScanning;
    private BluetoothLeScanner _mBluetoothLeScanner;
    private ScanCallback mScanCallback;
    private int _PERMISSION_REQUEST_COARSE_LOCATION;
    private double _filteredRssi;


    //creation du contrusteur
    public Beacon(Context context,Boolean mScanning,BluetoothLeScanner mBluetoothLeScanner,ScanCallback mScanCallback,int PERMISSION_REQUEST_COARSE_LOCATION) {
        this._context = context;
        this._mScanning=mScanning;
        this._mBluetoothLeScanner=mBluetoothLeScanner;
        this.mScanCallback=mScanCallback;
        this._PERMISSION_REQUEST_COARSE_LOCATION=PERMISSION_REQUEST_COARSE_LOCATION;
    }

    //creation du contrusteur
    public Beacon(Context context,double filteredRssi) {
        this._context = context;
        this._filteredRssi=filteredRssi;
    }

    public BluetoothLeScanner get_mBluetoothLeScanner() {
        return _mBluetoothLeScanner;
    }

    public Context get_context() {
        return _context;
    }

    public double get_filteredRssi() {
        return _filteredRssi;
    }

    public int get_PERMISSION_REQUEST_COARSE_LOCATION() {
        return _PERMISSION_REQUEST_COARSE_LOCATION;
    }

    public ScanCallback getmScanCallback() {
        return mScanCallback;
    }

    public void scanBeacon(final boolean enable) {
        if (enable) {
            // Start scanning for beacons
            _mScanning = true;
            if (ActivityCompat.checkSelfPermission(_context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                _mBluetoothLeScanner.startScan(mScanCallback);
            } else {
                ActivityCompat.requestPermissions((Activity) _context, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, _PERMISSION_REQUEST_COARSE_LOCATION);
            }
        } else {
            // Stop scanning for beacons
            _mScanning = false;
            _mBluetoothLeScanner.stopScan(mScanCallback);
        }
    }

    public double calculerDistance() {
        // La formule suivante est basée sur l'équation du modèle de propagation de Friis pour les ondes radio.
        // La fréquence du signal est supposée être de 2,4 GHz.
        double txPower = -59; // La puissance du signal à une distance de 1 mètre (estimation)
        double ratio = _filteredRssi * 1.0 / txPower;
        if (ratio < 1.0) {
            return Math.pow(ratio, 10);
        } else {
            double distance = (0.89976) * Math.pow(ratio, 7.7095) + 0.111;
            return distance;
        }
    }
}
