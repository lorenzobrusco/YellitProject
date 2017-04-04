package unical.master.computerscience.yellit.utiliies;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Francesco on 04/04/2017.
 */

public class BuilderFile {

    private static BuilderFile mBuilderFile;

    private BuilderFile() {

    }

    public static BuilderFile getInstance() {
        if (mBuilderFile == null) {
            mBuilderFile = new BuilderFile();
        }
        return mBuilderFile;
    }


    public void newXMLFile(final Context context, String name) {
        name += ".xml";
        final File file = new File(context.getFilesDir(), name);
        final FileOutputStream outputStream;
        final String startXML = "<?xml version = \" 1.0 \" econding = \"utf-8 \"?>";
        try {
            outputStream = context.openFileOutput(name, Context.MODE_APPEND);
            outputStream.write(startXML.getBytes());
            outputStream.write("\n".getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
