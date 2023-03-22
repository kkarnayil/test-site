/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.digitran.core.servlets;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;

import com.digitran.core.models.Table;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Component(service = { Servlet.class })
@SlingServletResourceTypes(resourceTypes = "digi-tran/components/row", methods = HttpConstants.METHOD_GET, extensions = "json")
public class RowServlet extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
			throws ServletException, IOException {

		final Resource resource = req.getResource();

		Gson gson = new GsonBuilder()
				  .excludeFieldsWithoutExposeAnnotation()
				  .create();

		final Resource tableResource = findTableResource(resource);

		if (null != tableResource) {
			resp.setContentType("application/json");
			Table table = tableResource.adaptTo(Table.class);
			table.setRowMap(resource.getValueMap());
			resp.getWriter().write(gson.toJson(table));
		} else {
			sendError(resp, gson);
		}
	}

	private Resource findTableResource(Resource resource) {

		if (resource == null) {
			return null;
		}

		if (resource.getParent() != null && "digi-tran/components/table".equals(resource.getParent().getResourceType())) {
			return resource.getParent();
		} else {
			return findTableResource(resource.getParent());
		}

	}

	private void sendError(final SlingHttpServletResponse resp, final Gson gson) throws IOException {
		resp.getWriter().write(gson.toJson("Table not found"));
	}
}
