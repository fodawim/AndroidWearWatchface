package com.jeremysteckling.androidwearexamplewatch_test;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.clockwork.watchfaces.IHomeBackgroundService;
import com.google.android.clockwork.watchfaces.WatchFaceIntents;
import com.google.android.clockwork.watchfaces.WatchFaceStyle;

public class TestActivity extends BaseActivity {

    private final BroadcastReceiver mActionReceiver = new BroadcastReceiver() {
        public void onReceive(Context paramContext, Intent paramIntent) {

            Log.d("TestActivity", "BroadcastReceiver.onReceive();");

            if (paramIntent.hasExtra("card_location"))
                TestActivity.this.onCardPeek(Rect.unflattenFromString(paramIntent.getStringExtra("card_location")));
            if (paramIntent.hasExtra("card_progress_enabled"))
                TestActivity.this.onCardProgressEnabled(paramIntent.getBooleanExtra("card_progress_enabled", false));
            if (paramIntent.hasExtra("card_progress"))
                TestActivity.this.onCardProgressUpdate(paramIntent.getFloatExtra("card_progress", 0F));
            if (paramIntent.hasExtra("ambient_mode"))
                TestActivity.this.onAmbientModeChanged(paramIntent.getBooleanExtra("ambient_mode", false));
        }
    };
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName paramComponentName, IBinder paramIBinder) {

            Log.d("TestActivity", "ServiceConnection.onServiceConnected();");

            IHomeBackgroundService localIHomeBackgroundService = IHomeBackgroundService.Stub.asInterface(paramIBinder);
            try {
                localIHomeBackgroundService.setStyle(TestActivity.this.buildStyle());
                return;
            } catch (RemoteException localRemoteException) {
                Log.e("WatchFace", "couldn't set style", localRemoteException);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    public void onReadyForContent() {
        //This is where the heavy lifting happens for the Android Wear Watchfaces
        Log.d("PhotosWatchFace", "onReadyForContent");

        setContentView(R.layout.activity_test);

        if (!(bindService(WatchFaceIntents.BIND_HOME_INTENT, this.mServiceConnection, Context.BIND_AUTO_CREATE)))
        {
            Log.e("WatchFace", "unable to connect to home background service, watch face will look weird");
            unbindService(this.mServiceConnection);
        }

        IntentFilter localIntentFilter = new IntentFilter("com.google.android.clockwork.home.action.BACKGROUND_ACTION");
        registerReceiver(this.mActionReceiver, localIntentFilter);
    }

    @Override
    public void onDestroy()
    {
        Log.d("TestActivity", "onDestroy();");
        unbindService(this.mServiceConnection);
        unregisterReceiver(this.mActionReceiver);
        onDestroy();
    }

    protected WatchFaceStyle buildStyle() {

        Log.d("TestActivity", "buildStyle();");

        WatchFaceStyle.Builder localBuilder = WatchFaceStyle.Builder.forActivity(this).setCardPeekMode(0).setPeekOpacityMode(1).setCardProgressMode(0).setBackgroundVisibility(0).setShowSystemUiTime(true).setViewProtection(3);

        if (super.isRound())
            localBuilder.setHotwordIndicatorGravity(48).setStatusBarGravity(48);
        else
            localBuilder.setHotwordIndicatorGravity(51).setStatusBarGravity(53);

        return localBuilder.build();

    }

    protected void onAmbientModeChanged(boolean paramBoolean) {
        Log.d("TestActivity", "onAmbientModeChanged();");
        /*
         *  Ambient Mode is when the user places their palm over the screen? (GUESS)
         */
    }

    protected void onCardPeek(Rect paramRect) {
        Log.d("WatchFace", "card peeking at " + paramRect);
    }

    protected void onCardProgressEnabled(boolean paramBoolean) {
        Log.d("WatchFace", "card progress enabled set to " + paramBoolean);
    }

    protected void onCardProgressUpdate(float paramFloat) {
        Log.d("WatchFace", "card progress set to " + paramFloat);
    }

    @Override
    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        Log.d("WatchFace", "onCreate();");
    }
}
