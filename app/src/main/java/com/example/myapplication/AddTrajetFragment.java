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

public class AddTrajetFragment extends Fragment {
    TextView nom,email,telephone,ville ,score ;
    ImageView photo;
    String name,mail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View addTrajetView = inflater.inflate(R.layout.fragment_add_trajet, container, false);




        return addTrajetView;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
