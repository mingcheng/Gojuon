package com.gracecode.android.gojuon.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.gracecode.android.gojuon.R;
import com.gracecode.android.gojuon.ui.fragment.PrefFragment;

public class PrefActivity extends BaseActivity {
    @InjectView(R.id.toolbar)
    Toolbar mToolbarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setTheme(android.R.style.Theme_Material_Light);
        } else {
            setTheme(android.R.style.Theme_Holo_Light);
        }

        setSupportActionBar(mToolbarView);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new PrefFragment())
                .commit();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_toolbar;
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle(getString(R.string.configure));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
