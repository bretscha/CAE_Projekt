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
	private String name;
	private String label;
	private String type;
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
	 * @param name
	 *            name of the button (module/device)
	 * @param type type of the first module
	 * @param graph dataset graph uri
	 */
	public Tab(String label, String name, String type, String graph) {

		this.name = name;
		this.label = label;
		this.type = type;
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
		initLayer1();
		initTree();

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
		JButton discardBttn = new JButton("geplante Änderungen verwerfen");
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

	private void initTree() {
		topNode = new DefaultMutableTreeNode(name);
		treeMap.put(topNode, new TreeDataHelper(name, type, tab));
		tree = new JTree(topNode);
		tree.setToggleClickCount(1);
		tree.addMouseListener(mouseListener);
	}

	private void initLayer1() {
		JButton module1 = new JButton(name);
		//module1.setOpaque(false);
		//module1.setContentAreaFilled(false);
		module1.setBackground(getColor(type));
		module1.addMouseListener(mouseListener);
		module1.setMargin(new Insets(20, 20, 20, 20));
		module1.setAlignmentX(Component.CENTER_ALIGNMENT);
		layer11.setAlignmentY(Component.TOP_ALIGNMENT);
		layer11.add(module1);
		layer1.add(layer11);
		bttnMap.put(module1, new BttnDataHelper(label, type, layer11, tab, false));

	}

	private Color getColor(String type) {
		if(type.equals("<http://eatld.et.tu-dresden.de/mso/ProcessCell>")) return Color.BLUE;
		else if(type.equals("<http://eatld.et.tu-dresden.de/mso/Unit>")) return Color.GREEN;
		else if(type.equals("<http://eatld.et.tu-dresden.de/mso/SubPlant>")) return Color.MAGENTA; //TODO stimmt das?
		else if(type.equals("<http://eatld.et.tu-dresden.de/mso/Unit>")) return Color.CYAN;
		else if(type.equals("<http://eatld.et.tu-dresden.de/mso/Equipment>")) return Color.GRAY;
		//hier noch den rest.... //TODO alles da?
		else return Color.WHITE;
	}

	private void initPopUp() {
		popUpMenu = new JPopupMenu();
		JMenuItem menuItem = new JMenuItem("Diese Komponente hinzufügen...");
		menuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {
/*				if (lastRightClick.getClass() == JButton.class) {
					GUI.newConnMap.put(((JButton) lastRightClick).getText(),
							GUI.tabbedPane.getTitleAt(GUI.tabbedPane.getSelectedIndex()));
				} else {
					GUI.newConnMap.put(
							((DefaultMutableTreeNode) ((JTree) lastRightClick).getSelectionPath()
									.getLastPathComponent()).toString(),
							GUI.tabbedPane.getTitleAt(GUI.tabbedPane.getSelectedIndex()));
				}*/
				GUI.newConnMap.put(lastRightClick, tab);
			}

		});
		popUpMenu.add(menuItem);

		menuItem = new JMenuItem("Alle ausgewählten hier hinzufügen");
		menuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {
				String type = "";
				String sub = "";
				allResults = "";
				
				for (Object key : GUI.newConnMap.keySet()) {
					if (key.getClass() == JButton.class) {
						type = GUI.newConnMap.get(key).bttnMap.get(key).type;
						sub = ((JButton) key).getText();
					} else {
						type = treeMap.get(key).type;
						sub = treeMap.get(key).name;
					}
					String[] parts = type.split("/");//TODO
					String newType = "";
					int i;
					for(i=0; i<parts.length-1; i++) {
						newType += parts[i] + "/";
					}
					newType += "has" + parts[i];
					String updateString = "INSERT DATA { GRAPH " + graph + " { " + ((JButton) lastRightClick).getText() + " " + newType + " " + sub + " }}"; //TODO testen
					Update_Execute.executeUpdate(GUI.frame, updateString, GUI.dsLocation);
					findAll(sub);
					try {
						FileOutputStream outStream = new FileOutputStream(new File("./UserInterface/src/main/resources/newTriples.csv")); //helper file
						PrintWriter p = new PrintWriter(outStream);
						p.write(allResults);
						p.close();
					} catch (FileNotFoundException e) {
					} 
					updateString = "LOAD <file:./UserInterface/src/main/resources/newTriples.csv> INTO GRAPH " + graph;
					Update_Execute.executeUpdate(GUI.frame, updateString, GUI.dsLocation);
				}
			}
/*				if (lastRightClick.getClass() == JButton.class) {
					allResults = "";
					String graphName = GUI.tabbedPane.getTitleAt(GUI.tabbedPane.getSelectedIndex());
				} else {
					System.out.println(((DefaultMutableTreeNode) ((JTree) lastRightClick).getSelectionPath()
							.getLastPathComponent()).toString());
				}
			}*/

		});
		popUpMenu.add(menuItem);

		menuItem = new JMenuItem("Verbindung zu vorheriger Ebene entfernen");
		menuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {

			}

		});
		popUpMenu.add(menuItem);

		menuItem = new JMenuItem("Verbindung zu folgender Ebene entfernen");
		menuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {

			}

		});
		popUpMenu.add(menuItem);

/*		menuItem = new JMenuItem("Mehr Informationen anzeigen");
		menuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {
				if (lastRightClick.getClass() == JButton.class) {
					System.out.println(((JButton) lastRightClick).getText());
					System.out.println(GUI.tabbedPane.getTitleAt(GUI.tabbedPane.getSelectedIndex()));
				} else {
					System.out.println(((DefaultMutableTreeNode) ((JTree) lastRightClick).getSelectionPath()
							.getLastPathComponent()).toString());
				}

			}

		});
		popUpMenu.add(menuItem);*/

	}

	protected void findAll(String sub) {
		String queryString = "SELECT ?p ?o WHERE { " + sub + " ?p ?o}";

		ResultSet result = Query_Execute.executeQuery(GUI.dsLocation, queryString, GUI.frame);
		List<QuerySolution> list = ResultSetFormatter.toList(result);
		for(QuerySolution sol : list) {
			String obj = "< " + sol.get("?o").toString() + ">";
			findAll(obj);
			allResults += sub + "," + sol.get("?p") + "," + obj + "\n";
		}
	}

	private void initMouseListener() {
		mouseListener = new MouseListener() {

			public void mouseReleased(MouseEvent me) {
				if (SwingUtilities.isLeftMouseButton(me)) {
					moduleClicked(me.getComponent());
				} else if (SwingUtilities.isRightMouseButton(me)) {
					lastRightClick = me.getComponent();
					popUpMenu.show(lastRightClick, me.getX(), me.getY());
				}
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				//Auto-generated method stub

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

		String sub = lastComponent.toString();
		String[] searchFor = {"<http://eatld.et.tu-dresden.de/mso/hasPlant>", "<http://eatld.et.tu-dresden.de/mso/hasSubPlant>", "<http://eatld.et.tu-dresden.de/mso/hasUnit>", "<http://eatld.et.tu-dresden.de/mso/hasEquipment>" };
		for (String pre : searchFor) {
			String queryString = "SELECT ?o WHERE { GRAPH " + graph + " { " + sub + " " + pre + " ?o }}";
			ResultSet result = Query_Execute.executeQuery(GUI.dsLocation, queryString, GUI.frame);
			List<QuerySolution> resList = ResultSetFormatter.toList(result);
			for(QuerySolution sol : resList) {
				String type = "";
				if(pre.equals("<http://eatld.et.tu-dresden.de/mso/hasUnit>")) type = "<http://eatld.et.tu-dresden.de/mso/Unit>";
				else if(pre.equals("<http://eatld.et.tu-dresden.de/mso/hasEquipment>")) type = "<http://eatld.et.tu-dresden.de/mso/Equipment>";
				else if(pre.equals("<http://eatld.et.tu-dresden.de/mso/hasPlant>")) type = "<http://eatld.et.tu-dresden.de/mso/Plant>";
				else if(pre.equals("<http://eatld.et.tu-dresden.de/mso/hasSubPlant>")) type = "<http://eatld.et.tu-dresden.de/mso/Sub_Plant>"; //TODO checken

				String name = "<" + sol.get("?o").toString() + ">";
				DefaultMutableTreeNode node = new DefaultMutableTreeNode(name);
				treeMap.put(node, new TreeDataHelper(name, type, tab));
				((DefaultMutableTreeNode) lastComponent).add(node);
			}
		}
	}

	private void inflateBttn(JButton sourceBttn) {

		bttnMap.get(sourceBttn);
		JPanel sourcePanel = bttnMap.get(sourceBttn).parentPanel;

		JPanel layer = new JPanel();
		layer.setLayout(new BoxLayout(layer, BoxLayout.X_AXIS));

		String sub = sourceBttn.getText();
		String[] searchFor = {"<http://eatld.et.tu-dresden.de/mso/hasPlant>", "<http://eatld.et.tu-dresden.de/mso/hasSubPlant>", "<http://eatld.et.tu-dresden.de/mso/hasUnit>", "<http://eatld.et.tu-dresden.de/mso/hasEquipment>" };
		HashMap<String, String> newBttnMap = new HashMap<String, String>();
		for (String pre : searchFor) {
			String queryString = "SELECT ?o WHERE { GRAPH " + graph + " { " + sub + " " + pre + " ?o }}";
			ResultSet result = Query_Execute.executeQuery(GUI.dsLocation, queryString, GUI.frame);
			List<QuerySolution> resList = ResultSetFormatter.toList(result);
			for(QuerySolution sol : resList) {
				String type = "";
				if(pre.equals("<http://eatld.et.tu-dresden.de/mso/hasUnit>")) type = "<http://eatld.et.tu-dresden.de/mso/Unit>";
				else if(pre.equals("<http://eatld.et.tu-dresden.de/mso/hasEquipment>")) type = "<http://eatld.et.tu-dresden.de/mso/Equipment>";
				else if(pre.equals("<http://eatld.et.tu-dresden.de/mso/hasPlant>")) type = "<http://eatld.et.tu-dresden.de/mso/Plant>";
				else if(pre.equals("<http://eatld.et.tu-dresden.de/mso/hasSubPlant>")) type = "<http://eatld.et.tu-dresden.de/mso/Sub_Plant>"; //TODO checken
				
				newBttnMap.put("<" + sol.get("?o").toString() + ">", type);	
			}
		}

		ArrayList<JButton> destBttns = new ArrayList<JButton>();

		for (String name : newBttnMap.keySet()) {
			JPanel subLayer = new JPanel();
			JButton button = new JButton(name);
			//button.setOpaque(false);
			//button.setContentAreaFilled(false);
			String type = newBttnMap.get(name);
			button.setBackground(getColor(type));
			button.addMouseListener(mouseListener);
			button.setMargin(new Insets(20, 20, 20, 20));
			button.setAlignmentX(Component.CENTER_ALIGNMENT);
			destBttns.add(button);
			subLayer.setLayout(new BoxLayout(subLayer, BoxLayout.Y_AXIS));
			subLayer.setAlignmentY(Component.TOP_ALIGNMENT);
			subLayer.add(button);
			bttnMap.put(button, new BttnDataHelper("label aus result", type, subLayer, tab, false));
			layer.add(subLayer);
			layer.add(Box.createRigidArea(new Dimension(40, 0)));
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

class BttnDataHelper {

	public String label;
	public JPanel parentPanel;
	public boolean inflated;
	public String type;
	public Tab hostTab;

	public BttnDataHelper(String label, String type, JPanel parentPanel, Tab hostTab, boolean inflated) {
		this.label = label;
		this.parentPanel = parentPanel;
		this.hostTab = hostTab;
		this.inflated = inflated;
		this.type = type;
	}
}

class TreeDataHelper {
	
	public String type;
	public String name;
	public Tab hostTab;
	
	public TreeDataHelper(String name, String type, Tab hostTab) {
		this.name = name;
		this.type = type;
		this.hostTab = hostTab;
	}
}