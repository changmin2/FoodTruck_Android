package org.techtown.navagation.FoodTruck;

public class MapCheck {
    String deviceId;

    public String getDeviceId() {
        return deviceId;
    }

    public MapCheck(String deviceId, int check) {
        this.deviceId = deviceId;
        this.check = check;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
    }

    int check;
}
