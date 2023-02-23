package com.digitran.core.models;

import org.osgi.annotation.versioning.ConsumerType;

import com.adobe.cq.wcm.core.components.models.Button;

@ConsumerType
public interface CustomButton extends Button {

	default boolean isOpenNewTab() {
		return false;
	}

}
