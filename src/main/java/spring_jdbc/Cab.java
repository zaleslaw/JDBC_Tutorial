package spring_jdbc;

import java.util.Date;

/**
 * Created by Alexey_Zinovyev on 31-Oct-16.
 */
public class Cab {

    private Date manufactureYear;
    private String carMake;
    private String licencePlate;
    private int capacity;
    private boolean hasBabyChair;

    public Cab(Date manufactureYear, String carMake, String licencePlate, int capacity, boolean hasBabyChair) {
        this.manufactureYear = manufactureYear;
        this.carMake = carMake;
        this.licencePlate = licencePlate;
        this.capacity = capacity;
        this.hasBabyChair = hasBabyChair;
    }

    public Cab() {
    }

    public Date getManufactureYear() {
        return manufactureYear;
    }

    public void setManufactureYear(Date manufactureYear) {
        this.manufactureYear = manufactureYear;
    }

    public String getCarMake() {
        return carMake;
    }

    public void setCarMake(String carMake) {
        this.carMake = carMake;
    }

    public String getLicencePlate() {
        return licencePlate;
    }

    public void setLicencePlate(String licencePlate) {
        this.licencePlate = licencePlate;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean isHasBabyChair() {
        return hasBabyChair;
    }

    public void setHasBabyChair(boolean hasBabyChair) {
        this.hasBabyChair = hasBabyChair;
    }

    @Override
    public String toString() {
        return "Product{" +
                "manufactureYear=" + manufactureYear +
                ", carMake='" + carMake + '\'' +
                ", licensePlate='" + licencePlate + '\'' +
                ", capacity=" + capacity +
                ", hasBabyChair=" + hasBabyChair +
                '}';
    }
}
