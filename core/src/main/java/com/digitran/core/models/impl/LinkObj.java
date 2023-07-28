package com.digitran.core.models.impl;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * The Class LinkObj.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class LinkObj {

	/** The link heading. */
	@ValueMapValue
	private String linkHeading;
	
	/** The link. */
	@ValueMapValue
	private String link;

	/** The sort order. */
	@ValueMapValue
	private int sortOrder;

	/**
	 * Gets the link.
	 *
	 * @return the link
	 */
	public String getLink() {
		return link;
	}

	/**
	 * Gets the sort order.
	 *
	 * @return the sort order
	 */
	public int getSortOrder() {
		return sortOrder;
	}
	
	/**
	 * Gets the link heading.
	 *
	 * @return the link heading
	 */
	public String getLinkHeading() {
		return linkHeading;
	}
	
	/**
	 * Sets the link heading.
	 *
	 * @param linkHeading the new link heading
	 */
	public void setLinkHeading(String linkHeading) {
		this.linkHeading = linkHeading;
	}

	/**
	 * Sets the link.
	 *
	 * @param link the new link
	 */
	public void setLink(String link) {
		this.link = link;
	}

	/**
	 * Sets the sort order.
	 *
	 * @param sortOrder the new sort order
	 */
	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}
}
