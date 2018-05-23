/**
 * @author shaco.zhu
 * @email shaco.zhu@dbappsecurity.com.cn
 * Date:2017年8月8日
 */
package org.parc.restes.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CleverListMap extends HashMap<String, List<String>> {

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
