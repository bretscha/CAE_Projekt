package org.UserInterface;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;

import utilities.Query_Execute;

/**
 * This class is a Helper to build the single elements of the UI
 * 
 * @author bretscha
 *
 */
public class GUIBuildHelper extends GUI {
    private GUIBuildHelper() {
	// to prevent Initialization
    }

    public static void buildMainPanel() {
	JScrollPane scrollPane = new JScrollPane(mainPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	tabbedPane.addTab("Main", scrollPane);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setSize(1600, 800);
	frame.add(tabbedPane);

	frame.setGlassPane(glassPane);
	glassPane.setVisible(true);
	frame.setVisible(true);

	mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

	String queryString = "SELECT ?g WHERE {GRAPH ?g {}}";
	ResultSet result = Query_Execute.executeQuery(dsLocation, queryString, frame);

	for (QuerySolution sol : ResultSetFormatter.toList(result)) {
	    graphList.add("<" + sol.get("?g").toString() + ">");
	}

	buildConfigPanel();
	mainPanel.add(configPanel);
	buildImportPanel();
	mainPanel.add(importPanel);
	buildExportPanel();
	mainPanel.add(exportPanel);
	buildGraphPanel();
	mainPanel.add(graphPanel);
	buildQueryPanel();
	mainPanel.add(queryPanel);
	buildFilterPanel();
	mainPanel.add(filterPanel);
	buildCustomizePanel();
	mainPanel.add(customizePanel);
	buildTablePanel();
	mainPanel.add(tablePanel);
	buildInsertPanel();
	mainPanel.add(insertPanel);
	buildChangePanel();
	mainPanel.add(changePanel);

	updateTabs();
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

    private static void buildConfigPanel() {
	JButton confBttn = new JButton("Save!");
	confBttn.setActionCommand("confBttn");
	configPanel.setLayout(new FlowLayout());
	configPanel.add(new JLabel("Dataset Location: "));
	configPanel.add(confTxtField);
	configPanel.add(confBttn);
	confBttn.addActionListener(onClickListener);
    }

    private static void buildExportPanel() {
	JButton expBttn = new JButton("Export!");
	expBttn.setActionCommand("expBttn");
	String[] expChoice = { "GraphML" }; // weitere theoretisch möglich
	expBox = new JComboBox<String>(expChoice);
	exportPanel.setLayout(new FlowLayout());
	exportPanel.add(new JLabel("Speichern unter: "));
	exportPanel.add(expTxtField);
	exportPanel.add(expBox);
	exportPanel.add(expBttn);
	expBttn.addActionListener(onClickListener);
    }

    private static void buildGraphPanel() {
	graphBox.addItem("All");
	for (String name : graphList)
	    graphBox.addItem(name);
	graphPanel.add(new JLabel("Graph aus Dataset für SPARQL Befehle auswählen: "));
	graphPanel.add(graphBox);
    }

    static void buildQueryPanel() {
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

    private static void buildTablePanel() {
	tablePanel.add(new JLabel("Ergebnis: (Zeilen und Spaltennummern beginnen mit \"0\")"));
	tablePanel.setLayout(new FlowLayout());

	DefaultTableModel tableModel = new DefaultTableModel(new Object[] { "number", "subject1", "predicate1", "object1" }, 10);
	table = new JTable(tableModel) {

	    /**
	     * 
	     */
	    private static final long serialVersionUID = 1L;

	    public String getToolTipText(MouseEvent e) {
		int row = rowAtPoint(e.getPoint());
		int column = columnAtPoint(e.getPoint());

		Object value = getValueAt(row, column);
		return value == null ? null : value.toString();
	    }

	};

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

    private static void buildInsertPanel() {
	JButton insertNewBttn = new JButton("Triple erstellen");
	insertNewBttn.addActionListener(onClickListener);
	insertNewBttn.setActionCommand("insertNew");
	JButton deleteBttn = new JButton("Triple löschen");
	deleteBttn.addActionListener(onClickListener);
	deleteBttn.setActionCommand("deleteTriple");
	insertPanel.add(newSubTxtField);
	insertPanel.add(newPreTxtField);
	insertPanel.add(newObjTxtField);
	insertPanel.add(insertNewBttn);
	insertPanel.add(deleteBttn);
    }

    public static void updateTabs() {

	tabList.clear();
	for (int i = tabbedPane.getTabCount() - 1; i > 0; i--) {
	    tabbedPane.remove(i);
	}
	String[] searchFor = { "<http://eatld.et.tu-dresden.de/mso/Site>", "<http://eatld.et.tu-dresden.de/mso/ProcessCell>", "<http://eatld.et.tu-dresden.de/mso/Unit>" }; // hierarchical

	for (String graph : graphList) {
	    ArrayList<String> ident = new ArrayList<String>();
	    ArrayList<String> label = new ArrayList<String>();
	    String type = "";
	    for (String obj : searchFor) {
		searchInGraph(graph, obj, ident, label);
		if (result != null) {
		    type = obj;
		    break;
		}
	    }
	    if (result != null && ident.size() != 0) {
		Tab tab = new Tab(label, ident, type, graph);
		tabList.add(tab);
		tabbedPane.addTab(graph, tab.scrollPane);
	    }
	}

	ArrayList<String> plus = new ArrayList<String>();
	plus.add("plus");

	initPlusTab();
	tabbedPane.addTab("+", plusTab);
	tabList.add(new Tab(plus, plus, "plus", "plus"));
    }

    protected static void initPlusTab() {
	plusTab = new JPanel();
	plusTab.setLayout(new FlowLayout());
	newModuleGraph.setToolTipText("Keine Leerzeichen erlaubt!");
	String[] moduleType = { "Werk", "Anlage" };
	newModuleBox = new JComboBox<String>(moduleType);
	JButton assumeNewBttn = new JButton("Neu erstellen");
	assumeNewBttn.setActionCommand("assumeNewCell");
	assumeNewBttn.addActionListener(onClickListener);
	plusTab.add(newModuleGraph);
	plusTab.add(newModuleName);
	plusTab.add(newModuleBox);
	plusTab.add(assumeNewBttn);
    }
}
