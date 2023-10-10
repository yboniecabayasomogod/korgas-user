package com.example.korgas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class UserLocation extends AppCompatActivity {

    FusedLocationProviderClient fusedLocationProviderClient;
    TextView address, city, country, latitude, longitude;
    FloatingActionButton materialButtonGetLocation;
    ProgressBar progressBar;

    private final  static  int REQUEST_CODE = 100;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, PetrolStationListRegistered.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_location);

        address=findViewById(R.id.idAddress);
        city=findViewById(R.id.idCity);
        country=findViewById(R.id.idCountry);
        latitude=findViewById(R.id.idLatitude);
        longitude=findViewById(R.id.idLongitude);
        progressBar = findViewById(R.id.idProgressBar);
        materialButtonGetLocation = findViewById(R.id.getLocation);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        materialButtonGetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLastLocation();
            }
        });
    }

    private void getLastLocation() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(android.location.Location location) {

                    if (location!=null){
                        Geocoder geocoder = new Geocoder(UserLocation.this, Locale.getDefault());
                        List<Address> addresses= null;
                        try {
                            addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(), 1);

                            latitude.setText("Latitude:  "+ addresses.get(0).getLatitude());
                            longitude.setText("Longitude:  "+ addresses.get(0).getLongitude());
                            address.setText("Address: "+ addresses.get(0).getAddressLine(0));
                            city.setText("Address: "+ addresses.get(0).getLocality());
                            country.setText("Address: "+ addresses.get(0).getCountryName());

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
        else {
            askPermission();
        }
    }
    private void askPermission() {

        ActivityCompat.requestPermissions(UserLocation.this, new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode==REQUEST_CODE){
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLocation();
            }
            else {
                Toast.makeText(this, "Required Permission", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}