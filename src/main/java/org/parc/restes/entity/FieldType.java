package org.parc.restes.entity;

public enum FieldType {
    search("search"), scenes("scenes"), alert("alert");
    private String type;

    FieldType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
