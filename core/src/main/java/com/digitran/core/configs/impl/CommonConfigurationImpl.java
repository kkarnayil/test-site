package com.digitran.core.configs.impl;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.digitran.core.configs.CommonConfiguration;

@Component(service = { CommonConfiguration.class })
@Designate(ocd = CommonConfigurationImpl.Config.class)
public class CommonConfigurationImpl implements CommonConfiguration {
	private static final Logger log = LoggerFactory.getLogger(CommonConfigurationImpl.class);

	@ObjectClassDefinition(name = "Digi Tran Configurations", description = "Common Configurations")
	@interface Config {

		@AttributeDefinition(name = "Articles Target Folder Path", description = "Articles Folder Path")
		String articlesPath();

		@AttributeDefinition(name = "Articles Model Path", description = "Model Path")
		String articleModelPath();

	}

	private String articlesPath;

	private String articleModelPath;

	@Activate
	protected void activate(Config config) {

		this.articleModelPath = config.articleModelPath();
		this.articlesPath = config.articlesPath();
		log.info("Activated CommonConfigurationImpl with articles path {} ",  this.articlesPath);
	}

	@Deactivate
	protected void deactivate() {
		log.info("CommonConfigurationImpl has been deactivated!");
	}

	@Override
	public String getArticlePath() {
		return this.articlesPath;
	}

	@Override
	public String getArticleModelPath() {
		return this.articleModelPath;
	}
}