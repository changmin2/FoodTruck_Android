package org.techtown.navagation.FoodTruck;

public class Plan {
    String open;

    String close;
    String day;
    public Plan(String open, String close, String day) {
        this.open = open;
        this.close = close;
        this.day = day;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
