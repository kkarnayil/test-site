package com.digitran.core.models;

import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.google.gson.annotations.Expose;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class FilterGroupModel {
	
	@Expose
	@ValueMapValue
	private String filterGroupName;

	@Expose
	@ChildResource(name = "filtervalues")
	private List<FilterGroupValueModel> filtervalues;
	
	/**
	 * @return the columnname
	 */
	public String getFilterGroupName() {
		return filterGroupName;
	}

	public List<FilterGroupValueModel> getFiltervalues(){
		return filtervalues;
	}

}
