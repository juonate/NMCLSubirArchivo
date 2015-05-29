/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.gmdnet.webmed.fg.action;

import de.gmdnet.webmed.fg.action.tools.MultipartUtility;
import de.gmdnet.webmed.fg.FgComponent;
import de.gmdnet.webmed.internal.LoggerNames;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.apache.batik.dom.util.HashTable;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author Juan
 */
public class ActionSubirArchivo extends FgActionAdapter {

    private static Logger logger = LoggerNames.FORM_LOGGER.getLogger();
    private static final String CLASS_NAME = ActionSubirArchivo.class.getSimpleName();
    private static final Long serialVersionUID = Long.valueOf(-76538273649872413L);
    String Servlet = null;
    String filePath = null;

    public ActionSubirArchivo() {
        registerActionMethod("SubirArchivo", new String[]{"pathFileSource", "Servlet", "pathFileTarget", "dateFileTarget"});
    }

    public void methodSubirArchivo(Hashtable<String, String> inputData, FgComponent requestingElement) throws MalformedURLException, TransformerConfigurationException, TransformerException, ParserConfigurationException, SAXException {

        Servlet = parseParam(inputData, "Servlet");
        filePath = parseParam(inputData, "pathFileSource");
        String pathFile = (String) inputData.get("pathFileTarget");
        String dateFile = (String) inputData.get("dateFileTarget");
        
//        Servlet = "http://localhost:8080/NMCLUploadArchivoServlet/uploadFile";
//        filePath = "C:\\Users\\Juan\\Documents\\Cualquieranomas.pdf";
//        String pathFile = "PATH_FILE";
//        String dateFile = "DATE_FILE";
//
//        try {
//            String charset = "UTF-8";
//            File uploadFile1 = new File(filePath);
//            String requestURL = Servlet;
//            String xmlResponse = null;
//            MultipartUtility multipart = new MultipartUtility(requestURL, charset);
//            multipart.addFilePart("file1", uploadFile1);
//            List<String> response = multipart.finish();
//
//            for (String line : response) {
//                xmlResponse = line;
//            }
//            DocumentBuilderFactory fctr = DocumentBuilderFactory.newInstance();
//            DocumentBuilder bldr = fctr.newDocumentBuilder();
//            InputSource insrc = new InputSource(new StringReader(xmlResponse));
//            Document doc = bldr.parse(insrc);
//            NodeList fechaList = doc.getElementsByTagName("fechaText");
//            NodeList rutaList = doc.getElementsByTagName("rutaArchivo");
//
//            String fechaArchivo = fechaList.item(0).getChildNodes().item(0).getNodeValue();
//            String rutaArchivo = rutaList.item(0).getChildNodes().item(0).getNodeValue();

        SaveImage sv = new SaveImage();
        sv.enviarImagen(Servlet, filePath, pathFile, dateFile);
    }

    public static void imprimir(Object o) {
        System.out.println(o);
    }
//    public static void main(String args[]) throws SAXException, TransformerConfigurationException, TransformerException, ParserConfigurationException, MalformedURLException {
//        ActionSubirArchivo scan = new ActionSubirArchivo();
//        scan.methodSubirArchivo(null, null);
//    }
    
    public class SaveImage {

        static final int BUFFER_SIZE = 16384;

        public void enviarImagen(String Servlet, String filePath, String pathFile, String dateFile) throws MalformedURLException, TransformerConfigurationException, TransformerException, ParserConfigurationException, SAXException {
            String charset = "UTF-8";
            File uploadFile1 = new File(filePath);
            String requestURL = Servlet;
            String xmlResponse = null;

            try {
                MultipartUtility multipart = new MultipartUtility(requestURL, charset);
                multipart.addFilePart("file1", uploadFile1);
                List<String> response = multipart.finish();
                
                
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
                imprimir("SERVER REPLIED:");
                imprimir(fechaArchivo + " " + rutaArchivo);
                
                getForm().setValue(pathFile, rutaArchivo);
                getForm().setValue(dateFile, fechaArchivo);
            } catch (IOException ex) {
                System.err.println(ex);
            }
            
        }
        
        
    }
    
}
