package com.milan.bataochatapplication.profile;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.milan.bataochatapplication.HomeActivity;
import com.milan.bataochatapplication.R;
import com.milan.bataochatapplication.signin.ForgetPasswordActivity;
import com.milan.bataochatapplication.signin.SignInActivity;
import com.milan.bataochatapplication.utils.HelperClass;
import com.milan.bataochatapplication.utils.SharedPrefHelper;
import com.squareup.picasso.Picasso;



public class ProfileFragment extends Fragment {

    View view;
    CardView cvForgotPassword, cvLogOut;
    FirebaseAuth auth;
    SharedPrefHelper sph;


    ImageView profilePic;
    TextView profileName, profileEmail, profileUsername, profileStatus, profileDesc, profilePhone;
    CardView cardStatus, cardUsername, cardDesc, cardPhone;

    ProgressBar progressBar;

    EditPhotoDialog dialog;

    private final int STORAGE_PERMISSION_REQUEST = 500;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public ProfileFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_profile, container, false);

        sph = new SharedPrefHelper(getContext());

        cvForgotPassword = view.findViewById(R.id.cv_forgot_password);
        cvLogOut = view.findViewById(R.id.cv_logout);
        profilePic = view.findViewById(R.id.iv_user_pic);
        profileName = view.findViewById(R.id.tv_person_name);
        profileUsername = view.findViewById(R.id.tv_username);
        profileEmail = view.findViewById(R.id.tv_user_email);
        profileStatus = view.findViewById(R.id.tv_status);
        profileDesc = view.findViewById(R.id.tv_desc);
        profilePhone = view.findViewById(R.id.tv_phone);
        progressBar = view.findViewById(R.id.progressBar);
        cardUsername = view.findViewById(R.id.cv_username);
        cardStatus = view.findViewById(R.id.cv_status);
        cardDesc = view.findViewById(R.id.cv_desc);
        cardPhone = view.findViewById(R.id.cv_phone);

        profileName.setText(sph.getUserData("firstname")+' '+sph.getUserData("lastname"));
        String email = sph.getUserData("email");
        profileEmail.setText(email);
        profileUsername.setText(sph.getUserData("username"));
        profilePhone.setText(sph.getUserData("phone"));


        Picasso.get().load(sph.getUserData("photo")).placeholder(R.drawable.icon_person).into(profilePic);

        cvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) getActivity()).openActivity(ForgetPasswordActivity.class.getSimpleName());
            }
        });


        cvLogOut.setOnClickListener(v -> {

            auth = FirebaseAuth.getInstance();
            auth.signOut();
            sph.clearData();
//            PackageManager pm  = getContext().getPackageManager();
//            ComponentName componentName=new ComponentName(getActivity().getPackageName(),getActivity().getPackageName()+".chat.SendToActivity");
//            pm.setComponentEnabledSetting(componentName,
//                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
//                    PackageManager.DONT_KILL_APP);

////            FirebaseMessaging.getInstance().unsubscribeFromTopic(groupName)
////                    .addOnCompleteListener(new OnCompleteListener<Void>() {
////                        @Override
////                        public void onComplete(@NonNull Task<Void> task) {
////                            String msg = "Successful";
////                            if (!task.isSuccessful()) {
////                                msg = "UnSuccessful";
////                            }
////
//////                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
////                        }
////                    });
//
//            FirebaseMessaging.getInstance().unsubscribeFromTopic("Students")
//                    .addOnCompleteListener(task -> {
//                        String msg = "Successful";
//                        if (!task.isSuccessful()) {
//                            msg = "UnSuccessful";
//                        }
//
//                    });
//
//            FirebaseMessaging.getInstance().unsubscribeFromTopic("Teachers")
//                    .addOnCompleteListener(task -> {
//                        String msg = "Successful";
//                        if (!task.isSuccessful()) {
//                            msg = "UnSuccessful";
//                        }
//
//                    });

            Intent defaultHome = new Intent(getContext(), SignInActivity.class);
            defaultHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(defaultHome);

        });



        profilePic.setOnClickListener(view -> {

            dialog = new EditPhotoDialog(getContext(), ProfileFragment.this);
            dialog.show();

        });

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        Log.d(TAG, "onActivityResult: ");



        if(requestCode == 200 && resultCode == Activity.RESULT_OK && data != null){

            progressBar.setVisibility(View.VISIBLE);

            auth = FirebaseAuth.getInstance();
            Uri selectedImage = data.getData();

            DatabaseReference userPhotoRef = HelperClass.dbReference().child("users/" + auth.getCurrentUser().getUid() + "/photo");


            String extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(getContext().getContentResolver().getType(selectedImage));
            StorageReference userRef = HelperClass.stReference().child("students/" + auth.getCurrentUser().getUid() + extension);
            userRef.putFile(selectedImage).addOnSuccessListener(taskSnapshot -> userRef.getDownloadUrl().addOnCompleteListener(task -> {



                String photoUrl = task.getResult().toString();
                userPhotoRef.setValue(photoUrl);
                sph.setStudentProperty("photo", photoUrl);
                Picasso.get().load(photoUrl).into(profilePic);
                progressBar.setVisibility(View.INVISIBLE);


            }));

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if(requestCode == STORAGE_PERMISSION_REQUEST && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            //permission granted

            if(dialog.isShowing())
                dialog.openFileChooser();

        }


    }
}