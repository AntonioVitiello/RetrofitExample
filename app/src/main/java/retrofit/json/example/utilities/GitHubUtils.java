package retrofit.json.example.utilities;

public class GitHubUtils {

    // final URL will be like: https://api.github.com/search/repositories?q=retrofit
    final static String GITHUB_BASE_URL = "https://api.github.com/search/";

    public static GitHubService getGitHubService() {
        return RetrofitClient.getClient(GITHUB_BASE_URL).create(GitHubService.class);
    }
}
