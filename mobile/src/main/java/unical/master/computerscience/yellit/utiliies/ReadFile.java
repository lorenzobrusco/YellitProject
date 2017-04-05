package unical.master.computerscience.yellit.utiliies;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by Francesco on 04/04/2017.
 */

public class ReadFile {

    private static ReadFile mReadFile;
    private static final String EMPTY = "empty";

    private ReadFile() {

    }

    public static ReadFile getInstance() {
        if (mReadFile == null) {
            mReadFile = new ReadFile();
        }

        return mReadFile;
    }

    public String readXMLFile(final Context context, String name) {
        String filename = name + ".xml";
        try {
            FileInputStream fis = context.openFileInput(filename);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (FileNotFoundException e) {
            return EMPTY;
        } catch (UnsupportedEncodingException e) {
            return EMPTY;
        } catch (IOException e) {
            return EMPTY;
        }
    }

}
