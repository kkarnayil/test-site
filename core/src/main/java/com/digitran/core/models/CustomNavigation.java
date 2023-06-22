package com.digitran.core.models;

import java.util.List;

import com.adobe.cq.wcm.core.components.models.Navigation;

/**
 * The Interface CustomNavigation.
 */
public interface CustomNavigation extends Navigation {

	/**
	 * Gets the custom nav items.
	 *
	 * @return the custom nav items
	 */
	List<CustomNavItem> getCustomNavItems();
	
}
