package org.UserInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OnClickListener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals("confBttn"))
			GUI.actConfig();
		else if (e.getSource().equals("impBttn"))
			GUI.actImport();
		else if (e.getSource().equals("expBttn"))
			GUI.actExport();
		else if (e.getActionCommand().equals("addFilterBttn"))
			GUI.actAddFilter();
		else if (e.getActionCommand().equals("remoFilterBttn"))
			GUI.actRemoFilter();
		else if (e.getActionCommand().equals("searchBttn"))
			GUI.actSelect();
		else if (e.getActionCommand().equals("constructBttn"))
			GUI.actConstruct();
		else if (e.getActionCommand().equals("assumeBttn"))
			GUI.actAssume();
		else if (e.getActionCommand().equals("updateDbBttn"))
			GUI.actDbUpdate();
		else if (e.getActionCommand().equals("inflate_module")) {
			GUI.tabbedPane.getSelectedIndex();
			GUI.tabList.get(GUI.tabbedPane.getSelectedIndex());
			GUI.tabList.get(GUI.tabbedPane.getSelectedIndex() - 1).moduleClicked(e);
		}
	}
}
