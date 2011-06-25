package models;

import douyu.mvc.Model;

@Model
public class UserDrinkData {
    private int id;
    private int userId;
    private int drinkId;
    private int year;
    private int month;
    private int num;

    public void set(int id, int userId, int drinkId, int year, int month, int num) {
        this.id = id;
        this.userId = userId;
        this.drinkId = drinkId;
        this.year = year;
        this.month = month;
        this.num = num;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getDrinkId() {
        return this.drinkId;
    }

    public void setDrinkId(int drinkId) {
        this.drinkId = drinkId;
    }

    public int getYear() {
        return this.year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return this.month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getNum() {
        return this.num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}