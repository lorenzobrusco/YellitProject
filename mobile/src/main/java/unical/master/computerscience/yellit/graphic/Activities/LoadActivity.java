package unical.master.computerscience.yellit.graphic.Activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import org.w3c.dom.Document;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import butterknife.Bind;
import butterknife.ButterKnife;
import unical.master.computerscience.yellit.R;
import unical.master.computerscience.yellit.logic.InfoManager;
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
        Animation zoomin = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        Animation zoomout = AnimationUtils.loadAnimation(this, R.anim.zoom_out);
        mLogoImageView.startAnimation(zoomin);
        mLogoImageView.startAnimation(zoomout);
        initXMLFile();
        PrefManager.getInstace(this);
        InfoManager.getInstance().setColorMode(PrefManager.getInstace(this).isColorMode());
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            public void run() {
                /**
                 * load the first 10 posts and also others stuff
                 */
                startActivity(new Intent(getApplicationContext(), SafeModeActivity.class));
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                finish();
            }
        };
        handler.postDelayed(runnable, 2000);
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
}
