package com.syahirah2023103611.electricbill;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {

    ImageView imageProfile;
    TextView textName, textMatric, textCourse, textCopyright;
    Button buttonGitHub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setTitle("About");

        imageProfile = findViewById(R.id.imageProfile);
        textName = findViewById(R.id.textViewName);
        textMatric = findViewById(R.id.textViewMatric);
        textCourse = findViewById(R.id.textViewCourse);
        textCopyright = findViewById(R.id.textViewCopyright);
        buttonGitHub = findViewById(R.id.buttonGitHub);

        imageProfile.setImageResource(R.drawable.syahirah_photo); // Pastikan image ini wujud di drawable

        buttonGitHub.setOnClickListener(v -> {
            String url = "https://github.com/syahirahnabihah/electricity-bill-app";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });
    }
}
