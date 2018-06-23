package org.UserInterface;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

public class Tab {

	public JScrollPane scrollPane;
	private String name;
	private JPanel layer1 = new JPanel();
	private GUI gui;
	public JPanel layer11 = new JPanel();
	private HashMap<JButton, DataHelper> bttnMap = new HashMap<JButton, DataHelper>();
	public HashMap<JButton, ArrayList<JButton>> connMap = new HashMap<JButton, ArrayList<JButton>>();

	public Tab(GUI gui, String label, String name) {

		this.gui = gui;
		this.name = name;
		scrollPane = new JScrollPane(layer1, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		layer1.setLayout(new BoxLayout(layer1, BoxLayout.X_AXIS));
		layer11.setLayout(new BoxLayout(layer11, BoxLayout.Y_AXIS));

		JButton module1 = new JButton(name);
		module1.setOpaque(false);
		module1.setContentAreaFilled(false);
		module1.setActionCommand("inflate_module");
		module1.addActionListener(gui.onClickListener);
		module1.setMargin(new Insets(20, 20, 20, 20));
		module1.setAlignmentX(Component.CENTER_ALIGNMENT);
		layer11.setAlignmentY(Component.TOP_ALIGNMENT);
		layer11.add(module1);
		layer1.add(layer11);
		bttnMap.put(module1, new DataHelper(label, layer11, false));

	}

	public void moduleClicked(ActionEvent e) {
		JButton sourceBttn = (JButton) e.getSource();
		if (bttnMap.get(sourceBttn).inflated) {
			deflateModule(sourceBttn);
		} else {
			inflateModule(sourceBttn);
		}

	}

	private void inflateModule(JButton sourceBttn) {

		String label = bttnMap.get(sourceBttn).label;
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
			button.setActionCommand("inflate_module");
			button.addActionListener(gui.onClickListener);
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

		/*
		 * sparql: label:HASallesmögliche:result resutl:hasLabel:label for result... new
		 * Button mit farben!! usw...
		 */

	}

	private void deflateModule(JButton sourceBttn) {
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
