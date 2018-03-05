package dupre.com.outerspacemanager.outerspacemanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnVG;
    private Button btnBat;
    private Button btnFlotte;
    private Button btnRech;
    private Button btnChant;
    private Button btnGalaxie;
    private Button btnQuitter;
    private ArrayList<Button> btnList;
    private TextView tvUsername;
    private Toast toastMessage;
    private TextView tvPoints;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnVG = (Button) findViewById(R.id.buttonVG);
            btnVG.setOnClickListener(this);
        btnBat = (Button) findViewById(R.id.buttonBat);
            btnBat.setOnClickListener(this);
        btnFlotte = (Button) findViewById(R.id.buttonFlotte);
            btnFlotte.setOnClickListener(this);
        btnRech = (Button) findViewById(R.id.buttonRech);
            btnRech.setOnClickListener(this);
        btnChant = (Button) findViewById(R.id.buttonChant);
            btnChant.setOnClickListener(this);
        btnGalaxie = (Button) findViewById(R.id.buttonGalaxie);
            btnGalaxie.setOnClickListener(this);
        btnQuitter = (Button) findViewById(R.id.buttonQuitter);
            btnQuitter.setOnClickListener(this);
        btnList = new ArrayList<Button>();
        btnList.add(btnVG);
        btnList.add(btnBat);
        btnList.add(btnFlotte);
        btnList.add(btnRech);
        btnList.add(btnChant);
        btnList.add(btnGalaxie);
        btnList.add(btnQuitter);



        tvUsername = (TextView) findViewById(R.id.textViewUsername);
        tvPoints = (TextView) findViewById(R.id.textViewPoints);

        SharedPreferences settings = getSharedPreferences("session",0);
        tvUsername.setText("Capitaine "+ucfirst(settings.getString("username" ,"").toString()));


        //-------------------------------

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://outer-space-manager.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        osma_service service = retrofit.create(osma_service.class);
        Call<CUResponse> request = service.getCurrentUser(settings.getString("token",""));

        request.enqueue(new Callback<CUResponse>(){
            @Override
            public void onResponse(Call<CUResponse> call, Response<CUResponse> response) {
                //toastMessage = Toast.makeText(getApplicationContext(), "Yep baby !"+response.toString(), Toast.LENGTH_LONG);
                //tvDebug.setText(toString());
                //toastMessage.show();
                final String PREFS_NAME = "session";

                SharedPreferences settings = getSharedPreferences(PREFS_NAME,0);
                SharedPreferences.Editor editor = settings.edit();


                if (response.code()==403) {

                    try {
                        tvPoints.setText(response.errorBody().string());
                        //tvPoints.setText(settings.getString("token",""));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }else if (response.code() ==401){
                    try{
                        tvPoints.setText(response.errorBody().string());
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }else{
                    editor.putString("gas", response.body().getGas());
                    editor.putString("gasModifier", response.body().getGasModifier());
                    editor.putString("minerals", response.body().getMinerals());
                    editor.putString("mineralsModifier", response.body().getMineralModifier());
                    editor.putString("points", response.body().getPoints());
                    editor.commit();

                    if (response.body().getPoints() == "1") {
                        tvPoints.setText("1 point");
                    } else {
                        tvPoints.setText(response.body().getPoints() + " points");
                    }
                }


            }

            @Override
            public void onFailure(Call<CUResponse> call, Throwable t) {
                toastMessage = Toast.makeText(getApplicationContext(), "Une erreur est survenue, impossible de récupérer les informations de l'utilisateur.", Toast.LENGTH_LONG);
                toastMessage.show();
            }
        });



        //-------------------------------
    }

    @Override
    public void onClick(View v) {
        //Toast.makeText(MainActivity.this,"Click",Toast.LENGTH_LONG);
        for (Iterator<Button> aButton = btnList.iterator(); aButton.hasNext();){
            Button button = aButton.next();
            if (v == button){
                button.animate().alpha(1.0f);
            }else{
                button.animate().alpha(0.5f);
            }
        }

        if (v == btnQuitter){
            SharedPreferences settings = getSharedPreferences("session",0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("token","");
            editor.putLong("expire",0);
            editor.putString("username","");
            editor.putString("email","");
            editor.commit();

            Intent intent = new Intent (MainActivity.this, SignUpActivity.class);
            startActivity(intent);
        }else if(v == btnVG){
            Intent intent = new Intent (MainActivity.this, VGActivity.class);
            startActivity(intent);
        }else if(v == btnBat){
            Intent intent = new Intent (MainActivity.this, BatActivity.class);
            startActivity(intent);
        }else if (v == btnFlotte){
            Intent intent = new Intent (MainActivity.this, FlotteActivity.class);
            startActivity(intent);
        }else if(v == btnRech){
            Intent intent = new Intent (MainActivity.this, RechActivity.class);
            startActivity(intent);
        }else if(v == btnChant){
            Intent intent = new Intent (MainActivity.this, ChantActivity.class);
            startActivity(intent);
        }else if(v == btnGalaxie){
            Intent intent = new Intent (MainActivity.this, GalaxieActivity.class);
            startActivity(intent);
        }


    }


    public static String ucfirst(String chaine){
        return chaine.substring(0, 1).toUpperCase()+ chaine.substring(1).toLowerCase();
    }

}
