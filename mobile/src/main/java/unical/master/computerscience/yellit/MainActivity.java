package unical.master.computerscience.yellit;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import butterknife.Bind;
import butterknife.ButterKnife;
import unical.master.computerscience.yellit.graphic.Activities.SettingActivity;
import unical.master.computerscience.yellit.graphic.Fragments.PostFragment;
import unical.master.computerscience.yellit.graphic.Fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity {

    private int currentItem;

    @Bind(R.id.bottom_navigation_view)
    AHBottomNavigation mBottomNavigation;
    @Bind(R.id.bottom_sheet1)
    View bottomSheet;
    @Bind(R.id.setting_buttom_menu)
    LinearLayout mSettingLayout;
    @Bind(R.id.custom_search_view)SearchView mSearchView;
    private Fragment currentFragment;
    private BottomSheetBehavior mBottomSheetBehavior;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSearchView.onActionViewExpanded();
        mSearchView.setIconified(true);
        mSearchView.setQueryHint("Query Hint");

        currentFragment = new PostFragment();
        MainActivity.this.setFragment(currentFragment);
        this.setupViews();

    }

    @TargetApi(Build.VERSION_CODES.M)
    private void setupViews() {
        AHBottomNavigationItem itemFitness = new AHBottomNavigationItem(R.string.tab_fitness, R.drawable.ic_fitness_center_black_24, R.color.color_bottom_navigation);
        AHBottomNavigationItem itemProfile = new AHBottomNavigationItem(R.string.tab_profile, R.drawable.ic_person_black_24, R.color.color_bottom_navigation);
        AHBottomNavigationItem itemHome = new AHBottomNavigationItem(R.string.tab_home, R.drawable.ic_home_black_24dp, R.color.color_bottom_navigation);
        AHBottomNavigationItem itemAdd = new AHBottomNavigationItem(R.string.tab_add_post, R.drawable.ic_public_black_24, R.color.color_bottom_navigation);
        AHBottomNavigationItem itemSomeThing = new AHBottomNavigationItem(R.string.tab_something, R.drawable.ic_menu_black_24
                , R.color.color_bottom_navigation);
        mBottomNavigation.addItem(itemFitness);
        mBottomNavigation.addItem(itemProfile);
        mBottomNavigation.addItem(itemHome);
        mBottomNavigation.addItem(itemAdd);
        mBottomNavigation.addItem(itemSomeThing);
        mBottomNavigation.setColoredModeColors(getResources().getColor(R.color.active_button_bottom_navitagiont, getTheme()), getResources().getColor(R.color.inactive_button_bottom_navitagiont, getTheme()));
        mBottomNavigation.setForceTint(true);
        mBottomNavigation.setTranslucentNavigationEnabled(true);
        mBottomNavigation.setTitleState(AHBottomNavigation.TitleState.SHOW_WHEN_ACTIVE);
        this.currentItem = 2;
        mBottomNavigation.setCurrentItem(currentItem);

        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setHideable(true);
        mBottomSheetBehavior.setPeekHeight(300);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        mBottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                switch (position) {
                    case 0:
                        if (mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN) {
                            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                        }
                        currentItem = 0;
                        break;
                    case 1:
                        if (mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN) {
                            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                        }
                        currentItem = 1;
                        removeFragment(currentFragment);
                        currentFragment = new ProfileFragment();
                        setFragment(currentFragment);
                        break;
                    case 2:
                        if (mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN) {
                            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                        }
                        currentItem = 2;
                        removeFragment(currentFragment);
                        currentFragment = new PostFragment();
                        setFragment(currentFragment);
                        break;
                    case 3:
                        if (mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN) {
                            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                        }
                        currentItem = 3;
                        break;
                    case 4:
                        if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
                            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        } else {
                            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                        }
                        break;
                }

                return true;
            }
        });

        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {

                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {

                } else if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    if (mBottomNavigation.getCurrentItem() == 4)
                        mBottomNavigation.setCurrentItem(currentItem);
                }
            }

            @Override
            public void onSlide(View bottomSheet, float slideOffset) {
            }
        });
        this.mSettingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        } else if (currentItem == 2) {
            super.onBackPressed();
        } else {
            currentItem = 2;
            mBottomNavigation.setCurrentItem(currentItem);
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
    }

    protected void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.commit();
    }

    protected void removeFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.remove(fragment);
        fragmentTransaction.commit();
    }
}
