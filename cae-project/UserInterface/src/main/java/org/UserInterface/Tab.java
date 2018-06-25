package org.UserInterface;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
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

public class Tab {

	public JScrollPane scrollPane;
	private JPanel mainPanel = new JPanel();
	private String name;
	private String label;
	public JPanel layer1 = new JPanel();
	private JPanel upperPanel = new JPanel();
	private JTree tree;
	private DefaultMutableTreeNode topNode;
	private JPanel layer11 = new JPanel();
	private HashMap<JButton, DataHelper> bttnMap = new HashMap<JButton, DataHelper>();
	public HashMap<JButton, ArrayList<JButton>> connMap = new HashMap<JButton, ArrayList<JButton>>();
	private MouseListener mouseListener;
	private JPopupMenu popUpMenu;
	private Component lastRightClick;

	public Tab(GUI gui, String label, String name) {

		this.name = name;
		this.label = label;
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
		JButton assumeBttn = new JButton("Änderungen übernehmen");
		assumeBttn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {
				mainPanel.revalidate();
			}

		});
		JButton discardBttn = new JButton("Änderungen verwerfen");
		discardBttn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {
				mainPanel.revalidate();
			}

		});
		upperPanel.setLayout(new BoxLayout(upperPanel, BoxLayout.X_AXIS));
		upperPanel.add(switchBttn);
		upperPanel.add(assumeBttn);
		upperPanel.add(discardBttn);
	}

	private void initTree() {
		topNode = new DefaultMutableTreeNode(name);
		tree = new JTree(topNode);
		tree.setToggleClickCount(1);
		tree.addMouseListener(mouseListener);
	}

	private void initLayer1() {
		JButton module1 = new JButton(name);
		module1.setOpaque(false);
		module1.setContentAreaFilled(false);
		module1.addMouseListener(mouseListener);
		module1.setMargin(new Insets(20, 20, 20, 20));
		module1.setAlignmentX(Component.CENTER_ALIGNMENT);
		layer11.setAlignmentY(Component.TOP_ALIGNMENT);
		layer11.add(module1);
		layer1.add(layer11);
		bttnMap.put(module1, new DataHelper(label, layer11, false));

	}

	private void initPopUp() {
		popUpMenu = new JPopupMenu();
		JMenuItem menuItem = new JMenuItem("Diese Komponente verbinden...");
		menuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {
				
			}

		});
		popUpMenu.add(menuItem);
		
		menuItem = new JMenuItem("... mit dieser Komponente verbinden");
		menuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {
				
			}

		});
		popUpMenu.add(menuItem);
		
		menuItem = new JMenuItem("Verbindung entfernen zu ...");
		menuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {
				
			}

		});
		popUpMenu.add(menuItem);
		
		menuItem = new JMenuItem("... Verbindung zu dieser Komponente entfernen");
		menuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {
				
			}

		});
		popUpMenu.add(menuItem);
		
		menuItem = new JMenuItem("Mehr Informationen anzeigen");
		menuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {
				
			}

		});
		popUpMenu.add(menuItem);
		
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
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		};
	}

	public void moduleClicked(Component source) {
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

		// SPARQL BLA bla siehe inflateBttn
		for (int i = 0; i < 3; i++) {
			DefaultMutableTreeNode node = new DefaultMutableTreeNode("name" + i);
			((DefaultMutableTreeNode) lastComponent).add(node);
		}
	}

	private void inflateBttn(JButton sourceBttn) {

		bttnMap.get(sourceBttn);
		JPanel sourcePanel = bttnMap.get(sourceBttn).parentPanel;

		JPanel layer = new JPanel();
		layer.setLayout(new BoxLayout(layer, BoxLayout.X_AXIS));

		/*
		 * sparql: label:HASallesmögliche:result resutl:hasLabel:label for result... new
		 * Button mit farben!! usw...
		 * 
		 * für jedes Modul in der nächsten schicht ein eigenes Panel anlegen! zuordnung
		 * mit hmap
		 */

		// test:

		int size = 3;

		ArrayList<JButton> destBttns = new ArrayList<JButton>();

		for (int i = 0; i < size; i++) {
			JPanel subLayer = new JPanel();
			JButton button = new JButton("name aus result");
			button.setOpaque(false);
			button.setContentAreaFilled(false);
			button.addMouseListener(mouseListener);
			button.setMargin(new Insets(20, 20, 20, 20));
			button.setAlignmentX(Component.CENTER_ALIGNMENT);
			destBttns.add(button);
			subLayer.setLayout(new BoxLayout(subLayer, BoxLayout.Y_AXIS));
			subLayer.setAlignmentY(Component.TOP_ALIGNMENT);
			subLayer.add(button);
			bttnMap.put(button, new DataHelper("label aus result", subLayer, false));
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

class DataHelper {

	public String label;
	public JPanel parentPanel;
	public boolean inflated;

	public DataHelper(String label, JPanel parentPanel, boolean inflated) {
		this.label = label;
		this.parentPanel = parentPanel;
		this.inflated = inflated;
	}
}