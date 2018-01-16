package dupre.com.outerspacemanager.outerspacemanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{

    private Toast toastMessage;
    private Button btnConnexion;
    private Button btnCreation;
    private Button btnValider;
    private EditText etUsername;
    private EditText etPassword;
    private EditText etMail;
    private TextView tvMail;
    private TextView tvDebug;

    //private osma_service service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        btnConnexion = (Button) findViewById(R.id.buttonConnexion);
            btnConnexion.setOnClickListener(this);
        btnCreation = (Button) findViewById(R.id.buttonCreation);
            btnCreation.setOnClickListener(this);
        btnValider = (Button) findViewById(R.id.buttonValider);
            btnValider.setOnClickListener(this);
        etUsername = (EditText) findViewById(R.id.editTextIdentifiant);
        etPassword = (EditText) findViewById(R.id.editTextPassword);
        etMail = (EditText) findViewById(R.id.editTextMail);
        tvMail = (TextView) findViewById(R.id.textViewMail);
        tvDebug = (TextView) findViewById(R.id.textViewDebug);

        etMail.animate().alpha(0.0f);
        tvMail.animate().alpha(0.0f);
        btnValider.animate().alpha(0.0f);






    }

    @Override
    public void onClick(View v){

        if (toastMessage != null){
            toastMessage.cancel();
        }

        if (v == btnConnexion) {

            User user = new User(etUsername.getText().toString(),etPassword.getText().toString(),etMail.getText().toString());
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://outer-space-manager.herokuapp.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            osma_service service = retrofit.create(osma_service.class);
            Call<AuthResponse> request = service.Connection(user);

            request.enqueue(new Callback<AuthResponse>(){
                @Override
                public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                    toastMessage = Toast.makeText(getApplicationContext(), "Yep baby !"+response.toString(), Toast.LENGTH_LONG);
                    toastMessage.show();
                }

                @Override
                public void onFailure(Call<AuthResponse> call, Throwable t) {
                    toastMessage = Toast.makeText(getApplicationContext(), "Oh shit oh shit oh shit oh shit oh shit", Toast.LENGTH_LONG);
                    toastMessage.show();
                    tvDebug.setText("Too bad ! "+ t.getMessage().toString());
                }
            });

        }else if(v == btnCreation){
            // Apparition du formulaire création de compte
            btnCreation.animate().alpha(0.0f);
            etMail.animate().alpha(1.0f);
            tvMail.animate().alpha(1.0f);
            btnValider.animate().alpha(1.0f);

        }else if(v == btnValider){
            User user = new User(etUsername.getText().toString(),etPassword.getText().toString(),etMail.getText().toString());
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://outer-space-manager.herokuapp.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            osma_service service = retrofit.create(osma_service.class);
            Call<AuthResponse> request = service.Creation(user);

            request.enqueue(new Callback<AuthResponse>(){
                @Override
                public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                    //toastMessage = Toast.makeText(getApplicationContext(), "Yep baby !"+response.toString(), Toast.LENGTH_LONG);
                    //toastMessage.show();
                    if (response.code() == 401 || response.code() == 400) {
                        try {
                            tvDebug.setText("Oh shit ! Error " + response.code() + " " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else{
                        tvDebug.setText("Yep baby ! " + response.body().getToken());
                    }

                }

                @Override
                public void onFailure(Call<AuthResponse> call, Throwable t) {
                    toastMessage = Toast.makeText(getApplicationContext(), "Oh shit oh shit oh shit oh shit oh shit", Toast.LENGTH_LONG);
                    toastMessage.show();
                }
            });


        }
    }



}
