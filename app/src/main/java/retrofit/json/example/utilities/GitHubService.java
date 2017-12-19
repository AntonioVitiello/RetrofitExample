package retrofit.json.example.utilities;

import retrofit.json.example.models.GitHubResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface GitHubService {

    // final URL will be eg: https://api.github.com/search/repositories?q=retrofit
    @GET("repositories")
    Call<GitHubResponse> getItems(@Query("q") String gitHubQuery);

}
