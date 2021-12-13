package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class activity_login extends AppCompatActivity {

    TextInputEditText email,password;
    Button login;
    TextView newusersignup;
    TextView forgotPassword;
    FirebaseAuth fAuth;
    ProgressBar progressbar;
    private Context mContext;
    private Activity mActivity;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.log_email);
        password = findViewById(R.id.log_password);
        login = findViewById(R.id.log_button);
        newusersignup = findViewById(R.id.log_Register);
        forgotPassword = findViewById(R.id.log_forgotpassword);
        fAuth = FirebaseAuth.getInstance();
        progressbar = findViewById(R.id.log_progress);
        mContext = getApplicationContext();
        mActivity = activity_login.this;

        if(fAuth.getCurrentUser()!= null && fAuth.getCurrentUser().isEmailVerified()){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        newusersignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity_login.this, activity_register.class));
            }
        });


        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText resetMail = new EditText(v.getContext());
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Mot de passe oublié ? : ");
                passwordResetDialog.setMessage("Entrez votre email pour restaurer votre mot de passe :");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("CONFIRMER", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String mail = resetMail.getText().toString().trim();
                        if (TextUtils.isEmpty(mail)) {
                            Toast.makeText(activity_login.this,"Email Requis",Toast.LENGTH_SHORT).show();
                            return;
                        } else if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
                            Toast.makeText(activity_login.this,"Entrez un email valide !",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(activity_login.this," Lien de restauration envoyé!!",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(activity_login.this," Erreur !!  "+e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

                passwordResetDialog.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                passwordResetDialog.create().show();

            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateandsubmit();

            }
        });


    }

    public void validateandsubmit(){
        if(!validate()){
            Toast.makeText(activity_login.this, "Remplissez correctement tous les champs !!", Toast.LENGTH_SHORT).show();
        }
        else{
            String mEmail = email.getText().toString().trim();

            String mPassword = password.getText().toString().trim();

            email.setText("");
//            email.setHint("Email");
//
            password.setText("");
//            password.setHint("Password");


            progressbar.setVisibility(View.VISIBLE);

            fAuth.signInWithEmailAndPassword(mEmail,mPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    FirebaseUser user = fAuth.getCurrentUser();

                    if(task.isSuccessful()){
                        if(user.isEmailVerified()){
                            progressbar.setVisibility(View.INVISIBLE);
                            Toast.makeText(activity_login.this, "Connecté!!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }else{
                            progressbar.setVisibility(View.INVISIBLE);
                            Toast.makeText(activity_login.this, "Eamail non verifié !!", Toast.LENGTH_SHORT).show();

                        }
                    }
                    else{
                        progressbar.setVisibility(View.INVISIBLE);
                        Toast.makeText(activity_login.this, "Erreur :"+task.getException().getMessage() , Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }

    public boolean validate(){

        String mEmail = email.getText().toString().trim();

        String mPassword = password.getText().toString().trim();

        boolean valid =  true;

        if (TextUtils.isEmpty(mEmail)) {
            email.setError("Le champ Email est vide");
            valid =  false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
            email.setError("Veuillez comfirmer votre mot de passe");
            valid =  false;
        }

        if (TextUtils.isEmpty(mPassword)) {
            password.setError("Le champ password est vide");
            valid =  false;

        }

        return valid;

    }

    @Override
    public void onBackPressed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);

        builder.setTitle("Comfirmez!!");
        builder.setMessage("Voulez vous quittez l'application ??");
        builder.setCancelable(true);

        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Do something when user want to exit the app
                // Let allow the system to handle the event, such as exit the app
//                activity_login.super.onBackPressed();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                System.exit(0);
            }
        });

        builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Do something when want to stay in the app
//                Toast.makeText(mContext,"thank you",Toast.LENGTH_LONG).show();
            }
        });

        // Create the alert dialog using alert dialog builder
        AlertDialog dialog = builder.create();

        // Finally, display the dialog when user press back button
        dialog.show();
    }




}
