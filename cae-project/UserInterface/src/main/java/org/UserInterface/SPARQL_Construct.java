package org.UserInterface;

import org.apache.jena.query.ResultSet;

public class SPARQL_Construct {

	private static String queryString;
	public static ResultSet result;

	public static String generateQuery() {
		queryString = "CONSTRUCT { ";
		queryString += GUI.newSubTxtField.getText() + " ";
		queryString += GUI.newPreTxtField.getText() + " ";
		queryString += GUI.newObjTxtField.getText() + " } ";

		queryString += "WHERE { ";

		for (int i = 0; i < GUI.subTxtList.size(); i++) {
			if (GUI.optChkList.get(i).isSelected())
				queryString += "OPTIONAL ";
			queryString += "{ " + GUI.subTxtList.get(i).getText() + " ";
			queryString += GUI.preTxtList.get(i).getText() + " ";
			queryString += GUI.objTxtList.get(i).getText() + " . } ";
		}

		queryString += "} ";
		return queryString;
	}

}
