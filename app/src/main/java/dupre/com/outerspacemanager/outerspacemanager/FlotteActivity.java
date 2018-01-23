package dupre.com.outerspacemanager.outerspacemanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FlotteActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flotte);

        btnMenu = (Button) findViewById(R.id.buttonMenuFlotte);
            btnMenu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        if (v == btnMenu){
            Intent intent = new Intent (FlotteActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

}
