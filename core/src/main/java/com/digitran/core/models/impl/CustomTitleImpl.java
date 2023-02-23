package com.digitran.core.models.impl;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.via.ResourceSuperType;

import com.adobe.cq.wcm.core.components.models.Title;
import com.day.cq.commons.Externalizer;
import com.digitran.core.models.CustomTitle;

import lombok.Getter;
import lombok.experimental.Delegate;

@Model(adaptables = SlingHttpServletRequest.class, adapters = CustomTitle.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = CustomTitleImpl.RESOURCE_TYPE)
public class CustomTitleImpl implements CustomTitle {

	protected static final String RESOURCE_TYPE = "digi-tran/components/title";

	@Self
	@Via(type = ResourceSuperType.class)
	@Delegate
	private Title title;

	@ValueMapValue
	@Named("absoluteurl")
	private boolean absoluteUrl;

	@SlingObject
	private ResourceResolver resolver;

	@Getter
	private String customLink;

	@PostConstruct
	void init() {

		if (title.getLink() == null)
			return;

		Externalizer externalizer = resolver.adaptTo(Externalizer.class);
		if (absoluteUrl) {
			customLink = externalizer.authorLink(resolver, title.getLink().getURL());
		} else {
			customLink = title.getLink().getURL();
		}
	}

}
