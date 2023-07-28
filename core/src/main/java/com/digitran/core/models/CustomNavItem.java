package com.digitran.core.models;

/**
 * The Interface CustomNavItem.
 */
public interface CustomNavItem {

	int getSortOrder();

	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	String getTitle();

	/**
	 * Gets the path.
	 *
	 * @return the path
	 */
	String getPath();

	/**
	 * @param path the path to set
	 */
	void setPath(String path);

	/**
	 * Sets the sort order.
	 *
	 * @param number the new sort order
	 */
	void setSortOrder(int number);

	/**
	 * Sets the title.
	 *
	 * @param title the new title
	 */
	void setTitle(String title);
}
