package dupre.com.outerspacemanager.outerspacemanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class VGActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vg);
        btnMenu = (Button) findViewById(R.id.buttonMenuVG);
            btnMenu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        Intent intent = new Intent(VGActivity.this, MainActivity.class);
        startActivity(intent);
    }

}
