package com.example.herrikoprueba;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.herrikoprueba.Clases.BaseActivity;

public class SobreNosotrosActivity extends BaseActivity {

    ImageButton botonInstagram;
    ImageButton botonFacebook;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_sobre_nosotros;  // Retorno el layout específico de MainActivity
    }

    private Button volverHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_sobre_nosotros);

        botonInstagram= findViewById(R.id.instagramBoton);
        botonFacebook= findViewById(R.id.facebookBoton);

        botonFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.facebook.com/profile.php?id=100009440192950";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });botonInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.instagram.com/andosillaherriko/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        /*botonFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String YourPageID = "100009440192950";
                try {
                    // intent para abrir la aplicación de Facebook, si está instalada
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/" + YourPageID));
                    startActivity(intent);
                } catch(Exception e) {
                    // si la aplicación de Facebook no está instalada, redirige al usuario a la página web
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/profile.php?id=" + YourPageID)));
                }
            }
        });*/


        VideoView videoView = findViewById(R.id.videoView);

// URL del video
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.video1;
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);

// Crea y configura un MediaController
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

// Comienza a reproducir el video
        videoView.start();

    }
}