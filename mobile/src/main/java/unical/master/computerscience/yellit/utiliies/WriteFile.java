package unical.master.computerscience.yellit.utiliies;

import android.content.Context;

import org.w3c.dom.Attr;
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

public class WriteFile {

    private static WriteFile mWriteFile;
    private static final String ROOT = "info";
    private static final String POST = "post";
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

    public void writeOnXMLFile(final Context context, String name) {
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

            // firstname elements
            Element fd = doc.createElement(FOODANDDRIK);
            fd.appendChild(doc.createTextNode("0"));
            post.appendChild(fd);

            // firstname elements
            Element travel = doc.createElement(TRAVEL);
            travel.appendChild(doc.createTextNode("0"));
            post.appendChild(travel);


            // firstname elements
            Element inside = doc.createElement(INSIDE);
            inside.appendChild(doc.createTextNode("0"));
            post.appendChild(inside);

            // firstname elements
            Element outide = doc.createElement(OUTSIDE);
            outide.appendChild(doc.createTextNode("0"));
            post.appendChild(outide);

            // firstname elements
            Element fitness = doc.createElement(FITNESS);
            fitness.appendChild(doc.createTextNode("0"));
            post.appendChild(fitness);

            // lastname elements


            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = null;

            transformer = transformerFactory.newTransformer();

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);

            // Output to console for testing
            // StreamResult result = new StreamResult(System.out);

            transformer.transform(source, result);

            System.out.println("File saved!");
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();

        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }


}
