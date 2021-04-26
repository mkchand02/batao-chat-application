package com.milan.bataochatapplication.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.github.chrisbanes.photoview.PhotoView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class HelperClass {


    public static void viewMedia(final File path, final String fileName, String
            mediaType, PhotoView photoView, LinearLayout photoViewLayout, Context context){
        if (mediaType.equals("Image")) {
            if(!isFileExist(fileName,path)){
                Toast.makeText(context, "File dosen't exist at desired location \nPlease view your file from original location", Toast.LENGTH_LONG).show();
                return;
            }
            BitmapFactory.Options options1 = new BitmapFactory.Options();
            options1.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path + "/" + fileName, options1);
            int imageHeight = options1.outHeight;
            int imageWidth = options1.outWidth;
            options1.inJustDecodeBounds = false;
//                  recreate the stream
//                  some calculation to define inSampleSize
            BitmapFactory.Options options2 = new BitmapFactory.Options();
            if (imageHeight > 3000 || imageWidth > 3000) {
                options2.inSampleSize = 8;
            } else if (imageHeight < 3000 && imageHeight > 1500 || imageWidth < 3000 && imageWidth > 1500) {
                options2.inSampleSize = 4;
            } else {
                options2.inSampleSize = 1;
            }
            Log.d("path", "path=" + path + "/" + fileName);
            Bitmap imageBitmap = BitmapFactory.decodeFile(path + "/" + fileName, options2);

            photoViewLayout.setVisibility(View.VISIBLE);
            photoView.setImageBitmap(imageBitmap);

        } else{
            if(!isFileExist(fileName,path)){
                Toast.makeText(context, "File doesn't exist at desired location \nPlease view your file from original location", Toast.LENGTH_LONG).show();
                return;
            }
            File file = new File(path + "/" + fileName);
            Log.d("file","filePath="+file);
            Intent intent = new Intent(Intent.ACTION_VIEW);

            String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileName.substring(fileName.lastIndexOf(".") + 1));
            Uri apkURI = FileProvider.getUriForFile(
                    context,
                    context.getApplicationContext()
                            .getPackageName() + ".provider", file);
            intent.setDataAndType(apkURI, mimeType);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(Intent.createChooser(intent, "Choose App To Open"));
        }

    }

    public static boolean isFileExist(String fileName, File rootPath){
        boolean exist = false;
        File folder_file = new File(String.valueOf(rootPath));
        File[] files = folder_file.listFiles();

        if(!(files ==null)){
            for(File file:files){
                if(file.getName().equals(fileName)){
                    exist=true;
                    break;
                }
            }
        }

        return exist;
    }

    public static DatabaseReference dbReference(){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("test");
        //mDatabase.keepSynced(true);
        return mDatabase;
    }

    public static StorageReference stReference(){
        StorageReference mDatabase = FirebaseStorage.getInstance().getReference("test");
        return mDatabase;
    }

    public static void createNotificationChannel(String channelId, String name, String description, Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}

