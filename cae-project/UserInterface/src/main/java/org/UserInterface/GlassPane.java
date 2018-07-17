package org.UserInterface;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Window;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

class GlassPane extends JComponent {

    /**
     * Auto generated default serial ID
     */
    private static final long serialVersionUID = 1L;

    protected void paintComponent(Graphics g) {
	super.paintComponent(g);
	Graphics2D g2D = (Graphics2D) g.create();

	if (GUI.tabbedPane.getSelectedIndex() != 0 && GUI.tabList.get(GUI.tabbedPane.getSelectedIndex() - 1).layer1.isVisible()) {
	    for (JButton start : GUI.tabList.get(GUI.tabbedPane.getSelectedIndex() - 1).connMap.keySet()) {
		for (JButton dest : GUI.tabList.get(GUI.tabbedPane.getSelectedIndex() - 1).connMap.get(start)) {
		    Window anch = SwingUtilities.getWindowAncestor(start);

		    int x1 = (int) (SwingUtilities.convertPoint(start.getParent(), start.getLocation(), anch).x + start.getVisibleRect().getWidth() / 2);
		    int y1 = (int) (SwingUtilities.convertPoint(start.getParent(), start.getLocation(), anch).y + start.getVisibleRect().getHeight());
		    int x2 = (int) (SwingUtilities.convertPoint(dest.getParent(), dest.getLocation(), anch).x + dest.getVisibleRect().getWidth() / 2);
		    int y2 = SwingUtilities.convertPoint(dest.getParent(), dest.getLocation(), anch).y;

		    g2D.drawLine(x1, y1, x2, y2);
		}
	    }
	}
    }
}