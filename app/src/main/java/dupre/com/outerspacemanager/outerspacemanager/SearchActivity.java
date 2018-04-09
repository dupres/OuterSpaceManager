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

public class SearchActivity extends AppCompatActivity implements AdapterView.OnClickListener, AdapterView.OnItemClickListener {

    private ArrayAdapter<Search> searchAdapter;
    private Button btnMenu;
    private TextView tvDebug;
    private Toast toastMessage;
    private ListView listSearches;
    private List<Search> searchesList;
    private TextView tvScore;
    private TextView tvGas;
    private TextView tvMinerals;
    private ArrayList<Button> searchBtns;
    private ArrayList<Integer> searchIds;
    private ArrayList<Integer> ttbuildList;
    private Retrofit retrofit;
    private osma_service service;
    private SharedPreferences settings;
    private String token;
    private Float gas;
    private Float minerals;

    private TextView tvDebug2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rech);

        btnMenu = (Button) findViewById(R.id.buttonMenuRech);
        btnMenu.setOnClickListener(this);

        tvScore = (TextView) findViewById(R.id.tvSearchScore);
        tvGas = (TextView) findViewById(R.id.tvSearchGas);
        tvMinerals = (TextView) findViewById(R.id.tvSearchMinerals);

        //tvDebug = (TextView) findViewById(R.id.textViewBatDebug);
        tvDebug2 = (TextView) findViewById(R.id.textViewDebug2) ;

        retrofit = new Retrofit.Builder()
                .baseUrl("https://outer-space-manager.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        String PREFS_NAME = "session";
        settings = getSharedPreferences(PREFS_NAME,0);
        service = retrofit.create(osma_service.class);

        token = settings.getString("token","");
        Call<SearchesResponse> request = service.getSearches(settings.getString("token","0"));
        SharedPreferences.Editor editor = settings.edit();
        gas = Float.parseFloat(settings.getString("gas","0"));
        minerals = Float.parseFloat(settings.getString("minerals","0"));

        tvScore.setText("");
        tvGas.setText("Gas : "+String.valueOf(gas));
        tvMinerals.setText("Min :"+String.valueOf(minerals));


        listSearches = (ListView) findViewById(R.id.listViewSearches);
        listSearches.setOnItemClickListener(this);

        final ArrayList<Integer> tmp_searchIds = new ArrayList<Integer>();
        final ArrayList<Integer> tmp_searchTtbuild = new ArrayList<Integer>();
        final ArrayList<Button> tmp_btnList = new ArrayList<Button>();

        request.enqueue(new Callback<SearchesResponse>(){
            @Override
            public void onResponse(Call<SearchesResponse> call, Response<SearchesResponse> response) {

                if (response.code()>400) {

                    try {
                        tvDebug2.setText(response.errorBody().string());
                        //tvPoints.setText(settings.getString("token",""));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{

                    tvDebug2.setText(response.raw().body().toString());

                    List<Search> searchesList = response.body().getSearches();
                    final Search[] searchesArray = searchesList.toArray(new Search[searchesList.size()]);

                    searchAdapter =
                            new ArrayAdapter<Search>(getApplicationContext(), R.layout.search_layout, searchesArray){
                                @Override
                                public View getView(int position, View convertView, ViewGroup parent){
                                    Search currentSearch = searchesArray[position];

                                    LayoutInflater li = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    convertView = li.inflate(R.layout.search_layout, parent, false);

                                    Integer level = Integer.parseInt(currentSearch.getLevel());
                                    String effect = currentSearch.getEffect();
                                    Integer amountOfEffectByLevel = Integer.parseInt(currentSearch.getAmountOfEffectByLevel());
                                    Integer amountOfEffectLevel0 = Integer.parseInt(currentSearch.getAmountOfEffectLevel0());
                                    Integer searchId = Integer.parseInt(currentSearch.getSearchId());
                                    Boolean search;
                                    if (currentSearch.getSearching() == "true")
                                        search = true;
                                    else
                                        search = false;
                                    Integer gasCostByLevel = Integer.parseInt(currentSearch.getGasCostByLevel());
                                    Integer gasCostLevel0 = Integer.parseInt(currentSearch.getGasCostLevel0());

                                    Integer mineralCostByLevel = Integer.parseInt(currentSearch.getMineralCostByLevel());
                                    Integer mineralCostLevel0 = Integer.parseInt(currentSearch.getMineralCostLevel0());
                                    String name = currentSearch.getName();
                                    Integer timeToBuildByLevel = Integer.parseInt(currentSearch.getTimeToBuildByLevel());
                                    Integer timeToBuildLevel0 = Integer.parseInt(currentSearch.getTimeToBuildLevel0());

                                    Integer effectAmount = amountOfEffectLevel0 + amountOfEffectByLevel * level;
                                    Integer gasCost = gasCostLevel0 + gasCostByLevel * level;
                                    Integer mineralCost = mineralCostLevel0 + mineralCostByLevel * level;
                                    final Integer ttbuild = timeToBuildLevel0 + timeToBuildByLevel * level;


                                    TextView tvSearchName = (TextView) convertView.findViewById(R.id.textViewSearchName);


                                    TextView tvSearchEffect = (TextView) convertView.findViewById(R.id.textViewSearchEffect);


                                    tvSearchName.setText(name + " " +level);
                                    tvSearchEffect.setText("Effet: "+effect+" "+effectAmount);

                                    Button btnSearch = (Button) convertView.findViewById(R.id.btnSearch);

                                    searchIds.add(searchId);

                                    tmp_searchTtbuild.add(ttbuild);
                                    tmp_btnList.add(btnSearch);

                                    if (!(search)) {
                                        if (gas >= gasCost && minerals >= mineralCost) {
                                            btnSearch.setText("Rechercher ? "+gasCost+" gas, "+mineralCost+" minéraux, "+ttbuild+"s");
                                            btnSearch.setEnabled(true);
                                            btnSearch.setOnClickListener(SearchActivity.this);
                                            //searchIds.add(searchIds.size(),searchId);
                                            //ttbuildList.add(searchIds.size(),ttbuild);
                                            //searchBtns.add(searchIds.size(),btnSearch);

                                        } else {
                                            btnSearch.setEnabled(false);
                                            String text = "Vous avez besoin de ";
                                            if(gasCost>gas && mineralCost>minerals){
                                                text = text + String.valueOf(gasCost-gas)+" gas et "+String.valueOf(mineralCost-minerals)+" minéraux";
                                            }else if(gasCost>gas){
                                                text = text + String.valueOf(gasCost-gas)+" gas";
                                            }else{
                                                text = text + String.valueOf(mineralCost-minerals)+" minéraux";
                                            }
                                            btnSearch.setText(text);
                                        }
                                    }else{
                                        btnSearch.setEnabled(false);
                                        //btnSearch.setText("Temps restant : " + ttbuild.toString());
                                        btnSearch.setText("Amélioration en cours");
                                    }

                                    return convertView;
                                }

                                // End search adapter
                            };

                    /*class DownloadImageTask extends AsyncTask<String,Void,Bitmap>{
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
                    }*/

                    listSearches.setAdapter(searchAdapter);

                    //editor.putString("gas", response.body().getSearches().toString());
                    //editor.commit();

                }

            }



            @Override
            public void onFailure(Call<SearchesResponse> call, Throwable t) {
                toastMessage = Toast.makeText(getApplicationContext(), "Une erreur est survenue, impossible de récupérer les informations de l'utilisateur.", Toast.LENGTH_LONG);
                toastMessage.show();
            }
        });

        // Passage des tableaux temporaires dans les tableaux de la classe accessibles partout
        searchBtns = tmp_btnList;
        searchIds = tmp_searchIds;
        ttbuildList = tmp_searchTtbuild;

    }


    @Override
    public void onClick(View v){
        if (v == btnMenu){
            Intent intent = new Intent(SearchActivity.this, MainActivity.class);
            startActivity(intent);
        }else if(searchBtns.contains(v)){

            final Integer searchIndex = searchBtns.indexOf(v);
            Integer rechId = searchIds.get(searchIndex);

            Call<SimpleResponse> request = service.startSearch( settings.getString("token","0") , rechId.toString() );

            request.enqueue(new Callback<SimpleResponse>() {
                @Override
                public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                    if (response.code() == 200) {
                        searchBtns.get(searchIndex).setEnabled(false);
                        searchBtns.get(searchIndex).setText("Amélioration lancée ! Temps restant : " + ttbuildList.get(searchIndex) + "s");
                    }

                }
                @Override
                public void onFailure(Call<SimpleResponse> call, Throwable t) {
                    toastMessage = Toast.makeText(getApplicationContext(), "Une erreur est survenue, impossible de créer le bâtiment.", Toast.LENGTH_LONG);
                    toastMessage.show();
                }

            });

            //Button btnSearch = (Button) v;
            //Integer ttbuild = ttbuildList.get(searchIndex);
            //btnSearch.setText("Remaining time : " + ttbuild.toString());

        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        final Search bat = searchesList.get(position);

        //Retrofit retrofit= new Retrofit.Builder().baseUrl("https://outer-space-manager.herokuapp.com").addConverterFactory(GsonConverterFactory.create()).build();
        //osma_service service = retrofit.create(osma_service.class);



        Call<SimpleResponse> request = service.startSearch(token,bat.getSearchId().toString());
        request.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {

                searchBtns.get(position).setEnabled(false);
                searchBtns.get(position).setText("Construction lancée !Temps restant : "+ttbuildList.get(position)+"s");
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                toastMessage = Toast.makeText(getApplicationContext(), "Une erreur est survenue, impossible de créer le bâtiment.", Toast.LENGTH_LONG);
                toastMessage.show();
            }
        });

    }
}
