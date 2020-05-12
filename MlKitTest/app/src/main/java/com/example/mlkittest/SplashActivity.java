package com.example.mlkittest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    Animation animationLogo;
    Animation animationText;
    AppCompatImageView logo;
    AppCompatTextView appName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        logo = (AppCompatImageView)findViewById(R.id.logo_view);
        animationLogo = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        animationLogo.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {


            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        logo.startAnimation(animationLogo);
    }

}
