package com.igniva.staggeredanimated.ui.activity;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by jitender-android on 20/6/17.
 */

public abstract class BaseActivity extends AppCompatActivity{
    protected abstract void setUpLayout();
    protected abstract void setUpToolbar();
    protected abstract void setDataInViewObjects();
    protected abstract void setAnalytics();
}
