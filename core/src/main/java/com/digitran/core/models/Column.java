package com.digitran.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Column {
	
	@ValueMapValue
	private String columnname;
	
	@ValueMapValue
	private String columntype;

	/**
	 * @return the columnname
	 */
	public String getColumnname() {
		return columnname;
	}

	/**
	 * @return the columntype
	 */
	public String getColumntype() {
		return columntype;
	}
}
