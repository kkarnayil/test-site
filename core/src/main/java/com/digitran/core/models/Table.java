package com.digitran.core.models;

import java.util.Collections;
import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.google.gson.annotations.Expose;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = Table.RESOURCE_TYPE)
public class Table {
	
	public static final String RESOURCE_TYPE = "digi-tran/components/table";

	@Expose
	@ChildResource(name = "columnList")
	private List<Column> columns;

	@Expose
	@ChildResource(name = "filtergrouplist")
	private List<FilterGroupModel> filtergrouplist;

	@Expose 
	private ValueMap valueMap;

	@Expose 
	@ValueMapValue
	private boolean filterable;

	public List<Column> getColumns() {
		if (null != columns) {
			return Collections.unmodifiableList(columns);
		} else {
			return Collections.emptyList();
		}
	}

	public List<FilterGroupModel> getFilterGroupList(){
		if (null != filtergrouplist) {
			return Collections.unmodifiableList(filtergrouplist);
		} else {
			return Collections.emptyList();
		}
	}

	public void setRowMap(ValueMap valueMap) {
		this.valueMap = valueMap;
	}

	public ValueMap getRowData() {
		return valueMap;
	}

	public boolean getFilterable(){
		return filterable;
	}
}
