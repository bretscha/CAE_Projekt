package org.UserInterface;

import javax.swing.JPanel;

/**
 * helper class to store important values which need to depict a site component in a JButton
 */
public class BttnDataHelper {

	/**
	 * unique identifier of a site component
	 */
	public String ident;
	/**
	 * panel in which the component is placed
	 */
	public JPanel parentPanel;
	/**
	 * flag, if button is inflated
	 */
	public boolean inflated;
	/**
	 * type of the site component
	 */
	public String type;
	/**
	 * Tab instance in which component is placed
	 */
	public Tab hostTab;

	/**
	 * constructor
	 * 
	 * @param ident
	 *            unique identifier of a site component
	 * @param type
	 *            type of the site component
	 * @param parentPanel
	 *            panel in which the component is placed
	 * @param hostTab
	 *            Tab instance in which component is placed
	 * @param inflated
	 *            flag, if button is inflated
	 */
	public BttnDataHelper(String ident, String type, JPanel parentPanel, Tab hostTab, boolean inflated) {
		this.ident = ident;
		this.parentPanel = parentPanel;
		this.hostTab = hostTab;
		this.inflated = inflated;
		this.type = type;
	}
}