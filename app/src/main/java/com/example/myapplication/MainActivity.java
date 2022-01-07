package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.type.LatLng;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity  implements   NavigationView.OnNavigationItemSelectedListener,LocationListener {
    private DrawerLayout drawerLayout;
    NavigationView navigationView;
    TextView nav_mail,nav_name;
    ImageView photo;
    FirebaseAuth fAuth;
    String name,mail,address;
    LinearLayout lin;
    LocationManager locationManager;
    LocationListener locationListener;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getLocation();

        drawerLayout = findViewById(R.id.drawer_layout);
        lin = findViewById(R.id.lin);
        fAuth = FirebaseAuth.getInstance();
        FirebaseUser user = fAuth.getCurrentUser();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        nav_mail=headerView.findViewById(R.id.nav_mail);
        nav_name=headerView.findViewById(R.id.nav_name);
        photo=headerView.findViewById(R.id.photo);
        FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String Url= String.valueOf(dataSnapshot.child("ImageUrl").getValue());

                if (Url==null)
                {  System.out.println("pastop");

                    photo.setImageResource(R.drawable.profile);
                }else{
                    System.out.println("top");
                    Picasso.get().load(Url).into(photo);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (user != null) {
            mail=user.getEmail();
            nav_mail.setText(mail);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference usersRef = db.collection("users").document(user.getUid());
            usersRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                             name = (String) document.get("fullname");
                            nav_name.setText(name);
                        } else {
                        }
                    } else {
                    }
                }
            });}
        navigationView.setNavigationItemSelectedListener(this);

        //Runtime permissions
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            },100);
        }

    }

    @SuppressLint("MissingPermission")
    private void getLocation() {

        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5,MainActivity.this);

        }catch (Exception e){
            e.printStackTrace();
        }

    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;
        Bundle bundle = new Bundle();
        if (id == R.id.nav_profile) {
            bundle.putString("name",name);
            bundle.putString("mail",mail);
            bundle.putString("address",address);
            fragment =new ProfileFragment();
            fragment.setArguments(bundle);

        } else if (id == R.id.nav_add) {
            bundle.putString("name",name);
            fragment =new AddTrajetFragment();
            fragment.setArguments(bundle);

        } else if (id == R.id.nav_trajet) {
            fragment =new MesTrajetFragment();

        } else if (id == R.id.nav_search) {}
         else if (id == R.id.nav_rate) {}
         else if (id == R.id.nav_stat) {}
         else if (id == R.id.nav_quitter) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(),activity_login.class));
            finish();
        }
        if (fragment != null) {
            lin.setVisibility(View.INVISIBLE);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, fragment);
            ft.commit();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(this, ""+location.getLatitude()+","+location.getLongitude(), Toast.LENGTH_SHORT).show();
        try {
            Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            address = addresses.get(0).getAddressLine(0);

            System.out.println(address);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

}