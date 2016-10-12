package azoni.likeithikeit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        Button toTrails = (Button) findViewById(R.id.toTrails);;
        View.OnClickListener MainActivity = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, TrailsActivity.class));
            }
        };
        toTrails.setOnClickListener(MainActivity);
    }
}
