package com.digitran.core.models.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.export.json.ExporterConstants;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.digitran.core.models.CustomNavItem;
import com.digitran.core.models.CustomNavigation;


/**
 * The Class CustomNavigationImpl.
 */
@Model(adaptables = SlingHttpServletRequest.class, adapters = CustomNavigation.class, resourceType = CustomNavigationImpl.RESOURCE_TYPE, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class CustomNavigationImpl implements CustomNavigation {

	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(CustomNavigationImpl.class);

	/** The Constant RESOURCE_TYPE. */
	protected static final String RESOURCE_TYPE = "digi-tran/components/customnavigation";

	/** The request. */
	@Self
	private SlingHttpServletRequest request;

	/** The resolver. */
	@SlingObject
	private ResourceResolver resolver;
	
	/** The navigation root. */
	@ValueMapValue
	private String navigationRoot;
	
	/** The include nav root. */
	@ValueMapValue
	private boolean includeNavRoot;

	/** The custom links. */
	@ChildResource
	private List<LinkObj> customLinks;

	/** The child pages. */
	@ChildResource
	private List<LinkObj> childPages;

	/** The parent child pages. */
	private List<CustomNavItem> parentChildPages;

	/** The items. */
	private List<CustomNavItem> items;
	
	/** The page manager. */
	private PageManager pageManager;

	/**
	 * Inits the Model.
	 */
	@PostConstruct
	protected void init() {
		
		pageManager = resolver.adaptTo(PageManager.class);
		
		items = new ArrayList<>();
		
		if (Objects.nonNull(request.getRequestPathInfo().getSuffix())) {
			populateChildPages(request.getRequestPathInfo().getSuffix());
		}

		if(includeNavRoot && StringUtils.isNotEmpty(navigationRoot)) {		
			addNavRoot();		
		}
		
		if (Objects.nonNull(childPages)) {
			childPages.stream().map(childPageItem -> {
				CustomNavItemImpl customNavItem = new CustomNavItemImpl();
				customNavItem.setTitle(childPageItem.getLinkHeading());
				customNavItem.setPath(childPageItem.getLink());
				customNavItem.setSortOrder(childPageItem.getSortOrder());
				return customNavItem;
			}).forEach(items::add);
		}

		
		if (Objects.nonNull(customLinks)) {
			customLinks.stream().map(externalLinkItem -> {
				CustomNavItemImpl customNavItem = new CustomNavItemImpl();
				customNavItem.setTitle(externalLinkItem.getLinkHeading());
				customNavItem.setPath(externalLinkItem.getLink());
				customNavItem.setSortOrder(externalLinkItem.getSortOrder());
				return customNavItem;
			}).forEach(items::add);
		}	

		log.info("Before Sort: {}", items);
		items.sort(new CustomSort("ASC"));
		log.info("After Sort: {}", items);
		
	}

	/**
	 * Adds the nav root.
	 */
	private void addNavRoot() {
		Page page = pageManager.getPage(navigationRoot);
		if(Objects.nonNull(page)) {
			CustomNavItem customNavItem = new CustomNavItemImpl();
			customNavItem.setTitle(page.getTitle());
			customNavItem.setPath(page.getPath());
			customNavItem.setSortOrder(-1);
			items.add(customNavItem);
		}
	}

	/**
	 * Populate child pages for dialog.
	 *
	 * @param rootPage the root page
	 */
	private void populateChildPages(final String rootPage) {
		
		log.debug("Root Page :{}", rootPage);
		
		final Page page = pageManager.getPage(rootPage);

		if (Objects.nonNull(page) && page.hasContent()) {
			parentChildPages = new ArrayList<>();
			final Iterator<Page> childPageIterator = page.listChildren();
			int sortOrder = 1;
			while (childPageIterator.hasNext()) {
				Page childPage = childPageIterator.next();
				if (childPage.isHideInNav()) {
					continue;
				}
				final CustomNavItem childPageItem = new CustomNavItemImpl();
				childPageItem.setPath(childPage.getPath());
				childPageItem.setTitle(StringUtils.isNotEmpty(childPage.getNavigationTitle()) ? childPage.getNavigationTitle() : childPage.getTitle());
				childPageItem.setSortOrder(sortOrder++);
				parentChildPages.add(childPageItem);
			}
		}
	}

	/**
	 * Gets the parent child pages.
	 *
	 * @return the parent child pages
	 */
	@Override
	public List<CustomNavItem> getParentChildPages() {
		if (Objects.nonNull(parentChildPages)) {
			return Collections.unmodifiableList(parentChildPages);
		}
		return Collections.emptyList();
	}

	/**
	 * Gets the custom nav items.
	 *
	 * @return the custom nav items
	 */
	@Override
	public List<CustomNavItem> getCustomNavItems() {
		if (Objects.nonNull(items)) {
			return Collections.unmodifiableList(items);
		}
		return Collections.emptyList();
	}

	/**
	 * The Class CustomSort.
	 */
	private static class CustomSort implements Comparator<CustomNavItem>, Serializable {

		/**
		 * Serial version UID.
		 */
		private static final long serialVersionUID = -707429230313589969L;

		/** The sort order. */

		/**
		 * Comparator for comparing pages.
		 */

		private final transient Comparator<CustomNavItem> pageComparator;

		/** The order. */
		String order;

		/**
		 * Construct a page sorting comparator.
		 *
		 * @param order the order
		 */
		CustomSort(String order) {
			this.order = order;
			this.pageComparator = (a, b) -> ObjectUtils.compare(a.getSortOrder(), b.getSortOrder(), true);
		}

		/**
		 * Compare.
		 *
		 * @param item1 the item 1
		 * @param item2 the item 2
		 * @return the int
		 */
		@Override
		public int compare(final CustomNavItem item1, final CustomNavItem item2) {
			int i = this.pageComparator.compare(item1, item2);
			if ("DESC".equalsIgnoreCase(order)) {
				i = i * -1;
			}
			return i;
		}
	}

}
