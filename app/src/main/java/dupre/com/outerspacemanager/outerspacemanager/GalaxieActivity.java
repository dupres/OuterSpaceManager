package dupre.com.outerspacemanager.outerspacemanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GalaxieActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galaxie);

        btnMenu = (Button) findViewById(R.id.buttonMenuGalaxie);
            btnMenu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        if(v == btnMenu){
            Intent intent = new Intent (GalaxieActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

}
