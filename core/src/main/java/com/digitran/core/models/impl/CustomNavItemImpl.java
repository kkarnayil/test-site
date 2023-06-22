package com.digitran.core.models.impl;

import java.util.Calendar;

import com.adobe.cq.wcm.core.components.commons.link.Link;
import com.adobe.cq.wcm.core.components.models.NavigationItem;
import com.day.cq.wcm.api.Page;
import com.digitran.core.models.CustomNavItem;

/**
 * The Class CustomNavItemImpl.
 */
public class CustomNavItemImpl implements CustomNavItem {

	/** The title. */
	private String title;

	/** The name. */
	private String name;

	/** The description. */
	private String description;

	/** The last modified. */
	private Calendar lastModified;

	/** The link. */
	private Link<Page> link;

	/** The path. */
	private String path;

	/** The current. */
	private boolean current;

	/** The active. */
	private boolean active;

	/**
	 * Instantiates a new custom nav item impl.
	 *
	 * @param item the item
	 */
	public CustomNavItemImpl(NavigationItem item) {
		title = item.getTitle();
		description = item.getDescription();
		link = item.getLink();
		path = item.getPath();
		current = item.isCurrent();
		active = item.isActive();
	}

	/**
	 * Gets the link.
	 *
	 * @return the link
	 */
	@Override
	public Link getLink() {
		return link;
	}

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
	 * Gets the description.
	 *
	 * @return the description
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/**
	 * Gets the last modified.
	 *
	 * @return the last modified
	 */
	@Override
	public Calendar getLastModified() {
		return lastModified;
	}

	/**
	 * Gets the path.
	 *
	 * @return the path
	 */
	@Override
	public String getPath() {
		return path;
	}

	/**
	 * Checks if is active.
	 *
	 * @return true, if is active
	 */
	@Override
	public boolean isActive() {
		return active;
	}

	/**
	 * Checks if is current.
	 *
	 * @return true, if is current
	 */
	@Override
	public boolean isCurrent() {
		return current;
	}
}
