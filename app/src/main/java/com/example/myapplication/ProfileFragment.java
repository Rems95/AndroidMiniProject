package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.TelephonyManager;
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

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import com.google.firebase.database.FirebaseDatabase;

import static android.Manifest.permission.READ_PHONE_NUMBERS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Avinash on 11-03-2017.
 */

public class ProfileFragment extends Fragment {
    TextView nom,email,telephone,ville ,score ;
    ImageView photo;
    String name,mail,address;
    Context context;

    private TextView save;
    private TextView changePhoto;
    private MaterialEditText username;

    private FirebaseUser fUser;
    private Uri mImageUri;
    private StorageTask uploadTask;
    private StorageReference storageRef;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View profileView = inflater.inflate(R.layout.fragment_profile, container, false);
        context= inflater.getContext();

        name=getArguments().getString("name");
        mail=getArguments().getString("mail");
        address=getArguments().getString("address");
        nom=profileView.findViewById(R.id.tv_name);
        email=profileView.findViewById(R.id.email);
        ville=profileView.findViewById(R.id.tv_address);
        telephone=profileView.findViewById(R.id.phone);
        score=profileView.findViewById(R.id.score);
        photo=profileView.findViewById(R.id.photo);
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        storageRef = FirebaseStorage.getInstance().getReference().child("Profile Images");
        nom.setText(name);
        email.setText(mail);
        ville.setText(address);

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setCropShape(CropImageView.CropShape.OVAL).start(context,ProfileFragment.this);
            }
        });
        System.out.println(FirebaseDatabase.getInstance().getReference().child("Users").child(fUser.getUid()));
        FirebaseDatabase.getInstance().getReference().child("Users").child(fUser.getUid()).addValueEventListener(new ValueEventListener() {
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

        if (ActivityCompat.checkSelfPermission(context, READ_SMS) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, READ_PHONE_NUMBERS) ==
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context,
                READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager tMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String mPhoneNumber = tMgr.getLine1Number();
            telephone.setText(mPhoneNumber);
        } else {
            requestPermission();
        }


        return profileView;
    }
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{READ_SMS, READ_PHONE_NUMBERS, READ_PHONE_STATE}, 100);
        }
    }
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100:
                TelephonyManager tMgr = (TelephonyManager)  context.getSystemService(Context.TELEPHONY_SERVICE);
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) !=
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context,
                        Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED  &&
                        ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) !=      PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                String mPhoneNumber = tMgr.getLine1Number();
                telephone.setText(mPhoneNumber);

                break;
        }
    }
   private void uploadImage() {
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("Uploading");
        pd.show();

        if (mImageUri != null) {
            final StorageReference fileRef = storageRef.child(System.currentTimeMillis() + ".jpeg");

            uploadTask = fileRef.putFile(mImageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return  fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String url = downloadUri.toString();

                        FirebaseDatabase.getInstance().getReference().child("Users").child(fUser.getUid()).child("ImageUrl").setValue(url);
                        pd.dismiss();
                    } else {
                        pd.dismiss();
                        Toast.makeText(context, "Upload failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            pd.dismiss();
            Toast.makeText(context, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            mImageUri = result.getUri();

            uploadImage();
        } else {
            Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
