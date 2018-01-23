package dupre.com.outerspacemanager.outerspacemanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class BatActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bat);

        btnMenu = (Button) findViewById(R.id.buttonMenuBat);
            btnMenu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        Intent intent = new Intent(BatActivity.this, MainActivity.class);
        startActivity(intent);
    }

}
