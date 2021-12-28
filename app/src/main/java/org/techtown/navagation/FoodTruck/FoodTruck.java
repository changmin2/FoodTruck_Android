package org.techtown.navagation.FoodTruck;

public class FoodTruck {
    String name;
    String payment;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    String deviceId;
    public FoodTruck(String name, String payment, String deviceId) {
        this.name = name;
        this.payment = payment;
        this.deviceId = deviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }
}
