package com.digitran.core.models.impl;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
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

	@Self
	private Resource resource;

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
	 * Gets the path.
	 *
	 * @return the path
	 */
	public Resource getResource() {
		return resource;
	}

}
