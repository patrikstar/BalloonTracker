package com.balloontracker;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by patri_000 on 18.12.2016.
 */

public class MainActivity extends AppCompatActivity {
    ImageButton car;
    ImageButton balloon;
    FloatingActionButton fab;
    Context context;
    String mCurrentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        car =(ImageButton) findViewById(R.id.ib_car);
        Picasso.with(this).load(R.drawable.car_button_pushf).into(car);


        balloon =(ImageButton) findViewById(R.id.ib_balloon);
        Picasso.with(this).load(R.drawable.balloon_button_pushf).into(balloon);

        fab = (FloatingActionButton) findViewById(R.id.fab_main);
        Picasso.with(this).load(R.drawable.camera_button_pushf).into(fab);
        init();

    }

    private void init() {
        car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent carintent = new Intent(getApplication(), MapsActivity.class);
                carintent.putExtra("car",101);
                startActivity(carintent);
            }
        });
        balloon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent balloonintent = new Intent(getApplication(), MapsActivity.class);
                balloonintent.putExtra("balloon", 102);
                startActivity(balloonintent);
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });


    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
         mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
}
