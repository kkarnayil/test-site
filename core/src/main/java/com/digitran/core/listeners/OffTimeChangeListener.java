package com.digitran.core.listeners;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.jackrabbit.commons.JcrUtils;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.observation.ResourceChange;
import org.apache.sling.api.resource.observation.ResourceChange.ChangeType;
import org.apache.sling.api.resource.observation.ResourceChangeListener;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.api.PageManager;

@Component(service = ResourceChangeListener.class, immediate = true, property = {
		ResourceChangeListener.PATHS + "=" + "glob:/content/digi-tran/**/jcr:content/root/container/**",
		ResourceChangeListener.CHANGES + "=" + "ADDED", 
		ResourceChangeListener.CHANGES + "=" + "CHANGED",
		ResourceChangeListener.CHANGES + "=" + "REMOVED",
		ResourceChangeListener.PROPERTY_NAMES_HINT + "=" + "componentOffTime", })
public class OffTimeChangeListener implements ResourceChangeListener {

	private static final String SERVICE_USER = "articlepubuserservice";

	private static final String OFF_TIME_DETAIL_NODE = "componentOffTimes";
	
	private static final String OFF_TIME_DETAIL_NODE_ABS_PATH = "/var/"+ OFF_TIME_DETAIL_NODE;

	private static final Logger LOGGER = LoggerFactory.getLogger(OffTimeChangeListener.class);

	@Reference
	private ResourceResolverFactory factory;

	private Session session;

	private PageManager pageManager;

	@Override
	public void onChange(List<ResourceChange> list) {

		try (ResourceResolver resolver = getServiceResolver(factory, SERVICE_USER);) {
			session = resolver.adaptTo(Session.class);
			pageManager = resolver.adaptTo(PageManager.class);

			Resource offTimeDetailNode = getOffTimeDetailsNode(resolver);

			for (ResourceChange change : list) {
				Resource resource = resolver.getResource(change.getPath());
				if (null != resource) {
					Date componentOffTime = resource.getValueMap().get("componentOffTime", Date.class);
					if (null != componentOffTime) {
						handleComponentOffTime(resolver, resource, componentOffTime, change.getType(),
								offTimeDetailNode);
					}else {
						Resource offTimeVarResource = resolver.getResource(OFF_TIME_DETAIL_NODE_ABS_PATH + resource.getPath());
						if(null != offTimeVarResource) {
							resolver.delete(offTimeVarResource);
						}
					}
				}
			}

			if (resolver.hasChanges()) {
				resolver.commit();
			}
		} catch (LoginException | PersistenceException | RepositoryException e) {
			LOGGER.error("Error while execution", e);
		}
	}

	private void handleComponentOffTime(ResourceResolver resolver, Resource changedResource, Date componentOffTime,
			ChangeType changeType, Resource offTimeDetailNode) throws RepositoryException {
		LOGGER.debug("Off Time Updated: {}, Type : {}", changedResource.getPath(), changeType);

		if (null != offTimeDetailNode) {
			Node node = JcrUtils.getOrCreateByPath(offTimeDetailNode.getPath() + changedResource.getPath(),
					JcrConstants.NT_UNSTRUCTURED, JcrConstants.NT_UNSTRUCTURED, session, true);

			if (null != node) {
				LOGGER.debug("Node path: {}", node.getPath());

				Resource nodeResource = resolver.getResource(node.getPath());
				ModifiableValueMap properties = nodeResource.adaptTo(ModifiableValueMap.class);
				
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(componentOffTime);
				
				properties.put("componentOffTime", calendar);
				properties.put("pagePath", pageManager.getContainingPage(changedResource).getPath());
				properties.put("resourceType", changedResource.getResourceType());
				properties.put("resourcePath", changedResource.getPath());
				
			}
		}
	}

	private Resource getOffTimeDetailsNode(ResourceResolver resolver) {

		Resource varNode = resolver.getResource("/var");

		Resource offTimeDetailNode = null;

		if (null != varNode && varNode.getChild(OFF_TIME_DETAIL_NODE) == null) {

			try {
				offTimeDetailNode = resolver.create(varNode, OFF_TIME_DETAIL_NODE, null);
			} catch (PersistenceException e) {
				LOGGER.error("Error while saving property", e);
			}
		} else if (null != varNode) {
			offTimeDetailNode = varNode.getChild(OFF_TIME_DETAIL_NODE);
		}
		return offTimeDetailNode;

	}

	private ResourceResolver getServiceResolver(final ResourceResolverFactory resourceResolverFactory,
			final String subServiceName) throws LoginException {
		final Map<String, Object> param = new HashMap<>();
		param.put(ResourceResolverFactory.SUBSERVICE, subServiceName);
		return resourceResolverFactory.getServiceResourceResolver(param);
	}

}