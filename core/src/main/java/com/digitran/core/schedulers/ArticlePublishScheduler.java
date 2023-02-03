package com.digitran.core.schedulers;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.sling.event.jobs.JobBuilder.ScheduleBuilder;
import org.apache.sling.event.jobs.JobManager;
import org.apache.sling.event.jobs.ScheduledJobInfo;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This scheduler will identify User that are expired and
 */
@Component
@Designate(ocd = ArticlePublishScheduler.Config.class)
public class ArticlePublishScheduler {

	@ObjectClassDefinition(name = "Article Publisher Scheduler", description = "User Admin Access Expiration Scheduler")
	public static @interface Config {
		@AttributeDefinition(name = "Cron-job expression")
		String schedulerExpression() default "0 0/2 * 1/1 * ? *";

		@AttributeDefinition(name = "Enabled?", description = "Whether to enable this scheduler")
		boolean enabled() default false;
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(ArticlePublishScheduler.class);
	private static final String ARTICLE_CONSUMER_TASK = "ArticlePublisherTopic";

	@Reference
	private JobManager jobManager;

	private String schedulerExpression;

	@Activate
	@Modified
	protected void activate(final Config config) {

		this.schedulerExpression = (String.valueOf(config.schedulerExpression()) != null)
				? String.valueOf(config.schedulerExpression())
				: null;

		removeScheduler();
		if (null != this.schedulerExpression && config.enabled()) {
			startScheduledJob(config);
		}
	}

	/**
	 * Method to remove already running job
	 */
	private void removeScheduler() {
		Collection<ScheduledJobInfo> scheduledJobInfos = jobManager.getScheduledJobs(ARTICLE_CONSUMER_TASK, 0,
				(Map<String, Object>[])null);
		for (ScheduledJobInfo scheduledJobInfo : scheduledJobInfos) {
			LOGGER.debug("Removing Scheduler for User Expiration : {}", scheduledJobInfo.getJobTopic());
			scheduledJobInfo.unschedule();
		}

	}

	/**
	 * Method to start job
	 * 
	 * @param config Config Object
	 */
	private void startScheduledJob(Config config) {
		final Map<String, Object> jobProperties = new HashMap<>();
		final ScheduleBuilder scheduleBuilder = jobManager.createJob(ARTICLE_CONSUMER_TASK)
				.properties(jobProperties).schedule();
		scheduleBuilder.cron(this.schedulerExpression);
		ScheduledJobInfo scheduledJobInfo = scheduleBuilder.add();
		if (scheduledJobInfo == null) {
			LOGGER.error("Failed to add Scheduler Job {}", ARTICLE_CONSUMER_TASK);
		} else {
			LOGGER.debug("Article Scheduler Job added to the Queue {}, Schedule :{}", ARTICLE_CONSUMER_TASK, this.schedulerExpression);
		}
	}

}
