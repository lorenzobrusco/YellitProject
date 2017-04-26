package unical.master.computerscience.yellit.graphic.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Handler;

import org.w3c.dom.Document;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import unical.master.computerscience.yellit.R;
import unical.master.computerscience.yellit.utilities.BaseURL;
import unical.master.computerscience.yellit.utilities.BuilderFile;

/**
 * Created by Lorenzo on 18/03/2017.
 */

public class LoadActivity extends AppCompatActivity {

    private static final String ROOT = "info";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_load);
        initXMLFile();
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            public void run() {
                /**
                 * load the first 10 posts and also others stuff
                 */
                startActivity(new Intent(getApplicationContext(), LoginSignupActivity.class));
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                finish();
            }
        };
        handler.postDelayed(runnable, 1000);
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
            if(doc.getElementById(ROOT) == null)
                BuilderFile.getInstance().newXMLFile(this, BaseURL.FILENAME);
        }catch (ParserConfigurationException e){

        }
    }
}
