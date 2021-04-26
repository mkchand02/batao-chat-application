package com.milan.bataochatapplication.profile;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.button.MaterialButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.milan.bataochatapplication.R;
import com.milan.bataochatapplication.utils.SharedPrefHelper;
import com.squareup.picasso.Picasso;

public class EditPhotoDialog extends Dialog {

    ImageView ivProfilePhoto;
    MaterialButton btnUploadPhoto;
    ProgressBar progressBar;
    SharedPrefHelper sph;
    ProfileFragment parent;
    private final int STORAGE_PERMISSION_REQUEST = 500;

    public EditPhotoDialog(@NonNull Context context, ProfileFragment profileFragment) {
        super(context);

        this.parent = profileFragment;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_edit_profile_photo);

        ivProfilePhoto = findViewById(R.id.iv_profile_large);
        btnUploadPhoto = findViewById(R.id.btn_upload);
        progressBar = findViewById(R.id.progressBar);

        sph = new SharedPrefHelper(getContext());

        Picasso.get().load(sph.getUserData("photo")).placeholder(R.drawable.icon_person).into(ivProfilePhoto);




        btnUploadPhoto.setOnClickListener(view -> {




            if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                //request permssion
                if(ActivityCompat.shouldShowRequestPermissionRationale(parent.getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)){

                    //show permssion reasoning
                    makeText("Please allow STORAGE permission");

                }else{

                    //no reasoning required
                    ActivityCompat.requestPermissions(parent.getActivity(), new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_REQUEST);

                }

            }else{

                //permission already acquired
                openFileChooser();

            }



        });





    }

    private void makeText(String message) {

        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    public void openFileChooser() {
        Intent imageChooser = new Intent();
        imageChooser.setAction(Intent.ACTION_GET_CONTENT);
        imageChooser.setType("image/*");

        parent.startActivityForResult(Intent.createChooser(imageChooser, "Select Image"), 200);
        dismiss();
    }


}