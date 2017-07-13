package unical.master.computerscience.yellit.graphic.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import org.w3c.dom.Document;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import unical.master.computerscience.yellit.R;
import unical.master.computerscience.yellit.connection.LoginService;
import unical.master.computerscience.yellit.logic.InfoManager;
import unical.master.computerscience.yellit.logic.objects.User;
import unical.master.computerscience.yellit.utilities.BaseURL;
import unical.master.computerscience.yellit.utilities.BuilderFile;
import unical.master.computerscience.yellit.utilities.PrefManager;

import static unical.master.computerscience.yellit.utilities.SystemUI.changeSystemBar;

/**
 * Created by Lorenzo on 18/03/2017.
 */

public class LoadActivity extends AppCompatActivity {

    private static final String ROOT = "info";
    @Bind(R.id.imageView_load_page)
    protected ImageView mLogoImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        changeSystemBar(this, false);
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_load);
        ButterKnife.bind(this);
        initXMLFile();
        PrefManager.getInstace(this);
        InfoManager.getInstance().setColorMode(PrefManager.getInstace(this).isColorMode());
        this.startNewActivity();

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.creeper", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));

            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    private void initXMLFile() {
        File mFile = new File(getFilesDir() + "/" + BaseURL.FILENAME + ".xml");
        if (!mFile.exists()) {
            BuilderFile.getInstance().newXMLFile(this, BaseURL.FILENAME);
            return;
        }
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = null;
            docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            if (doc.getElementById(ROOT) == null)
                BuilderFile.getInstance().newXMLFile(this, BaseURL.FILENAME);
        } catch (ParserConfigurationException e) {

        }
    }

    /**
     * Get the user info whether exists
     */
    private void getUserWhetherExists() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseURL.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        String user = PrefManager.getInstace(LoadActivity.this).getUser();
        final LoginService loginService = retrofit.create(LoginService.class);
        Call<User> call = loginService.getProfile(user.split("#")[0], user.split("#")[1].equals("NULL") ? "" : user.split("#")[1]);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                progressDialog.dismiss();
                User profile = response.body();
                InfoManager.getInstance().setmUser(profile);
                startActivity(new Intent(getApplicationContext(), SafeModeActivity.class));
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                finish();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("Facebook signin", "errore di signin");
                progressDialog.dismiss();
                startActivity(new Intent(getApplicationContext(), LoginSignupActivity.class));
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                finish();
            }
        });
    }

    /**
     * halt this activity and call the new activity
     */
    private void startNewActivity() {
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            public void run() {
                if (PrefManager.getInstace(LoadActivity.this).getUser() != null && !PrefManager.getInstace(LoadActivity.this).getUser().equals("")) {
                    getUserWhetherExists();
                } else {
                    startActivity(new Intent(getApplicationContext(), LoginSignupActivity.class));
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    finish();
                }

            }
        };
        handler.postDelayed(runnable, 1000);
    }
}
