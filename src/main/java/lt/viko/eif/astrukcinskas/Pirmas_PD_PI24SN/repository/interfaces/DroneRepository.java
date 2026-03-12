package lt.viko.eif.astrukcinskas.Pirmas_PD_PI24SN.repository.interfaces;

import lt.viko.eif.astrukcinskas.Pirmas_PD_PI24SN.model.Drone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DroneRepository extends JpaRepository<Drone, Integer> {

    Drone findByDroneName(String name);
    List<Drone> findAllByPriceGreaterThan(double price);
}
