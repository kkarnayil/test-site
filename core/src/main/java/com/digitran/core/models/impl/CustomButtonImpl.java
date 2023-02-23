package com.digitran.core.models.impl;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.via.ResourceSuperType;

import com.adobe.cq.wcm.core.components.models.Button;
import com.digitran.core.models.CustomButton;

import lombok.Getter;
import lombok.experimental.Delegate;

@Model(adaptables = SlingHttpServletRequest.class, adapters = CustomButton.class, resourceType = CustomButtonImpl.RESOURCE_TYPE)
public class CustomButtonImpl implements CustomButton {

	protected static final String RESOURCE_TYPE = "digi-tran/components/button";

	@Self
	@Via(type = ResourceSuperType.class)
	@Delegate(excludes = Handled.class)
	private Button button;

	@Optional
	@ValueMapValue
	@Getter
	private boolean openNewTab;

	@Override
	public String getText() {
		return button.getText().toUpperCase();
	}
	
	protected static interface Handled {
	    public String getText();
	    
	}

}
