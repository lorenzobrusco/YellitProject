package unical.master.computerscience.yellit;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.ArrayList;
import butterknife.Bind;
import butterknife.ButterKnife;
import unical.master.computerscience.yellit.graphic.Activities.LoginSignupActivity;
import unical.master.computerscience.yellit.graphic.Activities.PlaceActivity;
import unical.master.computerscience.yellit.graphic.Activities.SettingActivity;
import unical.master.computerscience.yellit.graphic.Fragments.AddPostFragment;
import unical.master.computerscience.yellit.graphic.Fragments.FitnessFragment;
import unical.master.computerscience.yellit.graphic.Fragments.PostFragment;
import unical.master.computerscience.yellit.graphic.Fragments.ProfileFragment;
import unical.master.computerscience.yellit.logic.GoogleApiClient;
import unical.master.computerscience.yellit.logic.InfoManager;
import unical.master.computerscience.yellit.utilities.PermissionCheckUtils;

public class MainActivity extends AppCompatActivity {

    private static final int PLACE_PICKER_REQUEST = 5;
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
    @Bind(R.id.map_buttom_menu)
    LinearLayout mMapLayout;
    @Bind(R.id.logout_buttom_menu)
    LinearLayout mLogoutLayout;
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
        this.setupViews();
        GoogleApiClient.getInstance(this);
        GoogleApiClient.getInstance(this).getPlaceDetection(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mBottomNavigation.setColored(InfoManager.getInstance().isColorMode());
        chooseColor(currentItem);
        if (!hasAllRequiredPermissions()) {
            requestAllRequiredPermissions();
        }
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
        mBottomNavigation.setAccentColor(getResources().getColor(R.color.active_button_bottom_navitagiont));
        mBottomNavigation.setInactiveColor(getResources().getColor(R.color.inactive_button_bottom_navitagiont));
        mBottomNavigation.setBehaviorTranslationEnabled(false);
        mBottomNavigation.setTranslucentNavigationEnabled(true);
        mBottomNavigation.setTitleState(AHBottomNavigation.TitleState.SHOW_WHEN_ACTIVE);
        this.currentItem = 2;
        mBottomNavigation.setCurrentItem(currentItem);
        mBottomNavigation.setColored(InfoManager.getInstance().isColorMode());
        chooseColor(currentItem);
        mBottomNavigation.setNotificationBackgroundColor(ContextCompat.getColor(this, R.color.color_notification_back));

        // Add or remove notification for each item
        mBottomNavigation.setNotification("1", PROFILE_FRAG_BUTTON);

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
                        } else {
                            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                        }
                        break;
                }
                filterImage.setVisibility(currentItem == HOME_FRAG_BUTTON ? View.VISIBLE : View.INVISIBLE);
                mBottomNavigation.setNotification("", currentItem);
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
        this.mMapLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build(MainActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
        this.mLogoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InfoManager.getInstance().destroy();
                startActivity(new Intent(getBaseContext(), LoginSignupActivity.class));
                finish();

            }
        });
    }


    private void chooseColor(int page) {
/*
        if (InfoManager.getInstance().isColorMode()) {
            switch (page) {
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
        } else {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }*/
    }

    private void buildDialogFilter() {

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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                startActivity(new Intent(getBaseContext(), PlaceActivity.class));
            }
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

