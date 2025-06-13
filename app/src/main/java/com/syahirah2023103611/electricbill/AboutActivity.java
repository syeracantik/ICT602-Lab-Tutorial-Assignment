package com.syahirah2023103611.electricbill;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AboutActivity extends AppCompatActivity {

    ImageView imageProfile;
    TextView textName, textMatric, textCourse, textCopyright;
    Button buttonGitHub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("👩‍💻 About This App");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageProfile = findViewById(R.id.imageProfile);
        textName = findViewById(R.id.textViewName);
        textMatric = findViewById(R.id.textViewMatric);
        textCourse = findViewById(R.id.textViewCourse);
        textCopyright = findViewById(R.id.textViewCopyright);
        buttonGitHub = findViewById(R.id.buttonGitHub);

        imageProfile.setImageResource(R.drawable.syahirah_photo); // make sure file exists

        // 🔹 GitHub Link Action
        buttonGitHub.setOnClickListener(v -> {
            String url = "https://github.com/syeracantik/ICT602-Lab-Tutorial-Assignment";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
