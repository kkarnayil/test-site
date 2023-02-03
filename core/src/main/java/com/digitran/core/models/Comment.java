package com.digitran.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Comment {

	@ValueMapValue
	private String comment;

	@ValueMapValue
	private String createdDate;

	public String getComment() {
		return comment;
	}

	public String getCreatedDate() {
		return createdDate;
	}

}