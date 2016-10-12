package edu.uw.facebook.readyseteat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class GreenActivity extends AppCompatActivity {

    private ImageView moutputpic;
    private TextView moutputtext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_green);
        moutputpic = (ImageView) findViewById(R.id.outputpic);
        moutputtext = (TextView) findViewById(R.id.message);

    }
}
