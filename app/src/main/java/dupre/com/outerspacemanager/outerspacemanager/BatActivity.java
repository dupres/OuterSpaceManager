package dupre.com.outerspacemanager.outerspacemanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BatActivity extends AppCompatActivity implements AdapterView.OnClickListener, AdapterView.OnItemClickListener {

    private ArrayAdapter<Building> buildingAdapter;
    private Button btnMenu;
    private TextView tvDebug;
    private Toast toastMessage;
    private ListView listBuildings;
    private TextView tvGas;
    private TextView tvMinerals;
    private List<Button> buildingBtns;
    private List<Integer> buildingIds;
    private List<Integer> ttbuildList;
    private osma_service service;
    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bat);

        btnMenu = (Button) findViewById(R.id.buttonMenuBat);
        btnMenu.setOnClickListener(this);

        tvGas = (TextView) findViewById(R.id.tvGas);
        tvMinerals = (TextView) findViewById(R.id.tvMinerals);

        tvDebug = (TextView) findViewById(R.id.textViewBatDebug);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://outer-space-manager.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        String PREFS_NAME = "session";
        settings = getSharedPreferences(PREFS_NAME,0);
        service = retrofit.create(osma_service.class);
        Call<buildingsResponse> request = service.getBuildings(settings.getString("token",""));
        SharedPreferences.Editor editor = settings.edit();
        final Float gas = Float.parseFloat(settings.getString("gas","0"));
        final Float minerals = Float.parseFloat(settings.getString("minerals","0"));

        tvGas.setText(String.valueOf(gas));
        tvMinerals.setText(String.valueOf(minerals));

        request.enqueue(new Callback<buildingsResponse>(){
            @Override
            public void onResponse(Call<buildingsResponse> call, Response<buildingsResponse> response) {


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

                    buildingAdapter =
                            new ArrayAdapter<Building>(getApplicationContext(), R.layout.building_layout, buildingsArray){
                                @Override
                                public View getView(int position, View convertView, ViewGroup parent){
                                    Building currentBuilding = buildingsArray[position];

                                    LayoutInflater li = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    convertView = li.inflate(R.layout.building_layout, parent, false);

                                    //convertView = getLayoutInflater()
                                    //        .inflate(R.layout.building_layout, parent, false);

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

                                    Integer level = Integer.parseInt(currentBuilding.getLevel());
                                    String effect = currentBuilding.getEffect();
                                    Integer amountOfEffectByLevel = Integer.parseInt(currentBuilding.getAmountOfEffectByLevel());
                                    Integer amountOfEffectLevel0 = Integer.parseInt(currentBuilding.getAmountOfEffectLevel0());
                                    Integer buildingId = Integer.parseInt(currentBuilding.getBuildingId());
                                    Boolean building = Boolean.getBoolean(currentBuilding.getBuilding());
                                    Integer gasCostByLevel = Integer.parseInt(currentBuilding.getGasCostByLevel());
                                    Integer gasCostLevel0 = Integer.parseInt(currentBuilding.getGasCostLevel0());
                                    String imageUrl = currentBuilding.getImageURL();
                                    Integer mineralCostByLevel = Integer.parseInt(currentBuilding.getMineralCostByLevel());
                                    Integer mineralCostLevel0 = Integer.parseInt(currentBuilding.getMineralCostLevel0());
                                    String name = currentBuilding.getName();
                                    Integer timeToBuildByLevel = Integer.parseInt(currentBuilding.getTimeToBuildByLevel());
                                    Integer timeToBuildLevel0 = Integer.parseInt(currentBuilding.getTimeToBuildLevel0());

                                    tvBuildingId.setText("Id : "+String.valueOf(buildingId));
                                    tvBuildingName.setText(name);
                                    tvBuildingImageUrl.setText("Image : "+imageUrl);
                                    tvBuildingLevel.setText("Level : "+String.valueOf(level));
                                    tvBuildingBuild.setText("Building : "+building.toString());
                                    tvBuildingEffect.setText(effect);

                                    Integer effectAmount = amountOfEffectLevel0 + amountOfEffectByLevel * level;
                                    Integer gasCost = gasCostLevel0 + gasCostByLevel * level;
                                    Integer mineralCost = mineralCostLevel0 + mineralCostByLevel * level;
                                    final Integer ttbuild = timeToBuildLevel0 + timeToBuildByLevel * level;

                                    tvBuildingEffectAmount.setText("Effect amount : "+String.valueOf(effectAmount));
                                    tvBuildingGC.setText("Gas cost : "+String.valueOf(String.valueOf(gasCost)));
                                    tvBuildingMC.setText("Minerals cost : "+String.valueOf(mineralCost));
                                    tvBuildingTTB.setText("Time to build : "+String.valueOf(ttbuild));

                                    Button btnBuilding = (Button) convertView.findViewById(R.id.btnBuilding);


                                    if (!(building)) {
                                        if (gas >= gasCost && minerals >= mineralCost) {
                                            btnBuilding.setOnClickListener(BatActivity.this);
                                            buildingIds.add(buildingIds.size(),buildingId);
                                            ttbuildList.add(buildingIds.size(),ttbuild);
                                            buildingBtns.add(buildingIds.size(),btnBuilding);

                                        } else {
                                            btnBuilding.setEnabled(false);
                                            String text = "Missing ";
                                            if(gasCost>gas && mineralCost>minerals){
                                                text = text + String.valueOf(gasCost-gas)+" gas and "+String.valueOf(mineralCost-minerals)+" minerals";
                                            }else if(gasCost>gas){
                                                text = text + String.valueOf(gasCost-gas)+" gas";
                                            }else{
                                                text = text + String.valueOf(mineralCost-minerals)+" minerals";
                                            }
                                            btnBuilding.setText(text);
                                        }
                                    }else{
                                        btnBuilding.setEnabled(false);
                                        btnBuilding.setText("Remaining time : " + ttbuild.toString());
                                    }



                                    return convertView;
                                }
                            };



                    //editor.putString("gas", response.body().getBuildings().toString());
                    //editor.commit();
                    listBuildings = (ListView) findViewById(R.id.listViewBuildings);
                    listBuildings.setAdapter(buildingAdapter);

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
        if (v == btnMenu){
            Intent intent = new Intent(BatActivity.this, MainActivity.class);
            startActivity(intent);
        }else if(buildingBtns.contains(v)){
            Integer buildingIndex = buildingBtns.indexOf(v);
            Integer batimentId = buildingIds.get(buildingIndex);

            Call<SimpleResponse> request = service.createBuilding( settings.getString("token","0") , batimentId.toString() );

            request.enqueue(new Callback<SimpleResponse>() {
                @Override
                public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {


                }
                @Override
                public void onFailure(Call<SimpleResponse> call, Throwable t) {
                    toastMessage = Toast.makeText(getApplicationContext(), "Une erreur est survenue, impossible de créer le bâtiment.", Toast.LENGTH_LONG);
                    toastMessage.show();
                }

            });

            Button btnBuilding = (Button) v;
            Integer ttbuild = ttbuildList.get(buildingIndex);
            btnBuilding.setText("Remaining time : " + ttbuild.toString());

        }

    }


    @Override
    public void onItemClick(ArrayAdapter<?> parent, View v,int pos,long id){

    }








}
