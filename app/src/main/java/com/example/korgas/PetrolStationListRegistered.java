package com.example.korgas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class PetrolStationListRegistered extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference database;
    PetrolStationAdapter petrolStationAdapter;
    ArrayList<ReadPetrolStationData> list;

    private ProgressBar progressBar;

    private SwipeRefreshLayout swipeContainer;

    int counter = 0;

    FusedLocationProviderClient fusedLocationProviderClient;
    private final  static  int REQUEST_CODE = 100;

    double longitude, latitude;

    private void swipeToRefresh() {
        swipeContainer = findViewById(R.id.swipeContainer);

        FloatingActionButton buttonLocation = findViewById(R.id.buttonLocation);
        buttonLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PetrolStationListRegistered.this, UserLocation.class);
                startActivity(intent);
                finish();
            }
        });

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(PetrolStationListRegistered.this, "refresh", Toast.LENGTH_SHORT).show();
                startActivity(getIntent());
                finish();
                overridePendingTransition(0,0);
                swipeContainer.setRefreshing(false);

            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petrol_station_list_registered);

        swipeToRefresh();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        progressBar = findViewById(R.id.idProgressBar);

        recyclerView = findViewById(R.id.petrolStationList);
        database = FirebaseDatabase.getInstance().getReference("Petrol Stations Registered");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        petrolStationAdapter = new PetrolStationAdapter(this, list);
        recyclerView.setAdapter(petrolStationAdapter);

        progressBar.setVisibility(View.VISIBLE);
        getLastLocation();

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //              clear list
                list.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ReadPetrolStationData readPetrolStationData = dataSnapshot.getValue(ReadPetrolStationData.class);
                    list.add(readPetrolStationData);

                    progressBar.setVisibility(View.GONE);
                }

                petrolStationAdapter.notifyDataSetChanged();

                Collections.sort(list, new Comparator<ReadPetrolStationData>() {
                    @Override
                    public int compare(ReadPetrolStationData petrolStationDataS, ReadPetrolStationData t1) {
                        return petrolStationDataS.name.compareToIgnoreCase(t1.getName());
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void askPermission() {

        ActivityCompat.requestPermissions(PetrolStationListRegistered.this, new String[]
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

    private void getLastLocation() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(android.location.Location location) {

                    if (location!=null){
                        Geocoder geocoder = new Geocoder(PetrolStationListRegistered.this, Locale.getDefault());
                        List<Address> addresses= null;
                        try {
                            addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(), 1);
                            longitude = addresses.get(0).getLongitude();
                            latitude = addresses.get(0).getLatitude();

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

    //    create a action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        Inflate menu item
        getMenuInflater().inflate(R.menu.common_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //    when menu item is selected
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_refresh:
                ////            refresh the page or activity
                Toast.makeText(PetrolStationListRegistered.this, "refresh", Toast.LENGTH_SHORT).show();
                startActivity(getIntent());
                finish();
                //            no animation
                overridePendingTransition(0,0);
                break;

            case R.id.AtoZ:

                petrolStationAdapter.notifyDataSetChanged();

                Collections.sort(list, new Comparator<ReadPetrolStationData>() {
                    @Override
                    public int compare(ReadPetrolStationData petrolStationDataS, ReadPetrolStationData t1) {
                        return petrolStationDataS.name.compareToIgnoreCase(t1.getName());
                    }
                });
                break;

            case R.id.ZtoA:

                petrolStationAdapter.notifyDataSetChanged();

                Collections.sort(list, new Comparator<ReadPetrolStationData>() {
                    @Override
                    public int compare(ReadPetrolStationData petrolStationDataS, ReadPetrolStationData t1) {
                        return petrolStationDataS.name.compareToIgnoreCase(t1.getName());
                    }
                });

                Collections.reverse(list);
                break;

            case R.id.cheapestGasoline:

                petrolStationAdapter.notifyDataSetChanged();

                Collections.sort(list, new Comparator<ReadPetrolStationData>() {
                    @Override
                    public int compare(ReadPetrolStationData petrolStationDataS, ReadPetrolStationData t1) {
                        return petrolStationDataS.gasolinePrice.compareToIgnoreCase(t1.getGasolinePrice());
                    }
                });
                break;

            case R.id.cheapestDiesel:

                petrolStationAdapter.notifyDataSetChanged();

                Collections.sort(list, new Comparator<ReadPetrolStationData>() {
                    @Override
                    public int compare(ReadPetrolStationData petrolStationDataS, ReadPetrolStationData t1) {
                        return petrolStationDataS.dieselPrice.compareToIgnoreCase(t1.getDieselPrice());
                    }
                });
                break;

            case R.id.cheapestKerosene:

                petrolStationAdapter.notifyDataSetChanged();

                Collections.sort(list, new Comparator<ReadPetrolStationData>() {
                    @Override
                    public int compare(ReadPetrolStationData petrolStationDataS, ReadPetrolStationData t1) {
                        return petrolStationDataS.kerosenePrice.compareToIgnoreCase(t1.getKerosenePrice());
                    }
                });
                break;



            default:
                Toast.makeText(PetrolStationListRegistered.this, "Something wrong!", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

}