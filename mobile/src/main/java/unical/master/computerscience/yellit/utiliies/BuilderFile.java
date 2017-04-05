package unical.master.computerscience.yellit.utiliies;

import android.content.Context;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by Francesco on 04/04/2017.
 */

public class BuilderFile {

    private static BuilderFile mBuilderFile;
    private static final String ROOT = "info";
    private static final String POST = "post";
    private static final String NTOT = "ntot";
    private static final String TOKEN = "token";
    private static final String FOODANDDRIK = "food_and_drink";
    private static final String FITNESS = "fitness";
    private static final String TRAVEL = "travel";
    private static final String INSIDE = "inside";
    private static final String OUTSIDE = "outside";

    private BuilderFile() {

    }

    public static BuilderFile getInstance() {
        if (mBuilderFile == null) {
            mBuilderFile = new BuilderFile();
        }
        return mBuilderFile;
    }


    private void addVersion(final Context context, String name) {
        name += ".xml";
        final File file = new File(context.getFilesDir(), name);
        final FileOutputStream outputStream;
        final String startXML = "<?xml version = \" 1.0 \" econding = \"utf-8 \"?>";
        try {
            outputStream = context.openFileOutput(name, Context.MODE_PRIVATE);
            outputStream.write(startXML.getBytes());
            outputStream.write("\n".getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeOnXMLFile(final Context context, String name) {
        try {
            name += ".xml";
            final File file = new File(context.getFilesDir(), name);
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = null;
            docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            Element rootElement = doc.createElement(ROOT);
            doc.appendChild(rootElement);

            Element token = doc.createElement(TOKEN);
            token.appendChild(doc.createTextNode("0"));
            rootElement.appendChild(token);

            Element post = doc.createElement(POST);
            rootElement.appendChild(post);

            Element nTot = doc.createElement(NTOT);
            nTot.appendChild(doc.createTextNode("0"));
            post.appendChild(nTot);

            Element fd = doc.createElement(FOODANDDRIK);
            fd.appendChild(doc.createTextNode("0"));
            post.appendChild(fd);

            Element travel = doc.createElement(TRAVEL);
            travel.appendChild(doc.createTextNode("0"));
            post.appendChild(travel);

            Element inside = doc.createElement(INSIDE);
            inside.appendChild(doc.createTextNode("0"));
            post.appendChild(inside);

            Element outide = doc.createElement(OUTSIDE);
            outide.appendChild(doc.createTextNode("0"));
            post.appendChild(outide);

            Element fitness = doc.createElement(FITNESS);
            fitness.appendChild(doc.createTextNode("0"));
            post.appendChild(fitness);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = null;

            transformer = transformerFactory.newTransformer();

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);

            transformer.transform(source, result);

        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();

        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    public void newXMLFile(final Context context, String name) {
        this.addVersion(context, name);
        this.writeOnXMLFile(context, name);
    }


}
