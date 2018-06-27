package org.UserInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OnClickListener implements ActionListener {
    public void actionPerformed(ActionEvent ae) {
	if (ae.getSource().equals("confBttn"))
	    GUI.actConfig();
	// TODO Vorsicht Chris! ich musste hier das getSource() für ein
	// getActionCommand() ändern damit es funktioniert.
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
