package unical.master.computerscience.yellit;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import unical.master.computerscience.yellit.graphic.Fragments.FitnessFragment;
import unical.master.computerscience.yellit.graphic.Fragments.PostFragment;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.content_frame)
    FrameLayout mFrame;
    private Fragment currentFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    MainActivity.this.removeFragment(currentFragment);
                    currentFragment = new PostFragment();
                    MainActivity.this.setFragment(currentFragment);
                    return true;
                case R.id.navigation_dashboard:
                    MainActivity.this.removeFragment(currentFragment);
                    currentFragment = new FitnessFragment();
                    MainActivity.this.setFragment(currentFragment);
                    return true;
                case R.id.navigation_notifications:
                    MainActivity.this.removeFragment(currentFragment);
                    currentFragment = new PostFragment();
                    MainActivity.this.setFragment(currentFragment);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        currentFragment = new PostFragment();
        MainActivity.this.setFragment(currentFragment);
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
