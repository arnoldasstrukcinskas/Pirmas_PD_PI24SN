package lt.viko.eif.astrukcinskas.Pirmas_PD_PI24SN.Services;

import lt.viko.eif.astrukcinskas.Pirmas_PD_PI24SN.model.Drone;
import lt.viko.eif.astrukcinskas.Pirmas_PD_PI24SN.repository.ServerRepository;
import lt.viko.eif.astrukcinskas.Pirmas_PD_PI24SN.service.ServerService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ServerServiceTest {

    @Autowired
    private ServerRepository serverRepository;

    @Autowired
    private ServerService serverService;

    @BeforeEach
    void setFileLocation(){
        serverRepository.setFilePath("src/test/resources/drone_test.xml");
    }

    @Test
    void createDroneInXml(){
        ObjectMapper objectMapper = new ObjectMapper();

        Drone drone = new Drone();
        drone.setDroneName("Sky-Fighter-X1");
        drone.setFramsShape('X');
        List<Drone> mockDrones = new ArrayList<>();
        mockDrones.add(drone);
        String droneJson = objectMapper.writeValueAsString(drone);

        Scanner mockScanner = new Scanner(droneJson);

        serverService.addDroneToXml(mockScanner);

        List<Drone> dronesTest = serverRepository.getAllDrones().getDrones();

        assertEquals(mockDrones, dronesTest);

    }

    @Test
    void testUpdateDroneInXml() {
        ObjectMapper objectMapper = new ObjectMapper();

        Drone drone = new Drone();
        drone.setDroneName("Sky-Fighter-X1");
        drone.setFramsShape('X');
        String droneJson = objectMapper.writeValueAsString(drone);

        Scanner mockScanner = new Scanner(droneJson);

        serverService.addDroneToXml(mockScanner);

        Drone updatedDrone = new Drone();
        updatedDrone.setDroneName("Cloud-Fighter-X1");
        updatedDrone.setFramsShape('X');
        List<Drone> mockDrones = new ArrayList<>();
        mockDrones.add(updatedDrone);
        String updatedDroneJson = objectMapper.writeValueAsString(updatedDrone);

        mockScanner = new Scanner(updatedDroneJson);

        serverService.updateDroneInXml(mockScanner, "Sky-Fighter-X1");

        List<Drone> testDroneList = serverRepository.getAllDrones().getDrones();

        assertEquals(mockDrones, testDroneList);
    }

    @Test
    void deleteDroneFromXML(){
        ObjectMapper objectMapper = new ObjectMapper();

        Drone drone = new Drone();
        drone.setDroneName("Sky-Fighter-X1");
        drone.setFramsShape('X');
        List<Drone> mockDrones = new ArrayList<>();
        mockDrones.add(drone);
        String droneJson = objectMapper.writeValueAsString(drone);

        Scanner mockScanner = new Scanner(droneJson);


        serverService.addDroneToXml(mockScanner);
        List<Drone> testDrones = serverRepository.getAllDrones().getDrones();
        assertEquals(mockDrones, testDrones);

        serverService.deleteDroneFromXml("Sky-Fighter-X1");
        testDrones = serverRepository.getAllDrones().getDrones();
        assertEquals(null, testDrones);
    }

    @AfterEach
    void resetXmlFile() throws IOException {
        String cleanXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n<drones>\n</drones>";
        Files.writeString(Path.of("src/test/resources/drone_test.xml"), cleanXml);
    }
}
