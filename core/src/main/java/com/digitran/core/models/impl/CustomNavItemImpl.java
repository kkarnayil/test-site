package com.digitran.core.models.impl;

import org.apache.commons.lang3.StringUtils;

import com.day.cq.dam.api.DamConstants;
import com.digitran.core.models.CustomNavItem;

/**
 * The Class CustomNavItemImpl.
 */
public class CustomNavItemImpl implements CustomNavItem {

	/** The title. */
	private String title;

	/** The path. */
	private String path;

	/** The sort order. */
	private int sortOrder;

	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	@Override
	public String getTitle() {
		return title;
	}

	/**
	 * Gets the path.
	 *
	 * @return the path
	 */
	@Override
	public String getPath() {
		if (StringUtils.isNotEmpty(path) && path.startsWith("/content")
				&& !path.startsWith(DamConstants.MOUNTPOINT_ASSETS) && !path.endsWith("html")) {
			path = path.concat(".html");
		}
		return path;
	}

	/**
	 * Sets the title.
	 *
	 * @param title the new title
	 */
	@Override
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Sets the sort order.
	 *
	 * @param number the new sort order
	 */
	@Override
	public void setSortOrder(int number) {
		this.sortOrder = number;
	}

	/**
	 * Gets the sort order.
	 *
	 * @return the sortOrder
	 */
	@Override
	public int getSortOrder() {
		return sortOrder;
	}

	/**
	 * Sets the path.
	 *
	 * @param path the path to set
	 */
	@Override
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "Item[title=" + title + ", path=" + getPath() + ", sortOrder=" + sortOrder + "]\n";
	}

}
