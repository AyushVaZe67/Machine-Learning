package com.example.laptoppricepredictor;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class IntroPage extends AppCompatActivity {

    ImageView logo;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_intro_page);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        logo = findViewById(R.id.logo);
        text = findViewById(R.id.text);

        animateLogo();
        animateText();

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(IntroPage.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, 2000);
    }

    private void animateLogo() {
        ObjectAnimator translateY = ObjectAnimator.ofFloat(logo, "translationY", -500f, 0f);
        translateY.setDuration(1500);

        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(logo, "alpha", 0f, 1f);
        fadeIn.setDuration(1500);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(translateY, fadeIn);
        animatorSet.start();
    }

    private void animateText() {
        ObjectAnimator translateY = ObjectAnimator.ofFloat(text, "translationY", 500f, 0f);
        translateY.setDuration(1500);

        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(text, "alpha", 0f, 1f);
        fadeIn.setDuration(1500);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(translateY, fadeIn);
        animatorSet.start();
    }
}
