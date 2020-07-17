package com.jisooZz.haru.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.jisooZz.haru.R;

public class SplashActivity extends AppCompatActivity {


    MediaPlayer mp;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);




        Handler hd = new Handler();
        hd.postDelayed(new splashhandler(),3000);

        mp=MediaPlayer.create(this,R.raw.theysay);
        mp.setLooping(true);
        mp.start();


        String str = "오늘 하루는 어땠나요 ?";
        textView = (TextView)findViewById(R.id.textView);
        SpannableStringBuilder ssb = new SpannableStringBuilder(str);
        ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#B69108")),3,5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(ssb);


    }

    private class splashhandler implements Runnable{
        public void run(){
            startActivity(new Intent(getApplication(), LoginActivity.class));
            SplashActivity.this.finish();
        }
    }
}
