package utilities;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFactory;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;

/**
 * implements a utility to execute SPARQL queries as String
 */
public class Query_Execute {

	/** 
	 * executes a SPARQL query
	 * @param dsLocation location to the SPARQL endpoint
	 * @param queryString query commands as String
	 * @return the query result as a ResultSet
	 */
	public static ResultSet executeQuery(String dsLocation, String queryString) {
		
		dsLocation += "query";

		Query query = QueryFactory.create(queryString);

		QueryEngineHTTP qexec = QueryExecutionFactory.createServiceRequest(dsLocation, query);
		qexec.setTimeout(10000, 10000);

		ResultSet result = qexec.execSelect();
		result = ResultSetFactory.copyResults(result);
		
		qexec.close();

		return result;
	}

}