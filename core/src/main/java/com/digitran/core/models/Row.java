package com.digitran.core.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Row {

	@Self
	private Resource row;

	private List<Object> values;

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
		return values;
	}
}
