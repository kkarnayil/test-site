package com.digitran.core.models;

import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Table {

	@ChildResource(name = "columnList")
	private List<Column> columns;
	
	private ValueMap valueMap;

	public List<Column> getColumns() {
		return columns;
	}
	
	public void setRowMap(ValueMap valueMap) {
		this.valueMap = valueMap;
	}
	
	public ValueMap getRowData() {
		return valueMap;
	}
}
