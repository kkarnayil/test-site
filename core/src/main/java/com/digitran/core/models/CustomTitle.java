package com.digitran.core.models;

import com.adobe.cq.wcm.core.components.models.Title;

public interface CustomTitle extends Title {
	
	/**
	 * @return the link
	 */
	default String getCustomLink() {
		return null;
	}

}
