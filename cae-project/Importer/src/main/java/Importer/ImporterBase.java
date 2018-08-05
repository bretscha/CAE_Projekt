
package Importer;

import java.io.File;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class ImporterBase {
    private String xmlPath = "Importer/src/main/resources/Comos.xml";
    private String xslPath = "Importer/src/main/resources/ComosTransform.xsl";
    private static final String rdfOutputPath = "src/main/resources/ComosOut.xml";

    /**
     * ImportBase reads the file in the <b>input</b> and the <b>mapping</b> path as
     * an InputStream {@link #xmlPath} and {@link #xslPath} and gives an RDF file as
     * output on the {@link #rdfOutputPath}
     * 
     * @param input
     *            - the path to the file to transform
     * @param mapping
     *            - the path to the mapping file
     * @param rdfOutputPath
     *            - the path to the directory to save the RDF file to as RDF/XML
     */
    public ImporterBase(String input, String mapping) {
	setImportProperties(input, mapping);
    }

    /**
     * ImportBase reads the file in the <b>input</b> and the <b>mapping</b> path as
     * an InputStream {@link #xmlPath} and {@link #xslPath} and gives an RDF file as
     * output on the {@link #rdfOutputPath}
     * 
     * @param input
     *            - the path to the file to transform
     * @param mapping
     *            - the path to the mapping file
     * @param rdfOutputPath
     *            - the path to the directory to save the RDF file to as RDF/XML
     */
    public ImporterBase() {
    }

    public void doImport(String impLocation) {
	try {

	    System.out.println("Writing RDF on new File: " + rdfOutputPath);
	    Source xslt = new StreamSource(new File(xslPath));
	    Source text;
	    if (impLocation != null && impLocation != "") {
		text = new StreamSource(new File(impLocation));
	    } else {
		xslt = new StreamSource(new File(xslPath));
		text = new StreamSource(new File(xmlPath));
	    }
	    xslt = new StreamSource(new File(xslPath));
	    text = new StreamSource(new File(xmlPath));
	    TransformerFactory factory = TransformerFactory.newInstance();
	    Transformer transformer = factory.newTransformer(xslt);
	    File file = new File(rdfOutputPath);
	    if (!file.exists()) {
		file.createNewFile();
	    }
	    Result out = new StreamResult(file);
	    transformer.transform(text, out);
	    transformer.transform(text, new StreamResult(file));
	    System.out.println("Writing done");
	} catch (Exception e) {
	    System.out.println(e.getStackTrace());
	}
    }

    public void setImportProperties(String inputXmlPath, String xsltPath) {
	this.xmlPath = inputXmlPath;
	this.xslPath = xsltPath;
    }

    public String getInputXmlPath() {
	return xmlPath;
    }

    public String getXslPath() {
	return xslPath;
    }

    /**
     * Gets the relative Path to the into RDF Transformed XML.
     * 
     * @return - The path ./Importer/src/main/resources/{fileName}.xml
     */
    public static String getRdfOutputPath() {
	return rdfOutputPath;
    }

    public void setInputXmlPath(String inputXml) {
	this.xmlPath = inputXml;
    }

    public void setXslPath(String mappingXml) {
	this.xslPath = mappingXml;
    }

}
