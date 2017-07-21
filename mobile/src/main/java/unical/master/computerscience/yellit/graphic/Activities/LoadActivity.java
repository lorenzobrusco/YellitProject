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
import java.util.ArrayList;
import java.util.List;

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
import unical.master.computerscience.yellit.connection.PostGestureService;
import unical.master.computerscience.yellit.connection.UsersService;
import unical.master.computerscience.yellit.graphic.Adapters.PostAdapter;
import unical.master.computerscience.yellit.graphic.Fragments.PostFragment;
import unical.master.computerscience.yellit.logic.GoogleApiClient;
import unical.master.computerscience.yellit.logic.InfoManager;
import unical.master.computerscience.yellit.logic.objects.Post;
import unical.master.computerscience.yellit.logic.objects.User;
import unical.master.computerscience.yellit.utilities.BaseURL;
import unical.master.computerscience.yellit.utilities.BuilderFile;
import unical.master.computerscience.yellit.utilities.PrefManager;
import unical.master.computerscience.yellit.utilities.UpdateGoogleInfo;
import unical.master.computerscience.yellit.utilities.UpdatePosts;

import static unical.master.computerscience.yellit.utilities.SystemUI.changeSystemBar;

/**
 * The first activity, used to load many stuffs before to start.
 */
public class LoadActivity extends AppCompatActivity {

    private static final String ROOT = "infoYellit";
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
    }


    /**
     * Initialize the internal xml where it saved all user's information
     */
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
            Toast.makeText(this, "Error from xml", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Get the user info whether exists
     */
    private void getUserWhetherExists() {
        this.getUsers();
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
                User profile = response.body();
                InfoManager.getInstance().setmUser(profile);
                startActivity(new Intent(getApplicationContext(), SafeModeActivity.class));
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                finish();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("Facebook signin", "errore di signin");
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
        UpdatePosts.getAllPost(getBaseContext());
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            public void run() {
                if (PrefManager.getInstace(LoadActivity.this).getUser() == null || PrefManager.getInstace(LoadActivity.this).getUser().equals("")) {
                    startActivity(new Intent(getApplicationContext(), LoginSignupActivity.class));
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    finish();
                } else {
                    getUserWhetherExists();
                }

            }
        };
        handler.postDelayed(runnable, 2000);
    }

    private void getUsers() {
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseURL.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final UsersService loginService = retrofit.create(UsersService.class);
        Call<User[]> call = loginService.getAllUsers("allUsers");
        call.enqueue(new Callback<User[]>() {
            @Override
            public void onResponse(Call<User[]> call, Response<User[]> response) {
                User[] users = response.body();
                List<User> usersList = new ArrayList<User>();
                for (User user : users) {
                    usersList.add(user);
                }
                InfoManager.getInstance().setmAllUsers(usersList);
            }

            @Override
            public void onFailure(Call<User[]> call, Throwable t) {
            }
        });
    }


}
