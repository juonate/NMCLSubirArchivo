/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.gmdnet.webmed.fg.action.tools;

import static de.gmdnet.webmed.fg.action.ActionSubirArchivo.imprimir;
import de.gmdnet.webmed.fg.action.FgActionAdapter;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author Juan
 */
public class SaveImage extends FgActionAdapter {

    static final int BUFFER_SIZE = 16384;
    String pathFile = null;
    String dateFile = null;

    public SaveImage(String pathFile, String dateFile) {
        this.pathFile = pathFile;
        this.dateFile = dateFile;

    }

    public void enviarImagen(String Servlet, String filePath) throws MalformedURLException, TransformerConfigurationException, TransformerException, ParserConfigurationException, SAXException {
        String charset = "UTF-8";
        File uploadFile1 = new File(filePath);
        String requestURL = Servlet;
        String xmlResponse = null;

        try {
            MultipartUtility multipart = new MultipartUtility(requestURL, charset);
            multipart.addFilePart("file1", uploadFile1);
            List<String> response = multipart.finish();

            imprimir("SERVER REPLIED:");
            for (String line : response) {
                xmlResponse = line;
            }
            DocumentBuilderFactory fctr = DocumentBuilderFactory.newInstance();
            DocumentBuilder bldr = fctr.newDocumentBuilder();
            InputSource insrc = new InputSource(new StringReader(xmlResponse));
            Document doc = bldr.parse(insrc);

            NodeList fechaList = doc.getElementsByTagName("fechaText");
            NodeList rutaList = doc.getElementsByTagName("rutaArchivo");

            String fechaArchivo = fechaList.item(0).getChildNodes().item(0).getNodeValue();
            String rutaArchivo = rutaList.item(0).getChildNodes().item(0).getNodeValue();

            getForm().setValue(pathFile, rutaArchivo);
            getForm().setValue(dateFile, fechaArchivo);

            imprimir(fechaArchivo + " " + rutaArchivo);

        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
}
