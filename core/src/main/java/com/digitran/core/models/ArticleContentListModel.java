package com.digitran.core.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import com.adobe.cq.dam.cfm.ContentFragment;
import com.digitran.core.configs.CommonConfiguration;

@Model(adaptables = Resource.class, resourceType = ArticleContentListModel.RESOURCE_TYPE)
public class ArticleContentListModel {

	protected static final String RESOURCE_TYPE = "digi-tran/components/articleform/v1/articleform";

	@OSGiService
	private CommonConfiguration config;

	@SlingObject
	private ResourceResolver resolver;

	private List<ArticleContentFragmentModel> articles;

	@PostConstruct
	public void init() {
		articles = new ArrayList<>();

		Resource articleRootPath = resolver.getResource(config.getArticlePath());
		if (null == articleRootPath) {
			return;
		}

		if (articleRootPath.hasChildren()) {

			articles = StreamSupport
					.stream(((Iterable<Resource>) () -> articleRootPath.listChildren()).spliterator(), false)
					.filter(resource -> null != resource.adaptTo(ContentFragment.class))
					.map(articleResource -> articleResource.adaptTo(ArticleContentFragmentModel.class))
					.collect(Collectors.toList());

		}
	}

	public List<ArticleContentFragmentModel> getArticles() {
		return Collections.unmodifiableList(articles);
	}

}