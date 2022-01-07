package com.example.myapplication;

import android.app.AlertDialog;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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


import java.util.Calendar;


/**
 * Created by Avinash on 11-03-2017.
 */

public class AddTrajetFragment extends Fragment {
    FirebaseDatabase rootNode;
    DatabaseReference reference;

    TextInputLayout from, to, displayDate,seat, displayTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    Button create, date, time;
    String dateData ;
    String timeData;
    String token;
    String unixdate = "";
    String unixtime = "";
    String unix = "";
    Context context;
    View addTrajetView;
    private FirebaseUser fUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        addTrajetView = inflater.inflate(R.layout.fragment_add_trajet, container, false);
        context= inflater.getContext();

        progressBarInvisible();



        from = addTrajetView.findViewById(R.id.from_id);
        to = addTrajetView.findViewById(R.id.to_id);
        date = addTrajetView.findViewById(R.id.set_date);
        seat = addTrajetView.findViewById(R.id.seat_id);
        displayDate = addTrajetView.findViewById(R.id.date_id);
        create = addTrajetView.findViewById(R.id.signup_bt);
        time = addTrajetView.findViewById(R.id.set_time);
        displayTime = addTrajetView.findViewById(R.id.time_id);
        fUser = FirebaseAuth.getInstance().getCurrentUser();

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                String mon = "";
                                if((monthOfYear+1) == 1) mon = "Jan";
                                if((monthOfYear+1) == 2) mon = "Feb";
                                if((monthOfYear+1) == 3) mon = "Mar";
                                if((monthOfYear+1) == 4) mon = "April";
                                if((monthOfYear+1) == 5) mon = "May";
                                if((monthOfYear+1) == 6) mon = "June";
                                if((monthOfYear+1) == 7) mon = "July";
                                if((monthOfYear+1) == 8) mon = "Aug";
                                if((monthOfYear+1) == 9) mon = "Sept";
                                if((monthOfYear+1) == 10) mon = "Oct";
                                if((monthOfYear+1) == 11) mon = "Nov";
                                if((monthOfYear+1) == 12) mon = "Dec";

                                dateData = dayOfMonth + " " + mon;

                                displayDate.getEditText().setText(dateData);

                                String mm = String.valueOf(monthOfYear+1);
                                if(monthOfYear+1 <= 9){
                                    mm = "0"+mm;
                                }
                                String dd = String.valueOf(dayOfMonth);
                                if(dayOfMonth<=9){
                                    dd = "0"+dd;
                                }
                                unixdate = mm + "/" + dd + "/" + String.valueOf(year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            }

        });


        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {


                                Integer hour = hourOfDay;
                                Integer minutes = minute;
                                String timeSet = "";
                                if (hour > 12) {
                                    hour -= 12;
                                    timeSet = "PM";
                                } else if (hour == 0) {
                                    hour += 12;
                                    timeSet = "AM";
                                } else if (hour == 12){
                                    timeSet = "PM";
                                }else{
                                    timeSet = "AM";
                                }

                                String min = "";
                                if (minutes < 10)
                                    min = "0" + minutes ;
                                else
                                    min = String.valueOf(minutes);

                                String aTime = new StringBuilder().append(hour).append(':')
                                        .append(min ).append(" ").append(timeSet).toString();

                                timeData = aTime;
                                displayTime.getEditText().setText(aTime);

                                String hh = String.valueOf(hour);
                                if(hour <=9){
                                    hh = "0"+hh;
                                }
                                String mm = String.valueOf(minute);
                                if(minute <= 9){
                                    mm = "0" + mm;
                                }
                                unixtime =hh + ":" + mm + ":" + "00";
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }

        });




        return addTrajetView;

    }
    private void progressBarVisible() {

        //hook
        ProgressBar progressBar = (ProgressBar) addTrajetView.findViewById(R.id.spin_kit);
        //type - change fadingcircle() to anything for more animation
        Sprite doubleBounce = new FadingCircle();
        progressBar.setIndeterminateDrawable(doubleBounce);

        //visibility
        progressBar.setVisibility(View.VISIBLE);

        //if progress bar visible then touch response on the activity will stop until it becomes invisible
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

    }

    private void progressBarInvisible() {

        ProgressBar progressBar = (ProgressBar) addTrajetView.findViewById(R.id.spin_kit);
        Sprite doubleBounce = new FadingCircle();
        progressBar.setIndeterminateDrawable(doubleBounce);

        progressBar.setVisibility(View.GONE);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

    }

    private boolean validate(TextInputLayout t) {


        String val = t.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            progressBarInvisible();
            t.setError("Field cannot be empty");
            return false;
        }
        else if(val.length()>25){
            progressBarInvisible();
            t.setError("Max characters should be less than 25");
            return false;
        }
        else {
            t.setError(null);
            return true;
        }
    }

    private boolean validateSeat(TextInputLayout t) {


        String val = t.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            progressBarInvisible();
            t.setError("Field cannot be empty");
            return false;
        }
        Integer num = Integer.parseInt(val);
        if(num > 10){
            progressBarInvisible();
            t.setError("Max 10");
            return false;
        }
        else {
            t.setError(null);
            return true;
        }
    }

    public void registerUser() {


        progressBarVisible();

        if (!validate(from) | !validate(to) | !validateSeat(seat) | !validate(displayDate) | !validate(displayTime))
            return;

        progressBarInvisible();




        final String name=getArguments().getString("name");
        final String fromData = from.getEditText().getText().toString().trim();
        final String toData = to.getEditText().getText().toString().trim();
        final String seatData = seat.getEditText().getText().toString().trim();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Trajets");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                Trajet gr_push = new Trajet();
                unix = unixdate+ " "+ unixtime;
                gr_push.setName(name);
                gr_push.setFrom(fromData);
                gr_push.setTo(toData);
                gr_push.setUnix(unix);
                gr_push.setSeat(seatData);
                ref.push().setValue(gr_push);


                        }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });








    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
