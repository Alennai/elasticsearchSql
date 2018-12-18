package org.parc.restes.util;


import org.parc.restes.entity.IField;

import java.text.Collator;
import java.util.Comparator;

public class FieldComparator implements Comparator<IField> {
    private Comparator<Object> c = Collator.getInstance(java.util.Locale.CHINA);

    /* (non-Javadoc)
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(IField o1, IField o2) {
        // TODO Auto-generated method stub
        return c.compare(o1.getName(), o2.getName());
    }

}
