package com.example.herrikoprueba;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.herrikoprueba.Clases.BaseActivity;

public class SobreNosotrosActivity extends BaseActivity {

    ImageButton botonInstagram;
    ImageButton botonFacebook;
    TextView descripcion;

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
        descripcion = findViewById(R.id.descripcionText);
        descripcion.setText("Somos la Asociación Andosilla Herriko Gazteak, una entidad nacida hace nueve años con el propósito de revitalizar la cultura y la lengua vasca en Andosilla. Formada por más de 200 socios de todas las edades, buscamos fomentar la inclusión, la diversidad y la sostenibilidad a través de actividades sociales y culturales variadas. Nuestro compromiso con la juventud y la comunidad nos mueve a promover la participación, el intercambio de ideas y la preservación de nuestras tradiciones y costumbres, convirtiéndonos en un motor dinamizador en la vida de nuestro pueblo.");

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