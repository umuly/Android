package com.umuly.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.umuly.R;

import static com.umuly.constants.Constants.TOKEN_KEY;

public class SplashActivity extends AppCompatActivity {
    Context context;
    TextView splashText;
    Typeface helvaticaFont;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharedPref = this.getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        helvaticaFont = Typeface.createFromAsset(getAssets(), "font/HelveticaMedium.ttf");
        bindView();

    }


    private void bindView() {
        splashText = findViewById(R.id.splash_text);
        splashText.setTypeface(helvaticaFont);
        timer();
    }

    private void timer() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if ("".equalsIgnoreCase(sharedPref.getString(TOKEN_KEY, ""))) {
                    finish();
                    startActivity(new Intent(context, LoginActivity.class));
                } else {
                    finish();
                    startActivity(new Intent(context, HistoryUrlActivity.class));
                }


            }
        }, 3000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        context = SplashActivity.this;
    }
}
