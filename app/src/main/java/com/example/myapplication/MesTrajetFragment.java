package com.example.myapplication;

import android.app.AlertDialog;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MesTrajetFragment extends Fragment {
Context context;
View mesTrajetView;
RecyclerView recyclerView;
RecyclerAdapter recyclerAdapter;
private FirebaseUser fUser;
List <Trajet> data = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mesTrajetView = inflater.inflate(R.layout.fragment_mes_trajet, container, false);
        context= inflater.getContext();
        fUser = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView = (RecyclerView) mesTrajetView.findViewById(R.id.recview);
        System.out.println("Recycler"+recyclerView);


        FirebaseDatabase.getInstance().getReference().child("Trajets").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    System.out.println(ds);
                    data.add(ds.getValue(Trajet.class));
                    recyclerAdapter = new RecyclerAdapter(data, context.getApplicationContext());
                    recyclerView.setAdapter(recyclerAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        GridLayoutManager gridLayoutManager=new GridLayoutManager(context,1);
        recyclerView.setLayoutManager(gridLayoutManager);



        return mesTrajetView;

    }




    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
