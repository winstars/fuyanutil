package models;

import douyu.mvc.Model;

@Model
public class AreaAndDrink {
    private int id;
    private int areaId;
    private String areaName;
    private String drinkName;

    public void set(int id, int areaId, String areaName, String drinkName) {
        this.id = id;
        this.areaId = areaId;
        this.areaName = areaName;
        this.drinkName = drinkName;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAreaId() {
        return this.areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return this.areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getDrinkName() {
        return this.drinkName;
    }

    public void setDrinkName(String drinkName) {
        this.drinkName = drinkName;
    }
}