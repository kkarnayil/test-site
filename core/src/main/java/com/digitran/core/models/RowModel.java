package com.digitran.core.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.google.gson.annotations.Expose;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = RowModel.RESOURCE_TYPE)
public class RowModel {

	public static final String RESOURCE_TYPE = "digi-tran/components/row";

	@Self
	private Resource row;

	@Expose
	private List<Object> values;
	
	@Expose
	@Named(value = "searchkey")
	@ValueMapValue
	private String searchKey;
	
	@Expose
	@Named(value = "filterkey")
	@ValueMapValue
	private String filterKey;

	@PostConstruct
	protected void init() {

		values = new ArrayList<>();

		ValueMap map = row.getValueMap();

		values = map.entrySet().stream().filter(e -> e.getKey().startsWith("colVal")).map(Map.Entry::getValue)
				.collect(Collectors.toList());
	}

	/**
	 * @return the values
	 */
	public List<Object> getValues() {
		if (null != values) {
			return Collections.unmodifiableList(values);
		} else {
			return Collections.emptyList();
		}
	}
}
