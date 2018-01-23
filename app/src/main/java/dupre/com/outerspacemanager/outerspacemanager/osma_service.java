package dupre.com.outerspacemanager.outerspacemanager;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by sdupre on 16/01/2018.
 */

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

    //@POST("api/") String stringConnexion(@Header("Authorization") String authorization, @Path("user") String user, @Query("sort") String sort, @Body User user);
    ///api/vXXX/auth/login
}
/*

@POST("users/{user}/repos") Call<List<Repo>> listRepos(@Header("Authorization") String authorization,
 @Path("user") String user, @Query("sort") String sort, @Body User user);
}*/