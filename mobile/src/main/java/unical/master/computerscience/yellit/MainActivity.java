package unical.master.computerscience.yellit;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import butterknife.Bind;
import butterknife.ButterKnife;
import unical.master.computerscience.yellit.graphic.Fragments.PostFragment;
import unical.master.computerscience.yellit.graphic.dialog.CustomDialogBottomSheet;

public class MainActivity extends AppCompatActivity {

    private static int FITNESS = 0;
    private static int PROFILE = 1;
    private static int HOME = 2;
    private static int ADD = 3;
    private static int MENU = 4;

    @Bind(R.id.content_frame)
    FrameLayout mFrame;
    @Bind(R.id.bottom_navigation_view)
    AHBottomNavigation mBottomNavigation;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        currentFragment = new PostFragment();
        MainActivity.this.setFragment(currentFragment);
        this.setupBottomNavigation(mBottomNavigation);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void setupBottomNavigation(final AHBottomNavigation mBottomNavigation) {
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

        mBottomNavigation.setCurrentItem(2);

        mBottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                switch (position) {
                    case 4:
                        final CustomDialogBottomSheet dialogBottomSheet = CustomDialogBottomSheet.newInstance("Modal Bottom Sheet");
                        dialogBottomSheet.show(getSupportFragmentManager().beginTransaction(),dialogBottomSheet.getTag());
                        break;
                }

                return true;
            }
        });

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
