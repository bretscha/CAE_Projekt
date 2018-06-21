package org.UserInterface;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;

public class Query_Execute {
	
    public static ResultSet executeQuery(String queryString) {

	Query query = QueryFactory.create(queryString);

	QueryEngineHTTP qexec = QueryExecutionFactory.createServiceRequest(GUI.dsLocation, query);

	ResultSet result = qexec.execSelect();

	return result;
    }

}
