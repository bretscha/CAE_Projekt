package org.UserInterface;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;

public class GUI {

    public static JFrame frame = new JFrame("ToolBox");
    public static JTabbedPane tabbedPane = new JTabbedPane();
    private static JPanel mainPanel = new JPanel();
    private static JPanel configPanel = new JPanel();
    private static JPanel importPanel = new JPanel();
    private static JPanel exportPanel = new JPanel();
    private static JPanel queryPanel = new JPanel();
    private static JPanel filterPanel = new JPanel();
    private static JPanel customizePanel = new JPanel();
    private static JPanel constructPanel = new JPanel();
    private static JPanel tablePanel = new JPanel();
    private static JPanel changePanel = new JPanel();
    private static JPanel updateDbPanel = new JPanel();

    /**
     * Click Listener for all buttons
     */
    public static OnClickListener onClickListener = new OnClickListener();

    /**
     * path to the SPARQL endpoint
     */
    public static String dsLocation = new String("http://localhost:3030/ds/query");
    private static String impLocation = new String("Bitte Pfad angeben...");
    private static String expLocation = new String("Bitte Pfad angeben...");
    private static int filterNumber = 1;
    private static String lastResult = "construct";
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
     * ArrayList that contains all "OPTIONAL"-CheckBoxes  for the Sparql Query
     */
    public static ArrayList<JCheckBox> optChkList = new ArrayList<JCheckBox>();
    /**
     * TextField that contains the new subject value for Triple -Construction 
     */
    public static JTextField newSubTxtField = new JTextField("new subject");
    /**
     * TextField that contains the new predicate value for Triple-Construction 
     */
    public static JTextField newPreTxtField = new JTextField("new predicate");
    /**
     * TextField that contains the new object value for Triple-Construction 
     */
    public static JTextField newObjTxtField = new JTextField("new object");
	/**
	 * TextField to define the row of the result table for value changes
	 */
	public static JTextField rowTxtField = new JTextField("Zeile ...");
	/**
	 * TextField to define the column of the result table for value changes
	 */
	public static JTextField colTxtField = new JTextField("Spalte ...");
	/**
	 * TextField to define the new value of the cell defined by colTxtField and rowTxtField
	 */
	public static JTextField newTxtField = new JTextField("neuer Wert ...");
	
	private static JButton assumeBttn = new JButton("Übernehmen!");
    private static JTextField confTxtField = new JTextField(dsLocation);
    private static JTextField impTxtField = new JTextField(impLocation);
    private static JTextField expTxtField = new JTextField(expLocation);
    private static JButton remoFilterBttn = new JButton("Zusätzlichen Filter entfernen");
    private static JComboBox<String> expBox;
    private static JTable table;
    private static JButton updateBttn = new JButton("Datenbank akualisieren!");
    public static JCheckBox limCheck = new JCheckBox("LIMIT: ");
    public static JCheckBox sortCheck = new JCheckBox("SORT BY: ");
    public static JTextField limTxtField = new JTextField("Limit eintragen");
    public static JComboBox<String> sortBox;
    private static ResultSet result;

    /**
     * start method
     * @param args start arguments
     */
    public static void main(String[] args) {
	buildGUI();
    }

    private static void buildGUI() {
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setSize(1600, 800);
	
	frame.add(tabbedPane);

	mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

	buildConfigPanel();
	mainPanel.add(configPanel);
	buildImportPanel();
	mainPanel.add(importPanel);
	buildExportPanel();
	mainPanel.add(exportPanel);
	buildQueryPanel();
	mainPanel.add(queryPanel);
	buildFilterPanel();
	mainPanel.add(filterPanel);
	buildCustomizePanel();
	mainPanel.add(customizePanel);
	buildConstructPanel();
	mainPanel.add(constructPanel);
	buildTablePanel();
	mainPanel.add(tablePanel);
	buildChangePanel();
	mainPanel.add(changePanel);
	buildUpdateDbPanel();
	mainPanel.add(updateDbPanel);

	JScrollPane scrollPane = new JScrollPane(mainPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	updateTabs();
	tabbedPane.addTab("Main", scrollPane);
	frame.setVisible(true);
    }

    private static void buildConfigPanel() {
	JButton confBttn = new JButton("Save!");
	confBttn.setActionCommand("confBttn");
	configPanel.setLayout(new FlowLayout());
	configPanel.add(new JLabel("Dataset Location: "));
	configPanel.add(confTxtField);
	configPanel.add(confBttn);
	confBttn.addActionListener(onClickListener);
    }

    /**
     * Configuration button click method:
     * stores the new value of Sparql endpoint in dsLocation
     */
    public static void actConfig() {
	dsLocation = confTxtField.getText().toString();
    }

    private static void buildImportPanel() {
	JButton impBttn = new JButton("Import!");
	impBttn.setActionCommand("impBttn");
	importPanel.setLayout(new FlowLayout());
	importPanel.add(new JLabel("Import Datei: "));
	importPanel.add(impTxtField);
	importPanel.add(impBttn);
	impBttn.addActionListener(onClickListener);
    }

    /**
     * Import button click method
     * calls Importer start method
     */
    public static void actImport() {
	impLocation = impTxtField.getText().toString();
	// startImport(impLocation);
    }

    private static void buildExportPanel() {
	JButton expBttn = new JButton("Export!");
	expBttn.setActionCommand("expBttn");
	String[] expChoice = { "XML", "GraphML" };
	expBox = new JComboBox<String>(expChoice);
	exportPanel.setLayout(new FlowLayout());
	exportPanel.add(new JLabel("Speichern unter: "));
	exportPanel.add(expTxtField);
	exportPanel.add(expBox);
	exportPanel.add(expBttn);
	expBttn.addActionListener(onClickListener);
    }

    /**
     * Export button click method
     * calls Ecporter start method
     */
    public static void actExport() {
	expLocation = expTxtField.getText().toString();
	// String outType = expBox.getSelectedItem().toString();
	// startExporter(expLocation, outType);
    }

    private static void buildQueryPanel() {
	int size = subTxtList.size();
	for (int i = size + 1; i < filterNumber + 1; i++) {
	    JCheckBox optChkBox = new JCheckBox("OPTIONAL: ");
	    JTextField subTxtField = new JTextField("Subject " + i);
	    JTextField preTxtField = new JTextField("predicate " + i);
	    JTextField objTxtField = new JTextField("object " + i);
	    optChkList.add(optChkBox);
	    subTxtList.add(subTxtField);
	    preTxtList.add(preTxtField);
	    objTxtList.add(objTxtField);
	}

	queryPanel.setLayout(new GridLayout(0, 4));
	for (int i = size; i < filterNumber; i++) {
	    queryPanel.add(optChkList.get(i));
	    queryPanel.add(subTxtList.get(i));
	    queryPanel.add(preTxtList.get(i));
	    queryPanel.add(objTxtList.get(i));
	}

	if (subTxtList.size() == 1)
	    remoFilterBttn.setEnabled(false);
	else
	    remoFilterBttn.setEnabled(true);
    }

    private static void buildFilterPanel() {
	filterPanel.setLayout(new GridLayout(2, 2));
	String[] sortChoice = { "kein", "Plan", "mal", "sehen" };
	sortBox = new JComboBox<String>(sortChoice);
	filterPanel.add(limCheck);
	filterPanel.add(limTxtField);
	filterPanel.add(sortCheck);
	filterPanel.add(sortBox);
    }

    private static void buildCustomizePanel() {
	JButton addFilterBttn = new JButton("Weiteren Filter hinzufügen");
	JButton searchBttn = new JButton("Suchen!");
	addFilterBttn.setActionCommand("addFilterBttn");
	addFilterBttn.addActionListener(onClickListener);
	searchBttn.setActionCommand("searchBttn");
	searchBttn.addActionListener(onClickListener);
	remoFilterBttn.setActionCommand("remoFilterBttn");
	remoFilterBttn.addActionListener(onClickListener);
	customizePanel.setLayout(new FlowLayout());
	customizePanel.add(addFilterBttn);
	customizePanel.add(remoFilterBttn);
	customizePanel.add(searchBttn);
    }

    /**
     * add Filter button click method
     * adds new line for Sparql-Select-conditions
     */
    public static void actAddFilter() {
	filterNumber++;
	buildQueryPanel();
	validateMainPanel();
    }

    /**
     * remove Filter button click method
     * removes a line for Sparql-Select-conditions
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
	buildQueryPanel();
	validateMainPanel();
    }

    /**
     * Spaqrl-select button click method
     * generates and executes Sparql-select-query
     */
    public static void actSelect() {
	String query = SPARQL_Select.generateQuery();
	result = Query_Execute.executeQuery(query);
	lastResult = "select";
	updateTable();
    }

    private static void buildConstructPanel() {
	JButton constructBttn = new JButton("Neue Tripel erstellen!");
	constructBttn.addActionListener(onClickListener);
	constructBttn.setActionCommand("constructBttn");
	constructPanel.setLayout(new FlowLayout());
	constructPanel.add(new JLabel("Neues Tripel erstellen aus:"));
	constructPanel.add(newSubTxtField);
	constructPanel.add(newPreTxtField);
	constructPanel.add(newObjTxtField);
	constructPanel.add(constructBttn);
    }

    /**
     * construct triples button click method
     * generates new triples using Sparql-select-conditions
     */
    public static void actConstruct() {
	String query = SPARQL_Construct.generateQuery();
	result = Query_Execute.executeQuery(query);
	lastResult = "construct";
	updateTable();
    }

    private static void updateTable() {

	List<QuerySolution> list = ResultSetFormatter.toList(result);

	DefaultTableModel tableModel = new DefaultTableModel();

	ArrayList<String> res_sub_data = new ArrayList<String>();
	ArrayList<String> res_pre_data = new ArrayList<String>();
	ArrayList<String> res_obj_data = new ArrayList<String>();
	Vector<String> sub_data;
	Vector<String> pre_data;
	Vector<String> obj_data;

	for (int i = 0; i < subTxtList.size(); i++) {
	    String sub_name = subTxtList.get(i).getText();
	    String pre_name = preTxtList.get(i).getText();
	    String obj_name = objTxtList.get(i).getText();

	    if (lastResult.equals("construct")) {
		sub_name = newSubTxtField.getText();
		pre_name = newPreTxtField.getText();
		obj_name = newObjTxtField.getText();
	    }

	    res_sub_data.clear();
	    res_pre_data.clear();
	    res_obj_data.clear();

	    for (int j = 0; j < list.size(); j++) {
		if (sub_name.charAt(0) == '?') {
		    String data = list.get(j).get(sub_name).toString();
		    if(data == null) data = "";
			res_sub_data.add(data);
		}
		if (pre_name.charAt(0) == '?') {
			String data = list.get(j).get(pre_name).toString();
		    if(data == null) data = "";
		    res_pre_data.add(data);
		}
		if (obj_name.charAt(0) == '?') {
			String data = list.get(j).get(obj_name).toString();
			if(data == null) data = "";
		    res_obj_data.add(data);
		}
	    }
	    if (sub_name.charAt(0) == '?') {
		sub_data = new Vector<String>(res_sub_data.size());
		sub_data.addAll(res_sub_data);
		tableModel.addColumn(sub_name, sub_data);
	    }
	    if (pre_name.charAt(0) == '?') {
		pre_data = new Vector<String>(res_pre_data.size());
		pre_data.addAll(res_pre_data);
		tableModel.addColumn(pre_name, pre_data);
	    }
	    if (obj_name.charAt(0) == '?') {
		obj_data = new Vector<String>(res_obj_data.size());
		obj_data.addAll(res_obj_data);
		tableModel.addColumn(obj_name, obj_data);
	    }
	}

	table.setModel(tableModel);
	validateMainPanel();
    }

    private static void buildTablePanel() {
	tablePanel.add(new JLabel("Ergebnis: "));
	tablePanel.setLayout(new FlowLayout());

	DefaultTableModel tableModel = new DefaultTableModel(new Object[] { "number", "subject1", "predicate1", "object1" }, 10);
	table = new JTable(tableModel);
	table.setPreferredScrollableViewportSize(new Dimension(800, 160));
	table.setEnabled(false);
	table.getTableHeader().setReorderingAllowed(false);
	tablePanel.add(new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED));
    }

    private static void buildChangePanel() {
	assumeBttn.addActionListener(onClickListener);
	assumeBttn.setActionCommand("assumeBttn");
	changePanel.add(rowTxtField);
	changePanel.add(colTxtField);
	changePanel.add(newTxtField);
	changePanel.add(assumeBttn);
    }

    public static void actAssume() {
	// tbd
    }

    private static void buildUpdateDbPanel() {
	updateBttn.addActionListener(onClickListener);
	updateBttn.setActionCommand("updateDbBttn");
	updateDbPanel.add(updateBttn);
    }

    public static void actDbUpdate() {
	// tbd
    }
    
    private static void updateTabs() {
    	ArrayList<Tab> tabsList = new ArrayList<Tab>();
    	//query = ...;
    	//for ...
    	
    	Tab tab = new Tab("label", "name");
    	tabsList.add(tab);
    	tabbedPane.addTab("name", tab.scrollPane);
//    	tab.drawLines();
    	tabbedPane.repaint();
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
