package com.temas.selectos.tomafoto;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICTURE =7;
    ImageButton imgbCamara;
    ImageView imgFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgbCamara= findViewById(R.id.imgbCamara);
        imgFoto= findViewById(R.id.imgFoto);
        imgbCamara.setOnClickListener(onClickCamara);

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
