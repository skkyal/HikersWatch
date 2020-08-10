package com.example.hikerswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    TextView details;

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                }
            }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        details = findViewById(R.id.details);

        locationManager = (LocationManager)this.getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateLocationInfo(location);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            Location lastKnownLocation = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
            if(lastKnownLocation!=null)
                updateLocationInfo(lastKnownLocation);
        }

    }
    public void updateLocationInfo(Location location){
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            String address = "";
            address+= "Latitude : "+String.valueOf(location.getLatitude()) + "\n\n" ;
            address+= "Longitude : "+String.valueOf(location.getLongitude()) + "\n\n" ;
            address+="Accuracy : "+ String.valueOf(location.getAccuracy()) + "\n\n";
            address+="Altitude : "+String.valueOf(location.getAltitude())+ "\n\n";
            address+="Address : \n";
            List<Address> listAddress = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            Log.i("Address 1",listAddress.get(0).toString());


            if (listAddress != null && listAddress.size() > 0) {

                if(listAddress.get(0).getThoroughfare()!=null)
                    address+=listAddress.get(0).getThoroughfare()+"\n";
                if(listAddress.get(0).getLocality()!=null)
                    address+=listAddress.get(0).getLocality()+"\n";
                if(listAddress.get(0).getAdminArea()!=null)
                    address+=listAddress.get(0).getAdminArea()+", ";
                if(listAddress.get(0).getCountryName()!=null)
                    address+=listAddress.get(0).getCountryName();
            }
            details.setText(address);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
