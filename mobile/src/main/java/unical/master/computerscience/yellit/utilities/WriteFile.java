package unical.master.computerscience.yellit.utilities;

import android.content.Context;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by Francesco on 04/04/2017.
 */

public class WriteFile {

    private static WriteFile mWriteFile;
    private static final String NTOT = "ntot";
    private static final String TOKEN = "token";
    private static final String FOODANDDRIK = "food_and_drink";
    private static final String FITNESS = "fitness";
    private static final String TRAVEL = "travel";
    private static final String INSIDE = "inside";
    private static final String OUTSIDE = "outside";

    private WriteFile() {

    }

    public static WriteFile getInstance() {
        if (mWriteFile == null) {
            mWriteFile = new WriteFile();
        }
        return mWriteFile;
    }

    private void modifyTag(final Context context, String name, String tag, String value) {
        try {
            name += ".xml";
            final File file = new File(context.getFilesDir(), name);
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = null;
            docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(file);
            doc.getElementsByTagName(tag).item(0).setTextContent(value);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = null;
            transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeToken(final Context context, String name, String value) {
        this.modifyTag(context, name, TOKEN, value);
    }

    public void writenTot(final Context context, String name, String value) {
        this.modifyTag(context, name, NTOT, value);
    }

    public void writeFoodAndDrink(final Context context, String name, String value) {
        this.modifyTag(context, name, FOODANDDRIK, value);
    }

    public void writefitness(final Context context, String name, String value) {
        this.modifyTag(context, name, FITNESS, value);
    }

    public void writeTravel(final Context context, String name, String value) {
        this.modifyTag(context, name, TRAVEL, value);
    }

    public void writeInside(final Context context, String name, String value) {
        this.modifyTag(context, name, INSIDE, value);
    }

    public void writeOutside(final Context context, String name, String value) {
        this.modifyTag(context, name, OUTSIDE, value);
    }

}
