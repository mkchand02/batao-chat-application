package com.milan.bataochatapplication;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.milan.bataochatapplication.chat.BroadcastActivity;
import com.milan.bataochatapplication.chat.ChatFragment;
import com.milan.bataochatapplication.profile.ProfileFragment;
import com.milan.bataochatapplication.signin.ForgetPasswordActivity;
import com.milan.bataochatapplication.signin.SignInActivity;
import com.milan.bataochatapplication.utils.SharedPrefHelper;

import static com.milan.bataochatapplication.utils.Util.checkInterentConnectivity;

public class HomeActivity extends AppCompatActivity {

    FrameLayout frameLayout;
    FirebaseAuth auth;
    FirebaseUser user;

    AlertDialog permissionRequestDialog;

    long lastBackPressed = 0;
    Context context;
    BottomNavigationView bottomNavigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        openFragment(ProfileFragment.newInstance("", ""));
        setTitle("Your Profile");

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_profile:
                            openFragment(ProfileFragment.newInstance("", ""));
                            return true;
                        case R.id.navigation_chats:
                            openFragment(ChatFragment.newInstance("", ""));
                            return true;
                        case R.id.navigation_user_list:
                            openFragment(UsersListFragment.newInstance("", ""));
                            return true;
                        default:
                            Log.d("TAG","Error in Home");
                    }
                    return false;
                }
            };

    @Override
    protected void onStart() {
        super.onStart();
        if(!hasPermissionsgallery()){
            requestPermissionGallery();
        }
        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() == null){
            Intent intent = new Intent(HomeActivity.this, SignInActivity.class);
            startActivity(intent);
            finish();
        }

        checkInterentConnectivity(this, frameLayout);
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void openActivity(String activity) {
        if (activity.equals(ForgetPasswordActivity.class.getSimpleName())) {
            Intent intent = new Intent(HomeActivity.this, ForgetPasswordActivity.class);
            startActivity(intent);
        }else if (activity.equals(BroadcastActivity.class.getSimpleName())) {
            Intent intent = new Intent(HomeActivity.this, BroadcastActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(auth.getCurrentUser() != null)
            getMenuInflater().inflate(R.menu.menu_main, menu);

        getMenuInflater().inflate(R.menu.menu_basic, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_signout) {
            auth = FirebaseAuth.getInstance();
            auth.signOut();
            SharedPrefHelper sph = new SharedPrefHelper(getApplicationContext());
            sph.clearData();
            Intent defaultHome = new Intent(HomeActivity.this, SignInActivity.class);
            defaultHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(defaultHome);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if(lastBackPressed == 0 && getSupportFragmentManager().getBackStackEntryCount() == 0){
            lastBackPressed = System.currentTimeMillis();
            Toast.makeText(this, "Press Back Again To Quit", Toast.LENGTH_SHORT).show();
            return;
        }else if(System.currentTimeMillis() - lastBackPressed > 2*1000 && getSupportFragmentManager().getBackStackEntryCount() == 0){
            lastBackPressed = System.currentTimeMillis();
            Toast.makeText(this, "Press Back Again To Quit", Toast.LENGTH_SHORT).show();
            return;
        }
        super.onBackPressed();
    }

    public boolean hasPermissionsgallery() {

        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermissionGallery() {
        String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, 1);
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            // for each permission check if the user granted/denied them
            for (int i = 0, len = permissions.length; i < len; i++) {
                String permission = permissions[i];
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    // user rejected the permission
                    boolean showRationale = shouldShowRequestPermissionRationale( permission );
                    if (! showRationale) {
                        // user also CHECKED "never ask again"
                        // you can either enable some fall back,
                        // disable features of your app
                        // or open another dialog explaining
                        // again the permission and directing to
                        // the app setting
                        if(permissionRequestDialog !=null){
                            permissionRequestDialog.dismiss();
                            permissionRequestDialog =null;
                        }
                        permissionRequestDialog =new AlertDialog.Builder(context).create();
                        permissionRequestDialog.setCancelable(false);
                        permissionRequestDialog.setCanceledOnTouchOutside(false);
                        permissionRequestDialog.setTitle("Permission is required for downloading media files, please Allow us.");
                        permissionRequestDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Allow", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivityForResult(intent, 2);
                                permissionRequestDialog.dismiss();
                            }
                        });
                        permissionRequestDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Deny", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        permissionRequestDialog.show();

                    } else {
                        // user did NOT check "never ask again"
                        // this is a good place to explain the user
                        // why you need the permission and ask if he wants
                        // to accept it (the rationale)
                        if(!hasPermissionsgallery()){
                            getPermissionRequestDialog();
                        }

                    }
                }
            }
        }
    }

    public void getPermissionRequestDialog(){
        if(permissionRequestDialog !=null){
            permissionRequestDialog.dismiss();
            permissionRequestDialog =null;
        }
        permissionRequestDialog =new AlertDialog.Builder(context).create();
        permissionRequestDialog.setCancelable(false);
        permissionRequestDialog.setCanceledOnTouchOutside(false);
        permissionRequestDialog.setTitle("Permission is neccessary to proceed,please allow.");
        permissionRequestDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Allow", (dialog, which) -> {
            requestPermissionGallery();
            permissionRequestDialog.dismiss();
        });
        permissionRequestDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Deny", (dialog, which) -> finish());
        permissionRequestDialog.show();
    }
}