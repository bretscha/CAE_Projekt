package org.UserInterface;

/**
 * helper class to store important values which need to depict a site component
 * in a JTree node
 */
public class TreeDataHelper {

	/**
	 * type of the site component
	 */
	public String type;
	/**
	 * unique identifier of a site component
	 */
	public String ident;
	/**
	 * Tab instance in which component is placed
	 */
	public Tab hostTab;
	/**
	 * flag, if button is inflated
	 */
	public boolean inflated;

	/**
	 * constructor
	 * 
	 * @param ident
	 *            unique identifier of a site component
	 * @param type
	 *            type of the site component
	 * @param hostTab
	 *            Tab instance in which component is placed
	 * @param inflated
	 *            flag, if button is inflated
	 */
	public TreeDataHelper(String ident, String type, Tab hostTab, boolean inflated) {
		this.ident = ident;
		this.type = type;
		this.hostTab = hostTab;
		this.inflated = inflated;
	}
}