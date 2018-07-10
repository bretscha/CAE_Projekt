package Exporter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.swing.JFrame;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.sparql.resultset.ResultsFormat;

import utilities.Query_Execute;

public class ExporterBase {
	private String inputPath = "./Exporter/src/main/resources/exporter_in.xml";
	private String xslPath = "./Exporter/src/main/resources/rdfToGraphML.xsl";
	private String outputPath = "./Exporter/src/main/resources/exporter_out.xml";
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

	public void doExport(String dsLocation, String type, String expLocation, JFrame frame) {
		
		outputPath = expLocation;

		try {
			String queryString = "SELECT ?s ?p ?o WHERE {?s ?p ?o}"; // TODO abfrage graph hinzufügen
			ResultSet result = Query_Execute.executeQuery(dsLocation, queryString, frame);
			FileOutputStream outStream = new FileOutputStream(new File("./Exporter/src/main/resources/in.xml"));

			PrintWriter p = new PrintWriter(outStream);
			p.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n");
			p.close();

			outStream = new FileOutputStream("./Exporter/src/main/resources/in.xml", true);
			ResultSetFormatter.output(outStream, result, ResultsFormat.FMT_RDF_XML);

		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}

		try {
			System.out.println("Writing XML on new File: " + outputPath);
			Source xslt = new StreamSource(new File(xslPath));
			Source text = new StreamSource(new File(inputPath));
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer(xslt);
			transformer.transform(text, new StreamResult(new File(outputPath)));
			System.out.println("Writing done");
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
