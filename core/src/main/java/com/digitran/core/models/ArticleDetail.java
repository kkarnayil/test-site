package com.digitran.core.models;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

@Model(adaptables = SlingHttpServletRequest.class, resourceType = ArticleDetail.RESOURCE_TYPE)
public class ArticleDetail extends ArticleContentListModel {

	@Self
	private SlingHttpServletRequest request;

	private ArticleContentFragmentModel article;

	@PostConstruct
	private void initModel() {

		String articlePath = request.getRequestPathInfo().getSuffix();

		if (StringUtils.isEmpty(articlePath)) {
			return;
		}

		article = getArticles().stream().filter(article -> article.getPath().equals(articlePath)).findAny()
				.orElse(null);

	}

	public ArticleContentFragmentModel getArticle() {
		return article;
	}
}
