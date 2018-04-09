package dupre.com.outerspacemanager.outerspacemanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BuildingActivity extends AppCompatActivity implements AdapterView.OnClickListener, AdapterView.OnItemClickListener {

    private ArrayAdapter<Building> buildingAdapter;
    private Button btnMenu;
    private TextView tvDebug;
    private Toast toastMessage;
    private ListView listBuildings;
    private List<Building> buildingsList;
    private TextView tvScore;
    private TextView tvGas;
    private TextView tvMinerals;
    private ArrayList<Button> buildingBtns;
    private ArrayList<Integer> buildingIds;
    private ArrayList<Integer> ttbuildList;
    private Retrofit retrofit;
    private osma_service service;
    private SharedPreferences settings;
    private String token;
    private Float gas;
    private Float minerals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bat);

        btnMenu = (Button) findViewById(R.id.buttonMenuBat);
        btnMenu.setOnClickListener(this);

        tvScore = (TextView) findViewById(R.id.tvBuildingScore);
        tvGas = (TextView) findViewById(R.id.tvBuildingGas);
        tvMinerals = (TextView) findViewById(R.id.tvBuildingMinerals);

        tvDebug = (TextView) findViewById(R.id.textViewBatDebug);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://outer-space-manager.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        String PREFS_NAME = "session";
        settings = getSharedPreferences(PREFS_NAME,0);
        service = retrofit.create(osma_service.class);

        token = settings.getString("token","");
        Call<buildingsResponse> request = service.getBuildings(settings.getString("token",""));
        SharedPreferences.Editor editor = settings.edit();
        gas = Float.parseFloat(settings.getString("gas","0"));
        minerals = Float.parseFloat(settings.getString("minerals","0"));

        tvScore.setText("");
        tvGas.setText("Gas : "+String.valueOf(gas));
        tvMinerals.setText("Minéraux : "+String.valueOf(minerals));


        listBuildings = (ListView) findViewById(R.id.listViewBuildings);
        listBuildings.setOnItemClickListener(this);

        final ArrayList<Integer> tmp_buildingIds = new ArrayList<Integer>();
        final ArrayList<Integer> tmp_buildingTtbuild = new ArrayList<Integer>();
        final ArrayList<Button> tmp_btnList = new ArrayList<Button>();

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

                                    Integer level = Integer.parseInt(currentBuilding.getLevel());
                                    String effect = currentBuilding.getEffect();
                                    Integer amountOfEffectByLevel = Integer.parseInt(currentBuilding.getAmountOfEffectByLevel());
                                    Integer amountOfEffectLevel0 = Integer.parseInt(currentBuilding.getAmountOfEffectLevel0());
                                    Integer buildingId = Integer.parseInt(currentBuilding.getBuildingId());
                                    Boolean building;
                                    if (currentBuilding.getBuilding() == "true")
                                        building = true;
                                    else
                                        building = false;
                                    Integer gasCostByLevel = Integer.parseInt(currentBuilding.getGasCostByLevel());
                                    Integer gasCostLevel0 = Integer.parseInt(currentBuilding.getGasCostLevel0());
                                    String imageUrl = currentBuilding.getImageURL();
                                    Integer mineralCostByLevel = Integer.parseInt(currentBuilding.getMineralCostByLevel());
                                    Integer mineralCostLevel0 = Integer.parseInt(currentBuilding.getMineralCostLevel0());
                                    String name = currentBuilding.getName();
                                    Integer timeToBuildByLevel = Integer.parseInt(currentBuilding.getTimeToBuildByLevel());
                                    Integer timeToBuildLevel0 = Integer.parseInt(currentBuilding.getTimeToBuildLevel0());

                                    Integer effectAmount = amountOfEffectLevel0 + amountOfEffectByLevel * level;
                                    Integer gasCost = gasCostLevel0 + gasCostByLevel * level;
                                    Integer mineralCost = mineralCostLevel0 + mineralCostByLevel * level;
                                    final Integer ttbuild = timeToBuildLevel0 + timeToBuildByLevel * level;


                                    TextView tvBuildingName = (TextView) convertView.findViewById(R.id.textViewBuildingName);
                                    ImageView ivBuilding = (ImageView) convertView.findViewById(R.id.imageViewBuilding);


                                    TextView tvBuildingEffect = (TextView) convertView.findViewById(R.id.textViewBuildingEffect);


                                    tvBuildingName.setText(name + " " +level);
                                    if (imageUrl != "null" && imageUrl != null ){
                                        new DownloadImageTask(ivBuilding).execute(imageUrl);
                                    }
                                    tvBuildingEffect.setText("Effet: "+effect+" "+effectAmount);

                                    Button btnBuilding = (Button) convertView.findViewById(R.id.btnBuilding);

                                    tmp_buildingIds.add( buildingId);

                                    tmp_buildingTtbuild.add(ttbuild);
                                    tmp_btnList.add(btnBuilding);

                                    if (!(building)) {
                                        if (gas >= gasCost && minerals >= mineralCost) {
                                            btnBuilding.setText("Améliorer ? "+gasCost+" gas, "+mineralCost+" minéraux, "+ttbuild+"s");
                                            btnBuilding.setEnabled(true);
                                            btnBuilding.setOnClickListener(BuildingActivity.this);
                                            //buildingIds.add(buildingIds.size(),buildingId);
                                            //ttbuildList.add(buildingIds.size(),ttbuild);
                                            //buildingBtns.add(buildingIds.size(),btnBuilding);

                                        } else {
                                            btnBuilding.setEnabled(false);
                                            String text = "Vous avez besoin de ";
                                            if(gasCost>gas && mineralCost>minerals){
                                                text = text + String.valueOf(gasCost-gas)+" gas et "+String.valueOf(mineralCost-minerals)+" minéraux";
                                            }else if(gasCost>gas){
                                                text = text + String.valueOf(gasCost-gas)+" gas";
                                            }else{
                                                text = text + String.valueOf(mineralCost-minerals)+" minéraux";
                                            }
                                            btnBuilding.setText(text);
                                        }
                                    }else{
                                        btnBuilding.setEnabled(false);
                                        //btnBuilding.setText("Temps restant : " + ttbuild.toString());
                                        btnBuilding.setText("Amélioration en cours");
                                    }

                                    return convertView;
                                }

                                // End building adapter
                            };

                    class DownloadImageTask extends AsyncTask<String,Void,Bitmap>{
                        ImageView imageView;

                        public DownloadImageTask(ImageView imageView){
                            this.imageView = imageView;
                        }

                        protected Bitmap doInBackground(String...urls){
                            String urlOfImage = urls[0];
                            Bitmap logo = null;
                            try{
                                InputStream is = new URL(urlOfImage).openStream();
                                logo = BitmapFactory.decodeStream(is);
                            }catch(Exception e){ // Catch the download exception
                                e.printStackTrace();
                            }
                            return logo;
                        }

                        protected void onPostExecute(Bitmap result){
                            imageView.setImageBitmap(result);
                        }
                    }

                    listBuildings.setAdapter(buildingAdapter);

                    //editor.putString("gas", response.body().getBuildings().toString());
                    //editor.commit();

                }

            }



            @Override
            public void onFailure(Call<buildingsResponse> call, Throwable t) {
                toastMessage = Toast.makeText(getApplicationContext(), "Une erreur est survenue, impossible de récupérer les informations de l'utilisateur.", Toast.LENGTH_LONG);
                toastMessage.show();
            }
        });

        // Passage des tableaux temporaires dans les tableaux de la classe accessibles partout
        buildingBtns = tmp_btnList;
        buildingIds = tmp_buildingIds;
        ttbuildList = tmp_buildingTtbuild;

    }


    @Override
    public void onClick(View v){
        if (v == btnMenu){
            Intent intent = new Intent(BuildingActivity.this, MainActivity.class);
            startActivity(intent);
        }else if(buildingBtns.contains(v)){
            final Integer buildingIndex = buildingBtns.indexOf(v);
            Integer batimentId = buildingIds.get(buildingIndex);

            Call<SimpleResponse> request = service.createBuilding( settings.getString("token","0") , batimentId.toString() );

            request.enqueue(new Callback<SimpleResponse>() {
                @Override
                public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                    if (response.code() == 200) {
                        buildingBtns.get(buildingIndex).setEnabled(false);
                        buildingBtns.get(buildingIndex).setText("Amélioration lancée ! Temps restant : " + ttbuildList.get(buildingIndex) + "s");
                    }

                }
                @Override
                public void onFailure(Call<SimpleResponse> call, Throwable t) {
                    toastMessage = Toast.makeText(getApplicationContext(), "Une erreur est survenue, impossible de créer le bâtiment.", Toast.LENGTH_LONG);
                    toastMessage.show();
                }

            });

            //Button btnBuilding = (Button) v;
            //Integer ttbuild = ttbuildList.get(buildingIndex);
            //btnBuilding.setText("Remaining time : " + ttbuild.toString());

        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        final Building bat = buildingsList.get(position);

        //Retrofit retrofit= new Retrofit.Builder().baseUrl("https://outer-space-manager.herokuapp.com").addConverterFactory(GsonConverterFactory.create()).build();
        //osma_service service = retrofit.create(osma_service.class);



        Call<SimpleResponse> request = service.createBuilding(token,bat.getBuildingId().toString());
        request.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {

                buildingBtns.get(position).setEnabled(false);
                buildingBtns.get(position).setText("Construction lancée !Temps restant : "+ttbuildList.get(position)+"s");
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                toastMessage = Toast.makeText(getApplicationContext(), "Une erreur est survenue, impossible de créer le bâtiment.", Toast.LENGTH_LONG);
                toastMessage.show();
            }
        });

    }
}
