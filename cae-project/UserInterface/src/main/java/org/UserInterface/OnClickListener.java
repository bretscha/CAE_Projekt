package org.UserInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * implements click listener for all buttons at main pane
 */
public class OnClickListener implements ActionListener {
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource().equals("confBttn"))
			GUI.actConfig();
		else if (ae.getActionCommand().equals("impBttn"))
			GUI.actImport();
		else if (ae.getSource().equals("expBttn"))
			GUI.actExport();
		else if (ae.getActionCommand().equals("addFilterBttn"))
			GUI.actAddFilter();
		else if (ae.getActionCommand().equals("remoFilterBttn"))
			GUI.actRemoFilter();
		else if (ae.getActionCommand().equals("searchBttn"))
			GUI.actSelect();
		else if (ae.getActionCommand().equals("constructBttn"))
			GUI.actConstruct();
		else if (ae.getActionCommand().equals("assumeBttn"))
			GUI.actAssume();
		else if (ae.getActionCommand().equals("updateDbBttn"))
			GUI.actDbUpdate();
	}
}
