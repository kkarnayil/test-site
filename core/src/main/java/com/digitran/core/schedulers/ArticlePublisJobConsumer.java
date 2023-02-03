package com.digitran.core.schedulers;

import java.util.HashMap;
import java.util.Map;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = JobConsumer.class, immediate = true, 
		   property = {
				   		Constants.SERVICE_DESCRIPTION + "=Sling job to identify and publish article",
				   		JobConsumer.PROPERTY_TOPICS + "=ArticlePublisherTopic" 
				   	  }
)
public class ArticlePublisJobConsumer implements JobConsumer {

	private static final String EXPIRY_SERVICE_USER = "dam-service-user";

	private static final Logger LOGGER = LoggerFactory.getLogger(ArticlePublisJobConsumer.class);

	
	@Reference
	private ResourceResolverFactory factory;

	@Override
	public JobResult process(final Job job) {

		LOGGER.debug("[---------------------- ArticlePublisJobConsumer Process Start ----------------------]");
		try (ResourceResolver resolver = getServiceResolver(factory, EXPIRY_SERVICE_USER);) {

			if (resolver == null) {
				LOGGER.debug("Resolver is null. Could not find sub-service name : {}", EXPIRY_SERVICE_USER);
				return JobResult.FAILED;
			}

	
		} catch (LoginException e) {
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