package unical.master.computerscience.yellit.graphic.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Handler;

import unical.master.computerscience.yellit.R;

/**
 * Created by Lorenzo on 18/03/2017.
 */

public class LoadActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_load);
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            public void run() {
                /**
                 * load the first 10 posts and also others stuff
                 */
                startActivity(new Intent(LoadActivity.this, LoginActivity.class));
                finish();
            }
        };
        handler.postDelayed(runnable, 1000);
    }

}
