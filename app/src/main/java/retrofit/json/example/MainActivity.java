package retrofit.json.example;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.util.Log;

import java.util.ArrayList;

import retrofit.json.example.adapters.GitHubItemsAdapter;
import retrofit.json.example.models.GitHubResponse;
import retrofit.json.example.models.GitHubResponseItem;
import retrofit.json.example.utilities.GitHubService;
import retrofit.json.example.utilities.GitHubUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    public GitHubItemsAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private GitHubService mService;
    private Button mSearchButton;
    private EditText mSearchQueryText;
    public final static String LIST_STATE_KEY = "recycler_list_state";
    Parcelable listState;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mService = GitHubUtils.getGitHubService();
        mRecyclerView = (RecyclerView) findViewById(R.id.git_recycler);
        mSearchQueryText = (EditText) findViewById(R.id.search_query);
        mSearchButton = (Button) findViewById(R.id.search_button);

        // click on search button
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mSearchQueryText.getText().length() > 0) {
                    startSearch();
                } else {
                    Toast.makeText(MainActivity.this, getText(R.string.error_query), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // click on GitHub item list
        mAdapter = new GitHubItemsAdapter(this, new ArrayList<GitHubResponseItem>(0), new GitHubItemsAdapter.PostItemListener() {

            @Override
            public void onPostClick(String url) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);

    }

    /**
     * final URL will be something like:
     * https://api.github.com/search/repositories?q=retrofit
     */
    public void startSearch() {
        mService.getItems(mSearchQueryText.getText().toString()).enqueue(new Callback<GitHubResponse>() {
            @Override
            public void onResponse(Call<GitHubResponse> call, Response<GitHubResponse> response) {

                if(response.isSuccessful()) {
                    GitHubResponse body = response.body();
                    mAdapter.updateAnswers(body.getItems());
                    Log.d("MainActivity", "succesfull json load");
                }else {
                    // handle request errors depending on status code...
                    // int statusCode  = response.code();
                }
            }

            @Override
            public void onFailure(Call<GitHubResponse> call, Throwable t) {
               // showErrorMessage();
                Log.d("MainActivity", "error loading json");

            }
        });
    }
    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        // Save list state
        listState = layoutManager.onSaveInstanceState();
        state.putParcelable(LIST_STATE_KEY, listState);
        Log.d(LOG_TAG, "onSaveInstanceState: listState = " + listState);
    }
    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        // Retrieve list state and list/item positions
        if(state != null)
            listState = state.getParcelable(LIST_STATE_KEY);
        Log.d(LOG_TAG, "onRestoreInstanceState: listState = " + listState);
    }

    @Override
    protected void onResume() {
        Log.d(LOG_TAG, "onResume: ");
        super.onResume();
        if (listState != null) {
            layoutManager.onRestoreInstanceState(listState);
        }
    }
}