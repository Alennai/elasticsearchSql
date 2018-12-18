package org.parc.sqlrestes.entity;

import java.util.Locale;

/**
 * Created by xusiao on 2018/5/4.
 */
public enum ShapeRelation {
    INTERSECTS("intersects"),
    DISJOINT("disjoint"),
    WITHIN("within"),
    CONTAINS("contains");

    private final String relationName;

    ShapeRelation(String relationName) {
        this.relationName = relationName;
    }

//    public static ShapeRelation readFromStream(StreamInput in) throws IOException {
//        return (ShapeRelation)in.readEnum(ShapeRelation.class);
//    }
//
//    public void writeTo(StreamOutput out) throws IOException {
//        out.writeEnum(this);
//    }

    public static ShapeRelation getRelationByName(String name) {
        name = name.toLowerCase(Locale.ENGLISH);
        ShapeRelation[] var1 = values();

        for (ShapeRelation relation : var1) {
            if (relation.relationName.equals(name)) {
                return relation;
            }
        }

        return null;
    }

    public String getRelationName() {
        return this.relationName;
    }
}

