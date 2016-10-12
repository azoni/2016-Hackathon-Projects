package edu.uw.facebook.readyseteat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView red;
    private ImageView yellow;
    private ImageView green;
    private CountDownTimer animationColor;
    private Button mStart;
    private Button mCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        red = (ImageView) findViewById(R.id.Red);
        yellow = (ImageView) findViewById(R.id.Yellow);
        green = (ImageView) findViewById(R.id.Green);
        mStart = (Button) findViewById(R.id.Start);

        mStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ImageRecognitionActivity.class);
                startActivity(intent);
            }
        });

        mCamera = (Button) findViewById(R.id.camerabtn);
        mCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
                startActivity(intent);
            }
        });

        animationColor = new CountDownTimer(20000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                final Drawable temp1 = red.getDrawable();
                final Drawable temp2 = yellow.getDrawable();
                red.setImageDrawable(green.getDrawable());
                yellow.setImageDrawable(temp1);
                green.setImageDrawable(temp2);
            }

            @Override
            public void onFinish() {
                animationColor.start();

            }
        }.start();

    }
}
