package Exporter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.sparql.resultset.ResultsFormat;

import utilities.Query_Execute;

/**
 * implements the call for the exporter
 */
public class ExporterBase {
	private String inputPath = "./Exporter/src/main/resources/exporter_in.xml";
	private static final String xslPath = "./Exporter/src/main/resources/rdfToGraphML.xsl";

	/**
	 * constructor to create an instance
	 */
	public ExporterBase() {
	}

	/**
	 * querys all triple from the defined graph and makes an transformation to the specified output format
	 * 
	 * @param dsLocation Sparql Endpoint Location
	 * @param outForm format in which the output file should be transformed (for later developments)
	 * @param expLocation path to output file
	 * @param graph graph which should be transformed
	 * @param frame main frame gui
	 */
	public void doExport(String dsLocation, String outForm, String expLocation, String graph, JFrame frame) {

		String outputPath = expLocation;

		if (graph.equals("All")) {
			JOptionPane.showMessageDialog(frame, "Bitte expliziten Graph auswählen.");
			return;
		}

		try {
			String queryString = "SELECT ?s ?p ?o WHERE { GRAPH " + graph + " {?s ?p ?o} }";
			ResultSet result = Query_Execute.executeQuery(dsLocation, queryString, frame);
			FileOutputStream outStream = new FileOutputStream(new File(inputPath));

			PrintWriter p = new PrintWriter(outStream);
			p.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n");
			p.close();

			outStream = new FileOutputStream(inputPath, true);
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
}
