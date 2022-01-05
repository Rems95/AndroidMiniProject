package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class activity_register extends AppCompatActivity {
    TextInputEditText email,password,confirmpwd,fullname;
    Button register;
    TextView alrlogin,guest;
    FirebaseAuth fAuth;
    ProgressBar progressbar;
    FirebaseFirestore db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.reg_email);
        password = findViewById(R.id.reg_password);
        confirmpwd = findViewById(R.id.reg_confirmpwd);
        register = findViewById(R.id.reg_button);
        alrlogin = findViewById(R.id.reg_Login);
        fullname=findViewById(R.id.reg_name);

        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        progressbar = findViewById(R.id.reg_progress);

        alrlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity_register.this,activity_login.class));
                finish();
            }
        });



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validateandsubmit();
            }
        });

    }

    public void validateandsubmit(){
        if(!validate()){
            Toast.makeText(activity_register.this, "Remplissez correctement tous les champs", Toast.LENGTH_SHORT).show();
        }
        else{
            String mEmail = email.getText().toString().trim();

            String mPassword = password.getText().toString().trim();

            String mCPassword = confirmpwd.getText().toString().trim();
            String Name = fullname.getText().toString().trim();

            email.setText("");
            email.setHint("Email");
            password.setText("");
            password.setHint("Password");
            confirmpwd.setText("");
            confirmpwd.setHint("Confirm Password");


            progressbar.setVisibility(View.VISIBLE);
            System.out.println(mEmail);
            System.out.println(mPassword);
            fAuth.createUserWithEmailAndPassword(mEmail,mPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){

                        FirebaseUser user = fAuth.getCurrentUser();
                        Map<String, Object> map = new HashMap<>();
                        map.put("fullname", "" + Name);
                        db.collection("users").document(user.getUid().toString()).set(map);

                        user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressbar.setVisibility(View.INVISIBLE);
                                Toast.makeText(activity_register.this, "Email de comfirmation envoyé !!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(activity_register.this,activity_login.class));
                                finish();



                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressbar.setVisibility(View.INVISIBLE);
                                Toast.makeText(activity_register.this, "Email non envoyé!!  "+e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });


                    }else{
                        progressbar.setVisibility(View.INVISIBLE);
                        Toast.makeText(activity_register.this, "Erreur"+task.getException().getMessage() , Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public boolean validate(){

        boolean valid =  true;

        String mEmail = email.getText().toString().trim();

        String mPassword = password.getText().toString().trim();

        String mCPassword = confirmpwd.getText().toString().trim();


        if (TextUtils.isEmpty(mEmail)) {
                email.setError("Email non renseigné");
            valid =  false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
            email.setError("Entrez un Email valide");
            valid =  false;
        }

        if (TextUtils.isEmpty(mPassword)) {
            password.setError("Le champ est vide");
            valid =  false;
        }

        if (TextUtils.isEmpty(mCPassword)){
            confirmpwd.setError("Veuillez comfirmer votre mot de passe");
            valid =  false;
        }else if(!(mPassword).equals(mCPassword)){
            confirmpwd.setError("Les mots de passe ne correspondent pas!");
            valid =  false;
        }


        return valid;

    }


}
