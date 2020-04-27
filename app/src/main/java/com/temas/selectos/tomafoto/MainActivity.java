package com.temas.selectos.tomafoto;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICTURE =7;
    private static final int REQUEST_CODE_ASK_PERMISION =10 ;
    ImageButton imgbCamara;
    ImageView imgFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgbCamara= findViewById(R.id.imgbCamara);
        imgFoto= findViewById(R.id.imgFoto);
        imgFoto.setOnClickListener(imgFotoClick);
        imgbCamara.setOnClickListener(onClickCamara);

    }


    View.OnClickListener imgFotoClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            PopupMenu popupMenu= new PopupMenu(MainActivity.this,imgFoto);
            popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(popmenuItemClick);
            popupMenu.show();
        }
    };

    PopupMenu.OnMenuItemClickListener popmenuItemClick= new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId())
            {
                case R.id.popmGuarda:
                    guardaFoto();

                    break;
                case R.id.popTomafoto:

                    break;

            }

            return true;
        }
    };

    private void guardaFoto() {
        pedirPermisos();
       File imgFile= getOutputMediaFile();
        BitmapDrawable bitmapDrawable=(BitmapDrawable) imgFoto.getDrawable();
        Bitmap bitmap= bitmapDrawable.getBitmap();
        ByteArrayOutputStream stream= new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,98,stream);
        byte[] byteArray= stream.toByteArray();

       try {
           FileOutputStream fos= new FileOutputStream(imgFile);
           fos.write(byteArray);
           fos.close();
       }
        catch (FileNotFoundException e) {
           e.printStackTrace();
       } catch (IOException e) {
           e.printStackTrace();
       }
        Toast.makeText(this, "El archivo está en " +imgFile.getAbsolutePath(),Toast.LENGTH_LONG).show();
    }



    private static Uri getOutputMediaFileUri(){
        return Uri.fromFile(getOutputMediaFile());
    }

    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "TomaFotoApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("TomaFotoApp", "No se pudo crear el directrio");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        //Log.d("Directorio","Dir: " +mediaStorageDir.getAbsolutePath() );
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");


        return mediaFile;
    }


    private void pedirPermisos() {
        int permisoStorage = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permisoCamara = ActivityCompat.checkSelfPermission(MainActivity.this,Manifest.permission.CAMERA);
        int permisoStorageRead= ActivityCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE);

        if( permisoStorage != PackageManager.PERMISSION_GRANTED || permisoCamara != PackageManager.PERMISSION_GRANTED || permisoStorageRead != PackageManager.PERMISSION_GRANTED)
        {
            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M)
            {
                requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE_ASK_PERMISION);
            }
        }
    }


    View.OnClickListener onClickCamara = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            invocaCamara();
        }
    };

    private void invocaCamara() {
        Intent tomarImagenIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if( tomarImagenIntent.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(tomarImagenIntent,REQUEST_IMAGE_PICTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( requestCode == REQUEST_IMAGE_PICTURE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap imagenBitmap = (Bitmap)extras.get("data");
            imgFoto.setImageBitmap(imagenBitmap);
        }
    }


}
