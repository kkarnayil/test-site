package com.digitran.core.servlets;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;

import com.digitran.core.configs.CommonConfiguration;
import com.digitran.core.models.CommentModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Component(service = { Servlet.class })
@SlingServletResourceTypes(resourceTypes = "digi-tran/components/articledetail/v1/articledetail", methods = {
		HttpConstants.METHOD_POST, HttpConstants.METHOD_GET }, selectors = "submit", extensions = "json")
@ServiceDescription("Article Form Servlet")
public class CommentsPostServlet extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 1L;

	@Reference
	private CommonConfiguration commonConfiguration;

	Gson gson = new GsonBuilder().create();

	@Override
	protected void doPost(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
			throws ServletException, IOException {

		ResourceResolver resolver = req.getResourceResolver();

		String articlePath = req.getParameter("articlePath");
		String comment = req.getParameter("comment");

		if (null != articlePath && null != comment) {
			Resource resource = resolver.getResource(articlePath);
			if (null != resource) {
				addComment(comment, resolver, resource);
				if (resolver.hasChanges()) {
					resolver.commit();
				}
				resp.setStatus(SlingHttpServletResponse.SC_CREATED);
				resp.getWriter().write(gson.toJson("Success"));
			} else {
				resp.setStatus(SlingHttpServletResponse.SC_BAD_REQUEST);
				resp.getWriter().write(gson.toJson("Resournce Not Found"));
			}
		} else {
			resp.setStatus(SlingHttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().write(gson.toJson("Asset Path not found"));
		}

	}

	@Override
	protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
			throws ServletException, IOException {

		String articlePath = req.getParameter("articlePath");

		ResourceResolver resolver = req.getResourceResolver();

		Resource contentRes = resolver.getResource(articlePath + "/jcr:content");

		if (null != contentRes) {
			
			CommentModel model = contentRes.adaptTo(CommentModel.class);

			if (null != model) {
				resp.setStatus(SlingHttpServletResponse.SC_OK);
				resp.getWriter().write(gson.toJson(model.getComments()));
			} else {
				resp.setStatus(SlingHttpServletResponse.SC_NO_CONTENT);
			}
		} else {
			resp.setStatus(SlingHttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().write(gson.toJson("Asset Path not found"));
		}

	}

	private void addComment(String comment, ResourceResolver resolver, Resource resource) throws PersistenceException {
		Resource jcrContentResource = resource.getChild("jcr:content");

		if (jcrContentResource != null && jcrContentResource.hasChildren()) {

			Resource commentsResource = jcrContentResource.getChild("comments");

			if (null == commentsResource) {
				commentsResource = resolver.create(jcrContentResource, "comments", null);
			}

			Map<String, Object> properties = new HashMap<>();
			properties.put("comment", comment);

			Date date = Calendar.getInstance().getTime();
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String createdDate = dateFormat.format(date);
			properties.put("createdDate", createdDate);

			resolver.create(commentsResource, UUID.randomUUID().toString(), properties);
		}
	}
}