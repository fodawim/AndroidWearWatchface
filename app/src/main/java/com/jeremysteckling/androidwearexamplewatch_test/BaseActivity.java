package com.jeremysteckling.androidwearexamplewatch_test;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.FrameLayout;

/**
 * Created by jeremy on 6/28/14.
 *
 *
 * This is the BaseActivity to base Watch face activities off of.
 *
 * YOU MUST IMPLEMENT onReadyForContent();
 *
 */
public abstract class BaseActivity extends Activity {

    private InsetView mInsetView;

    private void checkInsetsApplied()
    {
        Log.d("BaseActivity", "checkInsetsApplied();");

        if (!(mInsetView.isReady()))
            throw new IllegalStateException("BaseActivity not ready yet.");
    }

    public boolean isRound()
    {
        Log.d("BaseActivity", "isRound();");
        checkInsetsApplied();
        return this.mInsetView.isRound();
    }

    @Override
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        Log.d("BaseActivity", "onCreate();");

        ViewGroup localViewGroup = (ViewGroup)super.getWindow().getDecorView();
        this.mInsetView = new InsetView(this.mInsetView, this);
        localViewGroup.setOnApplyWindowInsetsListener(this.mInsetView);
        localViewGroup.setFitsSystemWindows(true);
        localViewGroup.requestApplyInsets();
    }

    public abstract void onReadyForContent();

    public void setContentView(int paramInt)
    {
        Log.d("BaseActivity", "setContentView(int);");
        checkInsetsApplied();
        super.getLayoutInflater().inflate(getResources().getLayout(paramInt), this.mInsetView, true);
        super.setContentView(this.mInsetView);
    }

    public void setContentView(View paramView)
    {
        Log.d("BaseActivity", "setContentView(view);");
        checkInsetsApplied();
        this.mInsetView.addView(paramView);
        super.setContentView(this.mInsetView);
    }

    public void setContentView(View paramView, ViewGroup.LayoutParams paramLayoutParams)
    {
        Log.d("BaseActivity", "setContentView(view, viewgroup);");
        checkInsetsApplied();
        this.mInsetView.addView(paramView, paramLayoutParams);
        super.setContentView(this.mInsetView);
    }




    private class InsetView extends FrameLayout implements View.OnApplyWindowInsetsListener
    {
        private Rect mInsets;
        private boolean mInsetsApplied;
        private boolean mIsRound;

        InsetView(View paramView, Context paramContext)
        {
            super(paramContext);
            Log.d("BaseActivity.InsetView", "init");
        }

        private boolean isReady()
        {
            return this.mInsetsApplied;
        }

        private boolean isRound()
        {
            return this.mIsRound;
        }

        public WindowInsets onApplyWindowInsets(View paramView, WindowInsets paramWindowInsets)
        {
            Log.d("BaseActivity.InsetView", "onApplyWindowInsets();");
            if (!(this.mInsetsApplied))
            {
                if (paramWindowInsets.isRound())
                {
                    this.mIsRound = true;
                    this.mInsets = new Rect(paramView.getLeft(), paramView.getTop(), paramView.getRight(), paramView.getBottom());
                }
                this.mInsetsApplied = true;
                BaseActivity.this.onReadyForContent();
            }
            return paramWindowInsets.consumeSystemWindowInsets();
        }
    }

}
