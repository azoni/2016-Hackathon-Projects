package azoni.likeithikeit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CalenderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calender_layout);

        Button toJoin = (Button) findViewById(R.id.toJoin);

        View.OnClickListener join = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CalenderActivity.this, JoinActivity.class));
            }
        };
        toJoin.setOnClickListener(join);

        Button toCreate = (Button) findViewById(R.id.toCreate);


        View.OnClickListener create = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CalenderActivity.this, CreateActivity.class));
            }
        };
        toCreate.setOnClickListener(create);
    }
}
