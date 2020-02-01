package com.example.helpme.everything;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.helpme.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class FullScreenImage extends AppCompatActivity {
    PhotoView imageView;
    ProgressBar progressBar;
    PhotoViewAttacher photoViewAttacher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        imageView = findViewById(R.id.full_screen_imageview);
        progressBar = findViewById(R.id.progressBar_full_screen_imageView);
        progressBar.setVisibility(View.VISIBLE);


        Picasso.with(getApplicationContext())
                .load(getIntent().getStringExtra("link"))
                .error(R.drawable.ic_failed_to_load_image)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.GONE);
                    }
                    @Override
                    public void onError() {
                        progressBar.setVisibility(View.GONE);
                    }
                });
        photoViewAttacher = new PhotoViewAttacher(imageView);
    }
}
