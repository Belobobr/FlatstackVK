package com.flatstack.flatstackvk.ui.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;

import com.flatstack.flatstackvk.R;
import com.flatstack.flatstackvk.content.dao.UserDAO;
import com.flatstack.flatstackvk.content.data.UserInfo;
import com.flatstack.flatstackvk.ui.AbstractActivity;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import butterknife.Bind;


public class MainActivity extends AbstractActivity implements VKCallback<VKAccessToken> {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            if (VKSdk.isLoggedIn()) {
                replaceFragment(NewsListFragemnt.newInstance(), false);
            } else {
                String scope = "friends,video,offline,wall";
                VKSdk.login(this, scope);
            }
        }
    }

    public void replaceFragment(final Fragment fragment, boolean addToBackStack) {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        final FragmentTransaction ft = fragmentManager.beginTransaction();
        if (addToBackStack) {
            ft.addToBackStack(fragment.toString());
        }
        ft.replace(R.id.content, fragment);
        ft.commit();
    }

    public void setToolbarTitle(final String title) {
        if (mToolbar != null) {
            mToolbar.setTitle(title);
        }
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    public void inflateMenuToToolbar(@MenuRes int menuResId) {
        mToolbar.getMenu().clear();
        mToolbar.inflateMenu(menuResId);
    }

    public void setMenuItemClickListener(Toolbar.OnMenuItemClickListener onMenuItemClickListener) {
        mToolbar.setOnMenuItemClickListener(onMenuItemClickListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, this)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onResult(VKAccessToken vkAccessToken) {
        replaceFragment(NewsListFragemnt.newInstance(), false);
        UserDAO userDAO = new UserDAO(MainActivity.this);
        userDAO.insert(new UserInfo());
    }

    @Override
    public void onError(VKError vkError) {
        replaceFragment(LoginStumbFragment.newInstance(), false);
    }
}
