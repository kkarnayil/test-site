package com.digitran.core.schedulers;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.jcr.Session;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.dam.cfm.ContentFragment;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.Replicator;
import com.digitran.core.configs.CommonConfiguration;
import com.digitran.core.models.ArticleContentFragmentModel;

@Component(service = JobConsumer.class, immediate = true, property = {
		Constants.SERVICE_DESCRIPTION + "=Sling job to identify and publish article",
		JobConsumer.PROPERTY_TOPICS + "=ArticlePublisherTopic" })
public class ArticlePublisJobConsumer implements JobConsumer {

	private static final String ARTICLE_PUB_SERVICE_USER = "articlepubuserservice";

	private static final Logger LOGGER = LoggerFactory.getLogger(ArticlePublisJobConsumer.class);

	@Reference
	private ResourceResolverFactory factory;

	@Reference
	private CommonConfiguration commonConfiguration;

	@Reference
	private Replicator replicator;

	@Override
	public JobResult process(final Job job) {

		LOGGER.debug("[---------------------- ArticlePublisJobConsumer Process Start ----------------------]");
		try (ResourceResolver resolver = getServiceResolver(factory, ARTICLE_PUB_SERVICE_USER);) {

			if (resolver == null) {
				LOGGER.debug("Resolver is null. Could not find sub-service name : {}", ARTICLE_PUB_SERVICE_USER);
				return JobResult.FAILED;
			}

			Session session = resolver.adaptTo(Session.class);

			Resource rootFolder = resolver.getResource(commonConfiguration.getArticlePath());

			if (null != rootFolder) {

				Iterator<Resource> itr = rootFolder.listChildren();
				
				while (itr.hasNext()) {
					Resource cf = itr.next();
					ContentFragment cfm = cf.adaptTo(ContentFragment.class);

					if (null != cfm) {
						ArticleContentFragmentModel article = cf.adaptTo(ArticleContentFragmentModel.class);

						if (article.getDate() != null) {
							final Date articlePublishDate = DateUtils.parseDate(article.getDate(), "yyyy-MM-dd");
							final Date currentDate = Calendar.getInstance().getTime();
							if (DateUtils.isSameDay(articlePublishDate, currentDate)) {
								replicator.replicate(session, ReplicationActionType.ACTIVATE, cf.getPath());
							}
						}
					}
				}
			}

		} catch (LoginException | ParseException | ReplicationException e) {
			LOGGER.error("Exception in execution of ArticlePublisJobConsumer", e);
			return JobResult.FAILED;
		}
		return JobResult.OK;
	}

	/**
	 * Method to get Resource Resolver
	 * 
	 * @param resourceResolverFactory ResolverFactory instance
	 * @param subServiceName          sub service name
	 * @return Resource resolver
	 * @throws LoginException
	 */
	private ResourceResolver getServiceResolver(final ResourceResolverFactory resourceResolverFactory,
			final String subServiceName) throws LoginException {

		final Map<String, Object> param = new HashMap<>();
		param.put(ResourceResolverFactory.SUBSERVICE, subServiceName);
		return resourceResolverFactory.getServiceResourceResolver(param);
	}

}