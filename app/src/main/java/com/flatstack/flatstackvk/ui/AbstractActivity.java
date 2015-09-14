package com.flatstack.flatstackvk.ui;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;


public class AbstractActivity extends AppCompatActivity {

    public void setContentView(@LayoutRes final int layoutResId) {
        super.setContentView(layoutResId);
        ButterKnife.bind(this);
    }

    public void setContentView(final View view) {
        super.setContentView(view);
        ButterKnife.bind(this);
    }

    @Override
    public void setContentView(final View view, final ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        ButterKnife.bind(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
