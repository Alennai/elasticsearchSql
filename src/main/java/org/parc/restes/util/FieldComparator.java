/**
 * @author shaco.zhu
 * @email shaco.zhu@dbappsecurity.com.cn
 * Date:2017年6月8日
 */
package org.parc.restes.util;

import java.text.Collator;
import java.util.Comparator;

import com.dbapp.cpsysportal.elasticsearch.entity.IField;

public class FieldComparator implements Comparator<IField> {
	Comparator<Object> c = Collator.getInstance(java.util.Locale.CHINA);
	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(IField o1, IField o2) {
		// TODO Auto-generated method stub
		return c.compare(o1.getName(), o2.getName());
	}

}
