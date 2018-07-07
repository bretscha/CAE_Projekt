package Exporter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.jena.query.ResultSet;

import utilities.Query_Execute;

public class ExporterBase {
	private String inputPath = "./Exporter/src/main/resources/in.xml";
	private String xslPath = "./Exporter/src/main/resources/test.xsl";
	private String outputPath = "./Exporter/src/main/resources/out.xml";
	// private static final String graphMLPath =
	// "./Exporter/src/main/resources/Manifestout.xml";

	/**
	 * ImportBase reads the file in the <b>Output</b> and the <b>mapping</b> path as
	 * an OutputStream {@link #outputPath} and {@link #xslPath} and gives an RDF
	 * file as output on the {@link #rdfOutputPath}
	 * 
	 * @param Output
	 *            - the path to the file to transform (leave Blank for Standard
	 *            Star-Universe Tripliser example)
	 * @param mapping
	 *            - the path to the mapping file (leave Blank for Standard
	 *            Star-Universe Tripliser example)
	 * @param rdfOutputPath
	 *            - the path to the directory to save the RDF file to as RDF/XML
	 */
	public ExporterBase(String outputPath, String mapping) {
		setOutputPath(outputPath);
		setXslPath(mapping);
	}

	public void doExport(String dsLocation, String type, String expLocation) {
		/*
		 * try { System.out.println("Writing XML on new File: " + outputPath); Source
		 * xslt = new StreamSource(new File(xslPath)); Source text = new
		 * StreamSource(new File(inputPath)); TransformerFactory factory =
		 * TransformerFactory.newInstance(); Transformer transformer =
		 * factory.newTransformer(xslt); transformer.transform(text, new
		 * StreamResult(new File(outputPath))); System.out.println("Writing done"); }
		 * catch (Exception e) { System.out.println(e.getStackTrace()); }
		 */

		try {
			String queryString = "SELECT ?s ?p ?o WHERE {?s ?p ?o}";
			ResultSet result = Query_Execute.executeQuery(dsLocation + "query", queryString);
			OutputStream outStream = new FileOutputStream(new File("./Exporter/src/main/resource/in.xml"));

		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
	}

	public void setExportProperties(String outputPath, String xsltPath) {
		setOutputPath(outputPath);
		setXslPath(xsltPath);
	}

	public String getInputPath() {
		return outputPath;
	}

	public String getOutputPath() {
		return outputPath;
	}

	public String getXslPath() {
		return xslPath;
	}

	public void setInputPath(String inputPath) {
		this.inputPath = inputPath;
	}

	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}

	public void setXslPath(String mappingXml) {
		this.xslPath = mappingXml;
	}

}