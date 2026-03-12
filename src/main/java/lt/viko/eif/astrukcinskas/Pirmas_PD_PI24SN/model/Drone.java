package lt.viko.eif.astrukcinskas.Pirmas_PD_PI24SN.model;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.*;
import lt.viko.eif.astrukcinskas.Pirmas_PD_PI24SN.adapter.CharAdapter;
import lt.viko.eif.astrukcinskas.Pirmas_PD_PI24SN.adapter.LocalDateTimeAdapter;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Objects;

@Table(name = "drones")
@ToString
@Entity
@XmlAccessorType(XmlAccessType.FIELD)
public class Drone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @XmlTransient
    private int id;

    @Column(name = "drone_name")
    private String droneName;

    @Column(name = "battery_capacity")
    private BigInteger batteryCapacity;

    @Column(name = "price")
    private double price;

    @Column(name = "drone_new")
    private boolean droneNew;

    @XmlJavaTypeAdapter(CharAdapter.class)
    @Column(name = "FrameShape")
    private Character framsShape;

    @Column(name = "is_autonomus")
    private boolean isAutonomus;

    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    @Column(name = "last_update_date")
    private LocalDateTime lastUpdateDate;

    @Column(name = "manager_name")
    private String managerName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDroneName() {
        return droneName;
    }

    public void setDroneName(String droneName) {
        this.droneName = droneName;
    }

    public BigInteger getBatteryCapacity() {
        return batteryCapacity;
    }

    public void setBatteryCapacity(BigInteger batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
    }

    public boolean getDroneNew() {
        return droneNew;
    }

    public void setDroneNew(boolean droneNew) {
        this.droneNew = droneNew;
    }

    public char getFramsShape() {
        return framsShape;
    }

    public void setFramsShape(char framsShape) {
        this.framsShape = framsShape;
    }

    public boolean isAutonomus() {
        return isAutonomus;
    }

    public void setAutonomus(boolean autonomus) {
        isAutonomus = autonomus;
    }

    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Drone drone = (Drone) o;
        return getDroneNew() == drone.getDroneNew() && getFramsShape() == drone.getFramsShape() && isAutonomus() == drone.isAutonomus() && Objects.equals(getDroneName(), drone.getDroneName()) && Objects.equals(getBatteryCapacity(), drone.getBatteryCapacity());
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(getDroneName());
        result = 31 * result + Objects.hashCode(getBatteryCapacity());
        result = 31 * result + Boolean.hashCode(getDroneNew());
        result = 31 * result + getFramsShape();
        result = 31 * result + Boolean.hashCode(isAutonomus());
        return result;
    }
}
