package utilities;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFactory;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;

/**
 * implements a utility to execute SPARQL queries as String
 */
public class Query_Execute {

	/**
	 * executes a SPARQL query
	 * 
	 * @param dsLocation
	 *            location to the SPARQL endpoint
	 * @param queryString
	 *            query commands as String
	 * @param frame mian frame gui
	 * @return the query result as a ResultSet
	 */
	public static ResultSet executeQuery(String dsLocation, String queryString, JFrame frame) {

		dsLocation += "query";

		Query query;
		QueryEngineHTTP qexec = null;
		ResultSet result = null;

		try {
			query = QueryFactory.create(queryString);
			qexec = QueryExecutionFactory.createServiceRequest(dsLocation, query);
			qexec.setTimeout(10000, 10000); // 10 sek
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame,
					"Fehler beim Erstellen des Query/Update Strings! /n Eingaben in den Textfeldern überprüfen und ToolTips beachten.");
			result = ResultSetFactory.makeResults(ModelFactory.createDefaultModel());
		}

		try {
			result = qexec.execSelect();
			result = ResultSetFactory.copyResults(result);
			qexec.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame,
					"Fehler beim Ausführen des Befehls! /n Bitte Verbindung zum Server überprüfen.");
			result = ResultSetFactory.makeResults(ModelFactory.createDefaultModel());
		}

		return result;
	}

}