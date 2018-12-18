package org.parc.restes.entity;

/**
 * Created by xusiao on 2017/7/27.
 */
public class Index {
    private int id;
    private String name;
    private int days;
    private String opstatus;
    private String health;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public String getOpstatus() {
        return opstatus;
    }

    public void setOpstatus(String opstatus) {
        if (opstatus.contains("OPEN")) {
            this.opstatus = "开启";
        } else {
            if (opstatus.contains("CLOSE")) {
                this.opstatus = "关闭";
            } else {
                this.opstatus = opstatus;
            }
        }

    }

    public String getHealth() {
        return health;
    }

    public void setHealth(String health) {
        this.health = health;
    }
}
