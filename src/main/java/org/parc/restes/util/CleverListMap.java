package org.parc.restes.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class CleverListMap extends HashMap<String, List<String>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void putSingle(String key, String value) {
		List<String> tmpV = get(key);
		if (tmpV == null) {
			tmpV = new ArrayList<>();
			put(key, tmpV);
		}
		if (!tmpV.contains(value)) {
			tmpV.add(value);
		}

	}

}
