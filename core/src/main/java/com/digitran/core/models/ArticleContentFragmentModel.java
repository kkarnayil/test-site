package com.digitran.core.models;

import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import com.adobe.cq.dam.cfm.ContentElement;
import com.adobe.cq.dam.cfm.ContentFragment;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ArticleContentFragmentModel {

	@Inject
	@Self
	private Resource resource;

	private Optional<ContentFragment> contentFragment;

	@PostConstruct
	public void init() {
		contentFragment = Optional.ofNullable(resource.adaptTo(ContentFragment.class));
	}

	public String getModelTitle() {
		return contentFragment.map(cf -> cf.getTitle()).orElse(StringUtils.EMPTY);
	}

	public String getDescription() {
		return contentFragment.map(cf -> cf.getElement("articleDescription")).map(ContentElement::getContent)
				.orElse(StringUtils.EMPTY);
	}

	public String getDate() {
		return contentFragment.map(cf -> cf.getElement("articleDate")).map(ContentElement::getContent)
				.orElse(StringUtils.EMPTY);
	}
}