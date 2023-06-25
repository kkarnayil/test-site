package com.digitran.core.models.impl;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.via.ResourceSuperType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.models.Navigation;
import com.adobe.cq.wcm.core.components.models.datalayer.ComponentData;
import com.digitran.core.models.CustomNavItem;
import com.digitran.core.models.CustomNavigation;

/**
 * The Class CustomNavigationImpl.
 */
@Model(adaptables = SlingHttpServletRequest.class, adapters = CustomNavigation.class, resourceType = CustomNavigationImpl.RESOURCE_TYPE, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class CustomNavigationImpl implements CustomNavigation {

	private static final Logger log = LoggerFactory.getLogger(CustomNavigationImpl.class);

	/** The Constant RESOURCE_TYPE. */
	protected static final String RESOURCE_TYPE = "digi-tran/components/customnavigation";

	/** The list. */
	@Self
	@Via(type = ResourceSuperType.class)
	private Navigation navigation;
	
	@SlingObject
	private ResourceResolver resolver;

	@ChildResource
	private List<LinkObj> customLinks;

	/** The items. */
	private List<CustomNavItem> items;

	/**
	 * Inits the Model.
	 */
	@SuppressWarnings("unchecked")
	@PostConstruct
	protected void init() {
		if (Objects.nonNull(navigation)) {
			items = navigation.getItems().stream().map(item -> new CustomNavItemImpl(item, resolver)).collect(Collectors.toList());
		}

		if(Objects.nonNull(customLinks)){
			customLinks.stream().map(externalLinkItem -> {
				CustomNavItemImpl customNavItem = new CustomNavItemImpl();
				customNavItem.setTitle(externalLinkItem.getLinkHeading());
				customNavItem.setLink(externalLinkItem.getLink());
				customNavItem.setSortOrder(externalLinkItem.getSortOrder());
				return customNavItem;
			}).forEach(items::add);
			
		}
		
		log.info("Before Sort: {}", items);
		items.sort(new CustomSort("ASC"));
		log.info("After Sort: {}", items);
		
		
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	@Override
	public String getId() {
		return navigation.getId();
	}

	/**
	 * Gets the data.
	 *
	 * @return the data
	 */
	@Override
	public ComponentData getData() {
		return navigation.getData();
	}

	/**
	 * Gets the applied css classes.
	 *
	 * @return the applied css classes
	 */
	@Override
	public String getAppliedCssClasses() {
		return navigation.getAppliedCssClasses();
	}

	/**
	 * Gets the exported type.
	 *
	 * @return the exported type
	 */
	@Override
	public String getExportedType() {
		return navigation.getExportedType();
	}

	/**
	 * Gets the accessibility label.
	 *
	 * @return the accessibility label
	 */
	@Override
	public String getAccessibilityLabel() {
		return navigation.getAccessibilityLabel();
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
