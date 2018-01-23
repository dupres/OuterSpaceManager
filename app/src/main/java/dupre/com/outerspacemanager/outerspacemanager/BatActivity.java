package dupre.com.outerspacemanager.outerspacemanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BatActivity extends AppCompatActivity implements AdapterView.OnClickListener {

    private Button btnMenu;
    private TextView tvDebug;
    private Toast toastMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bat);

        btnMenu = (Button) findViewById(R.id.buttonMenuBat);
            btnMenu.setOnClickListener(this);

        tvDebug = (TextView) findViewById(R.id.textViewBatDebug);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://outer-space-manager.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SharedPreferences settings = getSharedPreferences("session",0);
        osma_service service = retrofit.create(osma_service.class);
        Call<buildingsResponse> request = service.getBuildings(settings.getString("token",""));

        request.enqueue(new Callback<buildingsResponse>(){
            @Override
            public void onResponse(Call<buildingsResponse> call, Response<buildingsResponse> response) {
                //toastMessage = Toast.makeText(getApplicationContext(), "Yep baby !"+response.toString(), Toast.LENGTH_LONG);
                //tvDebug.setText(toString());
                //toastMessage.show();
                final String PREFS_NAME = "session";

                SharedPreferences settings = getSharedPreferences(PREFS_NAME,0);
                SharedPreferences.Editor editor = settings.edit();


                if (response.code()>400) {

                    try {
                        tvDebug.setText(response.errorBody().string());
                        //tvPoints.setText(settings.getString("token",""));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{

                    List<Building> buildingsList = response.body().getBuildings();
                    final Building[] buildingsArray = buildingsList.toArray(new Building[buildingsList.size()]);

                    ArrayAdapter<Building> buildingAdapter =
                            new ArrayAdapter<Building>(BatActivity.this, 0, buildingsArray){
                                @Override
                                public View getView(int position, View convertView, ViewGroup parent){
                                    Building currentBuilding = buildingsArray[position];

                                    if(convertView == null) {
                                        convertView = getLayoutInflater()
                                                .inflate(R.layout.building_layout, null, false);
                                    }

                                    TextView tvBuildingId = (TextView) convertView.findViewById(R.id.textViewBuildingId);
                                    TextView tvBuildingName = (TextView) convertView.findViewById(R.id.textViewBuildingName);
                                    TextView tvBuildingImageUrl = (TextView) convertView.findViewById(R.id.textViewBuildingImageURL);
                                    TextView tvBuildingLevel = (TextView) convertView.findViewById(R.id.textViewBuildingLevel);
                                    TextView tvBuildingBuild = (TextView) convertView.findViewById(R.id.textViewBuildingBuild);
                                    TextView tvBuildingEffect = (TextView) convertView.findViewById(R.id.textViewBuildingEffect);
                                    TextView tvBuildingEffectAmount = (TextView) convertView.findViewById(R.id.textViewBuildingEffectAmount);
                                    TextView tvBuildingGC = (TextView) convertView.findViewById(R.id.textViewBuildingGC);
                                    TextView tvBuildingMC = (TextView) convertView.findViewById(R.id.textViewBuildingMC);
                                    TextView tvBuildingTTB = (TextView) convertView.findViewById(R.id.textViewBuildingTTB);

                                    tvBuildingId.setText(currentBuilding.getBuildingId());
                                    tvBuildingName.setText(currentBuilding.getName());
                                    tvBuildingImageUrl.setText(currentBuilding.getImageURL());
                                    tvBuildingLevel.setText(currentBuilding.getLevel());
                                    tvBuildingBuild.setText(currentBuilding.getBuilding());
                                    tvBuildingEffect.setText(currentBuilding.getEffect());
                                    tvBuildingEffectAmount.setText((Integer.parseInt(currentBuilding.getGetAmountOfEffectLevel0())+ Integer.parseInt(currentBuilding.getGetAmountOfEffectLevel0().toString()) * Integer.parseInt(currentBuilding.getLevel().toString())));
                                    tvBuildingGC.setText((Integer.parseInt(currentBuilding.getGasCostLevel0())+ Integer.parseInt(currentBuilding.getGasCostByLevel().toString()) * Integer.parseInt(currentBuilding.getLevel().toString())));
                                    tvBuildingMC.setText((Integer.parseInt(currentBuilding.getMineralCostLevel0())+ Integer.parseInt(currentBuilding.getMineralCostByLevel().toString()) * Integer.parseInt(currentBuilding.getLevel().toString())));
                                    tvBuildingTTB.setText((Integer.parseInt(currentBuilding.getTimeToBuildLevel0())+ Integer.parseInt(currentBuilding.getTimeToBuildByLevel().toString()) * Integer.parseInt(currentBuilding.getLevel().toString())));

                                    //ICI

                                    return convertView;
                                }
                            };



                    //editor.putString("gas", response.body().getGas());
                    //editor.commit();
                }


            }

            @Override
            public void onFailure(Call<buildingsResponse> call, Throwable t) {
                toastMessage = Toast.makeText(getApplicationContext(), "Une erreur est survenue, impossible de récupérer les informations de l'utilisateur.", Toast.LENGTH_LONG);
                toastMessage.show();
            }
        });



    }

    @Override
    public void onClick(View v){
        Intent intent = new Intent(BatActivity.this, MainActivity.class);
        startActivity(intent);

    }

}
