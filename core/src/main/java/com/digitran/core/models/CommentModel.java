package com.digitran.core.models;

import java.util.Collection;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;

@Model(adaptables = {Resource.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CommentModel {

	@ChildResource
	private Collection<Comment> comments;
	
	public Collection<Comment> getComments() {
		return CollectionUtils.emptyIfNull(this.comments);
	}
}
