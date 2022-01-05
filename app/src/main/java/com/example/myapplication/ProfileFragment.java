package com.example.myapplication;

import android.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
/**
 * Created by Avinash on 11-03-2017.
 */

public class ProfileFragment extends Fragment {
    TextView nom,email,telephone,ville ,score ;
    ImageView photo;
    String name,mail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View profileView = inflater.inflate(R.layout.fragment_profile, container, false);

        name=getArguments().getString("name");
        mail=getArguments().getString("mail");
        nom=profileView.findViewById(R.id.tv_name);
        email=profileView.findViewById(R.id.email);
        ville=profileView.findViewById(R.id.tv_address);
        telephone=profileView.findViewById(R.id.phone);
        score=profileView.findViewById(R.id.score);
        photo=profileView.findViewById(R.id.photo);

        nom.setText(name);
        email.setText(mail);



        return profileView;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
