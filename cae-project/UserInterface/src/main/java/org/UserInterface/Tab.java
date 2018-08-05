package org.UserInterface;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import utilities.Query_Execute;
import utilities.Update_Execute;

/**
 * implements the view to the plant topology and some functions for changing and
 * combining it
 */
public class Tab {

	/**
	 * lowest layer which will be placed on the tappedPane in the main frame
	 */
	public JScrollPane scrollPane;
	private JPanel mainPanel = new JPanel();
	private Tab tab;
	private String graph;
	/**
	 * lowest layer for button-tree-structure
	 */
	public JPanel layer1 = new JPanel();
	private JPanel upperPanel = new JPanel();
	private JTree tree;
	private DefaultMutableTreeNode topNode;
	private JPanel layer11 = new JPanel();
	private HashMap<JButton, BttnDataHelper> bttnMap = new HashMap<JButton, BttnDataHelper>();
	private HashMap<DefaultMutableTreeNode, TreeDataHelper> treeMap = new HashMap<DefaultMutableTreeNode, TreeDataHelper>();
	private String allResults;
	private ArrayList<String> subPassed;
	private String[] searchForPredicate = { "<http://eatld.et.tu-dresden.de/mso/hasProcessCell>",
			"<http://eatld.et.tu-dresden.de/mso/has>", "<http://eatld.et.tu-dresden.de/mso/hasUnit>",
			"<http://eatld.et.tu-dresden.de/mso/hasEquipment>" };

	/**
	 * HashMap that stores all visible buttons (modules/devices) and the properly
	 * buttons in the next layer of the plant topology
	 */
	public HashMap<JButton, ArrayList<JButton>> connMap = new HashMap<JButton, ArrayList<JButton>>();
	private MouseListener mouseListener;
	private JPopupMenu popUpMenu;
	private Component lastRightClick;

	/**
	 * Constructor for a new Tab
	 * 
	 * @param label
	 *            unambiguously identifier for a button (module/device)
	 * @param ident
	 *            name of the button (module/device)
	 * @param type
	 *            type of the first module
	 * @param graph
	 *            dataset graph uri
	 */
	public Tab(ArrayList<String> label, ArrayList<String> ident, String type, String graph) {

		this.tab = this;
		this.graph = graph;
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		scrollPane = new JScrollPane(mainPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		layer1.setLayout(new BoxLayout(layer1, BoxLayout.X_AXIS));
		layer11.setLayout(new BoxLayout(layer11, BoxLayout.Y_AXIS));

		initUpperPanel();
		initPopUp();
		initMouseListener();
		initLayer1(label, ident, type);
		initTree(label, ident, type);

		mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
		mainPanel.add(upperPanel);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 40)));
		mainPanel.add(layer1);
		mainPanel.add(tree);

		tree.setVisible(false);

	}

	private void initUpperPanel() {
		JButton switchBttn = new JButton("Ansicht wechseln");
		switchBttn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {
				tree.setVisible(!tree.isVisible());
				layer1.setVisible(!layer1.isVisible());
				mainPanel.revalidate();
			}

		});
		JButton discardBttn = new JButton("Markierungen verwerfen");
		discardBttn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {
				GUI.newConnMap.clear();
			}

		});
		upperPanel.setLayout(new BoxLayout(upperPanel, BoxLayout.X_AXIS));
		upperPanel.add(switchBttn);
		upperPanel.add(discardBttn);
	}

	private void initTree(ArrayList<String> label, ArrayList<String> ident, String type) {
		for (int i = 0; i < label.size(); i++) {
			topNode = new DefaultMutableTreeNode(label.get(i));
			treeMap.put(topNode, new TreeDataHelper(ident.get(i), type, tab, false));
			tree = new JTree(topNode);
			tree.setToggleClickCount(1);
			tree.addMouseListener(mouseListener);
		}
	}

	private void initLayer1(ArrayList<String> label, ArrayList<String> ident, String type) {
		for (int i = 0; i < label.size(); i++) {
			JButton bttn = new JButton(label.get(i));
			// module1.setOpaque(false);
			// module1.setContentAreaFilled(false);
			bttn.setBackground(getColor(type));
			bttn.addMouseListener(mouseListener);
			bttn.setMargin(new Insets(20, 20, 20, 20));
			bttn.setAlignmentX(Component.CENTER_ALIGNMENT);
			layer11.setAlignmentY(Component.TOP_ALIGNMENT);
			layer11.add(bttn);
			bttnMap.put(bttn, new BttnDataHelper(ident.get(i), type, layer11, tab, false));
		}
		layer1.add(layer11);
	}

	private Color getColor(String type) {
		if (type.equals("<http://eatld.et.tu-dresden.de/mso/ProcessCell>"))
			return Color.BLUE;
		else if (type.equals("<http://eatld.et.tu-dresden.de/mso/Unit>"))
			return Color.GREEN;
		else if (type.equals("<http://eatld.et.tu-dresden.de/mso/Site>"))
			return Color.MAGENTA;
		else if (type.equals("<http://eatld.et.tu-dresden.de/mso/Armature>"))
			return Color.CYAN;
		else if (type.equals("<http://eatld.et.tu-dresden.de/mso/Equipment>"))
			return Color.GRAY;
		else if (type.equals("<http://eatld.et.tu-dresden.de/mso/Valve>"))
			return Color.YELLOW;
		else if (type.equals("<http://eatld.et.tu-dresden.de/mso/ConnectionObject>"))
			return Color.ORANGE;
		else
			return Color.WHITE;
	}

	private void initPopUp() {
		popUpMenu = new JPopupMenu();
		JMenuItem menuItem = new JMenuItem("Dieses Modul markieren...");
		menuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {
				GUI.newConnMap.put(lastRightClick, tab);
			}

		});
		popUpMenu.add(menuItem);

		menuItem = new JMenuItem("Alle markierten hier hinzufügen");
		menuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {
				String type = "";
				String addSub = "";
				String sub = "";
				allResults = "";

				for (Object markedObj : GUI.newConnMap.keySet()) {
					String oldGraph = GUI.newConnMap.get(markedObj).graph;
					if (markedObj.getClass() == JButton.class) {
						type = GUI.newConnMap.get(markedObj).bttnMap.get(markedObj).type;
						addSub = GUI.newConnMap.get(markedObj).bttnMap.get(markedObj).ident;
					} else {
						type = GUI.newConnMap.get(markedObj).treeMap
								.get(((JTree) markedObj).getLastSelectedPathComponent()).type;
						addSub = GUI.newConnMap.get(markedObj).treeMap
								.get(((JTree) markedObj).getLastSelectedPathComponent()).ident;
					}

					if (!tree.isVisible()) {
						sub = bttnMap.get(lastRightClick).ident;
					} else {
						sub = treeMap.get(((JTree) lastRightClick).getLastSelectedPathComponent()).ident;
					}

					String[] parts = type.split("/");
					String newType = "";
					int i;
					for (i = 0; i < parts.length - 1; i++) {
						newType += parts[i] + "/";
					}
					newType += "has" + parts[i];
					String updateString = "INSERT DATA { GRAPH " + graph + " { " + sub + " " + newType + " " + addSub
							+ " }}";
					Update_Execute.executeUpdate(GUI.frame, updateString, GUI.dsLocation);
					subPassed = new ArrayList<String>();
					findAllDependencies(addSub, oldGraph);
					try {
						FileOutputStream outStream = new FileOutputStream(
								new File("./UserInterface/src/main/resources/newTriples.nt")); // helper file
						PrintWriter p = new PrintWriter(outStream);
						p.write(allResults);
						p.close();
					} catch (FileNotFoundException e) {
					}
					updateString = "LOAD <file:./UserInterface/src/main/resources/newTriples.nt> INTO GRAPH " + graph;
					Update_Execute.executeUpdate(GUI.frame, updateString, GUI.dsLocation);
				}
				GUI.newConnMap.clear();
				if (lastRightClick.getClass() == JButton.class) {
					deflateBttn((JButton) lastRightClick);
					inflateBttn((JButton) lastRightClick);
				} else {
					DefaultMutableTreeNode marked = (DefaultMutableTreeNode) ((JTree) lastRightClick)
							.getLastSelectedPathComponent();
					treeMap.get(marked).inflated = false;
					inflateTree(lastRightClick);
				}
			}
		});
		popUpMenu.add(menuItem);

		menuItem = new JMenuItem("Modul aus vorheriger Ebene entfernen");
		menuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {
				String sub = "";
				String type = "";
				String parentSub = "";
				allResults = "";
				if (lastRightClick.getClass() == JButton.class) {
					sub = bttnMap.get((JButton) lastRightClick).ident;
					type = bttnMap.get(lastRightClick).type;
					for (JButton source : connMap.keySet()) {
						for (JButton dest : connMap.get(source)) {
							if (lastRightClick == dest) {
								parentSub = bttnMap.get(source).ident;
							}
						}
					}
				} else {
					sub = treeMap.get(((JTree) lastRightClick).getLastSelectedPathComponent()).ident;
					DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) ((DefaultMutableTreeNode) (tree
							.getLastSelectedPathComponent())).getParent();
					parentSub = treeMap.get(parentNode).ident;
					type = treeMap.get(parentNode).type;
				}

				String[] parts = type.split("/");
				type = parts[parts.length - 1];

				String queryString = "SELECT ?sub WHERE { GRAPH " + graph + " { ?sub ?x " + sub + " }}";
				ResultSet result = Query_Execute.executeQuery(GUI.dsLocation, queryString, GUI.frame);

				if (ResultSetFormatter.toList(result).size() > 1) {
					allResults = parentSub + " <http://eatld.et.tu-dresden.de/mso/has" + type + " " + sub + ".";
				} else {
					subPassed = new ArrayList<String>();
					findAllDependencies(sub, graph);
				}

				try {
					FileOutputStream outStream = new FileOutputStream(
							new File("./UserInterface/src/main/resources/newTriples.nt")); // helper file
					PrintWriter p = new PrintWriter(outStream);
					p.write(allResults);
					p.close();
				} catch (FileNotFoundException e) {
				}

				String updateString = "DELETE {?s ?p ?o} WHERE {?s ?p ?o}";
				Update_Execute.executeUpdate(GUI.frame, updateString, GUI.dsLocation);

				updateString = "LOAD <file:./UserInterface/src/main/resources/newTriples.nt>";
				Update_Execute.executeUpdate(GUI.frame, updateString, GUI.dsLocation);

				updateString = "DELETE { GRAPH " + graph + " {?s ?p ?o} } WHERE {  { ?s ?p ?o }}";
				Update_Execute.executeUpdate(GUI.frame, updateString, GUI.dsLocation);

				updateString = "DELETE {?s ?p ?o} WHERE {?s ?p ?o}";
				Update_Execute.executeUpdate(GUI.frame, updateString, GUI.dsLocation);

				if (lastRightClick.getClass() == JButton.class) {
					deflateBttn((JButton) layer11.getComponent(0));
					inflateBttn((JButton) layer11.getComponent(0));
				} else {
					DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
					model.removeNodeFromParent(
							(DefaultMutableTreeNode) ((JTree) lastRightClick).getLastSelectedPathComponent());
				}
			}
		});
		popUpMenu.add(menuItem);
	}

	private void findAllDependencies(String sub, String graph) {
		String queryString = "SELECT ?p ?o WHERE{ GRAPH " + graph + " { " + sub + " ?p ?o}}";

		ResultSet result = Query_Execute.executeQuery(GUI.dsLocation, queryString, GUI.frame);
		List<QuerySolution> list = ResultSetFormatter.toList(result);
		subPassed.add(sub);
		for (QuerySolution sol : list) {
			String data0 = sol.toString();
			String[] parts = data0.split("\\?o = ");
			parts = parts[1].split(" \\)"); // " )"
			String obj = parts[0];
			if (obj.charAt(0) == '<' && !subPassed.contains(obj) && !sub.equals(obj))
				findAllDependencies(obj, graph);

			allResults += sub + "	" + "<" + sol.get("?p") + ">" + "	" + obj + " . \n";
		}
	}

	private void initMouseListener() {
		mouseListener = new MouseListener() {

			public void mouseReleased(MouseEvent me) {
				if (SwingUtilities.isLeftMouseButton(me)) {
					moduleClicked(me.getComponent());
				} else if (SwingUtilities.isRightMouseButton(me)) {
					lastRightClick = me.getComponent();
					if (lastRightClick.getClass() == JButton.class) {
						String type = bttnMap.get(lastRightClick).type;
						if (type.equals("<http://eatld.et.tu-dresden.de/mso/ProcessCell>")
								|| type.equals("<http://eatld.et.tu-dresden.de/mso/Unit>")
								|| type.equals("<http://eatld.et.tu-dresden.de/mso/Site>"))
							popUpMenu.show(lastRightClick, me.getX(), me.getY());
					} else {
						String type = treeMap.get(((JTree) lastRightClick).getLastSelectedPathComponent()).type;
						if (type.equalsIgnoreCase("<http://eatld.et.tu-dresden.de/mso/ProcessCell>")
								|| type.equals("<http://eatld.et.tu-dresden.de/mso/Unit>")
								|| type.equals("<http://eatld.et.tu-dresden.de/mso/Site>"))
							popUpMenu.show(lastRightClick, me.getX(), me.getY());
					}

				}
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// Auto-generated method stub

			}
		};
	}

	private void moduleClicked(Component source) {
		if (source.getClass() == JButton.class) {
			if (bttnMap.get(source).inflated) {
				deflateBttn((JButton) source);
			} else {
				inflateBttn((JButton) source);
			}
		} else {
			inflateTree(source);
			mainPanel.revalidate();
		}
	}

	private void inflateTree(Component source) {

		TreePath tp = tree.getSelectionPath();
		Object lastComponent = tp.getLastPathComponent();

		if (treeMap.get(lastComponent).inflated == true) {
			treeMap.get(lastComponent).inflated = false;
			return;
		}

		String sub = treeMap.get(lastComponent).ident;
		for (String pre : searchForPredicate) {
			String queryString = "SELECT ?o ?label ?type WHERE { GRAPH " + graph + " { " + sub + " " + pre
					+ " ?o . ?o <http://www.w3.org/2000/01/rdf-schema#comment> " + " ?label . ?o a ?type }}";

			ResultSet result = Query_Execute.executeQuery(GUI.dsLocation, queryString, GUI.frame);
			List<QuerySolution> resList = ResultSetFormatter.toList(result);
			for (QuerySolution sol : resList) {

				String type = "<" + sol.get("?type") + ">";
				String ident = "<" + sol.get("?o").toString() + ">";
				String label = sol.get("?label").toString();

				DefaultMutableTreeNode node = new DefaultMutableTreeNode(label);
				treeMap.put(node, new TreeDataHelper(ident, type, tab, false));
				((DefaultMutableTreeNode) lastComponent).add(node);
			}
		}
		tree.expandPath(tp);
		treeMap.get(lastComponent).inflated = true;
	}

	private void inflateBttn(JButton sourceBttn) {

		JPanel sourcePanel = bttnMap.get(sourceBttn).parentPanel;

		JPanel layer = new JPanel();
		layer.setLayout(new BoxLayout(layer, BoxLayout.X_AXIS));

		ArrayList<JButton> destBttns = new ArrayList<JButton>();

		String sub = bttnMap.get(sourceBttn).ident;
		for (String pre : searchForPredicate) {
			String queryString = "SELECT ?o ?label ?type WHERE { GRAPH " + graph + " { " + sub + " " + pre
					+ " ?o . ?o <http://www.w3.org/2000/01/rdf-schema#comment> ?label . ?o a ?type}}";
			ResultSet result = Query_Execute.executeQuery(GUI.dsLocation, queryString, GUI.frame);
			List<QuerySolution> resList = ResultSetFormatter.toList(result);
			for (QuerySolution sol : resList) {
				String type = "<" + sol.get("?type") + ">";
				String ident = "";

				if (sub.contains("/"))
					ident = "<" + sol.get("?o").toString() + ">";
				else
					ident = "<_:" + sol.get("?o").toString() + ">";

				String label = sol.get("?label").toString();

				JPanel subLayer = new JPanel();
				JButton button = new JButton(label);
				// button.setOpaque(false);
				// button.setContentAreaFilled(false);
				// String type = newBttnMap.get(name);
				button.setBackground(getColor(type));
				button.addMouseListener(mouseListener);
				button.setMargin(new Insets(20, 20, 20, 20));
				button.setAlignmentX(Component.CENTER_ALIGNMENT);
				destBttns.add(button);
				subLayer.setLayout(new BoxLayout(subLayer, BoxLayout.Y_AXIS));
				subLayer.setAlignmentY(Component.TOP_ALIGNMENT);
				subLayer.add(button);
				bttnMap.put(button, new BttnDataHelper(ident, type, subLayer, tab, false));
				layer.add(subLayer);
				layer.add(Box.createRigidArea(new Dimension(40, 0)));

			}
		}

		bttnMap.get(sourceBttn).inflated = true;
		sourcePanel.add(Box.createRigidArea(new Dimension(0, 100)));
		sourcePanel.add(layer);
		connMap.put(sourceBttn, destBttns);

		layer1.revalidate();

	}

	private void deflateBttn(JButton sourceBttn) {
		connMapCleaner(sourceBttn);
		connMap.remove(sourceBttn);
		JPanel sourcePanel = bttnMap.get(sourceBttn).parentPanel;
		int size = sourcePanel.getComponentCount();
		for (int i = size - 1; i > 0; i--) {
			sourcePanel.remove(i);
		}
		bttnMap.get(sourceBttn).inflated = false;
		layer1.revalidate();
	}

	private void connMapCleaner(JButton sourceBttn) {
		ArrayList<JButton> btnList = connMap.get(sourceBttn);
		if (btnList != null) {
			for (JButton btn : btnList) {
				connMapCleaner(btn);
				connMap.remove(btn);
				bttnMap.remove(btn);
			}
		}
	}
}