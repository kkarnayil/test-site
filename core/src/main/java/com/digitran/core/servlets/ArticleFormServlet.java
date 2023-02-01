package com.digitran.core.servlets;

import java.io.IOException;
import java.util.Calendar;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;

import com.adobe.cq.dam.cfm.ContentElement;
import com.adobe.cq.dam.cfm.ContentFragment;
import com.adobe.cq.dam.cfm.ContentFragmentException;
import com.adobe.cq.dam.cfm.FragmentTemplate;
import com.digitran.core.configs.CommonConfiguration;

@Component(service = { Servlet.class })
@SlingServletResourceTypes(resourceTypes = "digi-tran/components/articleform/v1/articleform", methods = HttpConstants.METHOD_POST, selectors = "submit", extensions = "json")
@ServiceDescription("Article Form Servlet")
public class ArticleFormServlet extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 1L;

	@Reference
	private CommonConfiguration commonConfiguration;

	@Override
	protected void doPost(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
			throws ServletException, IOException {

		ResourceResolver resolver = req.getResourceResolver();

		if (null == commonConfiguration || StringUtils.isEmpty(commonConfiguration.getArticleModelPath())
				|| StringUtils.isEmpty(commonConfiguration.getArticlePath())) {
			resp.getWriter().write("Config not found");
			resp.setStatus(SlingHttpServletResponse.SC_NOT_ACCEPTABLE);
			return;
		}

		Resource templateOrModelRsc = resolver.getResource(commonConfiguration.getArticleModelPath());
		Resource sourceFolder = resolver.getResource(commonConfiguration.getArticlePath());

		FragmentTemplate tpl = templateOrModelRsc.adaptTo(FragmentTemplate.class);
		try {
			String name = "Article-" + Calendar.getInstance().getTimeInMillis();
			ContentFragment articleFragment = tpl.createFragment(sourceFolder, name, "Sample description.");
			String articleName = req.getParameter("articleName");
			String description = req.getParameter("articleDescription");
			String date = req.getParameter("articleDate");

			articleFragment.setTitle(articleName);

			ContentElement element = articleFragment.getElement("articleName");
			element.setContent(articleName, element.getContentType());
			ContentElement descriptionElement = articleFragment.getElement("articleDescription");
			descriptionElement.setContent(description, descriptionElement.getContentType());
			ContentElement dateEl = articleFragment.getElement("articleDate");
			dateEl.setContent(date, dateEl.getContentType());

			if (resolver.hasChanges()) {
				resolver.commit();
			}
		} catch (ContentFragmentException e) {
			resp.getWriter().write("Error");
		}

		resp.getWriter().write("Success");

	}
}