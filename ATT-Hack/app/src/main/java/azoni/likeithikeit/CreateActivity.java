package azoni.likeithikeit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CreateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_layout);

        Button toConfirm = (Button)findViewById(R.id.toConfirm);

        View.OnClickListener create = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateActivity.this, ConfirmActivity.class));
            }
        };
        toConfirm.setOnClickListener(create);
    }
}
