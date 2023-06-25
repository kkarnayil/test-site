package com.digitran.core.models.impl;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.adobe.cq.wcm.core.components.commons.link.Link;
import com.day.cq.dam.api.DamConstants;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;

/**
 * The Class LinkObj.
 */
@SuppressWarnings("rawtypes")
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
	 * Gets the link heading.
	 *
	 * @return the link heading
	 */
	public String getLinkHeading() {
		return linkHeading;
	}

	/**
	 * Gets the link.
	 *
	 * @return the link
	 */
	public Link getLink() {
		Link linkObj = null;
		if (StringUtils.isNotEmpty(link)) {
			if (link.startsWith("/content") && !link.startsWith(DamConstants.MOUNTPOINT_ASSETS)) {
				link = link.concat(".html");
			}
			linkObj = new LinkImpl<>(link, null);
		}
		return linkObj;
	}

	/**
	 * Gets the sort order.
	 *
	 * @return the sort order
	 */
	public int getSortOrder() {
		return sortOrder;
	}

	class LinkImpl<T> implements Link<T> {

		private String url;

		private Map<String, String> htmlAttributes;

		public LinkImpl(String url, Map<String, String> htmlAttributes) {
			this.url = url;
			this.htmlAttributes = buildHtmlAttributes(url, htmlAttributes);
		}
		
	    @Override
	    public boolean isValid() {
	        return url != null;
	    }
	    
	    @Override
	    @JsonInclude(Include.NON_EMPTY)
	    @JsonProperty("attributes")
	    public Map<String, String> getHtmlAttributes() {
	        return htmlAttributes;
	    }

		private Map<String, String> buildHtmlAttributes(String linkURL, Map<String, String> htmlAttributes) {
			Map<String, String> attributes = new LinkedHashMap<>();
			if (linkURL != null) {
				attributes.put("href", linkURL);
			}
			if (htmlAttributes != null) {
				Map<String, String> filteredAttributes = htmlAttributes.entrySet().stream()
						.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
				attributes.putAll(filteredAttributes);
			}
			return ImmutableMap.copyOf(attributes);
		}
	}

}
