package org.UserInterface;

import org.apache.jena.query.ResultSet;

public class SPARQL_Select {

    private static String queryString;
    public static ResultSet result;

    public static String generateQuery() {
	queryString = "SELECT ";

	for (int i = 0; i < GUI.subTxtList.size(); i++) {
	    String subject = GUI.subTxtList.get(i).getText();
	    String predicate = GUI.preTxtList.get(i).getText();
	    String object = GUI.objTxtList.get(i).getText();

	    if (subject.charAt(0) == '?') {
		queryString += subject + " ";
	    }
	    if (predicate.charAt(0) == '?') {
		queryString += predicate + " ";
	    }
	    if (object.charAt(0) == '?') {
		queryString += object + " ";
	    }
	}

	queryString += "WHERE { ";

	for (int i = 0; i < GUI.subTxtList.size(); i++) {
	    if (GUI.optChkList.get(i).isSelected())
		queryString += "OPTIONAL ";
	    queryString += "{ " + GUI.subTxtList.get(i).getText() + " ";
	    queryString += GUI.preTxtList.get(i).getText() + " ";
	    queryString += GUI.objTxtList.get(i).getText() + " . } ";
	}

	queryString += "} ";

	if (GUI.limCheck.isSelected()) {
	    queryString += "LIMIT " + GUI.limTxtField.getText() + " ";
	}
	if (GUI.sortCheck.isSelected()) {
	    queryString += "SORTBY " + GUI.sortBox.getSelectedItem();
	}

	return queryString;
    }

}
