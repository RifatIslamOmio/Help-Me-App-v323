package com.example.helpme.Models;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.core.content.FileProvider;

import com.example.helpme.Activities.PostActivity;
import com.example.helpme.Extras.Constants;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import id.zelory.compressor.Compressor;

public class Photo {

    private PostActivity activity;

    private File photoFile;
    private Uri photoURI;
    private String photoPath;

    private File compressPhotoFile;
    private String compressPhotoPath;

    public Photo(PostActivity activity) throws IOException {
        this.activity = activity;
        this.photoFile = createImageFile();

        if(photoFile!=null) {
            this.photoURI = FileProvider.getUriForFile( activity.getApplicationContext(),
                    "com.example.android.photofileprovider",
                    photoFile);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = activity.getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        photoPath = image.getAbsolutePath();
        return image;
    }

    public File getPhotoFile() {
        return photoFile;
    }

    public Uri getPhotoURI() {
        return photoURI;
    }

    public String getPhotoPath() {
        return photoPath;
    }


    public void compressPhotoFile() throws IOException {

        this.compressPhotoFile = new Compressor(this.activity)
                    .setQuality(75)
                    .setDestinationDirectoryPath(activity.getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath())
                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                .compressToFile(this.photoFile, "temp_image.jpg");

        this.compressPhotoPath = this.compressPhotoFile.getAbsolutePath();

    }

    public File getCompressPhotoFile() {
        return compressPhotoFile;
    }

    public String getCompressPhotoPath() {
        return compressPhotoPath;
    }

    public void takePhoto(){

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, this.photoURI);

        activity.startActivityForResult(takePictureIntent, Constants.REQUEST_TAKE_PHOTO);

    }
}
