package unical.master.computerscience.yellit;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import unical.master.computerscience.yellit.graphic.Activities.SettingActivity;
import unical.master.computerscience.yellit.graphic.Fragments.AddPostFragment;
import unical.master.computerscience.yellit.graphic.Fragments.FitnessFragment;
import unical.master.computerscience.yellit.graphic.Fragments.PostFragment;
import unical.master.computerscience.yellit.graphic.Fragments.ProfileFragment;
import unical.master.computerscience.yellit.logic.GoogleApiClient;
import unical.master.computerscience.yellit.utiliies.BaseURL;
import unical.master.computerscience.yellit.utiliies.BuilderFile;
import unical.master.computerscience.yellit.utiliies.PermissionCheckUtils;
import unical.master.computerscience.yellit.utiliies.ReadFile;

public class MainActivity extends AppCompatActivity {

    private static final int FITNESS_FRAG_BUTTON = 0;
    private static final int PROFILE_FRAG_BUTTON = 1;
    private static final int HOME_FRAG_BUTTON = 2;
    private static final int ADDPOST_FRAG_BUTTON = 3;
    private static final int OTHER_FRAG_BUTTON = 4;
    private int currentItem = 2;
    private static final int REQUEST_ALL_MISSING_PERMISSIONS = 1;


    @Bind(R.id.bottom_navigation_view)
    AHBottomNavigation mBottomNavigation;
    @Bind(R.id.bottom_sheet1)
    View bottomSheet;
    @Bind(R.id.setting_buttom_menu)
    LinearLayout mSettingLayout;
    @Bind(R.id.custom_search_view)
    SearchView mSearchView;
    @Bind(R.id.post_filter)
    ImageView filterImage;
    private Fragment currentFragment;
    private BottomSheetBehavior mBottomSheetBehavior;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        mSearchView.setFocusable(false);
        mSearchView.onActionViewExpanded();
        mSearchView.clearFocus();
        mSearchView.requestFocusFromTouch();
        mSearchView.setQueryHint(" Search ");
        currentFragment = new PostFragment();
        MainActivity.this.setFragment(currentFragment);
        chooseColor(currentItem);
        GoogleApiClient.getInstance(this).onConnected(savedInstanceState);
        this.setupViews();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GoogleApiClient.getInstance(this).disconnect();
    }

    private void setupViews() {
        AHBottomNavigationItem itemFitness = new AHBottomNavigationItem(R.string.tab_fitness, R.drawable.ic_fitness_center_black_24, R.color.page1);
        AHBottomNavigationItem itemProfile = new AHBottomNavigationItem(R.string.tab_profile, R.drawable.ic_person_black_24, R.color.page2);
        AHBottomNavigationItem itemHome = new AHBottomNavigationItem(R.string.tab_home, R.drawable.ic_home_black_24dp, R.color.page3);
        AHBottomNavigationItem itemAdd = new AHBottomNavigationItem(R.string.tab_add_post, R.drawable.ic_public_black_24, R.color.page4);
        AHBottomNavigationItem itemSomeThing = new AHBottomNavigationItem(R.string.tab_something, R.drawable.ic_menu_black_24, R.color.page5);
        mBottomNavigation.addItem(itemFitness);
        mBottomNavigation.addItem(itemProfile);
        mBottomNavigation.addItem(itemHome);
        mBottomNavigation.addItem(itemAdd);
        mBottomNavigation.addItem(itemSomeThing);
        mBottomNavigation.setColored(true);
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
                    case FITNESS_FRAG_BUTTON:
                        if (mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN) {
                            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                        }
                        if (currentItem != position) {
                            currentItem = position;
                            chooseColor(currentItem);
                            removeFragment(currentFragment);
                            currentFragment = new FitnessFragment();
                            setFragment(currentFragment);
                        }
                        break;
                    case PROFILE_FRAG_BUTTON:
                        if (mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN) {
                            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                        }

                        if (currentItem != position) {
                            getWindow().setStatusBarColor(getResources().getColor(R.color.page2));
                            currentItem = position;
                            chooseColor(currentItem);
                            removeFragment(currentFragment);
                            currentFragment = new ProfileFragment();
                            setFragment(currentFragment);
                        }

                        break;
                    case HOME_FRAG_BUTTON:
                        if (mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN) {
                            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                        }

                        if (currentItem != position) {
                            getWindow().setStatusBarColor(getResources().getColor(R.color.page3));
                            currentItem = position;
                            chooseColor(currentItem);
                            removeFragment(currentFragment);
                            currentFragment = new PostFragment();
                            setFragment(currentFragment);
                        }
                        break;
                    case ADDPOST_FRAG_BUTTON:
                        if (mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN) {
                            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                        }

                        if (currentItem != position) {
                            getWindow().setStatusBarColor(getResources().getColor(R.color.page4));
                            currentItem = position;
                            chooseColor(currentItem);
                            removeFragment(currentFragment);
                            currentFragment = new AddPostFragment();
                            setFragment(currentFragment);
                        }
                        break;
                    case OTHER_FRAG_BUTTON:
                        if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
                            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                            getWindow().setStatusBarColor(getResources().getColor(R.color.page5));
                        } else {
                            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                        }
                        break;
                }
                filterImage.setVisibility(currentItem == HOME_FRAG_BUTTON ? View.VISIBLE : View.INVISIBLE);
                return true;
            }
        });


        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {

                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {

                } else if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    if (mBottomNavigation.getCurrentItem() == 4) {
                        mBottomNavigation.setCurrentItem(currentItem);
                        chooseColor(currentItem);
                    }
                }
            }

            @Override
            public void onSlide(View bottomSheet, float slideOffset) {
            }
        });

        this.mSettingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SettingActivity.class));
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });
        this.filterImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buildDialogFilter();
            }
        });
    }


    private void chooseColor(int page){


        switch (page){
            case FITNESS_FRAG_BUTTON:
                getWindow().setStatusBarColor(getResources().getColor(R.color.page1));
                break;
            case PROFILE_FRAG_BUTTON:
                getWindow().setStatusBarColor(getResources().getColor(R.color.page2));
                break;
            case HOME_FRAG_BUTTON:
                getWindow().setStatusBarColor(getResources().getColor(R.color.page3));
                break;
            case ADDPOST_FRAG_BUTTON:
                getWindow().setStatusBarColor(getResources().getColor(R.color.page4));
                break;
            case OTHER_FRAG_BUTTON:
                getWindow().setStatusBarColor(getResources().getColor(R.color.page5));
                break;
        }

    }

    private void buildDialogFilter(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_post_filter);
        dialog.setTitle("Choose Filter");
        //TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        //text.setText("Choose Filter");

        Button dialogButton = (Button) dialog.findViewById(R.id.delete);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!hasAllRequiredPermissions()) {
            requestAllRequiredPermissions();
        }
    }

    @Override
    public void onBackPressed() {

        if (mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        } else if (currentItem == 2) {
            super.onBackPressed();
        } else {
            currentItem = 2;
            chooseColor(currentItem);
            removeFragment(currentFragment);
            currentFragment = new PostFragment();
            setFragment(currentFragment);
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

    /*****************************************************************************************/
    /**                                       Permits                                       **/
    /*****************************************************************************************/

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ALL_MISSING_PERMISSIONS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onAllRequiredPermissionsGranted();
                } else {
                    Toast.makeText(getApplicationContext(), "Please allow all permissions", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            }
        }
    }


    @SuppressLint("NewApi")
    private void requestAllRequiredPermissions() {
        ArrayList<String> notGrantedPermissions = new ArrayList<>();

        for (String permission : getRequiredPermissions()) {
            if (!PermissionCheckUtils.hasPermission(getApplicationContext(), permission)) {
                notGrantedPermissions.add(permission);
            }
        }

        if (notGrantedPermissions.size() > 0) {
            requestPermissions(notGrantedPermissions.toArray(new String[notGrantedPermissions.size()]),
                    REQUEST_ALL_MISSING_PERMISSIONS);
        }
    }

    /**
     * @return All permission which this activity needs
     */
    protected String[] getRequiredPermissions() {
        return new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.BODY_SENSORS
        };
    }

    /**
     * Called when all need permissions granted
     */
    protected void onAllRequiredPermissionsGranted() {
        Toast.makeText(getApplicationContext(), "all permissions are allow", Toast.LENGTH_SHORT).show();
    }

    private boolean hasAllRequiredPermissions() {
        for (String permission : getRequiredPermissions()) {
            if (!PermissionCheckUtils.hasPermission(getApplicationContext(), permission)) {
                return false;
            }
        }
        return true;
    }
}

