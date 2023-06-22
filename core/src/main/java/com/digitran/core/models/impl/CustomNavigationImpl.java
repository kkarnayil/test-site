package com.digitran.core.models.impl;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.via.ResourceSuperType;

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

	/** The Constant RESOURCE_TYPE. */
	protected static final String RESOURCE_TYPE = "digi-tran/components/customnavigation";

	/** The list. */
	@Self
	@Via(type = ResourceSuperType.class)
	private Navigation navigation;

	/** The items. */
	private List<CustomNavItem> items;

	/**
	 * Inits the Model.
	 */
	@PostConstruct
	protected void init() {
		if (Objects.nonNull(navigation)) {
			items = navigation.getItems().stream().map(CustomNavItemImpl::new).collect(Collectors.toList());
		}
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

}
