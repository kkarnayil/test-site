package com.digitran.core.models;

import java.util.List;

/**
 * The Interface CustomNavigation.
 */
public interface CustomNavigation {

	/**
	 * Gets the custom nav items.
	 *
	 * @return the custom nav items
	 */
	List<CustomNavItem> getCustomNavItems();

	/**
	 * Gets the parent child pages.
	 *
	 * @return the parent child pages
	 */
	List<CustomNavItem> getParentChildPages();

}
