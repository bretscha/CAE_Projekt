
package Importer;

import java.io.File;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class ImporterBase {
    private String xmlPath = "./Importer/src/main/resources/Manifest.aml";
    private String xslPath = "./Importer/src/main/resources/ManifestTransform.xsl";
    private static final String rdfOutputPath = "./Importer/src/main/resources/Manifestout.xml";

    /**
     * ImportBase reads the file in the <b>input</b> and the <b>mapping</b> path as
     * an InputStream {@link #xmlPath} and {@link #xslPath} and gives an RDF file as
     * output on the {@link #rdfOutputPath}
     * 
     * @param input
     *            - the path to the file to transform (leave Blank for Standard
     *            Star-Universe Tripliser example)
     * @param mapping
     *            - the path to the mapping file (leave Blank for Standard
     *            Star-Universe Tripliser example)
     * @param rdfOutputPath
     *            - the path to the directory to save the RDF file to as RDF/XML
     */
    public ImporterBase(String input, String mapping) {
	setImportProperties(input, mapping);
    }

    public void doImport() {
	try {
	    System.out.println("Writing RDF on new File: " + rdfOutputPath);
	    Source xslt = new StreamSource(new File(xslPath));
	    Source text = new StreamSource(new File(xmlPath));
	    TransformerFactory factory = TransformerFactory.newInstance();
	    Transformer transformer = factory.newTransformer(xslt);
	    transformer.transform(text, new StreamResult(new File(rdfOutputPath)));
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
