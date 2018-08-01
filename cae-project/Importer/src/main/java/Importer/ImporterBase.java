
package Importer;

import java.io.File;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * implements the class for the importer
 */
public class ImporterBase {
	private static final String xslPath = "./Importer/src/main/resources/ComosTransform.xsl";
	private static final String rdfOutputPath = "./Importer/src/main/resources/importer_out.xml";

	/**
	 * ImportBase reads the file in the <b>input</b> and the <b>mapping</b> path as
	 * an InputStream {@link #xmlPath} and {@link #xslPath} and gives an RDF file as
	 * output on the {@link #rdfOutputPath}
	 */
	public ImporterBase() {
	}

	/**
	 * reads the specified file and makes a transformation to a triple structure
	 * @param xmlPath path to the comos exported file which should be transformed
	 */
	public void doImport(String xmlPath) {
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

	/**
	 * Gets the relative Path to the into RDF Transformed XML.
	 * 
	 * @return the path
	 */
	public static String getRdfOutputPath() {
		return rdfOutputPath;
	}
}
