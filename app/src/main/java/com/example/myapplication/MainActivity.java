package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.type.LatLng;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawerLayout;
    NavigationView navigationView;
    TextView nav_mail,nav_name;
    FirebaseAuth fAuth;
    String name,mail;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        ArrayList< Object > locations;
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


    }




    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;
        Bundle bundle = new Bundle();
        if (id == R.id.nav_profile) {
            bundle.putString("name",name);
            bundle.putString("mail",mail);
            fragment =new ProfileFragment();
            fragment.setArguments(bundle);

        } else if (id == R.id.nav_add) {
        } else if (id == R.id.nav_trajet) {
        } else if (id == R.id.nav_search) {}
         else if (id == R.id.nav_rate) {}
         else if (id == R.id.nav_stat) {}
         else if (id == R.id.nav_quitter) {}
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, fragment);
            ft.commit();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}