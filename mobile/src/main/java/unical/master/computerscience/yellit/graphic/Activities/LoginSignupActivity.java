package unical.master.computerscience.yellit.graphic.Activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import unical.master.computerscience.yellit.R;

import static unical.master.computerscience.yellit.utilities.SystemUI.changeSystemBar;

/**
 * Activity that contains login fragment and sign in fragment
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
        if(getSupportActionBar() != null)
            getSupportActionBar().hide();
        this.setContentView(R.layout.activity_login_signup);
        ButterKnife.bind(this);
        this.mPages = new ArrayList<>();
        this.mTitlePages = new ArrayList<>();
        this.addPage(new LoginFragment(), "Login");
        this.addPage(new SignUpFragment(), "Signup");
        this.mFragmentPagerAdapter = new LoginSignupPagerAdapter(getSupportFragmentManager());
        this.mLoginSignupViewPager.setAdapter(this.mFragmentPagerAdapter);
        try {
            final PackageInfo info = getPackageManager().getPackageInfo(
                    "unical.master.computerscience.yellit", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                final MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            Toast.makeText(this,"There was some error during creation of facebook keys",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                finish();
            }
        }
    }


    /**
     * It add new fragment
     * @param fragment page to add
     * @param title the fragment title
     */
    public void addPage(final Fragment fragment, final String title) {
        this.mPages.add(fragment);
        this.mTitlePages.add(title);
    }

    /**
     * Adapter of page slider
     */
    private class LoginSignupPagerAdapter extends FragmentPagerAdapter {

        private LoginSignupPagerAdapter(FragmentManager fm) {
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
