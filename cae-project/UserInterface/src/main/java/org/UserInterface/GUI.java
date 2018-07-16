package org.UserInterface;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.Vector;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
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
	public static JFrame frame = new JFrame("ToolBox");
	private static GlassPane glassPane = new GlassPane();
	/**
	 * Pane in which all scollPanes of Tab.class instances will be placed
	 */
	public static JTabbedPane tabbedPane = new JTabbedPane();
	private static JPanel mainPanel = new JPanel();
	private static JPanel configPanel = new JPanel();
	private static JPanel importPanel = new JPanel();
	private static JPanel exportPanel = new JPanel();
	private static JPanel graphPanel = new JPanel();
	private static JPanel queryPanel = new JPanel();
	private static JPanel filterPanel = new JPanel();
	private static JPanel customizePanel = new JPanel();
	private static JPanel tablePanel = new JPanel();
	private static JPanel changePanel = new JPanel();
	private static JPanel insertPanel = new JPanel();
	private static JPanel plusTab = new JPanel();

	private static OnClickListener onClickListener;
	private static ImporterBase importerBase;
	private static ExporterBase exporterBase;

	/**
	 * path to the SPARQL endpoint
	 */
	public static String dsLocation = new String("http://localhost:3030/ds/");
	// Bitte Pfad angeben...
	private static String impLocation = "./Importer/src/main/resources/Comos.xml";
	// Bitte Pfad angeben...
	private static String expLocation = "./Exporter/src/main/resources/exporter_out.xml";
	// Bitte Pfad angeben...
	private static String mappingLocation = "./Importer/src/main/resources/ComosTransform.xsl";
	// private static String rdfImportLocation =
	// "C:/Users/abpma/Desktop/rdfOutput.xml";
	private static int filterNumber = 1;
	private static String lastResult = "construct";
	private static ArrayList<String> graphList = new ArrayList<String>();
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
	private static JTextField newSubTxtField = new JTextField("subject");
	private static JTextField newPreTxtField = new JTextField("predicate");
	private static JTextField newObjTxtField = new JTextField("object");
	private static JTextField rowTxtField = new JTextField("Zeile ...");
	private static JTextField colTxtField = new JTextField("Spalte ...");
	private static JTextField newTxtField = new JTextField("neuer Wert ...");
	/**
	 * ArrayList that stores all visible Tabs
	 */
	public static ArrayList<Tab> tabList = new ArrayList<Tab>();

	private static JButton assumeBttn = new JButton("Übernehmen!");
	private static JTextField confTxtField = new JTextField(dsLocation);
	private static JTextField impTxtField = new JTextField(impLocation);
	private static JTextField expTxtField = new JTextField(expLocation);
	private static JButton remoFilterBttn = new JButton("Zusätzlichen Filter entfernen");
	private static JComboBox<String> expBox;
	private static JComboBox<String> newModuleBox;
	private static JTable table;
	private static JTextField newModuleName = new JTextField("Neuer Name der Anlage");
	/**
	 * CheckBox for applying a "LIMIT"-filter for the SPARQL query generator
	 */
	public static JCheckBox limCheck = new JCheckBox("LIMIT: ");
	/**
	 * heckBox for applying a "SORT-BY"-filter for the SPARQL query generator
	 */
	public static JCheckBox sortCheck = new JCheckBox("SORT BY: ");
	/**
	 * TextField to define the "LIMIT"-filter value
	 */
	public static JTextField limTxtField = new JTextField("Limit eintragen");
	/**
	 * ComboBox for some "SORT-BY" possibilities
	 */
	public static JComboBox<String> sortBox;
	private static ResultSet result;

	/**
	 * Constructor for building up the main gui frame
	 */
	public GUI() {
		onClickListener = new OnClickListener();
		JScrollPane scrollPane = new JScrollPane(mainPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
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

		importerBase = new ImporterBase(impLocation, mappingLocation);
		exporterBase = new ExporterBase(expLocation, mappingLocation);
	}

	private void buildConfigPanel() {
		JButton confBttn = new JButton("Save!");
		confBttn.setActionCommand("confBttn");
		configPanel.setLayout(new FlowLayout());
		configPanel.add(new JLabel("Dataset Location: "));
		configPanel.add(confTxtField);
		configPanel.add(confBttn);
		confBttn.addActionListener(onClickListener);
	}

	/**
	 * Configuration button click method: stores the new value of Sparql endpoint in
	 * dsLocation
	 */
	public static void actConfig() {
		dsLocation = confTxtField.getText().toString();
	}

	private void buildImportPanel() {
		JButton impBttn = new JButton("Import!");
		impBttn.setActionCommand("impBttn");
		importPanel.setLayout(new FlowLayout());
		importPanel.add(new JLabel("Import Datei: "));
		importPanel.add(impTxtField);
		importPanel.add(impBttn);
		impBttn.addActionListener(onClickListener);
	}

	/**
	 * Import button click method calls Importer start method
	 */
	public static void actImport() {
		impLocation = impTxtField.getText().toString();
		try {
			importerBase.doImport();
		} catch (Exception e) {
			System.err.println(e);
			JOptionPane.showMessageDialog(frame, "Fehler beim Importieren (transformieren)");
			return;
		}
		
		String[] parts = impLocation.split("/");
		String name = parts[parts.length - 1].split("\\.")[0];
		String graphName = "<" + dsLocation + "data/" + name + ">";
		String updateString = "  LOAD <file:" + ImporterBase.getRdfOutputPath() + ">";
		Update_Execute.executeUpdate(frame, updateString, dsLocation);
		String queryString = "SELECT ?s ?newS ?p ?o ?next WHERE { ?s ?p ?o . OPTIONAL {?s <http://eatld.et.tu-dresden.de/mso/comosUid> ?newS } . OPTIONAL {?o <http://eatld.et.tu-dresden.de/mso/comosUid> ?next }}";
		result = Query_Execute.executeQuery(dsLocation, queryString, frame);
		List<QuerySolution> list = ResultSetFormatter.toList(result);

		HashMap<String, String> hMap = new HashMap<String, String>();
		String newID = "";
		for(QuerySolution sol : list) {
			
			try {
				newID = sol.get("?newS").toString();
			}catch(NullPointerException e) {
				newID = String.valueOf(UUID.randomUUID());
			}
			if(!hMap.containsKey(sol.get("?s").toString())) hMap.put(sol.get("?s").toString(), newID);
		}
		
		String allResults = "";
		for (QuerySolution sol : list) {
			RDFNode node = sol.get("?o");
			String obj;
			if (node.isLiteral())
				obj = "\"" + node.toString() + "\"";
			else {
				if(node.asNode().isBlank()) {
					if(sol.get("?next") == null) obj = "<" + hMap.get(sol.get("?o").toString()) + ">";
					else obj = "<" + sol.get("?next") + ">";
				}
				else obj = "<" + node.toString() + ">";
			}
			
			allResults += "<" + hMap.get(sol.get("?s").toString()) + "> <" + sol.get("?p") + "> " + obj + " . \n";
		}

		try {
			FileOutputStream outStream = new FileOutputStream(
					new File("./UserInterface/src/main/resources/newTriples.nt")); // helper file
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

		updateTabs();
	}

	private void buildExportPanel() {
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

	/**
	 * Export button click method calls Exporter start method
	 */
	public static void actExport() {
		expLocation = expTxtField.getText().toString();
		String outType = expBox.getSelectedItem().toString();
		String graph = graphBox.getSelectedItem().toString();
		exporterBase.doExport(dsLocation, outType, expLocation, graph, frame);
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

	private void buildCustomizePanel() {
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
	 * add Filter button click method adds new line for Sparql-Select-conditions
	 */
	public static void actAddFilter() {
		filterNumber++;
		buildQueryPanel();
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
		buildQueryPanel();
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

	private static void buildTablePanel() {
		tablePanel.add(new JLabel("Ergebnis: (Zeilen und Spaltennummern beginnen mit \"0\")"));
		tablePanel.setLayout(new FlowLayout());

		DefaultTableModel tableModel = new DefaultTableModel(
				new Object[] { "number", "subject1", "predicate1", "object1" }, 10);
		table = new JTable(tableModel) {
			/**
			 * Auto generated default serial ID
			 */
			private static final long serialVersionUID = 1L;

			public String getToolTipText(MouseEvent e) {
				int row = rowAtPoint(e.getPoint());
				int column = columnAtPoint(e.getPoint());

				Object value = getValueAt(row, column);
				return value == null ? null : value.toString();
			}
		};
		;
		table.setPreferredScrollableViewportSize(new Dimension(800, 160));
		table.setEnabled(false);
		table.getTableHeader().setReorderingAllowed(false);
		tablePanel.add(new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED));
	}

	private void buildChangePanel() {
		assumeBttn.addActionListener(onClickListener);
		assumeBttn.setActionCommand("assumeBttn");
		changePanel.add(rowTxtField);
		changePanel.add(colTxtField);
		changePanel.add(newTxtField);
		changePanel.add(assumeBttn);
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

	/**
	 * adds a Triple to the graph
	 */
	public static void actInsertNew() {
		String insertString = " INSERT DATA { GRAPH " + graphBox.getSelectedItem().toString() + " { "
				+ newSubTxtField.getText() + " " + newPreTxtField.getText() + " " + newObjTxtField.getText() + " } }";

		Update_Execute.executeUpdate(frame, insertString, dsLocation);
	}

	/**
	 * deletes the Triple from the graph
	 */
	public static void actDeleteTriple() {
		String deleteString = " DELETE DATA { GRAPH " + graphBox.getSelectedItem().toString() + " { "
				+ newSubTxtField.getText() + " " + newPreTxtField.getText() + " " + newObjTxtField.getText() + " } }";

		Update_Execute.executeUpdate(frame, deleteString, dsLocation);

	}

	private static void updateTabs() {

		tabList.clear();
		for (int i = tabbedPane.getTabCount() - 1; i > 0; i--) {
			tabbedPane.remove(i);
		}
		String[] searchFor = { "<http://eatld.et.tu-dresden.de/mso/Site>",
				"<http://eatld.et.tu-dresden.de/mso/ProcessCell>", "<http://eatld.et.tu-dresden.de/mso/Unit>" }; // hierarchical

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

	private static void searchInGraph(String graph, String obj, ArrayList<String> ident, ArrayList<String> label) {
		String queryString = "SELECT ?s ?label WHERE { GRAPH " + graph
				+ " {{ ?s <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> " + obj
				+ " }. {?s <http://www.w3.org/2000/01/rdf-schema#comment> ?label }}}";
		result = Query_Execute.executeQuery(dsLocation, queryString, frame);
		List<QuerySolution> list = ResultSetFormatter.toList(result);
		for (QuerySolution sol : list) {
			String sub = sol.get("?s").toString();
			String lbl = sol.get("?label").toString();
/*			if (sub.contains("/"))
				ident.add("<" + sub + ">");
			else
				ident.add("<_:" + sub + ">"); */
			
			ident.add("<" + sub + ">");
			label.add(lbl);
		}
	}

	private static void initPlusTab() {
		plusTab = new JPanel();
		plusTab.setLayout(new FlowLayout());
		String[] moduleType = { "Werk", "Anlage" };
		newModuleBox = new JComboBox<String>(moduleType);
		JButton assumeNewBttn = new JButton("Neu erstellen");
		assumeNewBttn.setActionCommand("assumeNewCell");
		assumeNewBttn.addActionListener(onClickListener);
		plusTab.add(newModuleName);
		plusTab.add(newModuleBox);
		plusTab.add(assumeNewBttn);
	}

	/**
	 * new process cell is added to the graph
	 */
	public static void actNewCell() {
		String name = newModuleName.getText();
		String graphName = "<" + dsLocation + "data/" + name + ">";
		String updateString = "CREATE GRAPH " + graphName;
		Update_Execute.executeUpdate(frame, updateString, dsLocation);

		updateString = " INSERT DATA { GRAPH " + graphName + " { ";
		updateString += "[] ";
		updateString += "<http://www.w3.org/1999/02/22-rdf-syntax-ns#type> "; // TODO
		if (newModuleBox.getSelectedItem().toString().equals("Werk"))
			updateString += "<http://eatld.et.tu-dresden.de/mso/Site> ";
		else
			updateString += "<http://eatld.et.tu-dresden.de/mso/ProcessCell> ";

		updateString += " ; <http://www.w3.org/2000/01/rdf-schema#comment> \"0\"}}";

		Update_Execute.executeUpdate(frame, updateString, dsLocation);
		graphList.add(graphName);

		updateTabs();

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

class GlassPane extends JComponent {

	/**
	 * Auto generated default serial ID
	 */
	private static final long serialVersionUID = 1L;

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2D = (Graphics2D) g.create();

		if (GUI.tabbedPane.getSelectedIndex() != 0
				&& GUI.tabList.get(GUI.tabbedPane.getSelectedIndex() - 1).layer1.isVisible()) {
			for (JButton start : GUI.tabList.get(GUI.tabbedPane.getSelectedIndex() - 1).connMap.keySet()) {
				for (JButton dest : GUI.tabList.get(GUI.tabbedPane.getSelectedIndex() - 1).connMap.get(start)) {
					Window anch = SwingUtilities.getWindowAncestor(start);

					int x1 = (int) (SwingUtilities.convertPoint(start.getParent(), start.getLocation(), anch).x
							+ start.getVisibleRect().getWidth() / 2);
					int y1 = (int) (SwingUtilities.convertPoint(start.getParent(), start.getLocation(), anch).y
							+ start.getVisibleRect().getHeight());
					int x2 = (int) (SwingUtilities.convertPoint(dest.getParent(), dest.getLocation(), anch).x
							+ dest.getVisibleRect().getWidth() / 2);
					int y2 = SwingUtilities.convertPoint(dest.getParent(), dest.getLocation(), anch).y;

					g2D.drawLine(x1, y1, x2, y2);
				}
			}
		}
	}
}