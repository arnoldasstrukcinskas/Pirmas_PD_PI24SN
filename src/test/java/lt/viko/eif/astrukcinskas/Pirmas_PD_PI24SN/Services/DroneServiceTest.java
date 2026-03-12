package lt.viko.eif.astrukcinskas.Pirmas_PD_PI24SN.Services;

import lt.viko.eif.astrukcinskas.Pirmas_PD_PI24SN.model.Drone;
import lt.viko.eif.astrukcinskas.Pirmas_PD_PI24SN.repository.interfaces.DroneRepository;
import lt.viko.eif.astrukcinskas.Pirmas_PD_PI24SN.service.DTO.DroneDTO;
import lt.viko.eif.astrukcinskas.Pirmas_PD_PI24SN.service.DroneService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class DroneServiceTest {

    @Autowired
    private DroneService droneService;

    @Autowired
    private DroneRepository droneRepository;

    @Test
    void testCreateDroneInDb(){
        DroneDTO drone = new DroneDTO();
        drone.setDroneName("Sky-Fighter-X1");

        String result = droneService.createDroneInDb(drone, "Jonas");

        assertEquals("Drone added to db: Sky-Fighter-X1", result);
    }

    @Test
    void testUpdateDroneInDb(){
        DroneDTO drone = new DroneDTO();
        drone.setDroneName("Sky-Fighter-X1");

        DroneDTO testDrone = new DroneDTO();
        testDrone.setDroneName("Cloud-Fighter-X1");

        droneService.createDroneInDb(drone, "Jonas");

        String result = droneService.updateDroneInDb("Sky-Fighter-X1", testDrone, "Jonas");

        assertEquals("Updated drone with name: Cloud-Fighter-X1", result);
    }

    @Test
    void testDeleteDroneFromDb(){
        DroneDTO drone = new DroneDTO();
        drone.setDroneName("Sky-Fighter-X1");

        droneService.createDroneInDb(drone, "Jonas");

        String result = droneService.deleteDroneFromDb("Sky-Fighter-X1");

        assertEquals("Deleted drone with name: Sky-Fighter-X1", result);
    }

    @Test
    void testGetAllDronesFromDb(){
        Drone drone = new Drone();
        drone.setDroneName("Sky-Fighter-X");

        List<Drone> testList = new ArrayList<>();

        for(int i = 0; i < 5; i++)
        {
            Drone dummy = new Drone();
            dummy.setDroneName("Sky-Fighter-X" + i);
            dummy.setFramsShape('X');
            testList.add(dummy);

            droneService.createDroneInDb(droneService.fromDroneToDto(dummy), "Jonas");
        }

        List<Drone> drones = droneService.getAllDronesFromDB();

        assertEquals(testList, drones);
    }

    @AfterEach
    void setUp() {
        // Išvalome bazę prieš kiekvieną testą, kad jie vienas kitam netrukdytų
        droneRepository.deleteAll();
    }
}
