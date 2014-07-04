package com.jeremysteckling.androidwearexamplewatch_test;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by jeremy on 7/4/14.
 */
public class ExampleWatchface extends Activity {

    ImageView background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v("WatchFace", "onCreate();");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        background = (ImageView) findViewById(R.id.background);

    }

    @Override
    protected void onPause() {
        //  onPause();
        Log.v("WatchFace", "onPause();");
        super.onPause();
        background.setImageResource(R.drawable.example_watch_background);
    }

    @Override
    protected void onResume() {
        //  onResume();
        Log.v("WatchFace", "onResume();");
        super.onResume();
        background.setImageResource(R.drawable.example_watch_background);
    }
}
