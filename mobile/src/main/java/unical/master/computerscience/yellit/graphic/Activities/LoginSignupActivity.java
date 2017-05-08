package unical.master.computerscience.yellit.graphic.Activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import unical.master.computerscience.yellit.R;
import unical.master.computerscience.yellit.graphic.Fragments.PostFragment;

import static unical.master.computerscience.yellit.utilities.SystemUI.changeSystemBar;

/**
 * Created by Lorenzo on 24/04/2017.
 */

public class LoginSignupActivity extends AppCompatActivity {

    private static final int REQUEST_SIGNUP = 0;

    @Bind(R.id.pager_login_signup)
    protected ViewPager mLoginSignupViewPager;
    private FragmentPagerAdapter mFragmentPagerAdapter;
    private List<Fragment> mPages;
    private List<String> mTitlePages;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        changeSystemBar(this, false);
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login_signup);
        ButterKnife.bind(this);
        this.mPages = new ArrayList<>();
        this.mTitlePages = new ArrayList<>();
        this.addPage(new LoginActivity(), "Login");
        this.addPage(new SignUpActivity(), "Signup");
        this.mFragmentPagerAdapter = new LoginSignupPagerAdapter(getSupportFragmentManager());
        this.mLoginSignupViewPager.setAdapter(this.mFragmentPagerAdapter);
        this.mLoginSignupViewPager.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        // When swiping between pages, select the
                        // corresponding tab.
//                        mPagerTitleStrip.
                    }
                });
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                finish();
            }
        }
    }


    /**
     * @param fragment page to addd
     *                 add new fragment
     */
    public void addPage(final Fragment fragment, final String title) {
        this.mPages.add(fragment);
        this.mTitlePages.add(title);
    }

    /**
     * adapter
     */
    private class LoginSignupPagerAdapter extends FragmentPagerAdapter {

        public LoginSignupPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mPages.get(position);
        }

        @Override
        public int getCount() {
            return mPages.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitlePages.get(position);
        }
    }
}
