package org.UserInterface;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.RDFNode;

import Exporter.ExporterBase;
import Importer.ImporterBase;
import utilities.Query_Execute;
import utilities.Update_Execute;

/**
 * implements the main graphical user interface
 */
public class GUI {

    /**
     * main frame gui
     */
    protected static JFrame frame = new JFrame("ToolBox");
    protected static GlassPane glassPane = new GlassPane();
    /**
     * Pane in which all scollPanes of Tab.class instances will be placed
     */
    protected static JTabbedPane tabbedPane = new JTabbedPane();
    protected static JPanel mainPanel = new JPanel();
    protected static JPanel configPanel = new JPanel();
    protected static JPanel importPanel = new JPanel();
    protected static JPanel exportPanel = new JPanel();
    protected static JPanel graphPanel = new JPanel();
    protected static JPanel queryPanel = new JPanel();
    protected static JPanel filterPanel = new JPanel();
    protected static JPanel customizePanel = new JPanel();
    protected static JPanel tablePanel = new JPanel();
    protected static JPanel changePanel = new JPanel();
    protected static JPanel insertPanel = new JPanel();
    protected static JPanel plusTab = new JPanel();

    protected static OnClickListener onClickListener;
    protected static ImporterBase importerBase;
    protected static ExporterBase exporterBase;

    /**
     * path to the SPARQL endpoint
     */
    protected static String dsLocation = new String("http://localhost:3030/caex/");
    // Bitte Pfad angeben...
    protected static String impLocation = "Importer/src/main/resources/Comos.xml";
    // Bitte Pfad angeben...
    protected static String expLocation = "Exporter/src/main/resources/exporter_out.xml";
    // Bitte Pfad angeben...
    protected static String mappingLocation = "Importer/src/main/resources/ComosTransform.xsl";
    // private static String rdfImportLocation =
    // "C:/Users/abpma/Desktop/rdfOutput.xml";
    protected static int filterNumber = 1;
    protected static String lastResult = "construct";
    protected static ArrayList<String> graphList = new ArrayList<String>();
    /**
     * ArrayList that contains all subject-TextFields for the Sparql Query
     */
    public static ArrayList<JTextField> subTxtList = new ArrayList<JTextField>();
    /**
     * ArrayList that contains all predicate-TextFields for the Sparql Query
     */
    public static ArrayList<JTextField> preTxtList = new ArrayList<JTextField>();
    /**
     * ArrayList that contains all object-TextFields for the Sparql Query
     */
    public static ArrayList<JTextField> objTxtList = new ArrayList<JTextField>();
    /**
     * ArrayList that contains all "OPTIONAL"-CheckBoxes for the Sparql Query
     */
    public static ArrayList<JCheckBox> optChkList = new ArrayList<JCheckBox>();
    /**
     * stores all available graph names in data set
     */
    public static JComboBox<String> graphBox = new JComboBox<String>();
    /**
     * stores all modules which should be connected to a new plant/process cell
     */
    public static HashMap<Object, Tab> newConnMap = new HashMap<Object, Tab>();
    protected static JTextField newSubTxtField = new JTextField("subject");
    protected static JTextField newPreTxtField = new JTextField("predicate");
    protected static JTextField newObjTxtField = new JTextField("object");
    protected static JTextField rowTxtField = new JTextField("Zeile ...");
    protected static JTextField colTxtField = new JTextField("Spalte ...");
    protected static JTextField newTxtField = new JTextField("neuer Wert ...");
    /**
     * ArrayList that stores all visible Tabs
     */
    public static ArrayList<Tab> tabList = new ArrayList<Tab>();

    protected static JButton assumeBttn = new JButton("Übernehmen!");
    protected static JTextField confTxtField = new JTextField(dsLocation);
    protected static JTextField impTxtField = new JTextField(impLocation);
    protected static JTextField expTxtField = new JTextField(expLocation);
    protected static JButton remoFilterBttn = new JButton("Zusätzlichen Filter entfernen");
    protected static JComboBox<String> expBox;
    protected static JComboBox<String> newModuleBox;
    protected static JTable table;
    protected static JTextField newModuleName = new JTextField("Neuer Name der Anlage");
    protected static JTextField newModuleGraph = new JTextField("Neuer Name des Graphen");
    /**
     * CheckBox for applying a "LIMIT"-filter for the SPARQL query generator
     */
    public static JCheckBox limCheck = new JCheckBox("LIMIT: ");
    /**
     * TextField to define the "LIMIT"-filter value
     */
    public static JTextField limTxtField = new JTextField("Limit eintragen");
    protected static ResultSet result;

    /**
     * Constructor for building up the main gui frame
     */
    public GUI() {
	onClickListener = new OnClickListener();

	GUIBuildHelper.buildMainPanel();

	importerBase = new ImporterBase();
	exporterBase = new ExporterBase();
    }

    /**
     * Configuration button click method: stores the new value of Sparql endpoint in
     * dsLocation
     */
    public static void actConfig() {
	dsLocation = confTxtField.getText().toString();
    }

    /**
     * Import button click method calls Importer start method
     */
    public static void actImport() {
	impLocation = impTxtField.getText().toString();
	try {
	    importerBase.doImport(impLocation);
	} catch (Exception e) {
	    System.err.println(e);
	    JOptionPane.showMessageDialog(frame, "Fehler beim Importieren (transformieren)");
	    return;
	}

	String updateString = "DELETE {?s ?p ?o} WHERE {?s ?p ?o}";
	Update_Execute.executeUpdate(GUI.frame, updateString, GUI.dsLocation);

	String[] parts = impLocation.split("/");
	String name = parts[parts.length - 1].split("\\.")[0];
	String graphName = "<" + dsLocation + "data/" + name + ">";
	updateString = "  LOAD <file:" + ImporterBase.getRdfOutputPath() + ">";
	Update_Execute.executeUpdate(frame, updateString, dsLocation);
	String queryString = "SELECT ?s ?newS ?p ?o ?next WHERE { ?s ?p ?o . OPTIONAL {?s <http://eatld.et.tu-dresden.de/mso/comosUid> ?newS } . OPTIONAL {?o <http://eatld.et.tu-dresden.de/mso/comosUid> ?next }}";
	result = Query_Execute.executeQuery(dsLocation, queryString, frame);
	List<QuerySolution> list = ResultSetFormatter.toList(result);

	HashMap<String, String> hMap = new HashMap<String, String>();
	String newID = "";
	for (QuerySolution sol : list) {

	    try {
		newID = sol.get("?newS").toString();
	    } catch (NullPointerException e) {
		newID = UUID.randomUUID().toString();
	    }
	    if (!hMap.containsKey(sol.get("?s").toString()))
		hMap.put(sol.get("?s").toString(), newID);
	}

	String allResults = "";
	for (QuerySolution sol : list) {
	    RDFNode node = sol.get("?o");
	    String obj;
	    if (node.isLiteral())
		obj = "\"" + node.toString() + "\"";
	    else {
		if (node.asNode().isBlank()) {
		    if (sol.get("?next") == null)
			obj = "<http://localhost:3030/ds/" + hMap.get(sol.get("?o").toString()) + ">";
		    else
			obj = "<http://localhost:3030/ds/" + sol.get("?next") + ">";
		} else
		    obj = "<" + node.toString() + ">";
	    }

	    allResults += "<http://localhost:3030/ds/" + hMap.get(sol.get("?s").toString()) + "> <" + sol.get("?p") + "> " + obj + " . \n";
	}

	try {
	    FileOutputStream outStream = new FileOutputStream(new File("./UserInterface/src/main/resources/newTriples.nt")); // helper file
	    PrintWriter p = new PrintWriter(outStream);
	    p.write(allResults);
	    p.close();
	} catch (FileNotFoundException e) {
	}
	updateString = "LOAD <file:./UserInterface/src/main/resources/newTriples.nt> INTO GRAPH " + graphName;
	Update_Execute.executeUpdate(GUI.frame, updateString, GUI.dsLocation);

	updateString = "DELETE {?s ?p ?o} WHERE {?s ?p ?o}";
	Update_Execute.executeUpdate(GUI.frame, updateString, GUI.dsLocation);

	graphList.add(graphName);

	graphBox.addItem(graphName);

	GUIBuildHelper.updateTabs();
    }

    /**
     * Export button click method calls Exporter start method
     */
    public static void actExport() {
	expLocation = expTxtField.getText().toString();
	String outForm = expBox.getSelectedItem().toString();
	String graph = graphBox.getSelectedItem().toString();
	exporterBase.doExport(dsLocation, outForm, expLocation, graph, frame);
    }

    /**
     * add Filter button click method adds new line for Sparql-Select-conditions
     */
    public static void actAddFilter() {
	filterNumber++;
	GUIBuildHelper.buildQueryPanel();
	validateMainPanel();
    }

    /**
     * remove Filter button click method removes a line for Sparql-Select-conditions
     * 1 still remaining
     */
    public static void actRemoFilter() {
	filterNumber--;
	int last = subTxtList.size() - 1;
	subTxtList.remove(last);
	preTxtList.remove(last);
	objTxtList.remove(last);
	optChkList.remove(last);
	for (int i = 4; i > 0; i--) {
	    queryPanel.remove(3 * last);
	}
	queryPanel.validate();
	queryPanel.updateUI();
	GUIBuildHelper.buildQueryPanel();
	validateMainPanel();
    }

    /**
     * Spaqrl-select button click method generates and executes Sparql-select-query
     */
    public static void actSelect() {
	String queryString = SPARQL_Select.generateQuery();
	result = Query_Execute.executeQuery(dsLocation, queryString, frame);
	lastResult = "select";
	updateTable();
    }

    private static void updateTable() {

	List<QuerySolution> list = ResultSetFormatter.toList(result);

	DefaultTableModel tableModel = new DefaultTableModel();

	ArrayList<String> res_gra_data = new ArrayList<String>();
	ArrayList<String> res_sub_data = new ArrayList<String>();
	ArrayList<String> res_pre_data = new ArrayList<String>();
	ArrayList<String> res_obj_data = new ArrayList<String>();

	for (int i = 0; i < subTxtList.size(); i++) {
	    String gra_name = graphBox.getSelectedItem().toString();
	    String sub_name = subTxtList.get(i).getText();
	    String pre_name = preTxtList.get(i).getText();
	    String obj_name = objTxtList.get(i).getText();

	    res_gra_data.clear();
	    res_sub_data.clear();
	    res_pre_data.clear();
	    res_obj_data.clear();

	    for (int j = 0; j < list.size(); j++) {
		if (gra_name.equals("All")) {
		    String data0 = list.get(j).toString();
		    String[] parts = data0.split("\\?g = ");
		    parts = parts[1].split(" \\)"); // " )"
		    if (parts[0] == null)
			parts[0] = "";
		    res_gra_data.add(parts[0]);
		} else {
		    res_gra_data.add(gra_name);
		}
		if (sub_name.charAt(0) == '?') {
		    String data0 = list.get(j).toString();
		    String[] parts = data0.split("\\" + sub_name + " = "); // \\? escape
		    parts = parts[1].split(" \\)");
		    // String data = list.get(j).get(sub_name).toString(); //ist kacke, weil da
		    // fehlen "" bzw. <>
		    if (parts[0] == null)
			parts[0] = "";
		    res_sub_data.add(parts[0]);
		}
		if (pre_name.charAt(0) == '?') {
		    String data0 = list.get(j).toString();
		    String[] parts = data0.split("\\" + pre_name + " = ");
		    parts = parts[1].split(" \\)");
		    // String data = list.get(j).get(pre_name).toString();
		    if (parts[0] == null)
			parts[0] = "";
		    res_pre_data.add(parts[0]);
		}
		if (obj_name.charAt(0) == '?') {
		    String data0 = list.get(j).toString();
		    String[] parts = data0.split("\\" + obj_name + " = ");
		    parts = parts[1].split(" \\)");
		    // String data = list.get(j).get(obj_name).toString();
		    if (parts[0] == null)
			parts[0] = "";
		    res_obj_data.add(parts[0]);
		}
	    }
	    Vector<String> gra_data = new Vector<String>(res_gra_data.size());
	    gra_data.addAll(res_gra_data);
	    tableModel.addColumn("Graph", gra_data);
	    if (sub_name.charAt(0) == '?') {
		Vector<String> sub_data = new Vector<String>(res_sub_data.size());
		sub_data.addAll(res_sub_data);
		tableModel.addColumn(sub_name, sub_data);
	    }
	    if (pre_name.charAt(0) == '?') {
		Vector<String> pre_data = new Vector<String>(res_pre_data.size());
		pre_data.addAll(res_pre_data);
		tableModel.addColumn(pre_name, pre_data);
	    }
	    if (obj_name.charAt(0) == '?') {
		Vector<String> obj_data = new Vector<String>(res_obj_data.size());
		obj_data.addAll(res_obj_data);
		tableModel.addColumn(obj_name, obj_data);
	    }
	}

	table.setModel(tableModel);
	validateMainPanel();
    }

    /**
     * deletes the old value and replaces it with the new value directly in the
     * graph
     */
    public static void actAssume() {
	int intRow = 0;
	int intCol = 0;
	try {
	    intRow = Integer.parseInt(rowTxtField.getText());
	    intCol = Integer.parseInt(colTxtField.getText());

	} catch (Exception e) {
	    JOptionPane.showMessageDialog(frame, "Nur Zahlen eingeben!");
	    return;
	}
	if (intCol == 0) {
	    JOptionPane.showMessageDialog(frame, "Graphnamen können nicht auf diese Weise geändert werden.");
	    return;
	}
	String oldValue = table.getValueAt(intRow, intCol).toString();
	String colName = table.getColumnName(intCol);
	String newValue = newTxtField.getText();
	String graph = table.getValueAt(intRow, 0).toString();

	if (oldValue.equals(newValue))
	    return;

	boolean sub_flag = false;
	boolean pre_flag = false;
	boolean obj_flag = false;

	int i = 0;
	for (JTextField field : subTxtList) {
	    if (field.getText().equals(colName)) {
		sub_flag = true;
		break;
	    }
	    i++;
	}
	if (!sub_flag) {
	    i = 0;
	    for (JTextField field : preTxtList) {
		if (field.getText().equals(colName)) {
		    pre_flag = true;
		    break;
		}
		i++;
	    }
	}
	if (!pre_flag) {
	    i = 0;
	    for (JTextField field : objTxtList) {
		if (field.getText().equals(colName)) {
		    obj_flag = true;
		    break;
		}
		i++;
	    }
	}

	String updateString = "WITH " + graph;
	updateString += "DELETE { ";
	if (sub_flag)
	    updateString += oldValue + " " + preTxtList.get(i).getText() + " " + objTxtList.get(i).getText() + " } ";
	else if (pre_flag)
	    updateString += subTxtList.get(i).getText() + " " + oldValue + " " + objTxtList.get(i).getText() + " } ";
	else if (obj_flag)
	    updateString += subTxtList.get(i).getText() + " " + preTxtList.get(i).getText() + " " + oldValue + " } ";

	updateString += " INSERT { ";
	if (sub_flag)
	    updateString += newValue + " " + preTxtList.get(i).getText() + " " + objTxtList.get(i).getText() + " } ";
	else if (pre_flag)
	    updateString += subTxtList.get(i).getText() + " " + newValue + " " + objTxtList.get(i).getText() + " } ";
	else if (obj_flag)
	    updateString += subTxtList.get(i).getText() + " " + preTxtList.get(i).getText() + " " + newValue + " } ";

	updateString += "WHERE { ";

	for (i = 0; i < GUI.subTxtList.size(); i++) {
	    if (GUI.optChkList.get(i).isSelected())
		updateString += "OPTIONAL ";
	    updateString += "{ ";
	    String sub = GUI.subTxtList.get(i).getText();
	    if (sub.equals(colName))
		updateString += oldValue + " ";
	    else
		updateString += sub + " ";

	    String pre = GUI.preTxtList.get(i).getText();
	    if (pre.equals(colName))
		updateString += oldValue + " ";
	    else
		updateString += pre + " ";

	    String obj = GUI.objTxtList.get(i).getText();
	    if (obj.equals(colName))
		updateString += oldValue + " . } ";
	    else
		updateString += obj + " . } ";
	}

	updateString += "}";

	Update_Execute.executeUpdate(frame, updateString, dsLocation);

	actSelect();
    }

    /**
     * adds a Triple to the graph
     */
    public static void actInsertNew() {
	String insertString = " INSERT DATA { GRAPH " + graphBox.getSelectedItem().toString() + " { " + newSubTxtField.getText() + " " + newPreTxtField.getText() + " " + newObjTxtField.getText() + " } }";

	Update_Execute.executeUpdate(frame, insertString, dsLocation);
    }

    /**
     * deletes the Triple from the graph
     */
    public static void actDeleteTriple() {
	String deleteString = " DELETE DATA { GRAPH " + graphBox.getSelectedItem().toString() + " { " + newSubTxtField.getText() + " " + newPreTxtField.getText() + " " + newObjTxtField.getText() + " } }";

	Update_Execute.executeUpdate(frame, deleteString, dsLocation);

    }

    protected static void searchInGraph(String graph, String obj, ArrayList<String> ident, ArrayList<String> label) {
	String queryString = "SELECT ?s ?label WHERE { GRAPH " + graph + " {{ ?s <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> " + obj + " }. {?s <http://www.w3.org/2000/01/rdf-schema#comment> ?label }}}";
	result = Query_Execute.executeQuery(dsLocation, queryString, frame);
	List<QuerySolution> list = ResultSetFormatter.toList(result);
	for (QuerySolution sol : list) {
	    String sub = sol.get("?s").toString();
	    String lbl = sol.get("?label").toString();

	    ident.add("<" + sub + ">");
	    label.add(lbl);
	}
    }

    /**
     * new process cell is added to the graph
     */
    public static void actNewCell() {
	String name = newModuleName.getText();
	String graph = newModuleGraph.getText();
	String graphName = "<" + dsLocation + "data/" + graph + ">";

	String updateString = " INSERT DATA { GRAPH " + graphName + " { ";
	updateString += "<http://localhost:3030/ds/" + UUID.randomUUID().toString() + "> ";
	updateString += "<http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ";
	if (newModuleBox.getSelectedItem().toString().equals("Werk"))
	    updateString += "<http://eatld.et.tu-dresden.de/mso/Site> ";
	else
	    updateString += "<http://eatld.et.tu-dresden.de/mso/ProcessCell> ";

	updateString += " ; <http://www.w3.org/2000/01/rdf-schema#comment> \"" + name + "\"}}";

	Update_Execute.executeUpdate(frame, updateString, dsLocation);
	graphList.add(graphName);

	GUIBuildHelper.updateTabs();

    }

    private static void validateMainPanel() {
	if (lastResult.equals("construct"))
	    assumeBttn.setEnabled(false);
	else
	    assumeBttn.setEnabled(true);
	mainPanel.validate();
	frame.validate();
    }
}
