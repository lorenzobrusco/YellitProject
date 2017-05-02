package unical.master.computerscience.yellit.graphic.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import unical.master.computerscience.yellit.R;

/**
 * Created by Lorenzo on 02/05/2017.
 */

public class PlaceActivity extends AppCompatActivity {

    @Bind (R.id.toolbar)
    protected Toolbar mToolbar;
    @Bind(R.id.collapsing_toolbar)
    protected CollapsingToolbarLayout mCollapsingToolbarLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        mCollapsingToolbarLayout.setTitle("McDonald's");
        mCollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        mCollapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
    }

 }
