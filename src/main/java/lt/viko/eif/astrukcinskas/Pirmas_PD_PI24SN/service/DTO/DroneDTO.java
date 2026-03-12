package lt.viko.eif.astrukcinskas.Pirmas_PD_PI24SN.service.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.annotation.Bean;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
public class DroneDTO {

    private String droneName;
    private BigInteger batteryCapacity;
    private double price;
    private boolean droneNew;
    private char framsShape;
    private boolean isAutonomus;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private LocalDateTime lastUpdateDate;
    private String managerName;

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
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
}
