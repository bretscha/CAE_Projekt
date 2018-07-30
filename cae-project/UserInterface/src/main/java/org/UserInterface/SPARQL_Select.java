package org.UserInterface;

/**
 * implements a method that generates a SPARQL Select Query
 */
public class SPARQL_Select {

	private static String queryString;

	/**
	 * @return the generated query as a String
	 */
	public static String generateQuery() {
		String selGraph = GUI.graphBox.getSelectedItem().toString();
		
		queryString = "SELECT ";

		for (int i = 0; i < GUI.subTxtList.size(); i++) {
			String subject = GUI.subTxtList.get(i).getText();
			String predicate = GUI.preTxtList.get(i).getText();
			String object = GUI.objTxtList.get(i).getText();

			if (selGraph.equals("All")) {
				queryString += "?g ";
			}
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
		
		if(selGraph.equals("All")) {
			queryString += "GRAPH ?g { ";
		} else {
			queryString += "GRAPH " + selGraph + " { ";
		}

		for (int i = 0; i < GUI.subTxtList.size(); i++) {
			if (GUI.optChkList.get(i).isSelected())
				queryString += "OPTIONAL ";
			queryString += "{ " + GUI.subTxtList.get(i).getText() + " ";
			queryString += GUI.preTxtList.get(i).getText() + " ";
			queryString += GUI.objTxtList.get(i).getText() + " } . "; // was ist mit dem Punkt???
		}

		queryString += "} } ";

		if (GUI.limCheck.isSelected()) {
			queryString += "LIMIT " + GUI.limTxtField.getText() + " ";
		}

		return queryString;
	}

}
