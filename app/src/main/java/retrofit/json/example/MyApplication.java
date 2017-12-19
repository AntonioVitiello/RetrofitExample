package retrofit.json.example;

import android.app.Application;

import com.util.Log;

/**
 * Created by antlap on 19/12/2017.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.init(BuildConfig.DEBUG, "antlap");
    }
}
