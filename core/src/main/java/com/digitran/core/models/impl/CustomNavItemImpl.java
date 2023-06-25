package com.digitran.core.models.impl;

import java.util.Calendar;
import java.util.Objects;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import com.adobe.cq.wcm.core.components.commons.link.Link;
import com.adobe.cq.wcm.core.components.models.NavigationItem;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.api.Page;
import com.digitran.core.models.CustomNavItem;

/**
 * The Class CustomNavItemImpl.
 */
public class CustomNavItemImpl implements CustomNavItem {

	/** The title. */
	private String title;

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
	
	private int sortOrder;

	/**
	 * Instantiates a new custom nav item impl.
	 */
	public CustomNavItemImpl(){}

	/**
	 * Instantiates a new custom nav item impl.
	 *
	 * @param item the item
	 * @param resolver 
	 */
	@SuppressWarnings("unchecked")
	public CustomNavItemImpl(NavigationItem item, ResourceResolver resolver) {
		title = item.getTitle();
		description = item.getDescription();
		setLink(item.getLink());
		path = item.getPath();
		current = item.isCurrent();
		active = item.isActive();
		Resource pageResource = resolver.getResource(path);
		if(Objects.nonNull(pageResource)) {
			sortOrder = pageResource.getChild(JcrConstants.JCR_CONTENT).getValueMap().get("sortOrder", 0);
		}
	}

	/**
	 * Gets the link.
	 *
	 * @return the link
	 */
	@SuppressWarnings("rawtypes")
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
	
	/**
	 * Sets the title.
	 *
	 * @param title the new title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Sets the link.
	 *
	 * @param link the link to set
	 */
	public void setLink(Link<Page> link) {
		this.link = link;
	}
	
	/**
	 * Sets the sort order.
	 *
	 * @param number the new sort order
	 */
	public void setSortOrder(int number) {
		this.sortOrder = number;
	}

	/**
	 * @return the sortOrder
	 */
	@Override
	public int getSortOrder() {
		return sortOrder;
	}

	@Override
	public String toString() {
		return "Item[title=" + title + ", path=" + path + ", sortOrder=" + sortOrder + "]\n";
	}
	
}
