package unical.master.computerscience.yellit.graphic.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import unical.master.computerscience.yellit.R;

/**
 * Created by Lorenzo on 24/04/2017.
 */

public class LoginSignupActivity extends AppCompatActivity {


    @Bind(R.id.pager_login_signup)
    ViewPager mLoginSignupViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);
        ButterKnife.bind(this);

    }
}
