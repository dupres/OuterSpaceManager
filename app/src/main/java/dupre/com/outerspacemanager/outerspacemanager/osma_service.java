package dupre.com.outerspacemanager.outerspacemanager;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface osma_service {

    String baseUrl = "https://outer-space-manager.herokuapp.com";

    @POST(baseUrl + "/api/v1/auth/login")
    Call<AuthResponse> Connection(@Body User user);

    @POST(baseUrl + "/api/v1/auth/create")
    Call<AuthResponse> Creation(@Body User user);

    @GET(baseUrl + "/api/v1/users/get")
    Call<CUResponse> getCurrentUser(@Header("x-access-token") String token);

    @GET(baseUrl + "/api/v1/buildings/list")
    Call<buildingsResponse> getBuildings(@Header("x-access-token") String token);

    @POST(baseUrl + "/api/v1/buildings/create/{buildingId}")
    Call<SimpleResponse> createBuilding(@Header("x-access-token") String token,@Path("buildingId") String buildingId);

    @GET(baseUrl + "/api/v1/searches/list")
    Call<SearchesResponse> getSearches(@Header("x-access-token") String token);

    @POST(baseUrl + "/api/v1/searches/create/{searchId}")
    Call<SimpleResponse> startSearch(@Header("x-access-token") String token,@Path("searchId") String searchId);

    //@POST("api/") String stringConnexion(@Header("Authorization") String authorization, @Path("user") String user, @Query("sort") String sort, @Body User user);
    ///api/vXXX/auth/login
}
/*

@POST("users/{user}/repos") Call<List<Repo>> listRepos(@Header("Authorization") String authorization,
 @Path("user") String user, @Query("sort") String sort, @Body User user);
}*/