package com.milan.bataochatapplication.start;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.milan.bataochatapplication.HomeActivity;
import com.milan.bataochatapplication.R;
import com.milan.bataochatapplication.utils.SharedPrefHelper;

import static com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE;

public class SplashScreenActivity extends AppCompatActivity {

    FirebaseAuth auth;
    AppUpdateManager appUpdateManager;
    private static final int MY_APP_UPDATE_REQUEST_CODE = 1;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        context=this;

        // Creates instance of the manager.
        appUpdateManager = AppUpdateManagerFactory.create(context);

        // Checks that the platform will allow the specified type of update.
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(IMMEDIATE)) {
                // Request the update.
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            // Pass the intent that is returned by 'getAppUpdateInfo()'.
                            appUpdateInfo,
                            // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                            IMMEDIATE,
                            // The current activity making the update request.
                            this,
                            // Include a request code to later monitor this update request.
                            MY_APP_UPDATE_REQUEST_CODE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        });


        Handler handler = new Handler();

        handler.postDelayed(() -> {

            auth = FirebaseAuth.getInstance();
            if(auth.getCurrentUser() != null){
                updateUi();
            }else{
//                PackageManager pm  = context.getPackageManager();
//                ComponentName componentName=new ComponentName(getPackageName(),getPackageName()+".chat.SendToActivity");
//                pm.setComponentEnabledSetting(componentName,
//                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
//                        PackageManager.DONT_KILL_APP);
                Intent intent = new Intent(SplashScreenActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }, 3000);


    }


    public void updateUi() {

        SharedPrefHelper sph = new SharedPrefHelper(getApplicationContext());
        String type = sph.getUserData("type");
        if(type.equals("user")) {
            PackageManager pm  = context.getPackageManager();
            ComponentName componentName=new ComponentName(getPackageName(),getPackageName()+".user.SendToActivity");
            pm.setComponentEnabledSetting(componentName,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP);
//            PackageManager pm2  = context.getPackageManager();
//            ComponentName componentName2=new ComponentName(getPackageName(),getPackageName()+".user.PSendToActivity");
//            pm2.setComponentEnabledSetting(componentName2,
//                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
//                    PackageManager.DONT_KILL_APP);
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else{
            Toast.makeText(this, "Error here", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        appUpdateManager
                .getAppUpdateInfo()
                .addOnSuccessListener(
                        appUpdateInfo -> {
                            if (appUpdateInfo.updateAvailability()
                                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                                // If an in-app update is already running, resume the update.
                                try {
                                    appUpdateManager.startUpdateFlowForResult(
                                            appUpdateInfo,
                                            IMMEDIATE,
                                            this,
                                            MY_APP_UPDATE_REQUEST_CODE);
                                } catch (IntentSender.SendIntentException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_APP_UPDATE_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                Log.d("msg","Update failed!");
                // If the update is cancelled or fails,
                // you can request to start the update again.
                try{
                    this.finishAffinity();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}